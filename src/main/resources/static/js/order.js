const statusTrigger = document.getElementById('order-status-trigger');
const statusMenu = document.getElementById('status-dropdown-menu');

if (statusTrigger && statusMenu) {
    const dropdown = statusTrigger.closest('.status-dropdown');

    const allowedActions = dropdown.dataset.allowedActions
        .split(',')
         .map(action => action.trim())
        .filter(action => action.length > 0);

    const actionLabels = {
        START: 'Начать выполнение',
        MARK_READY: 'Отметить как готовый',
        COMPLETE: 'Завершить заказ',
        CANCEL: 'Отменить заказ',
        INVALIDATE: 'Пометить как недействительный',
        MARK_OPEN: 'Отметить открытым'
    };

    statusTrigger.addEventListener('click', () => {
        statusMenu.hidden = !statusMenu.hidden;
    });

    allowedActions.forEach(action => {
        const button = document.createElement('button');

        button.type = 'button';
        button.textContent = actionLabels[action] ?? action;
        button.dataset.action = action;


        button.addEventListener('click', () => {

            const targetStatuses = {
                START: 'INPROGRESS',
                MARK_READY: 'READY',
                COMPLETE: 'COMPLETED',
                CANCEL: 'CANCELLED',
                MARK_OPEN: 'OPEN',
                INVALIDATE: 'INVALID'
            };
            const targetStatus = targetStatuses[action];
            if (!targetStatus) {
                return;
            }
            /*
            if (action !== 'START') {
                            return;
                      }
              */
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = `/orders/${dropdown.dataset.orderId}/status`;

            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'status';
//            statusInput.value = 'INPROGRESS';
            statusInput.value = targetStatus;

            form.appendChild(statusInput);
            document.body.appendChild(form);
            form.submit();
        });

        statusMenu.appendChild(button);
    });
}