<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% request.setAttribute("pageTitle","Préstamos"); %>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-hand-holding-usd me-2 text-success"></i>Préstamos</h4>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-success">
                    <tr>
                        <th>#</th><th>Asociado</th><th>Tipo</th>
                        <th>Monto (S/.)</th><th>Plazo</th><th>Tasa</th>
                        <th>F. Inicio</th><th>F. Fin</th><th>Estado</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty lista}">
                            <tr><td colspan="10" class="text-center text-muted py-4">No hay préstamos registrados.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="p" items="${lista}">
                            <tr>
                                <td>${p.idPrestamo}</td>
                                <td>${p.nombreAsociado}<br><small class="text-muted">${p.dniAsociado}</small></td>
                                <td>${p.nombreTipo}</td>
                                <td class="text-end fw-semibold">
                                    <fmt:formatNumber value="${p.montoAprobado}" pattern="#,##0.00"/>
                                </td>
                                <td>${p.plazoMeses} m.</td>
                                <td>${p.tasaInteres}%</td>
                                <td>${p.fechaInicioStr}</td>
                                <td>${p.fechaFinStr}</td>
                                <td>
                                    <span class="badge ${p.estado=='VIGENTE'?'bg-success':p.estado=='MORA'?'bg-danger':'bg-secondary'}">
                                        ${p.estado}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/app/prestamos?accion=ver&id=${p.idPrestamo}"
                                       class="btn btn-sm btn-outline-success me-1" title="Ver cuotas">
                                        <i class="fas fa-list-ol"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/app/reportes?tipo=excel-cuotas&idPrestamo=${p.idPrestamo}"
                                       class="btn btn-sm btn-outline-secondary" title="Descargar Excel">
                                        <i class="fas fa-file-excel"></i>
                                    </a>
                                </td>
                            </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/footer.jsp"/>
