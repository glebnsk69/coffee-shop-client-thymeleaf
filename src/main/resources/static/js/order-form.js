    function formatMoney(kopecks) {
        return (kopecks / 100).toLocaleString('ru-RU', {
            style: 'currency',
            currency: 'RUB'
        });
    }

    // Фильтрация каталога по категории

    const categorySelect = document.getElementById('category');

    categorySelect.addEventListener('change', function () {
        const selectedCategory = this.value;

        document.querySelectorAll('.product-card').forEach(card => {
            const categoryId = card.dataset.categoryId;

            card.style.display =
                !selectedCategory || categoryId === selectedCategory
                    ? ''
                    : 'none';
        });
    });


    // Корзина

    const basketItems = document.getElementById('basket-items');
    const basket = new Map();

document.getElementById('clear-basket').addEventListener('click', () => {
    if (basket.size === 0) {
        return;
    }

    if (confirm('Очистить всю корзину?')) {
        basket.clear();
        renderBasket();
    }
});

document.getElementById('create-order').addEventListener('click', () => {
    if (basket.size === 0) {
        return;
    }

    const confirmationItems =
        document.getElementById('confirmation-items');

    confirmationItems.innerHTML = '';

    basket.forEach(product => {
        const item = document.createElement('p');

        item.textContent =
            `${product.name} — ${product.quantity} шт. × ` +
            `${formatMoney(product.discountedPrice)} = ` +
            `${formatMoney(product.discountedPrice * product.quantity)}`;

        confirmationItems.appendChild(item);
    });

    const totalBeforeDiscount = [...basket.values()]
        .reduce((sum, product) =>
            sum + product.price * product.quantity, 0);

    const totalWithDiscount = [...basket.values()]
        .reduce((sum, product) =>
            sum + product.discountedPrice * product.quantity, 0);

    document.getElementById('confirmation-positions').textContent =
        basket.size;

    document.getElementById('confirmation-before').textContent =
        formatMoney(totalBeforeDiscount);

    document.getElementById('confirmation-discount').textContent =
        formatMoney(totalBeforeDiscount - totalWithDiscount);

    document.getElementById('confirmation-total').textContent =
        formatMoney(totalWithDiscount);

    document.getElementById('confirmation-dialog').showModal();
});

document.getElementById('cancel-confirmation').addEventListener('click', () => {
    document.getElementById('confirmation-dialog').close();
});

document.getElementById('confirm-order').addEventListener('click', async () => {
    const button = document.getElementById('confirm-order');

    if (basket.size === 0) return;

    button.disabled = true;

    const request = {
        items: [...basket.entries()].map(([productId, product]) => ({
            productId: Number(productId),
            quantity: product.quantity
        }))
    };

    try {
        const response = await fetch('/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            throw new Error(`Ошибка создания заказа: HTTP ${response.status}`);
        }

        document.getElementById('confirmation-dialog').close();

        basket.clear();
        renderBasket();

        alert('Заказ успешно создан!');
    } catch (error) {
        console.error(error);
        alert('Не удалось создать заказ. ' + error.message);
    } finally {
        button.disabled = false;
    }
});


    // Добавление товара по клику на карточку

    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('click', () => {
            const productId = card.dataset.productId;

            if (basket.has(productId)) {
                basket.get(productId).quantity++;
            } else {
                basket.set(productId, {
                    name: card.dataset.productName,
                    price: Number(card.dataset.price),
                    discountedPrice: Number(card.dataset.discountedPrice),
                    quantity: 1
                });
            }

            renderBasket();
        });
    });


    // Отображение корзины

    function renderBasket() {
        basketItems.innerHTML = '';

        let totalQuantity = 0;
        let totalBeforeDiscount = 0;
        let totalWithDiscount = 0;

        basket.forEach((product, productId) => {
            const item = document.createElement('tr');

            // Название товара
            const nameCell = document.createElement('td');
            const name = document.createElement('span');
            name.className = 'basket-product-name';
            name.textContent = product.name;
            nameCell.appendChild(name);

            // Управление количеством
            const quantityCell = document.createElement('td');

            const quantityControl = document.createElement('div');
            quantityControl.className = 'quantity-control';

            const decreaseButton = document.createElement('button');
            decreaseButton.type = 'button';
            decreaseButton.textContent = '−';

            const quantity = document.createElement('input');
            quantity.type = 'number';
            quantity.min = '1';
            quantity.step = '1';
            quantity.value = product.quantity;

            const increaseButton = document.createElement('button');
            increaseButton.type = 'button';
            increaseButton.textContent = '+';

            decreaseButton.addEventListener('click', () => {
                if (product.quantity > 1) {
                    product.quantity--;
                    renderBasket();
                }
            });

            increaseButton.addEventListener('click', () => {
                product.quantity++;
                renderBasket();
            });

            quantity.addEventListener('change', () => {
                const newQuantity = Number(quantity.value);

                if (!Number.isInteger(newQuantity) || newQuantity < 1) {
                    quantity.value = product.quantity;
                    return;
                }

                product.quantity = newQuantity;
                renderBasket();
            });

            quantityControl.append(decreaseButton, quantity, increaseButton);
            quantityCell.appendChild(quantityControl);

            // Сумма позиции
            const lineTotalCell = document.createElement('td');
            lineTotalCell.className = 'basket-line-total';
            lineTotalCell.textContent =
                formatMoney(product.discountedPrice * product.quantity);

            // Удаление позиции
            const removeCell = document.createElement('td');

            const removeButton = document.createElement('button');
            removeButton.type = 'button';
            removeButton.textContent = '🗑';
            removeButton.title = 'Удалить товар';

            removeButton.addEventListener('click', () => {
                basket.delete(productId);
                renderBasket();
            });

            removeCell.appendChild(removeButton);

            // Добавление ячеек в строку
            item.append(
                nameCell,
                quantityCell,
                lineTotalCell,
                removeCell
            );
            basketItems.appendChild(item);

            // Расчёт итогов

            totalQuantity += product.quantity;
            totalBeforeDiscount += product.price * product.quantity;
            totalWithDiscount += product.discountedPrice * product.quantity;
        });

        // Отображение итогов

        document.getElementById('basket-positions').textContent = basket.size;

        document.getElementById('basket-quantity').textContent = totalQuantity;

        document.getElementById('basket-total-before').textContent =
            formatMoney(totalBeforeDiscount);

        document.getElementById('basket-discount').textContent =
            formatMoney(totalBeforeDiscount - totalWithDiscount);

        document.getElementById('basket-total').textContent =
            formatMoney(totalWithDiscount);

        document.getElementById('create-order').disabled = basket.size === 0;
    }
