package com.ahorroperu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para manejo de contraseñas con SHA-256
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Genera el hash SHA-256 de una cadena (igual que MySQL SHA2(texto, 256)).
     */
    public static String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con su hash.
     */
    public static boolean verificar(String textoPlano, String hashAlmacenado) {
        return hash(textoPlano).equalsIgnoreCase(hashAlmacenado);
    }
}
