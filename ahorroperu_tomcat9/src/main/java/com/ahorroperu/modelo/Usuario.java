package com.ahorroperu.modelo;

import java.time.LocalDateTime;

/**
 * Modelo: Usuario (administrador o asociado)
 */
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String passwordHash;
    private String rol;          // ADMIN | ASOCIADO
    private boolean activo;
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido,
                   String email, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre    = nombre;
        this.apellido  = apellido;
        this.email     = email;
        this.rol       = rol;
        this.activo    = activo;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getIdUsuario()              { return idUsuario; }
    public void setIdUsuario(int v)        { this.idUsuario = v; }

    public String getNombre()              { return nombre; }
    public void setNombre(String v)        { this.nombre = v; }

    public String getApellido()            { return apellido; }
    public void setApellido(String v)      { this.apellido = v; }

    public String getNombreCompleto()      { return nombre + " " + apellido; }

    public String getEmail()               { return email; }
    public void setEmail(String v)         { this.email = v; }

    public String getPasswordHash()        { return passwordHash; }
    public void setPasswordHash(String v)  { this.passwordHash = v; }

    public String getRol()                 { return rol; }
    public void setRol(String v)           { this.rol = v; }

    public boolean isActivo()              { return activo; }
    public void setActivo(boolean v)       { this.activo = v; }

    public LocalDateTime getFechaRegistro()          { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v)    { this.fechaRegistro = v; }

    public boolean isAdmin()               { return "ADMIN".equals(rol); }
}
