package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;

public class ConfiguracionService {
    
    public static double getMoraPorDia() {
        String sql = "SELECT mora_por_dia FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("mora_por_dia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.5;
    }
    
    public static int getMaxEjemplaresPrestamo() {
        String sql = "SELECT max_ejemplares_prestamo FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("max_ejemplares_prestamo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3;
    }
    
    public static boolean actualizarMoraPorDia(double nuevaMora) {
        String sql = "UPDATE configuracion SET mora_por_dia = ? WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevaMora);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean actualizarDiasPrestamo(String tipo, int dias) {
        String sql = "UPDATE tipo_material SET dias_prestamo = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dias);
            pstmt.setString(2, tipo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void mostrarConfiguracion() {
        System.out.println("=== CONFIGURACIÓN ACTUAL ===");
        System.out.println("Mora por día: $" + getMoraPorDia());
        System.out.println("Máximo ejemplares por préstamo: " + getMaxEjemplaresPrestamo());
    }
}