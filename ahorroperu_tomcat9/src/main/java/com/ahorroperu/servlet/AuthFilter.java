package com.ahorroperu.servlet;

import com.ahorroperu.modelo.Usuario;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Filtro de autenticación para rutas /app/*
 */
@WebFilter("/app/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
        } else {
            chain.doFilter(request, response);
        }
    }
}
