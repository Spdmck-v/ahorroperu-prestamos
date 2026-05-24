package com.ahorroperu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad de conexión a MySQL 9.7
 * Cooperativa AHORROPERU
 */
public class Conexion {

    private static final String URL      = "jdbc:mysql://localhost:3307/ahorroperu_db"
                                         + "?useSSL=false&serverTimezone=America/Lima"
                                         + "&allowPublicKeyRetrieval=true"
                                         + "&useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1234";
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver MySQL: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Cierra la conexión de forma segura.
     */
    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
