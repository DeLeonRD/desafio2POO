package com.mediateca.model;

import java.util.Objects;

public class Material {

    protected int id;
    protected String tipo;
    protected int anioPublicacion;

    public Material() {
    }

    public Material(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Material(int id, String tipo, int anioPublicacion) {
        this.id = id;
        this.tipo = tipo;
        this.anioPublicacion = anioPublicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    @Override
    public String toString() {
        return "Material{id=" + id + ", tipo='" + tipo + "', anioPublicacion=" + anioPublicacion + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material m = (Material) o;
        return id == m.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}