<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% request.setAttribute("pageTitle","Nueva Solicitud"); %>
<jsp:include page="/WEB-INF/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold"><i class="fas fa-file-medical me-2 text-primary"></i>
        ${empty solicitud ? 'Nueva Solicitud de Préstamo' : 'Editar Solicitud'}
    </h4>
    <a href="${pageContext.request.contextPath}/app/solicitudes" class="btn btn-outline-secondary">
        <i class="fas fa-arrow-left me-1"></i>Volver
    </a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="row g-4">
    <div class="col-lg-7">
        <div class="card shadow-sm">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/app/solicitudes" method="post"
                      class="needs-validation" novalidate id="formSolicitud">
                    <c:if test="${not empty solicitud}">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="idSolicitud" value="${solicitud.idSolicitud}">
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Tipo de Préstamo <span class="text-danger">*</span></label>
                        <select name="idTipo" id="idTipo" class="form-select" required onchange="actualizarInfo()">
                            <option value="">-- Seleccione --</option>
                            <c:forEach var="t" items="${tipos}">
                                <option value="${t.idTipo}"
                                    data-tasa="${t.tasaInteres}"
                                    data-plazo="${t.plazoMaxMeses}"
                                    data-monto="${t.montoMax}"
                                    ${solicitud.idTipo==t.idTipo?'selected':''}>
                                    ${t.nombre} (${t.tasaInteres}% anual)
                                </option>
                            </c:forEach>
                        </select>
                        <div class="invalid-feedback">Seleccione un tipo.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Monto Solicitado (S/.) <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text">S/.</span>
                            <input type="number" name="monto" id="monto" class="form-control"
                                   value="${solicitud.montoSolicitado}" step="0.01" min="100" required
                                   oninput="calcularCuota()">
                        </div>
                        <div class="form-text text-muted" id="montoInfo"></div>
                        <div class="invalid-feedback">Ingrese un monto válido.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Plazo (meses) <span class="text-danger">*</span></label>
                        <input type="number" name="plazo" id="plazo" class="form-control"
                               value="${solicitud.plazoMeses}" min="1" required oninput="calcularCuota()">
                        <div class="form-text text-muted" id="plazoInfo"></div>
                        <div class="invalid-feedback">Ingrese el plazo.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Propósito del Préstamo</label>
                        <textarea name="proposito" class="form-control" rows="3" maxlength="500"
                                  placeholder="Describa el destino del préstamo...">${solicitud.proposito}</textarea>
                    </div>

                    <button type="submit" class="btn btn-primary px-4">
                        <i class="fas fa-paper-plane me-2"></i>${empty solicitud ? 'Enviar Solicitud' : 'Actualizar'}
                    </button>
                    <a href="${pageContext.request.contextPath}/app/solicitudes" class="btn btn-outline-secondary ms-2">Cancelar</a>
                </form>
            </div>
        </div>
    </div>

    <!-- Panel de información -->
    <div class="col-lg-5">
        <div class="card shadow-sm bg-light border-0">
            <div class="card-body">
                <h6 class="fw-bold text-primary mb-3"><i class="fas fa-calculator me-2"></i>Simulador de Cuota</h6>
                <div id="simulador" class="text-muted text-center py-3">
                    <i class="fas fa-arrow-left fa-2x mb-2 d-block"></i>
                    Seleccione tipo, monto y plazo para ver la cuota estimada.
                </div>
                <div id="resultSimulador" style="display:none">
                    <table class="table table-sm table-borderless mb-0">
                        <tr><td class="text-muted">Tasa mensual:</td><td id="rTasaMensual" class="fw-semibold text-end"></td></tr>
                        <tr><td class="text-muted">Cuota mensual:</td><td id="rCuota" class="fw-semibold text-end text-success fs-5"></td></tr>
                        <tr><td class="text-muted">Total a pagar:</td><td id="rTotal" class="fw-semibold text-end"></td></tr>
                        <tr><td class="text-muted">Total intereses:</td><td id="rIntereses" class="fw-semibold text-end text-danger"></td></tr>
                    </table>
                </div>

                <hr>
                <h6 class="fw-bold text-primary mb-2"><i class="fas fa-info-circle me-2"></i>Condiciones del Tipo</h6>
                <div id="infotipo" class="text-muted small">Seleccione un tipo de préstamo.</div>
            </div>
        </div>
    </div>
</div>

<script>
function actualizarInfo() {
    const sel = document.getElementById('idTipo');
    const opt = sel.options[sel.selectedIndex];
    if (!opt || !opt.value) {
        document.getElementById('infotipo').innerHTML = 'Seleccione un tipo de préstamo.';
        document.getElementById('plazoInfo').textContent = '';
        document.getElementById('montoInfo').textContent = '';
        return;
    }
    const tasa   = opt.getAttribute('data-tasa');
    const plazo  = opt.getAttribute('data-plazo');
    const monto  = opt.getAttribute('data-monto');
    document.getElementById('plazo').max = plazo;
    document.getElementById('monto').max = monto;
    document.getElementById('plazoInfo').textContent = 'Máximo: ' + plazo + ' meses';
    document.getElementById('montoInfo').textContent = 'Máximo: S/. ' + parseFloat(monto).toLocaleString('es-PE',{minimumFractionDigits:2});
    document.getElementById('infotipo').innerHTML =
        '<b>Tasa anual:</b> ' + tasa + '%<br>' +
        '<b>Plazo máximo:</b> ' + plazo + ' meses<br>' +
        '<b>Monto máximo:</b> S/. ' + parseFloat(monto).toLocaleString('es-PE',{minimumFractionDigits:2});
    calcularCuota();
}

function calcularCuota() {
    const sel   = document.getElementById('idTipo');
    const opt   = sel.options[sel.selectedIndex];
    const monto = parseFloat(document.getElementById('monto').value);
    const plazo = parseInt(document.getElementById('plazo').value);
    if (!opt || !opt.value || !monto || !plazo) {
        document.getElementById('resultSimulador').style.display='none';
        document.getElementById('simulador').style.display='block';
        return;
    }
    const tasaAnual  = parseFloat(opt.getAttribute('data-tasa'));
    const tasaMensual = tasaAnual / 12 / 100;
    let cuota;
    if (tasaMensual === 0) {
        cuota = monto / plazo;
    } else {
        const factor = Math.pow(1 + tasaMensual, plazo);
        cuota = monto * tasaMensual * factor / (factor - 1);
    }
    const total     = cuota * plazo;
    const intereses = total - monto;
    const fmt = v => 'S/. ' + v.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    document.getElementById('rTasaMensual').textContent = (tasaMensual*100).toFixed(4) + '%';
    document.getElementById('rCuota').textContent       = fmt(cuota);
    document.getElementById('rTotal').textContent       = fmt(total);
    document.getElementById('rIntereses').textContent   = fmt(intereses);
    document.getElementById('simulador').style.display      = 'none';
    document.getElementById('resultSimulador').style.display = 'block';
}

(function () {
    document.querySelectorAll('.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) { event.preventDefault(); event.stopPropagation(); }
            form.classList.add('was-validated');
        }, false);
    });
    actualizarInfo();
})();
</script>

<jsp:include page="/WEB-INF/footer.jsp"/>
