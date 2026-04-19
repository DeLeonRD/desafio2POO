package com.mediateca.dao;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.mediateca.model.Libro;
import java.sql.*;

/**
 * DAO encargado de gestionar las operaciones CRUD de Libro
 * en la base de datos Mediateca.
 *
 * Incluye uso de procedimientos almacenados y transacciones
 * para garantizar integridad de los datos.
 */
public class LibroDAO extends MaterialDAO<Libro> {

    /**
     * Inserta un libro en la base de datos usando un procedimiento almacenado.
     * Maneja transacción para asegurar consistencia.
     *
     * @param libro objeto Libro a insertar
     */
    @Override
    public void insertar(Libro libro) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            CallableStatement cs = conn.prepareCall("{CALL insertar_libro(?, ?, ?)}");
            cs.setString(1, libro.getTitulo());
            cs.setString(2, libro.getAutor());
            cs.setInt(3, libro.getAnio());

            cs.execute();

            conn.commit();
            System.out.println("✔ Libro insertado correctamente");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Error rollback: " + ex.getMessage());
            }
            System.out.println("❌ Error insertar libro: " + e.getMessage());
        }
    }

    /**
     * Lista todos los libros registrados en la base de datos.
     */
    @Override
    public void listar() {
        try {
            Connection conn = getConexion();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM libro");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("titulo") + " | " +
                        rs.getString("autor") + " | " +
                        rs.getInt("anio")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error listar libros: " + e.getMessage());
        }
    }

    /**
     * Actualiza un libro existente en la base de datos.
     * Maneja transacción para asegurar consistencia.
     *
     * @param id identificador del libro a actualizar
     * @param libro objeto con los nuevos datos
     */
    @Override
    public void actualizar(int id, Libro libro) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE libro SET titulo=?, autor=?, anio=? WHERE id=?"
            );

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnio());
            ps.setInt(4, id);

            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ Libro actualizado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Error rollback: " + ex.getMessage());
            }
            System.out.println("❌ Error actualizar libro: " + e.getMessage());
        }
    }

    public List<Libro> obtenerLibros() {
    List<Libro> lista = new ArrayList<>();

    try {
        Connection conn = getConexion(); // si no tienes getConexion(), usa DatabaseConnection.getConnection()
        String sql = "SELECT * FROM libro";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Libro libro = new Libro();
            libro.setId(rs.getInt("id"));
            libro.setTitulo(rs.getString("titulo"));
            libro.setAutor(rs.getString("autor"));
            libro.setAnio(rs.getInt("anio"));

            lista.add(libro);
        }

    } catch (Exception e) {
        System.out.println("Error al obtener libros: " + e.getMessage());
    }

    return lista;
    }
    /**
     * Elimina un libro de la base de datos por su ID.
     * Maneja transacción para evitar inconsistencias.
     *
     * @param id identificador del libro a eliminar
     */
    @Override
    public void eliminar(int id) {

        Connection conn = getConexion();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM libro WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.commit();
            System.out.println("✔ Libro eliminado");

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("❌ Error rollback: " + ex.getMessage());
            }
            System.out.println("❌ Error eliminar libro: " + e.getMessage());
        }
    }
}