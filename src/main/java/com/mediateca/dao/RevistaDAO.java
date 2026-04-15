package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.model.Revista;

import java.sql.*;

/**
 * DAO encargado de gestionar las operaciones CRUD de Revista
 * en la base de datos Mediateca.
 *
 * Incluye uso de procedimientos almacenados y operaciones SQL
 * para mantener la persistencia de datos.
 */
public class RevistaDAO extends MaterialDAO<Revista> {

    /**
     * Inserta una revista en la base de datos usando un procedimiento almacenado.
     * Maneja transacción para asegurar consistencia de datos.
     *
     * @param revista objeto Revista a insertar
     */
    @Override
    public void insertar(Revista revista) {

        Connection conn = DatabaseConnection.getInstancia().getConexion();

        try {
            conn.setAutoCommit(false);

            String sql = "{CALL insertar_revista(?, ?)}";
            CallableStatement cs = conn.prepareCall(sql);

            cs.setString(1, revista.getNombre());
            cs.setInt(2, revista.getEdicion());

            cs.execute();

            conn.commit();
            System.out.println("✔ Revista insertada correctamente");

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Rollback error: " + ex.getMessage());
            }

            System.out.println("❌ Error insertar revista: " + e.getMessage());
        }
    }

    /**
     * Lista todas las revistas registradas en la base de datos.
     */
    @Override
    public void listar() {

        try {
            Connection conn = DatabaseConnection.getInstancia().getConexion();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM revista");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("nombre") + " | " +
                        rs.getInt("edicion")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error listar revistas: " + e.getMessage());
        }
    }

    /**
     * Actualiza una revista existente en la base de datos.
     *
     * @param id identificador de la revista a actualizar
     * @param revista objeto con los nuevos datos
     */
    @Override
    public void actualizar(int id, Revista revista) {

        try {
            Connection conn = DatabaseConnection.getInstancia().getConexion();

            String sql = "UPDATE revista SET nombre=?, edicion=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, revista.getNombre());
            ps.setInt(2, revista.getEdicion());
            ps.setInt(3, id);

            ps.executeUpdate();

            System.out.println("✔ Revista actualizada");

        } catch (Exception e) {
            System.out.println("❌ Error actualizar revista: " + e.getMessage());
        }
    }

    /**
     * Elimina una revista de la base de datos por su ID.
     *
     * @param id identificador de la revista a eliminar
     */
    @Override
    public void eliminar(int id) {

        try {
            Connection conn = DatabaseConnection.getInstancia().getConexion();

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM revista WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("✔ Revista eliminada");

        } catch (Exception e) {
            System.out.println("❌ Error eliminar revista: " + e.getMessage());
        }
    }
}