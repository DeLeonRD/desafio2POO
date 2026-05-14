package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoMaterialService {
    
    public static List<String> listarTipos() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre FROM tipo_material ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tipos.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }
    
    public static int getDiasPrestamo(String tipo) {
        String sql = "SELECT dias_prestamo FROM tipo_material WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("dias_prestamo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 7;
    }
    
    public static boolean crearTipo(String nombre, int dias) {
        String sql = "INSERT INTO tipo_material (nombre, dias_prestamo) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, dias);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean editarTipo(String nombreAntiguo, String nombreNuevo, int dias) {
        String sql = "UPDATE tipo_material SET nombre = ?, dias_prestamo = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo);
            pstmt.setInt(2, dias);
            pstmt.setString(3, nombreAntiguo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void mostrarTipos() {
        List<String> tipos = listarTipos();
        System.out.println("=== TIPOS DE MATERIALES ===");
        for (String tipo : tipos) {
            int dias = getDiasPrestamo(tipo);
            System.out.println("- " + tipo + " (" + dias + " días)");
        }
    }
}