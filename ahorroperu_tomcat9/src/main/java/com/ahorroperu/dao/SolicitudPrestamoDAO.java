package com.ahorroperu.dao;

import com.ahorroperu.modelo.SolicitudPrestamo;
import com.ahorroperu.util.Conexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: acceso a datos de SolicitudPrestamo
 */
public class SolicitudPrestamoDAO {

    private static final String BASE_SELECT =
        "SELECT s.id_solicitud, s.id_asociado, s.id_tipo, s.monto_solicitado, s.plazo_meses, "
      + "s.tasa_interes, s.proposito, s.estado, s.fecha_solicitud, s.fecha_resolucion, "
      + "s.observacion, s.id_admin_resolucion, "
      + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado, a.dni AS dni_asociado, "
      + "tp.nombre AS nombre_tipo, "
      + "CONCAT(ua.nombre,' ',ua.apellido) AS nombre_admin "
      + "FROM solicitudes_prestamo s "
      + "JOIN asociados a ON s.id_asociado = a.id_asociado "
      + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
      + "JOIN tipo_prestamo tp ON s.id_tipo = tp.id_tipo "
      + "LEFT JOIN usuarios ua ON s.id_admin_resolucion = ua.id_usuario ";

    public List<SolicitudPrestamo> listar() throws SQLException {
        List<SolicitudPrestamo> lista = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY s.fecha_solicitud DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<SolicitudPrestamo> listarPorAsociado(int idAsociado) throws SQLException {
        List<SolicitudPrestamo> lista = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE s.id_asociado = ? ORDER BY s.fecha_solicitud DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsociado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<SolicitudPrestamo> listarPorEstado(String estado) throws SQLException {
        List<SolicitudPrestamo> lista = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE s.estado = ? ORDER BY s.fecha_solicitud DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public SolicitudPrestamo buscarPorId(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE s.id_solicitud = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public int insertar(SolicitudPrestamo s) throws SQLException {
        String sql = "INSERT INTO solicitudes_prestamo "
                   + "(id_asociado, id_tipo, monto_solicitado, plazo_meses, tasa_interes, proposito, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 'PENDIENTE')";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getIdAsociado());
            ps.setInt(2, s.getIdTipo());
            ps.setBigDecimal(3, s.getMontoSolicitado());
            ps.setInt(4, s.getPlazoMeses());
            ps.setBigDecimal(5, s.getTasaInteres());
            ps.setString(6, s.getProposito());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean actualizar(SolicitudPrestamo s) throws SQLException {
        String sql = "UPDATE solicitudes_prestamo SET id_tipo=?, monto_solicitado=?, "
                   + "plazo_meses=?, proposito=? WHERE id_solicitud=? AND estado='PENDIENTE'";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdTipo());
            ps.setBigDecimal(2, s.getMontoSolicitado());
            ps.setInt(3, s.getPlazoMeses());
            ps.setString(4, s.getProposito());
            ps.setInt(5, s.getIdSolicitud());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM solicitudes_prestamo WHERE id_solicitud=? AND estado='PENDIENTE'";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cambia el estado de una solicitud a APROBADO o RECHAZADO.
     */
    public boolean cambiarEstado(int idSolicitud, String nuevoEstado,
                                  int idAdmin, String observacion) throws SQLException {
        String sql = "UPDATE solicitudes_prestamo SET estado=?, fecha_resolucion=NOW(), "
                   + "id_admin_resolucion=?, observacion=? WHERE id_solicitud=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idAdmin);
            ps.setString(3, observacion);
            ps.setInt(4, idSolicitud);
            return ps.executeUpdate() > 0;
        }
    }

    public int contarPorEstado(String estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitudes_prestamo WHERE estado=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // ── Para reportes ──────────────────────────────────────────────────────

    /**
     * Devuelve conteo agrupado por estado: [[estado, cantidad], ...]
     */
    public List<Object[]> contarPorEstadoGrafico() throws SQLException {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT estado, COUNT(*) AS total FROM solicitudes_prestamo GROUP BY estado";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.add(new Object[]{rs.getString("estado"), rs.getInt("total")});
            }
        }
        return datos;
    }

    /**
     * Devuelve montos aprobados por tipo: [[tipo, monto_total], ...]
     */
    public List<Object[]> montosPorTipo() throws SQLException {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT tp.nombre, SUM(s.monto_solicitado) AS total "
                   + "FROM solicitudes_prestamo s "
                   + "JOIN tipo_prestamo tp ON s.id_tipo = tp.id_tipo "
                   + "WHERE s.estado='APROBADO' GROUP BY tp.nombre";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.add(new Object[]{rs.getString("nombre"), rs.getBigDecimal("total")});
            }
        }
        return datos;
    }

    private SolicitudPrestamo mapear(ResultSet rs) throws SQLException {
        SolicitudPrestamo s = new SolicitudPrestamo();
        s.setIdSolicitud(rs.getInt("id_solicitud"));
        s.setIdAsociado(rs.getInt("id_asociado"));
        s.setIdTipo(rs.getInt("id_tipo"));
        s.setMontoSolicitado(rs.getBigDecimal("monto_solicitado"));
        s.setPlazoMeses(rs.getInt("plazo_meses"));
        s.setTasaInteres(rs.getBigDecimal("tasa_interes"));
        s.setProposito(rs.getString("proposito"));
        s.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("fecha_solicitud");
        if (ts != null) s.setFechaSolicitud(ts.toLocalDateTime());
        Timestamp tr = rs.getTimestamp("fecha_resolucion");
        if (tr != null) s.setFechaResolucion(tr.toLocalDateTime());
        s.setObservacion(rs.getString("observacion"));
        int idAdmin = rs.getInt("id_admin_resolucion");
        if (!rs.wasNull()) s.setIdAdminResolucion(idAdmin);
        s.setNombreAsociado(rs.getString("nombre_asociado"));
        s.setDniAsociado(rs.getString("dni_asociado"));
        s.setNombreTipo(rs.getString("nombre_tipo"));
        s.setNombreAdmin(rs.getString("nombre_admin"));
        return s;
    }
}
