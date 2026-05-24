package com.ahorroperu.servlet;

import com.ahorroperu.dao.*;
import com.ahorroperu.modelo.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet(name = "SolicitudServlet", urlPatterns = "/app/solicitudes")
public class SolicitudServlet extends HttpServlet {

    private final SolicitudPrestamoDAO solicitudDAO = new SolicitudPrestamoDAO();
    private final TipoPrestamoDAO      tipoDAO      = new TipoPrestamoDAO();
    private final PrestamoDAO          prestamoDAO  = new PrestamoDAO();
    private final SoporteDAO           soporteDAO   = new SoporteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            if ("nueva".equals(accion)) {
                req.setAttribute("tipos", tipoDAO.listar());
                req.getRequestDispatcher("/vistas/prestamo/formSolicitud.jsp").forward(req, resp);

            } else if ("ver".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                SolicitudPrestamo sol = solicitudDAO.buscarPorId(id);
                req.setAttribute("solicitud", sol);
                req.setAttribute("soportes", soporteDAO.listarPorSolicitud(id));
                req.getRequestDispatcher("/vistas/prestamo/detalleSolicitud.jsp").forward(req, resp);

            } else if ("editar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("solicitud", solicitudDAO.buscarPorId(id));
                req.setAttribute("tipos", tipoDAO.listar());
                req.getRequestDispatcher("/vistas/prestamo/formSolicitud.jsp").forward(req, resp);

            } else if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                solicitudDAO.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/app/solicitudes?msg=eliminado");

            } else {
                if (usuario.isAdmin()) {
                    String filtro = req.getParameter("estado");
                    if (filtro != null && !filtro.isBlank()) {
                        req.setAttribute("lista", solicitudDAO.listarPorEstado(filtro));
                        req.setAttribute("filtroEstado", filtro);
                    } else {
                        req.setAttribute("lista", solicitudDAO.listar());
                    }
                } else {
                    Asociado asoc = (Asociado) req.getSession().getAttribute("asociadoSesion");
                    if (asoc != null) {
                        req.setAttribute("lista", solicitudDAO.listarPorAsociado(asoc.getIdAsociado()));
                    }
                }
                req.getRequestDispatcher("/vistas/prestamo/listaSolicitud.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/vistas/prestamo/listaSolicitud.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "insertar";

        try {
            if ("aprobar".equals(accion)) {
                if (!usuario.isAdmin()) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }
                int idSolicitud = Integer.parseInt(req.getParameter("idSolicitud"));
                String obs      = req.getParameter("observacion");
                solicitudDAO.cambiarEstado(idSolicitud, "APROBADO", usuario.getIdUsuario(), obs);
                SolicitudPrestamo sol = solicitudDAO.buscarPorId(idSolicitud);
                prestamoDAO.crearPrestamoConCuotas(
                    idSolicitud,
                    sol.getMontoSolicitado(),
                    sol.getPlazoMeses(),
                    sol.getTasaInteres()
                );
                resp.sendRedirect(req.getContextPath() + "/app/solicitudes?msg=aprobado");

            } else if ("rechazar".equals(accion)) {
                if (!usuario.isAdmin()) { resp.sendRedirect(req.getContextPath() + "/app/dashboard"); return; }
                int idSolicitud = Integer.parseInt(req.getParameter("idSolicitud"));
                String obs      = req.getParameter("observacion");
                solicitudDAO.cambiarEstado(idSolicitud, "RECHAZADO", usuario.getIdUsuario(), obs);
                resp.sendRedirect(req.getContextPath() + "/app/solicitudes?msg=rechazado");

            } else if ("actualizar".equals(accion)) {
                SolicitudPrestamo s = new SolicitudPrestamo();
                s.setIdSolicitud(Integer.parseInt(req.getParameter("idSolicitud")));
                s.setIdTipo(Integer.parseInt(req.getParameter("idTipo")));
                s.setMontoSolicitado(new BigDecimal(req.getParameter("monto")));
                s.setPlazoMeses(Integer.parseInt(req.getParameter("plazo")));
                s.setProposito(req.getParameter("proposito"));
                solicitudDAO.actualizar(s);
                resp.sendRedirect(req.getContextPath() + "/app/solicitudes?msg=actualizado");

            } else {
                Asociado asoc = (Asociado) req.getSession().getAttribute("asociadoSesion");
                if (asoc == null && usuario.isAdmin()) {
                    asoc = new Asociado();
                    asoc.setIdAsociado(Integer.parseInt(req.getParameter("idAsociado")));
                }
                int idTipo = Integer.parseInt(req.getParameter("idTipo"));
                TipoPrestamo tipo = tipoDAO.buscarPorId(idTipo);
                SolicitudPrestamo s = new SolicitudPrestamo();
                s.setIdAsociado(asoc.getIdAsociado());
                s.setIdTipo(idTipo);
                s.setMontoSolicitado(new BigDecimal(req.getParameter("monto")));
                s.setPlazoMeses(Integer.parseInt(req.getParameter("plazo")));
                s.setTasaInteres(tipo.getTasaInteres());
                s.setProposito(req.getParameter("proposito"));
                solicitudDAO.insertar(s);
                resp.sendRedirect(req.getContextPath() + "/app/solicitudes?msg=creado");
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            try { req.setAttribute("tipos", tipoDAO.listar()); } catch (SQLException ex) { /* ignorar */ }
            req.getRequestDispatcher("/vistas/prestamo/formSolicitud.jsp").forward(req, resp);
        }
    }
}
