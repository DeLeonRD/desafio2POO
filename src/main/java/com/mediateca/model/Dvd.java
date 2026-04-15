package com.mediateca.model;

/**
 * Clase que representa un DVD dentro de la mediateca.
 * Hereda de MaterialAudiovisual e incluye información específica del director.
 */
public class Dvd extends MaterialAudiovisual {

    private String director;

    /**
     * Constructor vacío
     */
    public Dvd() {}

    /**
     * Constructor con parámetros
     * @param id identificador del DVD
     * @param tipo tipo de material audiovisual
     * @param duracion duración del DVD en minutos
     * @param director nombre del director
     */
    public Dvd(int id, String tipo, int duracion, String director) {
        super(id, tipo, duracion);
        this.director = director;
    }

    /**
     * Obtiene el nombre del director
     * @return nombre del director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Establece el nombre del director
     * @param director nombre del director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Representación en texto del DVD
     * @return cadena con los datos del DVD
     */
    @Override
    public String toString() {
        return "Dvd{" +
                "id=" + getId() +
                ", tipo='" + getTipo() + '\'' +
                ", duracion=" + getDuracion() +
                ", director='" + director + '\'' +
                '}';
    }
}