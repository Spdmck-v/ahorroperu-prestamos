<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% request.setAttribute("pageTitle","Cuotas"); %>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold">
        <i class="fas fa-calendar-check me-2 text-warning"></i>
        <c:choose>
            <c:when test="${not empty idPrestamo}">Cuotas del Préstamo #${idPrestamo}</c:when>
            <c:otherwise>Cuotas Vencidas</c:otherwise>
        </c:choose>
    </h4>
    <a href="${pageContext.request.contextPath}/app/prestamos" class="btn btn-outline-secondary btn-sm">
        <i class="fas fa-arrow-left me-1"></i>Préstamos
    </a>
</div>

<c:if test="${not empty param.msg and param.msg=='pagado'}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="fas fa-check-circle me-1"></i>Pago registrado correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-warning">
                    <tr>
                        <th>N°</th><th>Préstamo</th><th>Asociado</th>
                        <th>Vencimiento</th>
                        <th class="text-end">Capital</th>
                        <th class="text-end">Interés</th>
                        <th class="text-end">Total</th>
                        <th>Estado</th><th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty cuotas}">
                            <tr><td colspan="9" class="text-center text-muted py-4">
                                <i class="fas fa-check-circle text-success me-2"></i>No hay cuotas vencidas.
                            </td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="c" items="${cuotas}">
                            <tr class="${c.estado=='PAGADO'?'table-success':'table-danger'}">
                                <td>${c.numeroCuota}</td>
                                <td>#${c.idPrestamo}</td>
                                <td>${c.nombreAsociado}</td>
                                <td>${c.fechaVencimientoStr}</td>
                                <td class="text-end"><fmt:formatNumber value="${c.montoCapital}" pattern="#,##0.00"/></td>
                                <td class="text-end"><fmt:formatNumber value="${c.montoInteres}" pattern="#,##0.00"/></td>
                                <td class="text-end fw-bold"><fmt:formatNumber value="${c.montoTotal}" pattern="#,##0.00"/></td>
                                <td>
                                    <span class="badge ${c.estado=='PAGADO'?'bg-success':'bg-danger'}">${c.estado}</span>
                                </td>
                                <td>
                                    <c:if test="${c.estado=='PENDIENTE'}">
                                        <form action="${pageContext.request.contextPath}/app/cuotas" method="post" class="d-inline">
                                            <input type="hidden" name="idCuota"    value="${c.idCuota}">
                                            <input type="hidden" name="idPrestamo" value="${c.idPrestamo}">
                                            <button type="submit" class="btn btn-sm btn-success"
                                                    onclick="return confirm('¿Registrar pago?')">
                                                <i class="fas fa-check me-1"></i>Pagar
                                            </button>
                                        </form>
                                    </c:if>
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
