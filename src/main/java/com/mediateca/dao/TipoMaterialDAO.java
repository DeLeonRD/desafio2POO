package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoMaterialDAO {
    
    public List<String> listarTipos() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre FROM tipo_material ORDER BY nombre";
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
    
    public boolean crearTipo(String nombre, int diasPrestamo) {
        String sql = "INSERT INTO tipo_material (nombre, dias_prestamo) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre.toUpperCase());
            pstmt.setInt(2, diasPrestamo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }
    
    public boolean editarTipo(String nombreAntiguo, String nombreNuevo, int diasPrestamo) {
        String sql = "UPDATE tipo_material SET nombre = ?, dias_prestamo = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo.toUpperCase());
            pstmt.setInt(2, diasPrestamo);
            pstmt.setString(3, nombreAntiguo.toUpperCase());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }
    
    public int getDiasPrestamo(String tipo) {
        String sql = "SELECT dias_prestamo FROM tipo_material WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo.toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("dias_prestamo");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return 15;
    }
    
    public void mostrarTipos() {
        System.out.println("\n--- TIPOS DE MATERIALES ---");
        System.out.println("+----+--------------------+---------------+");
        System.out.println("| #  | Tipo               | Dias Prestamo |");
        System.out.println("+----+--------------------+---------------+");
        String sql = "SELECT nombre, dias_prestamo FROM tipo_material ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int i = 1;
            while (rs.next()) {
                System.out.printf("| %2d | %-18s | %13d |\n", i++, rs.getString("nombre"), rs.getInt("dias_prestamo"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------+---------------+");
    }
}