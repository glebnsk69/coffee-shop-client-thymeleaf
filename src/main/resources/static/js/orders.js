const filterCheckbox = document.getElementById('show-inactive-orders');
//const orderRows = document.querySelectorAll('tbody tr[data-status]');

function filterOrders() {
    if (!filterCheckbox) {
        return;
    }
    const showInactive = filterCheckbox.checked;
    const orderRows = document.querySelectorAll('tbody tr[data-status]');

    orderRows.forEach(row => {
        const status = row.dataset.status;

        const isInactive =
            status === 'COMPLETED' || status === 'CANCELLED' || status === 'INVALID';

        row.hidden = isInactive && !showInactive;
    });
}

//----------------------------------------------
async function refreshOrders() {
    const tbody = document.getElementById('orders-body');

    if (!tbody) {
        return;
    }

    try {
        const response = await fetch('/orders/data', {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        const orders = await response.json();

        if (orders.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4">Заказов нет</td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = orders.map(order => {
            const hours = Math.floor(order.elapsedSeconds / 3600);
            const minutes = Math.floor((order.elapsedSeconds % 3600) / 60);
            const seconds = order.elapsedSeconds % 60;

            const total = (order.totalWithDiscount / 100).toFixed(2);

            return `
                <tr data-status="${order.status}">
                    <td>
                        <a href="/orders/${order.id}">${order.id}</a>
                    </td>
                    <td>${total} руб.</td>
                    <td>
                        <span class="order-status status-${order.status}">
                            ${order.statusName}
                        </span>
                    </td>
                    <td>
                        ${hours} ч.
                        ${minutes} мин.
                        ${seconds} сек.
                    </td>
                </tr>
            `;
        }).join('');

        filterOrders();

    } catch (error) {
        console.error('Ошибка обновления списка заказов:', error);
    }
}

// Автоматическое обновление списка заказов каждые 5 секунд
refreshOrders();
setInterval(refreshOrders, 5000);

//----------------------------------------------
// Применяем фильтр и регистрируем обработчик,
// только если элемент существует
if (filterCheckbox) {
    filterOrders();
    filterCheckbox.addEventListener('change', filterOrders);
}

// Получаем текущую дату и день недели

function updateFooterDate() {
    const footerDate = document.getElementById('footer-date');

    if (!footerDate) {
        return;
    }

    const now = new Date();

    const date = new Intl.DateTimeFormat('ru-RU', {
        day: 'numeric',
        month: 'long'
    }).format(now);

    const weekday = new Intl.DateTimeFormat('ru-RU', {
        weekday: 'long'
    }).format(now);

    footerDate.textContent = `Сегодня ${date}, ${weekday}`;
}

updateFooterDate();


