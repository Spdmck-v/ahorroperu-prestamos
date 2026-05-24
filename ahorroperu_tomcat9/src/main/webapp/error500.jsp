<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error del servidor - AhorroPeru</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body class="bg-light d-flex align-items-center" style="min-height:100vh">
<div class="container text-center py-5">
    <h1 class="display-1 fw-bold text-danger">500</h1>
    <h4 class="mb-3">Error interno del servidor</h4>
    <p class="text-muted">Ocurrió un error inesperado. Por favor intente nuevamente.</p>
    <% if (exception != null) { %>
    <div class="alert alert-danger text-start small">
        <strong>Detalle:</strong> <%= exception.getMessage() %>
    </div>
    <% } %>
    <a href="${pageContext.request.contextPath}/app/dashboard" class="btn btn-danger">
        Ir al Dashboard
    </a>
</div>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
