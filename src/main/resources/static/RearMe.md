# CoffeeShop — продолжение оформления страницы создания заказа

## 1. Контекст проекта

Разрабатывается клиентская часть CoffeeShop на Spring Boot + Thymeleaf.

- Java 21
- Spring Boot 3.5.x
- Клиент работает на порту `8081`.
- Backend работает на порту `8080`.
- HTML: `order-form.html`
- CSS: `style.css`
- JavaScript: `order-form.js`

Текущая задача — изменение дизайна страницы создания заказа без изменения существующей бизнес-логики.

## 2. Правила совместной работы

- Работать строго по одному небольшому изменению за раз.
- После каждого изменения пользователь проверяет результат.
- Не переходить к следующему шагу без подтверждения.
- Не переписывать целые файлы без необходимости.
- Не менять JavaScript и backend, если задача касается только оформления.
- Не добавлять новые функции без согласования.

## 3. Согласованный дизайн страницы

Ориентир — предоставленный пользователем эскиз.

### Шапка

- Логотип и заголовок «Создание заказа».
- Категории отображаются в шапке.
- Вкладки категорий — отдельные прямоугольные кнопки со скруглёнными углами.
- Активная категория выделяется цветом.

### Каталог товаров

- Отдельная панель слева.
- Карточки товаров располагаются сеткой.
- Вся карточка является кликабельной областью и добавляет товар в корзину.
- Отдельная кнопка «В корзину» не нужна.
- Карточки имеют полупрозрачный фон.
- Прокрутку пока не меняем.

### Корзина

- Отдельная панель справа от каталога.
- Изображения товаров не нужны.
- Для каждой позиции отображаются:
    - название;
    - цена;
    - количество;
    - управление количеством кнопками `−` и `+`;
    - возможность удалить позицию.
- Внизу отображаются сумма без скидки, скидка и итоговая стоимость.
- Кнопки «Оформить заказ» и «Очистить корзину».

Существующую логику расчётов, скидок и отправки заказа на backend сохранять.

## 4. Текущее состояние каталога

Каталог визуально принят пользователем.

HTML:
- `.catalog-section`
- `.product-catalog`
- `.product-card`

Актуальные CSS:

```css
.order-layout {
    display: grid;
    grid-template-columns: minmax(0, 65fr) minmax(280px, 35fr);
    gap: 24px;
    align-items: start;
}

.catalog-section {
    min-width: 0;
    background: rgba(255, 240, 218, 0.88);
    border: 1px solid rgb(214, 185, 160);
    border-radius: 18px;
    padding: 20px;
}

.product-catalog {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 16px;
    background: transparent;
    border: none;
}

.product-card {
    border-radius: 8px;
    padding: 12px;
    text-align: center;
    background: rgba(255, 240, 218, 0.78);
    border: 1px solid rgb(214, 185, 160);

    transition: transform 0.2s ease,
                box-shadow 0.2s ease,
                border-color 0.2s ease;
}

.product-card:hover {
    transform: translateY(-4px);
    border-color: rgb(180, 135, 95);
    box-shadow: 0 5px 14px rgba(78, 41, 13, 0.95);
}

.product-card img {
    width: 100%;
    height: 140px;
    object-fit: contain;
}

.product-card h3 {
    min-height: 2.5em;
    margin: 10px 0;
}
```

Примечание: интенсивность тени при наведении пока не корректировалась.

## 5. Категории
Вкладки находятся внутри .app-header.
```
<div class="category-tabs">
    <button type="button"
            class="category-tab"
            th:each="category : ${categories}"
            th:attr="data-category-id=${category.id}"
            th:text="${category.name}">
    </button>
</div>
```
Фильтрация выполняется в order-form.js через data-category-id.

При загрузке страницы выбирается первая категория.

Порядок категорий пока оставлен без изменений.
## 6. Текущий HTML корзины
   Структура корзины уже предоставлена пользователем и согласована как основа нового дизайна. Перестраивать её без необходимости не нужно.
 ```html
 <aside class="basket">

    <h2>Корзина</h2>

    <div class="basket-summary">

        <div class="summary-row">
            <span>Позиций:</span>
            <span id="basket-positions">0</span>
        </div>

        <div class="summary-row">
            <span>Всего товаров:</span>
            <span id="basket-quantity">0</span>
        </div>

        <div class="summary-row">
            <span>Сумма без скидки:</span>
            <span id="basket-total-before">0,00 ₽</span>
        </div>

        <div class="summary-row">
            <span>Скидка:</span>
            <span id="basket-discount">0,00 ₽</span>
        </div>

        <div class="summary-total">
            <span>К оплате:</span>
            <strong id="basket-total">0,00 ₽</strong>
        </div>

        <div class="basket-actions">

            <button id="create-order" type="button" disabled>
                Оформить заказ
            </button>

            <button id="clear-basket" type="button">
                Очистить корзину
            </button>

        </div>

    </div>

    <div class="basket-items-container">

        <table class="basket-table">
            <thead>
            <tr>
                <th>Товар</th>
                <th>Кол-во</th>
                <th>Сумма</th>
                <th></th>
            </tr>
            </thead>

            <tbody id="basket-items"></tbody>
        </table>

    </div>

</aside>
```

## 7. Текущий CSS корзины

``` css
.basket {
    position: sticky;
    top: 24px;
    right: 24px;
    width: 30%;
    max-width: 420px;
    max-height: calc(100vh - 48px);
    overflow-y: auto;

    margin-top: 24px;
    padding: 16px;

    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-sizing: border-box;
    z-index: 10;
}

.basket-summary {
    flex-shrink: 0;
}

.basket-items-container {
    overflow-y: auto;
    min-height: 0;
}
```
position: fixed пробовали, но результат оказался неудовлетворительным. Вернули position: sticky.

Корзину пока не изменяли в соответствии с новым эскизом.
8. Следующий шаг
   Пользователь предоставил HTML корзины и попросил продолжить оформление.

Ассистент запросил актуальные CSS-блоки:
- .basket
- .basket-summary
- .basket-items-container
- .basket-table
  Пользователь ещё не предоставил эти CSS-блоки.

Начать следующий чат с ожидания этих CSS-фрагментов.

Первым делом оформить внешний вид корзины как отдельной панели в той же цветовой гамме, что и каталог. Не менять HTML и JavaScript без необходимости.

