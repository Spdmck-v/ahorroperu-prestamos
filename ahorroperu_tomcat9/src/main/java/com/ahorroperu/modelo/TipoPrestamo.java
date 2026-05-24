package com.ahorroperu.modelo;

import java.math.BigDecimal;

/**
 * Modelo: Tipo de Préstamo
 */
public class TipoPrestamo {

    private int idTipo;
    private String nombre;
    private String descripcion;
    private BigDecimal tasaInteres;   // Tasa anual %
    private int plazoMaxMeses;
    private BigDecimal montoMax;

    public TipoPrestamo() {}

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getIdTipo()                    { return idTipo; }
    public void setIdTipo(int v)              { this.idTipo = v; }

    public String getNombre()                 { return nombre; }
    public void setNombre(String v)           { this.nombre = v; }

    public String getDescripcion()            { return descripcion; }
    public void setDescripcion(String v)      { this.descripcion = v; }

    public BigDecimal getTasaInteres()        { return tasaInteres; }
    public void setTasaInteres(BigDecimal v)  { this.tasaInteres = v; }

    public int getPlazoMaxMeses()             { return plazoMaxMeses; }
    public void setPlazoMaxMeses(int v)       { this.plazoMaxMeses = v; }

    public BigDecimal getMontoMax()           { return montoMax; }
    public void setMontoMax(BigDecimal v)     { this.montoMax = v; }
}
