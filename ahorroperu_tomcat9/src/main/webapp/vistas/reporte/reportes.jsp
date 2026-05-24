<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("pageTitle","Reportes"); %>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h4 class="fw-bold"><i class="fas fa-chart-bar me-2 text-primary"></i>Reportes y Estadísticas</h4>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<!-- Descargas Excel -->
<div class="row g-3 mb-4">
    <div class="col-12">
        <h5 class="fw-bold text-secondary border-bottom pb-2">
            <i class="fas fa-file-excel me-2 text-success"></i>Reportes en Excel
        </h5>
    </div>
    <div class="col-md-4">
        <div class="card shadow-sm h-100">
            <div class="card-body text-center py-4">
                <i class="fas fa-file-spreadsheet fa-3x text-success mb-3"></i>
                <h6 class="fw-bold">Todas las Solicitudes</h6>
                <p class="text-muted small">Exporta el listado completo de solicitudes de préstamo con estado y detalle.</p>
                <a href="${pageContext.request.contextPath}/app/reportes?tipo=excel-solicitudes"
                   class="btn btn-success">
                    <i class="fas fa-download me-2"></i>Descargar Excel
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card shadow-sm h-100">
            <div class="card-body text-center py-4">
                <i class="fas fa-calendar-alt fa-3x text-primary mb-3"></i>
                <h6 class="fw-bold">Cuotas por Préstamo</h6>
                <p class="text-muted small">Ingresa el número de préstamo para descargar su cronograma de cuotas.</p>
                <form action="${pageContext.request.contextPath}/app/reportes" method="get" class="d-flex gap-2 justify-content-center">
                    <input type="hidden" name="tipo" value="excel-cuotas">
                    <input type="number" name="idPrestamo" class="form-control form-control-sm w-50"
                           placeholder="N° Préstamo" min="1" required>
                    <button type="submit" class="btn btn-primary btn-sm">
                        <i class="fas fa-download"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card shadow-sm h-100 border-info">
            <div class="card-body text-center py-4">
                <i class="fas fa-info-circle fa-3x text-info mb-3"></i>
                <h6 class="fw-bold">Acceso Rápido a Préstamos</h6>
                <p class="text-muted small">Ve al listado de préstamos para descargar Excel de cuotas desde cada fila.</p>
                <a href="${pageContext.request.contextPath}/app/prestamos" class="btn btn-info text-white">
                    <i class="fas fa-list me-2"></i>Ver Préstamos
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Gráficos -->
<div class="row g-3">
    <div class="col-12">
        <h5 class="fw-bold text-secondary border-bottom pb-2">
            <i class="fas fa-chart-pie me-2 text-primary"></i>Gráficos Estadísticos
        </h5>
    </div>

    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="fas fa-chart-pie me-2 text-warning"></i>Solicitudes por Estado
            </div>
            <div class="card-body text-center">
                <img src="${pageContext.request.contextPath}/app/reportes?tipo=grafico-estados"
                     alt="Gráfico solicitudes por estado"
                     class="img-fluid rounded"
                     onerror="this.src=''; this.alt='No hay datos suficientes para el gráfico.';">
            </div>
        </div>
    </div>

    <div class="col-md-7">
        <div class="card shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="fas fa-chart-bar me-2 text-primary"></i>Montos Aprobados por Tipo de Préstamo
            </div>
            <div class="card-body text-center">
                <img src="${pageContext.request.contextPath}/app/reportes?tipo=grafico-montos"
                     alt="Gráfico montos por tipo"
                     class="img-fluid rounded"
                     onerror="this.src=''; this.alt='No hay datos suficientes para el gráfico.';">
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp"/>
