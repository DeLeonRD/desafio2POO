package com.mediateca.dao;

import com.mediateca.model.Dvd;
import java.sql.*;

/**
 * DAO encargado de gestionar las operaciones CRUD de DVD
 * en la base de datos Mediateca.
 *
 * Utiliza procedimientos almacenados y transacciones
 * para garantizar la integridad de los datos.
 */
public class DvdDAO extends MaterialDAO<Dvd> {

    /**
     * Inserta un DVD en la base de datos usando un procedimiento almacenado.
     * Maneja transacción para asegurar consistencia.
     *
     * @param dvd objeto Dvd a insertar
     */
    @Override
    public void insertar(Dvd dvd) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            CallableStatement cs = conn.prepareCall("{CALL insertar_dvd(?, ?)}");
            cs.setString(1, dvd.getDirector());
            cs.setInt(2, dvd.getDuracion());

            cs.execute();

            conn.commit();
            System.out.println("✔ DVD insertado correctamente");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error insertar DVD: " + e.getMessage());
        }
    }

    /**
     * Lista todos los DVDs registrados en la base de datos.
     */
    @Override
    public void listar() {
        try {
            Connection conn = getConexion();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM dvd");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("director") + " | " +
                        rs.getInt("duracion")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error listar DVD: " + e.getMessage());
        }
    }

    /**
     * Actualiza un DVD existente en la base de datos.
     *
     * @param id identificador del DVD a actualizar
     * @param dvd objeto con los nuevos datos
     */
    @Override
    public void actualizar(int id, Dvd dvd) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE dvd SET director=?, duracion=? WHERE id=?"
            );

            ps.setString(1, dvd.getDirector());
            ps.setInt(2, dvd.getDuracion());
            ps.setInt(3, id);

            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ DVD actualizado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error actualizar DVD: " + e.getMessage());
        }
    }

    /**
     * Elimina un DVD de la base de datos por su ID.
     *
     * @param id identificador del DVD a eliminar
     */
    @Override
    public void eliminar(int id) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM dvd WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ DVD eliminado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error eliminar DVD: " + e.getMessage());
        }
    }
}