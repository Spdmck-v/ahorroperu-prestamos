<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("pageTitle","Solicitudes de Préstamo");
    com.ahorroperu.modelo.Usuario u = (com.ahorroperu.modelo.Usuario) session.getAttribute("usuarioSesion");
%>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-file-alt me-2 text-primary"></i>Solicitudes de Préstamo</h4>
    <a href="${pageContext.request.contextPath}/app/solicitudes?accion=nueva" class="btn btn-primary">
        <i class="fas fa-plus me-1"></i>Nueva Solicitud
    </a>
</div>

<c:if test="${not empty param.msg}">
    <div class="alert alert-success alert-dismissible fade show">
        <c:choose>
            <c:when test="${param.msg=='creado'}">Solicitud enviada correctamente.</c:when>
            <c:when test="${param.msg=='aprobado'}">Solicitud aprobada. Préstamo y cuotas generados.</c:when>
            <c:when test="${param.msg=='rechazado'}">Solicitud rechazada.</c:when>
            <c:when test="${param.msg=='eliminado'}">Solicitud eliminada.</c:when>
            <c:when test="${param.msg=='actualizado'}">Solicitud actualizada.</c:when>
        </c:choose>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- Filtros (solo admin) -->
<% if (u != null && u.isAdmin()) { %>
<div class="mb-3 d-flex gap-2 flex-wrap">
    <a href="${pageContext.request.contextPath}/app/solicitudes" class="btn btn-sm ${empty param.estado?'btn-primary':'btn-outline-primary'}">Todas</a>
    <a href="${pageContext.request.contextPath}/app/solicitudes?estado=PENDIENTE" class="btn btn-sm ${param.estado=='PENDIENTE'?'btn-warning':'btn-outline-warning'}">Pendientes</a>
    <a href="${pageContext.request.contextPath}/app/solicitudes?estado=APROBADO" class="btn btn-sm ${param.estado=='APROBADO'?'btn-success':'btn-outline-success'}">Aprobadas</a>
    <a href="${pageContext.request.contextPath}/app/solicitudes?estado=RECHAZADO" class="btn btn-sm ${param.estado=='RECHAZADO'?'btn-danger':'btn-outline-danger'}">Rechazadas</a>
</div>
<% } %>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-primary">
                    <tr>
                        <th>#</th>
                        <% if (u != null && u.isAdmin()) { %><th>Asociado</th><% } %>
                        <th>Tipo</th><th>Monto (S/.)</th><th>Plazo</th>
                        <th>Estado</th><th>Fecha</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty lista}">
                            <tr><td colspan="8" class="text-center text-muted py-4">No hay solicitudes.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="s" items="${lista}">
                            <tr>
                                <td>${s.idSolicitud}</td>
                                <% if (u != null && u.isAdmin()) { %>
                                <td>${s.nombreAsociado}<br><small class="text-muted">${s.dniAsociado}</small></td>
                                <% } %>
                                <td>${s.nombreTipo}</td>
                                <td class="text-end fw-semibold">
                                    <fmt:formatNumber value="${s.montoSolicitado}" pattern="#,##0.00"/>
                                </td>
                                <td>${s.plazoMeses} m.</td>
                                <td>
                                    <span class="badge ${s.estado=='APROBADO'?'bg-success':s.estado=='RECHAZADO'?'bg-danger':'bg-warning text-dark'}">
                                        <i class="fas ${s.estado=='APROBADO'?'fa-check':s.estado=='RECHAZADO'?'fa-times':'fa-clock'} me-1"></i>
                                        ${s.estado}
                                    </span>
                                </td>
                                <td><small>${s.fechaSolicitud}</small></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/app/solicitudes?accion=ver&id=${s.idSolicitud}"
                                       class="btn btn-sm btn-outline-primary me-1"><i class="fas fa-eye"></i></a>
                                    <c:if test="${s.estado=='PENDIENTE'}">
                                        <a href="${pageContext.request.contextPath}/app/solicitudes?accion=eliminar&id=${s.idSolicitud}"
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('¿Eliminar solicitud?')"><i class="fas fa-trash"></i></a>
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
