/* ============================================================
   AhorroPeru - Scripts personalizados
   ============================================================ */

// Auto-cerrar alertas de éxito después de 4 segundos
document.addEventListener('DOMContentLoaded', function () {

    // Auto-dismiss alertas de éxito
    document.querySelectorAll('.alert-success, .alert-info').forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) bsAlert.close();
        }, 4000);
    });

    // Activar tooltips Bootstrap
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function (el) {
        new bootstrap.Tooltip(el);
    });

    // Marcar nav-link activo según URL actual
    const path = window.location.pathname;
    document.querySelectorAll('.navbar-nav .nav-link').forEach(function (link) {
        if (link.href && path.includes(link.getAttribute('href'))) {
            link.classList.add('active');
        }
    });

    // Confirmación doble para eliminar/desactivar
    document.querySelectorAll('[data-confirm]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            if (!confirm(btn.getAttribute('data-confirm'))) {
                e.preventDefault();
            }
        });
    });

    // Formatear inputs numéricos de monto al perder foco
    document.querySelectorAll('input[name="monto"]').forEach(function (input) {
        input.addEventListener('blur', function () {
            const val = parseFloat(input.value);
            if (!isNaN(val)) input.value = val.toFixed(2);
        });
    });
});
