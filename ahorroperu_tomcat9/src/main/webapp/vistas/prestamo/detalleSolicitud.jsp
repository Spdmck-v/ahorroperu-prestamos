<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("pageTitle","Detalle Solicitud");
    com.ahorroperu.modelo.Usuario u = (com.ahorroperu.modelo.Usuario) session.getAttribute("usuarioSesion");
%>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-file-alt me-2 text-primary"></i>Detalle de Solicitud #${solicitud.idSolicitud}</h4>
    <a href="${pageContext.request.contextPath}/app/solicitudes" class="btn btn-outline-secondary">
        <i class="fas fa-arrow-left me-1"></i>Volver
    </a>
</div>

<div class="row g-4">
    <!-- Información principal -->
    <div class="col-lg-7">
        <div class="card shadow-sm mb-3">
            <div class="card-header bg-white fw-bold">
                <i class="fas fa-info-circle me-2 text-primary"></i>Información de la Solicitud
            </div>
            <div class="card-body">
                <dl class="row mb-0">
                    <dt class="col-sm-4 text-muted">Asociado</dt>
                    <dd class="col-sm-8 fw-semibold">${solicitud.nombreAsociado} <span class="text-muted">(DNI: ${solicitud.dniAsociado})</span></dd>

                    <dt class="col-sm-4 text-muted">Tipo Préstamo</dt>
                    <dd class="col-sm-8">${solicitud.nombreTipo}</dd>

                    <dt class="col-sm-4 text-muted">Monto Solicitado</dt>
                    <dd class="col-sm-8 fw-bold text-success fs-5">
                        S/. <fmt:formatNumber value="${solicitud.montoSolicitado}" pattern="#,##0.00"/>
                    </dd>

                    <dt class="col-sm-4 text-muted">Plazo</dt>
                    <dd class="col-sm-8">${solicitud.plazoMeses} meses</dd>

                    <dt class="col-sm-4 text-muted">Tasa de Interés</dt>
                    <dd class="col-sm-8">${solicitud.tasaInteres}% anual</dd>

                    <dt class="col-sm-4 text-muted">Propósito</dt>
                    <dd class="col-sm-8">${not empty solicitud.proposito ? solicitud.proposito : '<span class="text-muted">No especificado</span>'}</dd>

                    <dt class="col-sm-4 text-muted">Fecha Solicitud</dt>
                    <dd class="col-sm-8">${solicitud.fechaSolicitud}</dd>

                    <dt class="col-sm-4 text-muted">Estado</dt>
                    <dd class="col-sm-8">
                        <span class="badge fs-6 ${solicitud.estado=='APROBADO'?'bg-success':solicitud.estado=='RECHAZADO'?'bg-danger':'bg-warning text-dark'}">
                            <i class="fas ${solicitud.estado=='APROBADO'?'fa-check-circle':solicitud.estado=='RECHAZADO'?'fa-times-circle':'fa-clock'} me-1"></i>
                            ${solicitud.estado}
                        </span>
                    </dd>

                    <c:if test="${not empty solicitud.nombreAdmin}">
                        <dt class="col-sm-4 text-muted">Revisado por</dt>
                        <dd class="col-sm-8">${solicitud.nombreAdmin} — ${solicitud.fechaResolucion}</dd>
                        <dt class="col-sm-4 text-muted">Observación</dt>
                        <dd class="col-sm-8">${solicitud.observacion}</dd>
                    </c:if>
                </dl>
            </div>
        </div>

        <!-- Soportes adjuntos -->
        <div class="card shadow-sm">
            <div class="card-header bg-white fw-bold">
                <i class="fas fa-paperclip me-2 text-secondary"></i>Documentos Adjuntos
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty soportes}">
                        <p class="text-muted mb-0"><i class="fas fa-folder-open me-1"></i>No hay documentos adjuntos.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="list-group list-group-flush">
                            <c:forEach var="s" items="${soportes}">
                                <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                    <span><i class="fas fa-file me-2 text-primary"></i>${s.nombreArchivo}</span>
                                    <small class="text-muted">${s.tipoArchivo}</small>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Panel de acciones (solo admin y pendiente) -->
    <div class="col-lg-5">
        <% if (u != null && u.isAdmin()) { %>
        <c:if test="${solicitud.estado == 'PENDIENTE'}">
            <!-- APROBAR -->
            <div class="card shadow-sm border-success mb-3">
                <div class="card-header bg-success text-white fw-bold">
                    <i class="fas fa-check-circle me-2"></i>Aprobar Solicitud
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/app/solicitudes" method="post">
                        <input type="hidden" name="accion" value="aprobar">
                        <input type="hidden" name="idSolicitud" value="${solicitud.idSolicitud}">
                        <div class="mb-3">
                            <label class="form-label">Observación (opcional)</label>
                            <textarea name="observacion" class="form-control" rows="3"
                                      placeholder="Condiciones de aprobación..."></textarea>
                        </div>
                        <p class="text-muted small">
                            <i class="fas fa-info-circle me-1"></i>
                            Al aprobar se generarán <strong>${solicitud.plazoMeses} cuotas</strong> automáticamente.
                        </p>
                        <button type="submit" class="btn btn-success w-100"
                                onclick="return confirm('¿Confirmar aprobación?')">
                            <i class="fas fa-check me-2"></i>Aprobar y Generar Cuotas
                        </button>
                    </form>
                </div>
            </div>

            <!-- RECHAZAR -->
            <div class="card shadow-sm border-danger">
                <div class="card-header bg-danger text-white fw-bold">
                    <i class="fas fa-times-circle me-2"></i>Rechazar Solicitud
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/app/solicitudes" method="post">
                        <input type="hidden" name="accion" value="rechazar">
                        <input type="hidden" name="idSolicitud" value="${solicitud.idSolicitud}">
                        <div class="mb-3">
                            <label class="form-label">Motivo de Rechazo <span class="text-danger">*</span></label>
                            <textarea name="observacion" class="form-control" rows="3"
                                      placeholder="Indique el motivo..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-danger w-100"
                                onclick="return confirm('¿Confirmar rechazo?')">
                            <i class="fas fa-times me-2"></i>Rechazar Solicitud
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
        <% } %>

        <!-- Simulador de cuota -->
        <div class="card shadow-sm bg-light border-0 mt-3">
            <div class="card-body">
                <h6 class="fw-bold text-primary"><i class="fas fa-calculator me-2"></i>Resumen Financiero</h6>
                <table class="table table-sm table-borderless mb-0">
                    <tr>
                        <td class="text-muted">Monto:</td>
                        <td class="text-end fw-semibold">
                            S/. <fmt:formatNumber value="${solicitud.montoSolicitado}" pattern="#,##0.00"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-muted">Tasa anual:</td>
                        <td class="text-end">${solicitud.tasaInteres}%</td>
                    </tr>
                    <tr>
                        <td class="text-muted">Tasa mensual:</td>
                        <td class="text-end" id="tasaMens">—</td>
                    </tr>
                    <tr class="table-success">
                        <td class="fw-bold">Cuota estimada:</td>
                        <td class="text-end fw-bold" id="cuotaEst">—</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
(function(){
    const monto = ${solicitud.montoSolicitado};
    const plazo = ${solicitud.plazoMeses};
    const tasaA = ${solicitud.tasaInteres};
    const tasaM = tasaA / 12 / 100;
    let cuota;
    if (tasaM === 0) {
        cuota = monto / plazo;
    } else {
        const f = Math.pow(1 + tasaM, plazo);
        cuota = monto * tasaM * f / (f - 1);
    }
    const fmt = v => 'S/. ' + v.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    document.getElementById('tasaMens').textContent = (tasaM*100).toFixed(4) + '%';
    document.getElementById('cuotaEst').textContent = fmt(cuota);
})();
</script>

<jsp:include page="/WEB-INF/footer.jsp"/>
