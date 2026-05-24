<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    boolean esNuevo = request.getAttribute("asociado") == null;
    request.setAttribute("pageTitle", esNuevo ? "Nuevo Asociado" : "Editar Asociado");
%>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold">
        <i class="fas fa-${empty asociado ? 'user-plus' : 'user-edit'} me-2 text-primary"></i>
        ${empty asociado ? 'Nuevo Asociado' : 'Editar Asociado'}
    </h4>
    <a href="${pageContext.request.contextPath}/app/asociados" class="btn btn-outline-secondary">
        <i class="fas fa-arrow-left me-1"></i>Volver
    </a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/app/asociados" method="post" class="needs-validation" novalidate>
            <c:if test="${not empty asociado}">
                <input type="hidden" name="idAsociado" value="${asociado.idAsociado}">
            </c:if>

            <div class="row g-3">
                <!-- Datos personales -->
                <div class="col-12"><h6 class="text-primary fw-bold border-bottom pb-1">Datos Personales</h6></div>
                <div class="col-md-6">
                    <label class="form-label fw-semibold">Nombre <span class="text-danger">*</span></label>
                    <input type="text" name="nombre" class="form-control"
                           value="${asociado.nombre}" required maxlength="100">
                    <div class="invalid-feedback">Ingrese el nombre.</div>
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-semibold">Apellido <span class="text-danger">*</span></label>
                    <input type="text" name="apellido" class="form-control"
                           value="${asociado.apellido}" required maxlength="100">
                    <div class="invalid-feedback">Ingrese el apellido.</div>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">DNI <span class="text-danger">*</span></label>
                    <input type="text" name="dni" class="form-control"
                           value="${asociado.dni}" required maxlength="15"
                           ${not empty asociado ? 'readonly' : ''}>
                    <div class="invalid-feedback">Ingrese el DNI.</div>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Fecha de Nacimiento</label>
                    <input type="date" name="fechaNacimiento" class="form-control"
                           value="${asociado.fechaNacimiento}">
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Teléfono</label>
                    <input type="tel" name="telefono" class="form-control"
                           value="${asociado.telefono}" maxlength="20">
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Dirección</label>
                    <input type="text" name="direccion" class="form-control"
                           value="${asociado.direccion}" maxlength="255">
                </div>

                <!-- Acceso al sistema (solo en nuevo) -->
                <c:if test="${empty asociado}">
                    <div class="col-12"><h6 class="text-primary fw-bold border-bottom pb-1 mt-2">Acceso al Sistema</h6></div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Email <span class="text-danger">*</span></label>
                        <input type="email" name="email" class="form-control" required maxlength="150">
                        <div class="invalid-feedback">Ingrese un email válido.</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Contraseña <span class="text-danger">*</span></label>
                        <input type="password" name="password" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Mínimo 6 caracteres.</div>
                    </div>
                </c:if>

                <!-- Estado (solo en editar) -->
                <c:if test="${not empty asociado}">
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Estado</label>
                        <select name="estado" class="form-select">
                            <option value="ACTIVO"   ${asociado.estado=='ACTIVO'  ?'selected':''}>Activo</option>
                            <option value="INACTIVO" ${asociado.estado=='INACTIVO'?'selected':''}>Inactivo</option>
                        </select>
                    </div>
                </c:if>

                <div class="col-12 mt-3">
                    <button type="submit" class="btn btn-primary px-4">
                        <i class="fas fa-save me-2"></i>${empty asociado ? 'Registrar' : 'Actualizar'}
                    </button>
                    <a href="${pageContext.request.contextPath}/app/asociados" class="btn btn-outline-secondary ms-2">Cancelar</a>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
// Bootstrap validation
(function () {
    'use strict';
    document.querySelectorAll('.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();
</script>

<jsp:include page="/WEB-INF/footer.jsp"/>
