package com.ahorroperu.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo: Cuota de un préstamo
 */
public class Cuota {

    private int idCuota;
    private int idPrestamo;
    private int numeroCuota;
    private LocalDate fechaVencimiento;
    private BigDecimal montoCapital;
    private BigDecimal montoInteres;
    private BigDecimal montoTotal;
    private String estado;            // PENDIENTE | PAGADO | VENCIDO
    private LocalDateTime fechaPago;

    // Join
    private String nombreAsociado;

    public Cuota() {}

    public int getIdCuota()                      { return idCuota; }
    public void setIdCuota(int v)                { this.idCuota = v; }

    public int getIdPrestamo()                   { return idPrestamo; }
    public void setIdPrestamo(int v)             { this.idPrestamo = v; }

    public int getNumeroCuota()                  { return numeroCuota; }
    public void setNumeroCuota(int v)            { this.numeroCuota = v; }

    public LocalDate getFechaVencimiento()       { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate v) { this.fechaVencimiento = v; }

    public BigDecimal getMontoCapital()          { return montoCapital; }
    public void setMontoCapital(BigDecimal v)    { this.montoCapital = v; }

    public BigDecimal getMontoInteres()          { return montoInteres; }
    public void setMontoInteres(BigDecimal v)    { this.montoInteres = v; }

    public BigDecimal getMontoTotal()            { return montoTotal; }
    public void setMontoTotal(BigDecimal v)      { this.montoTotal = v; }

    public String getEstado()                    { return estado; }
    public void setEstado(String v)              { this.estado = v; }

    public LocalDateTime getFechaPago()          { return fechaPago; }
    public void setFechaPago(LocalDateTime v)    { this.fechaPago = v; }

    public String getNombreAsociado()            { return nombreAsociado; }
    public void setNombreAsociado(String v)      { this.nombreAsociado = v; }

    // Métodos helper para JSP (evita incompatibilidad con fmt:formatDate)
    public String getFechaVencimientoStr() {
        if (fechaVencimiento == null) return "";
        return String.format("%02d/%02d/%04d",
            fechaVencimiento.getDayOfMonth(),
            fechaVencimiento.getMonthValue(),
            fechaVencimiento.getYear());
    }
    public String getFechaPagoStr() {
        if (fechaPago == null) return "-";
        return String.format("%02d/%02d/%04d",
            fechaPago.getDayOfMonth(),
            fechaPago.getMonthValue(),
            fechaPago.getYear());
    }
}
