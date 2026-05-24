package com.ahorroperu.dao;

import com.ahorroperu.modelo.Prestamo;
import com.ahorroperu.util.Conexion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Préstamos y Cuotas
 */
public class PrestamoDAO {

    // ── PRÉSTAMOS ──────────────────────────────────────────────────────────

    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.id_solicitud, p.monto_aprobado, p.fecha_inicio, "
                   + "p.fecha_fin, p.estado, "
                   + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado, a.dni AS dni_asociado, "
                   + "tp.nombre AS nombre_tipo, s.plazo_meses, s.tasa_interes "
                   + "FROM prestamos p "
                   + "JOIN solicitudes_prestamo s ON p.id_solicitud = s.id_solicitud "
                   + "JOIN asociados a ON s.id_asociado = a.id_asociado "
                   + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "JOIN tipo_prestamo tp ON s.id_tipo = tp.id_tipo "
                   + "ORDER BY p.fecha_inicio DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearPrestamo(rs));
        }
        return lista;
    }

    public List<Prestamo> listarPorAsociado(int idAsociado) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.id_solicitud, p.monto_aprobado, p.fecha_inicio, "
                   + "p.fecha_fin, p.estado, "
                   + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado, a.dni AS dni_asociado, "
                   + "tp.nombre AS nombre_tipo, s.plazo_meses, s.tasa_interes "
                   + "FROM prestamos p "
                   + "JOIN solicitudes_prestamo s ON p.id_solicitud = s.id_solicitud "
                   + "JOIN asociados a ON s.id_asociado = a.id_asociado "
                   + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "JOIN tipo_prestamo tp ON s.id_tipo = tp.id_tipo "
                   + "WHERE s.id_asociado = ? ORDER BY p.fecha_inicio DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsociado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPrestamo(rs));
            }
        }
        return lista;
    }

    public Prestamo buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.id_prestamo, p.id_solicitud, p.monto_aprobado, p.fecha_inicio, "
                   + "p.fecha_fin, p.estado, "
                   + "CONCAT(u.nombre,' ',u.apellido) AS nombre_asociado, a.dni AS dni_asociado, "
                   + "tp.nombre AS nombre_tipo, s.plazo_meses, s.tasa_interes "
                   + "FROM prestamos p "
                   + "JOIN solicitudes_prestamo s ON p.id_solicitud = s.id_solicitud "
                   + "JOIN asociados a ON s.id_asociado = a.id_asociado "
                   + "JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "JOIN tipo_prestamo tp ON s.id_tipo = tp.id_tipo "
                   + "WHERE p.id_prestamo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPrestamo(rs);
            }
        }
        return null;
    }

    /**
     * Crea un préstamo y genera sus cuotas automáticamente.
     * Se llama desde el servlet de aprobación.
     */
    public int crearPrestamoConCuotas(int idSolicitud, BigDecimal montoAprobado,
                                       int plazoMeses, BigDecimal tasaAnual) throws SQLException {
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaFin    = fechaInicio.plusMonths(plazoMeses);

            // 1. Insertar préstamo
            String sqlP = "INSERT INTO prestamos (id_solicitud, monto_aprobado, fecha_inicio, fecha_fin, estado) "
                        + "VALUES (?, ?, ?, ?, 'VIGENTE')";
            int idPrestamo;
            try (PreparedStatement ps = con.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idSolicitud);
                ps.setBigDecimal(2, montoAprobado);
                ps.setDate(3, Date.valueOf(fechaInicio));
                ps.setDate(4, Date.valueOf(fechaFin));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) idPrestamo = rs.getInt(1);
                    else throw new SQLException("No se pudo crear el préstamo.");
                }
            }

            // 2. Calcular e insertar cuotas (sistema francés - cuota fija)
            //    Tasa mensual = tasaAnual / 12 / 100
            BigDecimal tasaMensual = tasaAnual
                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

            BigDecimal cuotaTotal;
            if (tasaMensual.compareTo(BigDecimal.ZERO) == 0) {
                cuotaTotal = montoAprobado.divide(BigDecimal.valueOf(plazoMeses), 2, RoundingMode.HALF_UP);
            } else {
                // C = P * i / (1 - (1+i)^-n)
                BigDecimal factor = BigDecimal.ONE.add(tasaMensual).pow(plazoMeses);
                cuotaTotal = montoAprobado
                        .multiply(tasaMensual)
                        .multiply(factor)
                        .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
            }

            String sqlC = "INSERT INTO cuotas (id_prestamo, numero_cuota, fecha_vencimiento, "
                        + "monto_capital, monto_interes, monto_total, estado) "
                        + "VALUES (?, ?, ?, ?, ?, ?, 'PENDIENTE')";

            BigDecimal saldo = montoAprobado;
            try (PreparedStatement ps = con.prepareStatement(sqlC)) {
                for (int i = 1; i <= plazoMeses; i++) {
                    BigDecimal interes = saldo.multiply(tasaMensual).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal capital;
                    if (i == plazoMeses) {
                        capital = saldo; // último pago ajustado
                    } else {
                        capital = cuotaTotal.subtract(interes);
                    }
                    BigDecimal total = capital.add(interes);
                    saldo = saldo.subtract(capital);

                    ps.setInt(1, idPrestamo);
                    ps.setInt(2, i);
                    ps.setDate(3, Date.valueOf(fechaInicio.plusMonths(i)));
                    ps.setBigDecimal(4, capital);
                    ps.setBigDecimal(5, interes);
                    ps.setBigDecimal(6, total);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            return idPrestamo;

        } catch (SQLException e) {
            if (con != null) { try { con.rollback(); } catch (SQLException ex) { /* ignorar */ } }
            throw e;
        } finally {
            Conexion.cerrar(con);
        }
    }

    public int contarVigentes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE estado='VIGENTE'";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setIdPrestamo(rs.getInt("id_prestamo"));
        p.setIdSolicitud(rs.getInt("id_solicitud"));
        p.setMontoAprobado(rs.getBigDecimal("monto_aprobado"));
        p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        p.setEstado(rs.getString("estado"));
        p.setNombreAsociado(rs.getString("nombre_asociado"));
        p.setDniAsociado(rs.getString("dni_asociado"));
        p.setNombreTipo(rs.getString("nombre_tipo"));
        p.setPlazoMeses(rs.getInt("plazo_meses"));
        p.setTasaInteres(rs.getBigDecimal("tasa_interes"));
        return p;
    }
}
