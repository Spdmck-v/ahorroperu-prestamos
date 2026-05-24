<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("pageTitle","Dashboard");
    com.ahorroperu.modelo.Usuario u = (com.ahorroperu.modelo.Usuario) session.getAttribute("usuarioSesion");
%>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h4 class="fw-bold"><i class="fas fa-tachometer-alt me-2 text-primary"></i>Dashboard</h4>
    <span class="text-muted small">Bienvenido/a, <strong><%= u != null ? u.getNombreCompleto() : "" %></strong></span>
</div>

<% if (u != null && u.isAdmin()) { %>
<!-- TARJETAS ADMIN -->
<div class="row g-3 mb-4">
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100 card-stat bg-primary text-white">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="text-white-50 small">Asociados Activos</div>
                    <div class="fs-2 fw-bold">${totalAsociados}</div>
                </div>
                <i class="fas fa-users fa-2x opacity-50"></i>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/app/asociados" class="text-white text-decoration-none small">
                    Ver todos <i class="fas fa-arrow-right ms-1"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100 card-stat bg-warning text-dark">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="text-dark-50 small">Solicitudes Pendientes</div>
                    <div class="fs-2 fw-bold">${totalPendientes}</div>
                </div>
                <i class="fas fa-clock fa-2x opacity-50"></i>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/app/solicitudes?estado=PENDIENTE" class="text-dark text-decoration-none small">
                    Revisar <i class="fas fa-arrow-right ms-1"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100 card-stat bg-success text-white">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="text-white-50 small">Préstamos Vigentes</div>
                    <div class="fs-2 fw-bold">${totalPrestamos}</div>
                </div>
                <i class="fas fa-hand-holding-usd fa-2x opacity-50"></i>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/app/prestamos" class="text-white text-decoration-none small">
                    Ver todos <i class="fas fa-arrow-right ms-1"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm h-100 card-stat bg-danger text-white">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="text-white-50 small">Cuotas Vencidas</div>
                    <div class="fs-2 fw-bold">${cuotasVencidas}</div>
                </div>
                <i class="fas fa-exclamation-triangle fa-2x opacity-50"></i>
            </div>
            <div class="card-footer bg-transparent border-0">
                <a href="${pageContext.request.contextPath}/app/cuotas" class="text-white text-decoration-none small">
                    Ver cuotas <i class="fas fa-arrow-right ms-1"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Solicitudes pendientes recientes -->
<div class="card shadow-sm mb-4">
    <div class="card-header bg-white fw-bold">
        <i class="fas fa-bell me-2 text-warning"></i>Solicitudes Pendientes de Revisión
    </div>
    <div class="card-body p-0">
        <c:choose>
            <c:when test="${empty ultimasSolicitudes}">
                <p class="text-muted text-center py-4"><i class="fas fa-check-circle text-success me-2"></i>No hay solicitudes pendientes.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>#</th><th>Asociado</th><th>Tipo</th>
                                <th>Monto</th><th>Plazo</th><th>Fecha</th><th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${ultimasSolicitudes}">
                            <tr>
                                <td>${s.idSolicitud}</td>
                                <td>${s.nombreAsociado}</td>
                                <td>${s.nombreTipo}</td>
                                <td class="text-end">S/. <fmt:formatNumber value="${s.montoSolicitado}" pattern="#,##0.00"/></td>
                                <td>${s.plazoMeses} m.</td>
                                <td>${s.fechaSolicitudStr}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/app/solicitudes?accion=ver&id=${s.idSolicitud}"
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="fas fa-eye"></i> Revisar
                                    </a>
                                </td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Accesos rápidos -->
<div class="row g-3">
    <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/app/reportes" class="card shadow-sm text-decoration-none text-dark h-100 hover-card">
            <div class="card-body text-center py-4">
                <i class="fas fa-chart-pie fa-3x text-primary mb-2"></i>
                <div class="fw-semibold">Ver Reportes</div>
                <div class="text-muted small">Gráficos y Excel</div>
            </div>
        </a>
    </div>
    <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/app/asociados?accion=nuevo" class="card shadow-sm text-decoration-none text-dark h-100 hover-card">
            <div class="card-body text-center py-4">
                <i class="fas fa-user-plus fa-3x text-success mb-2"></i>
                <div class="fw-semibold">Nuevo Asociado</div>
                <div class="text-muted small">Registrar asociado</div>
            </div>
        </a>
    </div>
    <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/app/cuotas" class="card shadow-sm text-decoration-none text-dark h-100 hover-card">
            <div class="card-body text-center py-4">
                <i class="fas fa-calendar-check fa-3x text-warning mb-2"></i>
                <div class="fw-semibold">Cuotas Vencidas</div>
                <div class="text-muted small">Gestionar pagos</div>
            </div>
        </a>
    </div>
</div>

<% } else { %>
<!-- VISTA ASOCIADO -->
<div class="row g-3 mb-4">
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-white fw-bold"><i class="fas fa-file-alt me-2 text-primary"></i>Mis Solicitudes</div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty misSolicitudes}">
                        <p class="text-muted text-center py-4">No tienes solicitudes aún.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-sm table-hover mb-0">
                                <thead class="table-light"><tr><th>#</th><th>Tipo</th><th>Monto</th><th>Estado</th></tr></thead>
                                <tbody>
                                    <c:forEach var="s" items="${misSolicitudes}">
                                    <tr>
                                        <td>${s.idSolicitud}</td>
                                        <td>${s.nombreTipo}</td>
                                        <td>S/. <fmt:formatNumber value="${s.montoSolicitado}" pattern="#,##0.00"/></td>
                                        <td>
                                            <span class="badge ${s.estado=='APROBADO'?'bg-success':s.estado=='RECHAZADO'?'bg-danger':'bg-warning text-dark'}">
                                                ${s.estado}
                                            </span>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-footer bg-white">
                <a href="${pageContext.request.contextPath}/app/solicitudes?accion=nueva" class="btn btn-primary btn-sm">
                    <i class="fas fa-plus me-1"></i>Nueva Solicitud
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-white fw-bold"><i class="fas fa-hand-holding-usd me-2 text-success"></i>Mis Préstamos</div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty misPrestamos}">
                        <p class="text-muted text-center py-4">No tienes préstamos activos.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-sm table-hover mb-0">
                                <thead class="table-light"><tr><th>#</th><th>Tipo</th><th>Monto</th><th>Estado</th></tr></thead>
                                <tbody>
                                    <c:forEach var="p" items="${misPrestamos}">
                                    <tr>
                                        <td>${p.idPrestamo}</td>
                                        <td>${p.nombreTipo}</td>
                                        <td>S/. <fmt:formatNumber value="${p.montoAprobado}" pattern="#,##0.00"/></td>
                                        <td><span class="badge bg-success">${p.estado}</span></td>
                                    </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<% } %>

<jsp:include page="/WEB-INF/footer.jsp"/>
