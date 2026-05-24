package com.ahorroperu.dao;

import com.ahorroperu.modelo.Soporte;
import com.ahorroperu.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: acceso a datos de Soporte (archivos adjuntos)
 */
public class SoporteDAO {

    public List<Soporte> listarPorSolicitud(int idSolicitud) throws SQLException {
        List<Soporte> lista = new ArrayList<>();
        String sql = "SELECT id_soporte, id_solicitud, nombre_archivo, tipo_archivo, "
                   + "ruta_archivo, fecha_subida FROM soportes WHERE id_solicitud=? ORDER BY fecha_subida";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean insertar(Soporte s) throws SQLException {
        String sql = "INSERT INTO soportes (id_solicitud, nombre_archivo, tipo_archivo, ruta_archivo) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdSolicitud());
            ps.setString(2, s.getNombreArchivo());
            ps.setString(3, s.getTipoArchivo());
            ps.setString(4, s.getRutaArchivo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM soportes WHERE id_soporte=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Soporte buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_soporte, id_solicitud, nombre_archivo, tipo_archivo, "
                   + "ruta_archivo, fecha_subida FROM soportes WHERE id_soporte=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Soporte mapear(ResultSet rs) throws SQLException {
        Soporte s = new Soporte();
        s.setIdSoporte(rs.getInt("id_soporte"));
        s.setIdSolicitud(rs.getInt("id_solicitud"));
        s.setNombreArchivo(rs.getString("nombre_archivo"));
        s.setTipoArchivo(rs.getString("tipo_archivo"));
        s.setRutaArchivo(rs.getString("ruta_archivo"));
        Timestamp ts = rs.getTimestamp("fecha_subida");
        if (ts != null) s.setFechaSubida(ts.toLocalDateTime());
        return s;
    }
}
