<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("pageTitle","Detalle Préstamo");
    com.ahorroperu.modelo.Usuario u = (com.ahorroperu.modelo.Usuario) session.getAttribute("usuarioSesion");
%>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-hand-holding-usd me-2 text-success"></i>
        Préstamo #${prestamo.idPrestamo} — ${prestamo.nombreAsociado}
    </h4>
    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/app/reportes?tipo=excel-cuotas&idPrestamo=${prestamo.idPrestamo}"
           class="btn btn-outline-success btn-sm">
            <i class="fas fa-file-excel me-1"></i>Excel Cuotas
        </a>
        <a href="${pageContext.request.contextPath}/app/prestamos" class="btn btn-outline-secondary btn-sm">
            <i class="fas fa-arrow-left me-1"></i>Volver
        </a>
    </div>
</div>

<!-- Resumen -->
<div class="row g-3 mb-4">
    <div class="col-md-3">
        <div class="card border-0 bg-success text-white shadow-sm">
            <div class="card-body text-center py-3">
                <div class="small text-white-50">Monto Aprobado</div>
                <div class="fw-bold fs-5">S/. <fmt:formatNumber value="${prestamo.montoAprobado}" pattern="#,##0.00"/></div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 bg-primary text-white shadow-sm">
            <div class="card-body text-center py-3">
                <div class="small text-white-50">Tipo</div>
                <div class="fw-bold">${prestamo.nombreTipo}</div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 bg-info text-white shadow-sm">
            <div class="card-body text-center py-3">
                <div class="small text-white-50">Plazo / Tasa</div>
                <div class="fw-bold">${prestamo.plazoMeses} m. / ${prestamo.tasaInteres}%</div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 ${prestamo.estado=='VIGENTE'?'bg-success':prestamo.estado=='MORA'?'bg-danger':'bg-secondary'} text-white shadow-sm">
            <div class="card-body text-center py-3">
                <div class="small text-white-50">Estado</div>
                <div class="fw-bold">${prestamo.estado}</div>
            </div>
        </div>
    </div>
</div>

<!-- Tabla de cuotas -->
<div class="card shadow-sm">
    <div class="card-header bg-white fw-bold">
        <i class="fas fa-list-ol me-2 text-primary"></i>Cronograma de Cuotas
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr>
                        <th>N°</th><th>Vencimiento</th>
                        <th class="text-end">Capital (S/.)</th>
                        <th class="text-end">Interés (S/.)</th>
                        <th class="text-end">Total (S/.)</th>
                        <th>Estado</th><th>Fecha Pago</th>
                        <% if (u != null && u.isAdmin()) { %><th>Acción</th><% } %>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${cuotas}">
                    <tr class="${c.estado=='PAGADO'?'table-success':c.estado=='VENCIDO'?'table-danger':''}">
                        <td class="fw-semibold">${c.numeroCuota}</td>
                        <td>${c.fechaVencimientoStr}</td>
                        <td class="text-end"><fmt:formatNumber value="${c.montoCapital}" pattern="#,##0.00"/></td>
                        <td class="text-end"><fmt:formatNumber value="${c.montoInteres}" pattern="#,##0.00"/></td>
                        <td class="text-end fw-semibold"><fmt:formatNumber value="${c.montoTotal}" pattern="#,##0.00"/></td>
                        <td>
                            <span class="badge ${c.estado=='PAGADO'?'bg-success':c.estado=='VENCIDO'?'bg-danger':'bg-warning text-dark'}">
                                ${c.estado}
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty c.fechaPago}">
                                    <small>${c.fechaPagoStr}</small>
                                </c:when>
                                <c:otherwise><span class="text-muted">—</span></c:otherwise>
                            </c:choose>
                        </td>
                        <% if (u != null && u.isAdmin()) { %>
                        <td>
                            <c:if test="${c.estado=='PENDIENTE'}">
                                <form action="${pageContext.request.contextPath}/app/cuotas" method="post" class="d-inline">
                                    <input type="hidden" name="idCuota"    value="${c.idCuota}">
                                    <input type="hidden" name="idPrestamo" value="${prestamo.idPrestamo}">
                                    <button type="submit" class="btn btn-sm btn-success"
                                            onclick="return confirm('¿Registrar pago de cuota ${c.numeroCuota}?')">
                                        <i class="fas fa-check me-1"></i>Pagar
                                    </button>
                                </form>
                            </c:if>
                        </td>
                        <% } %>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp"/>
