<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    com.ahorroperu.modelo.Usuario u =
        (com.ahorroperu.modelo.Usuario) session.getAttribute("usuarioSesion");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AhorroPeru - <%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "Sistema" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/app/dashboard">
            <i class="fas fa-piggy-bank me-2"></i>AhorroPeru
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMenu">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/app/dashboard">
                        <i class="fas fa-tachometer-alt me-1"></i>Dashboard
                    </a>
                </li>
                <% if (u != null && u.isAdmin()) { %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/app/asociados">
                        <i class="fas fa-users me-1"></i>Asociados
                    </a>
                </li>
                <% } %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/app/solicitudes">
                        <i class="fas fa-file-alt me-1"></i>Solicitudes
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/app/prestamos">
                        <i class="fas fa-hand-holding-usd me-1"></i>Préstamos
                    </a>
                </li>
                <% if (u != null && u.isAdmin()) { %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/app/reportes">
                        <i class="fas fa-chart-bar me-1"></i>Reportes
                    </a>
                </li>
                <% } %>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle me-1"></i>
                        <%= u != null ? u.getNombreCompleto() : "Usuario" %>
                        <span class="badge bg-light text-primary ms-1"><%= u != null ? u.getRol() : "" %></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                        </a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid mt-3">
