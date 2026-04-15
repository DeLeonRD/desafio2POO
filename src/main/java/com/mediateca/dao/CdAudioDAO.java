package com.mediateca.dao;

import com.mediateca.model.CdAudio;
import java.sql.*;

/**
 * DAO encargado de gestionar las operaciones CRUD de CD de audio
 * en la base de datos Mediateca.
 *
 * Utiliza procedimientos almacenados y transacciones
 * para garantizar la integridad de los datos.
 */
public class CdAudioDAO extends MaterialDAO<CdAudio> {

    /**
     * Inserta un CD de audio en la base de datos usando un procedimiento almacenado.
     * Maneja transacción para asegurar consistencia.
     *
     * @param cd objeto CdAudio a insertar
     */
    @Override
    public void insertar(CdAudio cd) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            CallableStatement cs = conn.prepareCall("{CALL insertar_cd(?, ?)}");
            cs.setString(1, cd.getArtista());
            cs.setInt(2, cd.getDuracion());

            cs.execute();

            conn.commit();
            System.out.println("✔ CD insertado correctamente");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error insertar CD: " + e.getMessage());
        }
    }

    /**
     * Lista todos los CDs de audio registrados en la base de datos.
     */
    @Override
    public void listar() {
        try {
            Connection conn = getConexion();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM cd_audio");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("artista") + " | " +
                        rs.getInt("duracion")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error listar CD: " + e.getMessage());
        }
    }

    /**
     * Actualiza un CD de audio existente en la base de datos.
     *
     * @param id identificador del CD a actualizar
     * @param cd objeto con los nuevos datos
     */
    @Override
    public void actualizar(int id, CdAudio cd) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE cd_audio SET artista=?, duracion=? WHERE id=?"
            );

            ps.setString(1, cd.getArtista());
            ps.setInt(2, cd.getDuracion());
            ps.setInt(3, id);

            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ CD actualizado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error actualizar CD: " + e.getMessage());
        }
    }

    /**
     * Elimina un CD de audio de la base de datos por su ID.
     *
     * @param id identificador del CD a eliminar
     */
    @Override
    public void eliminar(int id) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM cd_audio WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ CD eliminado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }
            System.out.println("❌ Error eliminar CD: " + e.getMessage());
        }
    }
}