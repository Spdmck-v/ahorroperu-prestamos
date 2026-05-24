package com.ahorroperu.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo: Solicitud de Préstamo
 */
public class SolicitudPrestamo {

    private int idSolicitud;
    private int idAsociado;
    private int idTipo;
    private BigDecimal montoSolicitado;
    private int plazoMeses;
    private BigDecimal tasaInteres;
    private String proposito;
    private String estado;               // PENDIENTE | APROBADO | RECHAZADO
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;
    private String observacion;
    private Integer idAdminResolucion;

    // Datos de join para vistas
    private String nombreAsociado;
    private String dniAsociado;
    private String nombreTipo;
    private String nombreAdmin;

    public SolicitudPrestamo() {}

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getIdSolicitud()                   { return idSolicitud; }
    public void setIdSolicitud(int v)             { this.idSolicitud = v; }

    public int getIdAsociado()                    { return idAsociado; }
    public void setIdAsociado(int v)              { this.idAsociado = v; }

    public int getIdTipo()                        { return idTipo; }
    public void setIdTipo(int v)                  { this.idTipo = v; }

    public BigDecimal getMontoSolicitado()        { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal v)  { this.montoSolicitado = v; }

    public int getPlazoMeses()                    { return plazoMeses; }
    public void setPlazoMeses(int v)              { this.plazoMeses = v; }

    public BigDecimal getTasaInteres()            { return tasaInteres; }
    public void setTasaInteres(BigDecimal v)      { this.tasaInteres = v; }

    public String getProposito()                  { return proposito; }
    public void setProposito(String v)            { this.proposito = v; }

    public String getEstado()                     { return estado; }
    public void setEstado(String v)               { this.estado = v; }

    public LocalDateTime getFechaSolicitud()      { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime v){ this.fechaSolicitud = v; }

    public LocalDateTime getFechaResolucion()     { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime v){ this.fechaResolucion = v; }

    public String getObservacion()                { return observacion; }
    public void setObservacion(String v)          { this.observacion = v; }

    public Integer getIdAdminResolucion()         { return idAdminResolucion; }
    public void setIdAdminResolucion(Integer v)   { this.idAdminResolucion = v; }

    public String getNombreAsociado()             { return nombreAsociado; }
    public void setNombreAsociado(String v)       { this.nombreAsociado = v; }

    public String getDniAsociado()                { return dniAsociado; }
    public void setDniAsociado(String v)          { this.dniAsociado = v; }

    public String getNombreTipo()                 { return nombreTipo; }
    public void setNombreTipo(String v)           { this.nombreTipo = v; }

    public String getNombreAdmin()                { return nombreAdmin; }
    public void setNombreAdmin(String v)          { this.nombreAdmin = v; }

    // Métodos helper para JSP
    public String getFechaSolicitudStr() {
        if (fechaSolicitud == null) return "";
        return String.format("%02d/%02d/%04d %02d:%02d",
            fechaSolicitud.getDayOfMonth(), fechaSolicitud.getMonthValue(),
            fechaSolicitud.getYear(), fechaSolicitud.getHour(), fechaSolicitud.getMinute());
    }
    public String getFechaResolucionStr() {
        if (fechaResolucion == null) return "";
        return String.format("%02d/%02d/%04d %02d:%02d",
            fechaResolucion.getDayOfMonth(), fechaResolucion.getMonthValue(),
            fechaResolucion.getYear(), fechaResolucion.getHour(), fechaResolucion.getMinute());
    }
}
