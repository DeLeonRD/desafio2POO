package com.mediateca.model;

/**
 * Clase que representa un CD de audio dentro de la mediateca.
 * Hereda de MaterialAudiovisual e incluye información específica del artista.
 */
public class CdAudio extends MaterialAudiovisual {

    private String artista;

    /**
     * Constructor vacío
     */
    public CdAudio() {}

    /**
     * Constructor con parámetros
     * @param id identificador del CD
     * @param tipo tipo de material audiovisual
     * @param duracion duración del CD en minutos
     * @param artista nombre del artista
     */
    public CdAudio(int id, String tipo, int duracion, String artista) {
        super(id, tipo, duracion);
        this.artista = artista;
    }

    /**
     * Obtiene el nombre del artista
     * @return nombre del artista
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Establece el nombre del artista
     * @param artista nombre del artista
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Representación en texto del CD de audio
     * @return cadena con los datos del CD
     */
    @Override
    public String toString() {
        return "CdAudio{" +
                "id=" + getId() +
                ", tipo='" + getTipo() + '\'' +
                ", duracion=" + getDuracion() +
                ", artista='" + artista + '\'' +
                '}';
    }
}