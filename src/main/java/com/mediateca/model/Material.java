package com.mediateca.model;

import java.util.Objects;

/**
 * Clase base que representa un material dentro de la mediateca.
 * Contiene los atributos comunes que comparten todos los tipos de materiales.
 * Esta clase es la superclase de todo el sistema de herencia.
 * 
 * @author Mediateca System
 */
public class Material {

    protected int id;
    protected String tipo;

    /**
     * Constructor vacío
     */
    public Material() {
    }

    /**
     * Constructor con parámetros
     * @param id identificador único del material
     * @param tipo tipo de material (libro, revista, audio, etc.)
     */
    public Material(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID del material
     * @return identificador del material
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del material
     * @param id identificador del material
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el tipo de material
     * @return tipo de material
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de material
     * @param tipo tipo de material
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Representación en texto del objeto Material
     * @return cadena con los datos del material
     */
    @Override
    public String toString() {
        return "Material{id=" + id + ", tipo='" + tipo + "'}";
    }

    /**
     * Compara dos objetos Material por su ID
     * @param o objeto a comparar
     * @return true si los IDs son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material m = (Material) o;
        return id == m.id;
    }

    /**
     * Genera hash basado en el ID del material
     * @return código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}