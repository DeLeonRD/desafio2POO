package com.mediateca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prestamo {

    private int idPrestamo;
    private int idUsuario;
    private int idMaterial;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado; // ACTIVO, DEVUELTO, VENCIDO
    private double moraTotal;

    // Constructor vacío
    public Prestamo() {
    }

    // Constructor completo
    public Prestamo(int idPrestamo, int idUsuario, int idMaterial, 
                    LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada,
                    LocalDate fechaDevolucionReal, String estado, double moraTotal) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.idMaterial = idMaterial;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estado = estado;
        this.moraTotal = moraTotal;
    }

    // Constructor para préstamos nuevos (sin devolución)
    public Prestamo(int idUsuario, int idMaterial, LocalDate fechaPrestamo, 
                    LocalDate fechaDevolucionEsperada) {
        this.idUsuario = idUsuario;
        this.idMaterial = idMaterial;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.estado = "ACTIVO";
        this.moraTotal = 0;
    }

    // Getters y Setters
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMoraTotal() {
        return moraTotal;
    }

    public void setMoraTotal(double moraTotal) {
        this.moraTotal = moraTotal;
    }

    /**
     * Calcula los días de retraso del préstamo
     * @return días de retraso (0 si no hay retraso o ya fue devuelto)
     */
    public long getDiasRetraso() {
        // Si ya fue devuelto, no hay retraso
        if (fechaDevolucionReal != null) {
            return 0;
        }
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaDevolucionEsperada)) {
            return ChronoUnit.DAYS.between(fechaDevolucionEsperada, hoy);
        }
        return 0;
    }

    /**
     * Verifica si el préstamo está vencido
     * @return true si está vencido, false en caso contrario
     */
    public boolean isVencido() {
        if ("ACTIVO".equals(estado)) {
            return getDiasRetraso() > 0;
        }
        return false;
    }

    /**
     * Calcula la mora automáticamente usando los días de retraso y la tarifa por día
     * @param moraPorDia tarifa de mora por día
     * @return monto total de mora
     */
    public double calcularMora(double moraPorDia) {
        return getDiasRetraso() * moraPorDia;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "idPrestamo=" + idPrestamo +
                ", idUsuario=" + idUsuario +
                ", idMaterial=" + idMaterial +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucionEsperada=" + fechaDevolucionEsperada +
                ", fechaDevolucionReal=" + fechaDevolucionReal +
                ", estado='" + estado + '\'' +
                ", moraTotal=" + moraTotal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prestamo)) return false;
        Prestamo prestamo = (Prestamo) o;
        return idPrestamo == prestamo.idPrestamo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo);
    }
}