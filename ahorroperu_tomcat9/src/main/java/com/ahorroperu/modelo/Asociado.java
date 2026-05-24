package com.ahorroperu.modelo;

import java.time.LocalDate;

/**
 * Modelo: Asociado de la cooperativa
 */
public class Asociado {

    private int idAsociado;
    private int idUsuario;
    private String dni;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String estado;           // ACTIVO | INACTIVO

    // Datos del usuario asociado (join)
    private String nombre;
    private String apellido;
    private String email;

    public Asociado() {}

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getIdAsociado()               { return idAsociado; }
    public void setIdAsociado(int v)         { this.idAsociado = v; }

    public int getIdUsuario()                { return idUsuario; }
    public void setIdUsuario(int v)          { this.idUsuario = v; }

    public String getDni()                   { return dni; }
    public void setDni(String v)             { this.dni = v; }

    public String getTelefono()              { return telefono; }
    public void setTelefono(String v)        { this.telefono = v; }

    public String getDireccion()             { return direccion; }
    public void setDireccion(String v)       { this.direccion = v; }

    public LocalDate getFechaNacimiento()    { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate v) { this.fechaNacimiento = v; }

    public String getEstado()                { return estado; }
    public void setEstado(String v)          { this.estado = v; }

    public String getNombre()                { return nombre; }
    public void setNombre(String v)          { this.nombre = v; }

    public String getApellido()              { return apellido; }
    public void setApellido(String v)        { this.apellido = v; }

    public String getNombreCompleto()        { return nombre + " " + apellido; }

    public String getEmail()                 { return email; }
    public void setEmail(String v)           { this.email = v; }
}
