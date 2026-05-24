<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("pageTitle","Asociados"); %>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-users me-2 text-primary"></i>Asociados</h4>
    <a href="${pageContext.request.contextPath}/app/asociados?accion=nuevo" class="btn btn-primary">
        <i class="fas fa-user-plus me-1"></i>Nuevo Asociado
    </a>
</div>

<c:if test="${not empty param.msg}">
    <div class="alert alert-success alert-dismissible fade show">
        <c:choose>
            <c:when test="${param.msg=='creado'}"><i class="fas fa-check-circle me-1"></i>Asociado registrado correctamente.</c:when>
            <c:when test="${param.msg=='actualizado'}"><i class="fas fa-check-circle me-1"></i>Asociado actualizado.</c:when>
            <c:when test="${param.msg=='eliminado'}"><i class="fas fa-check-circle me-1"></i>Asociado desactivado.</c:when>
        </c:choose>
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
                <thead class="table-primary">
                    <tr>
                        <th>#</th><th>Nombre</th><th>DNI</th><th>Email</th>
                        <th>Teléfono</th><th>Estado</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty lista}">
                            <tr><td colspan="7" class="text-center text-muted py-4">No hay asociados registrados.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="a" items="${lista}">
                            <tr>
                                <td>${a.idAsociado}</td>
                                <td><i class="fas fa-user-circle text-secondary me-1"></i>${a.nombreCompleto}</td>
                                <td>${a.dni}</td>
                                <td>${a.email}</td>
                                <td>${a.telefono}</td>
                                <td>
                                    <span class="badge ${a.estado=='ACTIVO'?'bg-success':'bg-secondary'}">${a.estado}</span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/app/asociados?accion=editar&id=${a.idAsociado}"
                                       class="btn btn-sm btn-outline-primary me-1" title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/app/solicitudes?idAsociado=${a.idAsociado}"
                                       class="btn btn-sm btn-outline-info me-1" title="Ver solicitudes">
                                        <i class="fas fa-file-alt"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/app/asociados?accion=eliminar&id=${a.idAsociado}"
                                       class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('¿Desactivar este asociado?')" title="Desactivar">
                                        <i class="fas fa-user-slash"></i>
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
