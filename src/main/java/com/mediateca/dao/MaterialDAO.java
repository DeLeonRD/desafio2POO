package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.Connection;

/**
 * Clase abstracta base para los DAO del sistema Mediateca.
 * Proporciona la conexión a la base de datos y define las operaciones CRUD
 * que deben implementar todas las clases DAO específicas.
 *
 * @param <T> tipo de objeto que manejará el DAO
 */
public abstract class MaterialDAO<T> {

    /**
     * Obtiene la conexión activa a la base de datos.
     * Utiliza el Singleton DatabaseConnection.
     *
     * @return conexión a MySQL
     */
    protected Connection getConexion() {
        return DatabaseConnection.getInstancia().getConexion();
    }

    /**
     * Inserta un objeto en la base de datos.
     * @param obj objeto a insertar
     */
    public abstract void insertar(T obj);

    /**
     * Lista todos los registros del tipo correspondiente.
     */
    public abstract void listar();

    /**
     * Actualiza un registro existente por su ID.
     *
     * @param id identificador del registro
     * @param obj objeto con los nuevos datos
     */
    public abstract void actualizar(int id, T obj);

    /**
     * Elimina un registro por su ID.
     *
     * @param id identificador del registro a eliminar
     */
    public abstract void eliminar(int id);
}