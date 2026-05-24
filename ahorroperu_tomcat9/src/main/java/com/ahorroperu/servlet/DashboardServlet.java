package com.ahorroperu.servlet;

import com.ahorroperu.dao.*;
import com.ahorroperu.modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet: Dashboard principal
 * GET /app/dashboard
 */
@WebServlet(name = "DashboardServlet", urlPatterns = "/app/dashboard")
public class DashboardServlet extends HttpServlet {

    private final AsociadoDAO        asociadoDAO   = new AsociadoDAO();
    private final SolicitudPrestamoDAO solicitudDAO = new SolicitudPrestamoDAO();
    private final PrestamoDAO         prestamoDAO  = new PrestamoDAO();
    private final CuotaDAO            cuotaDAO     = new CuotaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");

        try {
            if (usuario.isAdmin()) {
                req.setAttribute("totalAsociados",   asociadoDAO.contarActivos());
                req.setAttribute("totalPendientes",  solicitudDAO.contarPorEstado("PENDIENTE"));
                req.setAttribute("totalAprobados",   solicitudDAO.contarPorEstado("APROBADO"));
                req.setAttribute("totalPrestamos",   prestamoDAO.contarVigentes());
                req.setAttribute("cuotasVencidas",   cuotaDAO.contarPendientes());
                req.setAttribute("ultimasSolicitudes", solicitudDAO.listarPorEstado("PENDIENTE"));
            } else {
                // Asociado: ver sus propios datos
                com.ahorroperu.modelo.Asociado asoc =
                    (com.ahorroperu.modelo.Asociado) req.getSession().getAttribute("asociadoSesion");
                if (asoc != null) {
                    req.setAttribute("misSolicitudes", solicitudDAO.listarPorAsociado(asoc.getIdAsociado()));
                    req.setAttribute("misPrestamos",   prestamoDAO.listarPorAsociado(asoc.getIdAsociado()));
                }
            }
        } catch (SQLException e) {
            req.setAttribute("errorMsg", "Error al cargar datos: " + e.getMessage());
        }

        req.getRequestDispatcher("/vistas/dashboard.jsp").forward(req, resp);
    }
}
