<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Página no encontrada - AhorroPeru</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body class="bg-light d-flex align-items-center" style="min-height:100vh">
<div class="container text-center py-5">
    <h1 class="display-1 fw-bold text-primary">404</h1>
    <h4 class="mb-3">Página no encontrada</h4>
    <p class="text-muted">La página que buscas no existe o fue movida.</p>
    <a href="${pageContext.request.contextPath}/app/dashboard" class="btn btn-primary">
        Ir al Dashboard
    </a>
</div>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
