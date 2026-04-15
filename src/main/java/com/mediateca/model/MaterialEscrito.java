package com.mediateca.model;

/**
 * Clase que representa un material escrito dentro de la mediateca.
 * Hereda de Material y agrega el atributo autor.
 * Sirve como clase base para libros y revistas.
 */
public class MaterialEscrito extends Material {

    protected String autor;

    /**
     * Constructor vacío
     */
    public MaterialEscrito() {
    }

    /**
     * Constructor con parámetros
     * @param id identificador del material
     * @param tipo tipo de material
     * @param autor autor del material escrito
     */
    public MaterialEscrito(int id, String tipo, String autor) {
        super(id, tipo);
        this.autor = autor;
    }

    /**
     * Obtiene el autor del material
     * @return autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del material
     * @param autor nombre del autor
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Representación en texto del material escrito
     * @return cadena con datos del material escrito
     */
    @Override
    public String toString() {
        return "MaterialEscrito{" +
                "id=" + getId() +
                ", tipo='" + getTipo() + '\'' +
                ", autor='" + autor + '\'' +
                '}';
    }
}