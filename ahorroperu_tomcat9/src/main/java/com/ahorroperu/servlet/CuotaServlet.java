package com.ahorroperu.servlet;

import com.ahorroperu.dao.CuotaDAO;
import com.ahorroperu.modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet: Gestión de Cuotas
 * GET  /app/cuotas?idPrestamo=X → lista cuotas del préstamo
 * POST /app/cuotas?accion=pagar → registrar pago
 */
@WebServlet(name = "CuotaServlet", urlPatterns = "/app/cuotas")
public class CuotaServlet extends HttpServlet {

    private final CuotaDAO cuotaDAO = new CuotaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("idPrestamo");
        try {
            if (idStr != null) {
                int idPrestamo = Integer.parseInt(idStr);
                req.setAttribute("cuotas", cuotaDAO.listarPorPrestamo(idPrestamo));
                req.setAttribute("idPrestamo", idPrestamo);
            } else {
                req.setAttribute("cuotas", cuotaDAO.listarVencidas());
            }
            req.getRequestDispatcher("/vistas/cuota/listaCuota.jsp").forward(req, resp);
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/vistas/cuota/listaCuota.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
        if (!usuario.isAdmin()) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }

        try {
            int idCuota    = Integer.parseInt(req.getParameter("idCuota"));
            int idPrestamo = Integer.parseInt(req.getParameter("idPrestamo"));
            cuotaDAO.registrarPago(idCuota);
            resp.sendRedirect(req.getContextPath() + "/app/cuotas?idPrestamo=" + idPrestamo + "&msg=pagado");
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error al registrar pago: " + e.getMessage());
            req.getRequestDispatcher("/vistas/cuota/listaCuota.jsp").forward(req, resp);
        }
    }
}
