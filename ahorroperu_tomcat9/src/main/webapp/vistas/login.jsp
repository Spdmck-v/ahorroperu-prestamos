<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AhorroPeru - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { background: linear-gradient(135deg, #1a539e 0%, #0d3a70 100%); min-height: 100vh; display:flex; align-items:center; }
        .login-card { border-radius: 1rem; border:0; box-shadow: 0 10px 30px rgba(0,0,0,0.3); }
        .login-logo  { font-size:3rem; color:#1a539e; }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">
            <div class="card login-card p-4">
                <div class="card-body">
                    <div class="text-center mb-4">
                        <div class="login-logo"><i class="fas fa-piggy-bank"></i></div>
                        <h4 class="fw-bold text-primary mt-2">AhorroPeru</h4>
                        <p class="text-muted small">Cooperativa de Ahorro y Crédito</p>
                    </div>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show py-2" role="alert">
                        <i class="fas fa-exclamation-circle me-1"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <% } %>

                    <% if ("logout".equals(request.getParameter("msg"))) { %>
                    <div class="alert alert-success py-2">
                        <i class="fas fa-check-circle me-1"></i> Sesión cerrada correctamente.
                    </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Correo Electrónico</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                <input type="email" name="email" class="form-control" placeholder="correo@ejemplo.com" required autofocus>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Contraseña</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                <input type="password" name="password" id="password" class="form-control" placeholder="••••••••" required>
                                <button class="btn btn-outline-secondary" type="button" onclick="togglePass()">
                                    <i class="fas fa-eye" id="eyeIcon"></i>
                                </button>
                            </div>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-sign-in-alt me-2"></i>Ingresar
                            </button>
                        </div>
                    </form>

                    <div class="mt-3 text-center text-muted small">
                        <strong>Admin:</strong> admin@ahorroperu.com / Admin1234<br>
                        <strong>Asociado:</strong> juan@test.com / Test1234
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script>
function togglePass() {
    const p = document.getElementById('password');
    const i = document.getElementById('eyeIcon');
    if (p.type === 'password') { p.type='text'; i.className='fas fa-eye-slash'; }
    else { p.type='password'; i.className='fas fa-eye'; }
}
</script>
</body>
</html>
