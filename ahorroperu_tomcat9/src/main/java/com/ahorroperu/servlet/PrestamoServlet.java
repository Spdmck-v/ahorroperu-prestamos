package com.ahorroperu.servlet;

import com.ahorroperu.dao.CuotaDAO;
import com.ahorroperu.dao.PrestamoDAO;
import com.ahorroperu.modelo.Asociado;
import com.ahorroperu.modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "PrestamoServlet", urlPatterns = "/app/prestamos")
public class PrestamoServlet extends HttpServlet {

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final CuotaDAO   cuotaDAO    = new CuotaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if ("ver".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("prestamo", prestamoDAO.buscarPorId(id));
                req.setAttribute("cuotas",   cuotaDAO.listarPorPrestamo(id));
                req.getRequestDispatcher("/vistas/prestamo/detallePrestamo.jsp").forward(req, resp);
            } else {
                if (usuario.isAdmin()) {
                    req.setAttribute("lista", prestamoDAO.listar());
                } else {
                    Asociado asoc = (Asociado) req.getSession().getAttribute("asociadoSesion");
                    if (asoc != null)
                        req.setAttribute("lista", prestamoDAO.listarPorAsociado(asoc.getIdAsociado()));
                }
                req.getRequestDispatcher("/vistas/prestamo/listaPrestamo.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/vistas/prestamo/listaPrestamo.jsp").forward(req, resp);
        }
    }
}
