package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    // ======================================================
    // VALIDACIONES ADICIONALES
    // ======================================================

    /**
     * Verifica si existe un material por su ID.
     * @param idMaterial ID del material a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existeMaterial(int idMaterial) {
        String sql = "SELECT COUNT(*) FROM material WHERE id = ?";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de material: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si hay disponibilidad de un material.
     * @param idMaterial ID del material a verificar
     * @return true si hay al menos 1 ejemplar disponible, false en caso contrario
     */
    public boolean tieneDisponibilidad(int idMaterial) {
        String sql = "SELECT cantidad_disponible FROM material WHERE id = ?";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad_disponible") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar disponibilidad: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene la cantidad disponible de un material.
     * @param idMaterial ID del material
     * @return cantidad disponible, 0 si hay error o no existe
     */
    public int getCantidadDisponible(int idMaterial) {
        String sql = "SELECT cantidad_disponible FROM material WHERE id = ?";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad_disponible");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener disponibilidad: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Reduce en 1 la disponibilidad de un material (para préstamos).
     * @param idMaterial ID del material
     * @return true si se redujo correctamente, false en caso contrario
     */
    public boolean reducirDisponibilidad(int idMaterial) {
        String sql = "UPDATE material SET cantidad_disponible = cantidad_disponible - 1 WHERE id = ? AND cantidad_disponible > 0";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al reducir disponibilidad: " + e.getMessage());
        }
        return false;
    }

    /**
     * Aumenta en 1 la disponibilidad de un material (para devoluciones).
     * @param idMaterial ID del material
     * @return true si se aumentó correctamente, false en caso contrario
     */
    public boolean aumentarDisponibilidad(int idMaterial) {
        String sql = "UPDATE material SET cantidad_disponible = cantidad_disponible + 1 WHERE id = ?";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al aumentar disponibilidad: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene el año de publicación de un material.
     * @param idMaterial ID del material
     * @return año de publicación, año actual si hay error
     */
    public int getAnioPublicacion(int idMaterial) {
        String sql = "SELECT anio_publicacion FROM material WHERE id = ?";
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("anio_publicacion");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener año de publicación: " + e.getMessage());
        }
        return java.time.Year.now().getValue();
    }

    /**
     * Verifica si un material tiene disponible y es válido para préstamo.
     * @param idMaterial ID del material
     * @return true si existe y tiene disponibilidad, false en caso contrario
     */
    public boolean isValidForLoan(int idMaterial) {
        return existeMaterial(idMaterial) && tieneDisponibilidad(idMaterial);
    }
}