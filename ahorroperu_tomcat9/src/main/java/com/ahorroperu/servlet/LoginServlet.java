package com.ahorroperu.servlet;

import com.ahorroperu.dao.AsociadoDAO;
import com.ahorroperu.dao.UsuarioDAO;
import com.ahorroperu.modelo.Asociado;
import com.ahorroperu.modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet: Login y Logout
 * GET  /login  → muestra formulario
 * POST /login  → procesa autenticación
 * GET  /logout → cierra sesión
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO  usuarioDAO  = new UsuarioDAO();
    private final AsociadoDAO asociadoDAO = new AsociadoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?msg=logout");
            return;
        }

        // Si ya tiene sesión activa, redirige al dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Ingrese email y contraseña.");
            req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.login(email.trim(), password);
            if (usuario == null) {
                req.setAttribute("error", "Credenciales incorrectas.");
                req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioSesion", usuario);

            // Si es asociado, cargar su objeto asociado en sesión
            if ("ASOCIADO".equals(usuario.getRol())) {
                Asociado asociado = asociadoDAO.buscarPorIdUsuario(usuario.getIdUsuario());
                session.setAttribute("asociadoSesion", asociado);
            }

            resp.sendRedirect(req.getContextPath() + "/app/dashboard");

        } catch (SQLException e) {
            req.setAttribute("error", "Error del sistema: " + e.getMessage());
            req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
        }
    }
}
