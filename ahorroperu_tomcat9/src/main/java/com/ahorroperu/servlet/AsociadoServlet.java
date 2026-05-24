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
import java.time.LocalDate;

@WebServlet(name = "AsociadoServlet", urlPatterns = "/app/asociados")
public class AsociadoServlet extends HttpServlet {

    private final AsociadoDAO asociadoDAO = new AsociadoDAO();
    private final UsuarioDAO  usuarioDAO  = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!esAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if ("nuevo".equals(accion)) {
                req.getRequestDispatcher("/vistas/asociado/form.jsp").forward(req, resp);

            } else if ("editar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("asociado", asociadoDAO.buscarPorId(id));
                req.getRequestDispatcher("/vistas/asociado/form.jsp").forward(req, resp);

            } else if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                asociadoDAO.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/app/asociados?msg=eliminado");

            } else {
                req.setAttribute("lista", asociadoDAO.listar());
                req.getRequestDispatcher("/vistas/asociado/lista.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            try { req.setAttribute("lista", asociadoDAO.listar()); } catch (SQLException ex) { /* ignorar */ }
            req.getRequestDispatcher("/vistas/asociado/lista.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!esAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }
        String idStr = req.getParameter("idAsociado");
        boolean esNuevo = (idStr == null || idStr.isBlank());

        try {
            if (esNuevo) {
                String email = req.getParameter("email");
                if (usuarioDAO.emailExiste(email)) {
                    req.setAttribute("error", "El email ya está registrado.");
                    req.getRequestDispatcher("/vistas/asociado/form.jsp").forward(req, resp);
                    return;
                }
                String dni = req.getParameter("dni");
                if (asociadoDAO.dniExiste(dni)) {
                    req.setAttribute("error", "El DNI ya está registrado.");
                    req.getRequestDispatcher("/vistas/asociado/form.jsp").forward(req, resp);
                    return;
                }

                Usuario u = new Usuario();
                u.setNombre(req.getParameter("nombre"));
                u.setApellido(req.getParameter("apellido"));
                u.setEmail(email);
                u.setPasswordHash(req.getParameter("password"));
                u.setRol("ASOCIADO");
                int idUsuario = usuarioDAO.insertar(u);

                Asociado a = new Asociado();
                a.setIdUsuario(idUsuario);
                a.setDni(dni);
                a.setTelefono(req.getParameter("telefono"));
                a.setDireccion(req.getParameter("direccion"));
                String fn = req.getParameter("fechaNacimiento");
                if (fn != null && !fn.isBlank()) a.setFechaNacimiento(LocalDate.parse(fn));
                asociadoDAO.insertar(a);
                resp.sendRedirect(req.getContextPath() + "/app/asociados?msg=creado");

            } else {
                Asociado a = asociadoDAO.buscarPorId(Integer.parseInt(idStr));
                a.setDni(req.getParameter("dni"));
                a.setTelefono(req.getParameter("telefono"));
                a.setDireccion(req.getParameter("direccion"));
                String fn = req.getParameter("fechaNacimiento");
                if (fn != null && !fn.isBlank()) a.setFechaNacimiento(LocalDate.parse(fn));
                a.setEstado(req.getParameter("estado"));
                asociadoDAO.actualizar(a);

                Usuario u = usuarioDAO.buscarPorId(a.getIdUsuario());
                u.setNombre(req.getParameter("nombre"));
                u.setApellido(req.getParameter("apellido"));
                usuarioDAO.actualizar(u);
                resp.sendRedirect(req.getContextPath() + "/app/asociados?msg=actualizado");
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error al guardar: " + e.getMessage());
            req.getRequestDispatcher("/vistas/asociado/form.jsp").forward(req, resp);
        }
    }

    private boolean esAdmin(HttpServletRequest req) {
        Usuario u = (Usuario) req.getSession().getAttribute("usuarioSesion");
        return u != null && u.isAdmin();
    }
}
