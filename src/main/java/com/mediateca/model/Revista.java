package com.mediateca.model;

/**
 * Clase que representa una revista dentro de la mediateca.
 * Hereda de Material e incluye información específica como nombre y edición.
 */
public class Revista extends Material {

    private String nombre;
    private int edicion;

    /**
     * Constructor vacío
     */
    public Revista() {}

    /**
     * Constructor con parámetros
     * @param id identificador de la revista
     * @param nombre nombre de la revista
     * @param edicion número de edición
     */
    public Revista(int id, String nombre, int edicion) {
        this.id = id;
        this.nombre = nombre;
        this.edicion = edicion;
    }

    /**
     * Obtiene el nombre de la revista
     * @return nombre de la revista
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la revista
     * @param nombre nombre de la revista
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la edición de la revista
     * @return edición
     */
    public int getEdicion() {
        return edicion;
    }

    /**
     * Establece la edición de la revista
     * @param edicion número de edición
     */
    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    /**
     * Representación en texto de la revista
     * @return cadena con los datos de la revista
     */
    @Override
    public String toString() {
        return "Revista{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edicion=" + edicion +
                '}';
    }
}