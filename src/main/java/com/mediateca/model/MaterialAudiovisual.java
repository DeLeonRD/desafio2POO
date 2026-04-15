package com.mediateca.model;

/**
 * Clase que representa un material audiovisual dentro de la mediateca.
 * Hereda de Material y agrega el atributo duración.
 * Sirve como clase base para CDs y DVDs.
 */
public class MaterialAudiovisual extends Material {

    protected int duracion;

    /**
     * Constructor vacío
     */
    public MaterialAudiovisual() {
    }

    /**
     * Constructor con parámetros
     * @param id identificador del material
     * @param tipo tipo de material audiovisual
     * @param duracion duración del material en minutos
     */
    public MaterialAudiovisual(int id, String tipo, int duracion) {
        super(id, tipo);
        this.duracion = duracion;
    }

    /**
     * Obtiene la duración del material audiovisual
     * @return duración en minutos
     */
    public int getDuracion() {
        return duracion;
    }

    /**
     * Establece la duración del material audiovisual
     * @param duracion duración en minutos
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    /**
     * Representación en texto del material audiovisual
     * @return cadena con los datos del material audiovisual
     */
    @Override
    public String toString() {
        return "MaterialAudiovisual{" +
                "id=" + getId() +
                ", tipo='" + getTipo() + '\'' +
                ", duracion=" + duracion +
                '}';
    }
}