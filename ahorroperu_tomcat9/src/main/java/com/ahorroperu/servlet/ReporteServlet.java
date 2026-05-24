package com.ahorroperu.servlet;

import com.ahorroperu.dao.CuotaDAO;
import com.ahorroperu.dao.SolicitudPrestamoDAO;
import com.ahorroperu.modelo.Cuota;
import com.ahorroperu.modelo.SolicitudPrestamo;
import com.ahorroperu.modelo.Usuario;
import com.ahorroperu.util.ExcelUtil;
import com.ahorroperu.util.GraficoUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ReporteServlet", urlPatterns = "/app/reportes")
public class ReporteServlet extends HttpServlet {

    private final SolicitudPrestamoDAO solicitudDAO = new SolicitudPrestamoDAO();
    private final CuotaDAO             cuotaDAO    = new CuotaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
        if (!usuario.isAdmin()) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }

        String tipo = req.getParameter("tipo");
        if (tipo == null) {
            req.getRequestDispatcher("/vistas/reporte/reportes.jsp").forward(req, resp);
            return;
        }

        try {
            if ("excel-solicitudes".equals(tipo)) {
                List<SolicitudPrestamo> solicitudes = solicitudDAO.listar();
                byte[] bytes = ExcelUtil.reporteSolicitudes(solicitudes);
                resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                resp.setHeader("Content-Disposition", "attachment; filename=solicitudes_ahorroperu.xlsx");
                resp.setContentLength(bytes.length);
                resp.getOutputStream().write(bytes);

            } else if ("excel-cuotas".equals(tipo)) {
                int idPrestamo = Integer.parseInt(req.getParameter("idPrestamo"));
                List<Cuota> cuotas = cuotaDAO.listarPorPrestamo(idPrestamo);
                byte[] bytes = ExcelUtil.reporteCuotas(cuotas, idPrestamo);
                resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                resp.setHeader("Content-Disposition", "attachment; filename=cuotas_prestamo_" + idPrestamo + ".xlsx");
                resp.setContentLength(bytes.length);
                resp.getOutputStream().write(bytes);

            } else if ("grafico-estados".equals(tipo)) {
                List<Object[]> datos = solicitudDAO.contarPorEstadoGrafico();
                byte[] img = GraficoUtil.graficoPorEstado(datos);
                resp.setContentType("image/png");
                resp.setContentLength(img.length);
                resp.getOutputStream().write(img);

            } else if ("grafico-montos".equals(tipo)) {
                List<Object[]> datos = solicitudDAO.montosPorTipo();
                byte[] img = GraficoUtil.graficoMontosPorTipo(datos);
                resp.setContentType("image/png");
                resp.setContentLength(img.length);
                resp.getOutputStream().write(img);

            } else {
                resp.sendRedirect(req.getContextPath() + "/app/reportes");
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error al generar reporte: " + e.getMessage());
            req.getRequestDispatcher("/vistas/reporte/reportes.jsp").forward(req, resp);
        }
    }
}
