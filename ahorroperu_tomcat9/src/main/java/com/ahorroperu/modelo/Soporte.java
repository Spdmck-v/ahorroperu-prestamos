package com.ahorroperu.modelo;

import java.time.LocalDateTime;

/**
 * Modelo: Soporte (documento adjunto a solicitud)
 */
public class Soporte {

    private int idSoporte;
    private int idSolicitud;
    private String nombreArchivo;
    private String tipoArchivo;
    private String rutaArchivo;
    private LocalDateTime fechaSubida;

    public Soporte() {}

    public int getIdSoporte()                    { return idSoporte; }
    public void setIdSoporte(int v)              { this.idSoporte = v; }

    public int getIdSolicitud()                  { return idSolicitud; }
    public void setIdSolicitud(int v)            { this.idSolicitud = v; }

    public String getNombreArchivo()             { return nombreArchivo; }
    public void setNombreArchivo(String v)       { this.nombreArchivo = v; }

    public String getTipoArchivo()               { return tipoArchivo; }
    public void setTipoArchivo(String v)         { this.tipoArchivo = v; }

    public String getRutaArchivo()               { return rutaArchivo; }
    public void setRutaArchivo(String v)         { this.rutaArchivo = v; }

    public LocalDateTime getFechaSubida()        { return fechaSubida; }
    public void setFechaSubida(LocalDateTime v)  { this.fechaSubida = v; }
}
