const filterCheckbox = document.getElementById('show-inactive-orders');
const orderRows = document.querySelectorAll('tbody tr[data-status]');

function filterOrders() {
    if (!filterCheckbox) {
        return;
    }
    const showInactive = filterCheckbox.checked;

    orderRows.forEach(row => {
        const status = row.dataset.status;

        const isInactive =
            status === 'COMPLETED' || status === 'CANCELLED' || status === 'INVALID';

        row.hidden = isInactive && !showInactive;
    });
}

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


