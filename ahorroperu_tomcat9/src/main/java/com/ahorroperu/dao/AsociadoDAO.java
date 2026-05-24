package com.ahorroperu.dao;

import com.ahorroperu.modelo.Asociado;
import com.ahorroperu.util.Conexion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: acceso a datos de Asociado
 */
public class AsociadoDAO {

    public List<Asociado> listar() throws SQLException {
        List<Asociado> lista = new ArrayList<>();
        String sql = "SELECT a.id_asociado, a.id_usuario, a.dni, a.telefono, a.direccion, "
                   + "a.fecha_nacimiento, a.estado, u.nombre, u.apellido, u.email "
                   + "FROM asociados a JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "ORDER BY u.apellido, u.nombre";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Asociado buscarPorId(int id) throws SQLException {
        String sql = "SELECT a.id_asociado, a.id_usuario, a.dni, a.telefono, a.direccion, "
                   + "a.fecha_nacimiento, a.estado, u.nombre, u.apellido, u.email "
                   + "FROM asociados a JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "WHERE a.id_asociado = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Asociado buscarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT a.id_asociado, a.id_usuario, a.dni, a.telefono, a.direccion, "
                   + "a.fecha_nacimiento, a.estado, u.nombre, u.apellido, u.email "
                   + "FROM asociados a JOIN usuarios u ON a.id_usuario = u.id_usuario "
                   + "WHERE a.id_usuario = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public boolean dniExiste(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asociados WHERE dni = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean insertar(Asociado a) throws SQLException {
        String sql = "INSERT INTO asociados (id_usuario, dni, telefono, direccion, fecha_nacimiento, estado) "
                   + "VALUES (?, ?, ?, ?, ?, 'ACTIVO')";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdUsuario());
            ps.setString(2, a.getDni());
            ps.setString(3, a.getTelefono());
            ps.setString(4, a.getDireccion());
            if (a.getFechaNacimiento() != null)
                ps.setDate(5, Date.valueOf(a.getFechaNacimiento()));
            else
                ps.setNull(5, Types.DATE);
            ps.executeUpdate();
            return true;
        }
    }

    public boolean actualizar(Asociado a) throws SQLException {
        String sql = "UPDATE asociados SET dni=?, telefono=?, direccion=?, fecha_nacimiento=?, estado=? "
                   + "WHERE id_asociado=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getDni());
            ps.setString(2, a.getTelefono());
            ps.setString(3, a.getDireccion());
            if (a.getFechaNacimiento() != null)
                ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            else
                ps.setNull(4, Types.DATE);
            ps.setString(5, a.getEstado());
            ps.setInt(6, a.getIdAsociado());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE asociados SET estado='INACTIVO' WHERE id_asociado=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int contarActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM asociados WHERE estado='ACTIVO'";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Asociado mapear(ResultSet rs) throws SQLException {
        Asociado a = new Asociado();
        a.setIdAsociado(rs.getInt("id_asociado"));
        a.setIdUsuario(rs.getInt("id_usuario"));
        a.setDni(rs.getString("dni"));
        a.setTelefono(rs.getString("telefono"));
        a.setDireccion(rs.getString("direccion"));
        Date fn = rs.getDate("fecha_nacimiento");
        if (fn != null) a.setFechaNacimiento(fn.toLocalDate());
        a.setEstado(rs.getString("estado"));
        a.setNombre(rs.getString("nombre"));
        a.setApellido(rs.getString("apellido"));
        a.setEmail(rs.getString("email"));
        return a;
    }
}
