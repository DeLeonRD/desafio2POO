package com.mediateca.model;

/**
 * Clase que representa un libro dentro de la mediateca.
 * Hereda de MaterialEscrito e incluye información específica del libro como título y año.
 */
public class Libro extends MaterialEscrito {

    private String titulo;
    private int anio;

    /**
     * Constructor vacío
     */
    public Libro() {
    }

    /**
     * Obtiene el título del libro
     * @return título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro
     * @param titulo título del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el año de publicación del libro
     * @return año de publicación
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Establece el año de publicación del libro
     * @param anio año de publicación
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Representación en texto del libro
     * @return cadena con los datos del libro
     */
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + getId() +
                ", tipo='" + getTipo() + '\'' +
                ", autor='" + getAutor() + '\'' +
                ", titulo='" + titulo + '\'' +
                ", anio=" + anio +
                '}';
    }
}
