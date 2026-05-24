package com.ahorroperu.dao;

import com.ahorroperu.modelo.Cuota;
import com.ahorroperu.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: acceso a datos de Cuota
 */
public class CuotaDAO {

    public List<Cuota> listarPorPrestamo(int idPrestamo) throws SQLException {
        List<Cuota> lista = new ArrayList<>();
        String sql = "SELECT c.id_cuota, c.id_prestamo, c.numero_cuota, c.fecha_vencimiento, "
                   + "c.monto_capital, c.monto_interes, c.monto_total, c.estado, c.fecha_pago, "
                   + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado "
                   + "FROM cuotas c "
                   + "JOIN prestamos p ON c.id_prestamo = p.id_prestamo "
                   + "JOIN solicitudes_prestamo s ON p.id_solicitud = s.id_solicitud "
                   + "JOIN asociados a ON s.id_asociado = a.id_asociado "
                   + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "WHERE c.id_prestamo = ? ORDER BY c.numero_cuota";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Cuota> listarVencidas() throws SQLException {
        List<Cuota> lista = new ArrayList<>();
        String sql = "SELECT c.id_cuota, c.id_prestamo, c.numero_cuota, c.fecha_vencimiento, "
                   + "c.monto_capital, c.monto_interes, c.monto_total, c.estado, c.fecha_pago, "
                   + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado "
                   + "FROM cuotas c "
                   + "JOIN prestamos p ON c.id_prestamo = p.id_prestamo "
                   + "JOIN solicitudes_prestamo s ON p.id_solicitud = s.id_solicitud "
                   + "JOIN asociados a ON s.id_asociado = a.id_asociado "
                   + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "WHERE c.estado='PENDIENTE' AND c.fecha_vencimiento < CURDATE() "
                   + "ORDER BY c.fecha_vencimiento";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean registrarPago(int idCuota) throws SQLException {
        String sql = "UPDATE cuotas SET estado='PAGADO', fecha_pago=NOW() WHERE id_cuota=? AND estado='PENDIENTE'";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            return ps.executeUpdate() > 0;
        }
    }

    public int contarPendientes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cuotas WHERE estado='PENDIENTE' AND fecha_vencimiento < CURDATE()";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Cuota mapear(ResultSet rs) throws SQLException {
        Cuota c = new Cuota();
        c.setIdCuota(rs.getInt("id_cuota"));
        c.setIdPrestamo(rs.getInt("id_prestamo"));
        c.setNumeroCuota(rs.getInt("numero_cuota"));
        c.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        c.setMontoCapital(rs.getBigDecimal("monto_capital"));
        c.setMontoInteres(rs.getBigDecimal("monto_interes"));
        c.setMontoTotal(rs.getBigDecimal("monto_total"));
        c.setEstado(rs.getString("estado"));
        Timestamp fp = rs.getTimestamp("fecha_pago");
        if (fp != null) c.setFechaPago(fp.toLocalDateTime());
        c.setNombreAsociado(rs.getString("nombre_asociado"));
        return c;
    }
}
