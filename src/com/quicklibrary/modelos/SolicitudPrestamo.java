package com.quicklibrary.modelos;

/**
 * Representa una solicitud de préstamo registrada por un estudiante en la biblioteca.
 */
public class SolicitudPrestamo {
    private String codigoEstudiante;
    private String nombreEstudiante;
    private int codigoLibro;
    private String fechaSolicitud;

    public SolicitudPrestamo(String codigoEstudiante, String nombreEstudiante, int codigoLibro, String fechaSolicitud) {
        this.codigoEstudiante = codigoEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.codigoLibro = codigoLibro;
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public int getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(int codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    @Override
    public String toString() {
        return String.format("Estudiante: %s (%s) | Código Libro Solicitado: %d | Fecha: %s",
                nombreEstudiante, codigoEstudiante, codigoLibro, fechaSolicitud);
    }
}
