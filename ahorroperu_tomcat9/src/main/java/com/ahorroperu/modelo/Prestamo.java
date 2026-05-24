package com.ahorroperu.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo: Préstamo aprobado
 */
public class Prestamo {

    private int idPrestamo;
    private int idSolicitud;
    private BigDecimal montoAprobado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;            // VIGENTE | CANCELADO | MORA

    // Datos de join
    private String nombreAsociado;
    private String dniAsociado;
    private String nombreTipo;
    private int plazoMeses;
    private BigDecimal tasaInteres;

    public Prestamo() {}

    public int getIdPrestamo()                   { return idPrestamo; }
    public void setIdPrestamo(int v)             { this.idPrestamo = v; }

    public int getIdSolicitud()                  { return idSolicitud; }
    public void setIdSolicitud(int v)            { this.idSolicitud = v; }

    public BigDecimal getMontoAprobado()         { return montoAprobado; }
    public void setMontoAprobado(BigDecimal v)   { this.montoAprobado = v; }

    public LocalDate getFechaInicio()            { return fechaInicio; }
    public void setFechaInicio(LocalDate v)      { this.fechaInicio = v; }

    public LocalDate getFechaFin()               { return fechaFin; }
    public void setFechaFin(LocalDate v)         { this.fechaFin = v; }

    public String getEstado()                    { return estado; }
    public void setEstado(String v)              { this.estado = v; }

    public String getNombreAsociado()            { return nombreAsociado; }
    public void setNombreAsociado(String v)      { this.nombreAsociado = v; }

    public String getDniAsociado()               { return dniAsociado; }
    public void setDniAsociado(String v)         { this.dniAsociado = v; }

    public String getNombreTipo()                { return nombreTipo; }
    public void setNombreTipo(String v)          { this.nombreTipo = v; }

    public int getPlazoMeses()                   { return plazoMeses; }
    public void setPlazoMeses(int v)             { this.plazoMeses = v; }

    public BigDecimal getTasaInteres()           { return tasaInteres; }
    public void setTasaInteres(BigDecimal v)     { this.tasaInteres = v; }

    // Métodos helper para JSP
    public String getFechaInicioStr() {
        if (fechaInicio == null) return "";
        return String.format("%02d/%02d/%04d",
            fechaInicio.getDayOfMonth(), fechaInicio.getMonthValue(), fechaInicio.getYear());
    }
    public String getFechaFinStr() {
        if (fechaFin == null) return "";
        return String.format("%02d/%02d/%04d",
            fechaFin.getDayOfMonth(), fechaFin.getMonthValue(), fechaFin.getYear());
    }
}
