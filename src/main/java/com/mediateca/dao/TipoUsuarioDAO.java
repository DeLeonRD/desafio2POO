package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoUsuarioDAO {
    
    public List<String> listarTipos() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre FROM tipo_usuario ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tipos.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return tipos;
    }
    
    public boolean crearTipo(String nombre) {
        String sql = "INSERT INTO tipo_usuario (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre.toUpperCase());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }
    
    public boolean editarTipo(String nombreAntiguo, String nombreNuevo) {
        String sql = "UPDATE tipo_usuario SET nombre = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo.toUpperCase());
            pstmt.setString(2, nombreAntiguo.toUpperCase());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }
    
    public void mostrarTipos() {
        System.out.println("\n--- TIPOS DE USUARIOS ---");
        System.out.println("+----+--------------------+");
        System.out.println("| #  | Tipo               |");
        System.out.println("+----+--------------------+");
        String sql = "SELECT nombre FROM tipo_usuario ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int i = 1;
            while (rs.next()) {
                System.out.printf("| %2d | %-18s |\n", i++, rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------+");
    }
}