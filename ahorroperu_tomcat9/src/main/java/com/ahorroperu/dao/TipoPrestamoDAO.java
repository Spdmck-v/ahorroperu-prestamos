package com.ahorroperu.dao;

import com.ahorroperu.modelo.TipoPrestamo;
import com.ahorroperu.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: acceso a datos de TipoPrestamo
 */
public class TipoPrestamoDAO {

    public List<TipoPrestamo> listar() throws SQLException {
        List<TipoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre, descripcion, tasa_interes, plazo_max_meses, monto_max "
                   + "FROM tipo_prestamo ORDER BY nombre";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public TipoPrestamo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_tipo, nombre, descripcion, tasa_interes, plazo_max_meses, monto_max "
                   + "FROM tipo_prestamo WHERE id_tipo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public boolean insertar(TipoPrestamo t) throws SQLException {
        String sql = "INSERT INTO tipo_prestamo (nombre, descripcion, tasa_interes, plazo_max_meses, monto_max) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setBigDecimal(3, t.getTasaInteres());
            ps.setInt(4, t.getPlazoMaxMeses());
            ps.setBigDecimal(5, t.getMontoMax());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(TipoPrestamo t) throws SQLException {
        String sql = "UPDATE tipo_prestamo SET nombre=?, descripcion=?, tasa_interes=?, "
                   + "plazo_max_meses=?, monto_max=? WHERE id_tipo=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setBigDecimal(3, t.getTasaInteres());
            ps.setInt(4, t.getPlazoMaxMeses());
            ps.setBigDecimal(5, t.getMontoMax());
            ps.setInt(6, t.getIdTipo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM tipo_prestamo WHERE id_tipo=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private TipoPrestamo mapear(ResultSet rs) throws SQLException {
        TipoPrestamo t = new TipoPrestamo();
        t.setIdTipo(rs.getInt("id_tipo"));
        t.setNombre(rs.getString("nombre"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setTasaInteres(rs.getBigDecimal("tasa_interes"));
        t.setPlazoMaxMeses(rs.getInt("plazo_max_meses"));
        t.setMontoMax(rs.getBigDecimal("monto_max"));
        return t;
    }
}
