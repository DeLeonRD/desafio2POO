package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;

public class ConfiguracionPrestamoDAO {
    
    public int getDiasPorTipo(String tipo) {
        String sql = "SELECT dias_" + tipo.toLowerCase() + " FROM configuracion_prestamo WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener días para " + tipo + ": " + e.getMessage());
        }
        // Valores por defecto
        switch (tipo.toUpperCase()) {
            case "LIBRO": return 15;
            case "REVISTA": return 10;
            case "CD": return 7;
            case "DVD": return 7;
            default: return 15;
        }
    }
    
    public boolean actualizarDias(String tipo, int dias) {
        String sql = "UPDATE configuracion_prestamo SET dias_" + tipo.toLowerCase() + " = ? WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dias);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar días para " + tipo + ": " + e.getMessage());
            return false;
        }
    }
    
    public void mostrarConfiguracion() {
        String sql = "SELECT * FROM configuracion_prestamo WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("\n--- CONFIGURACION DE DIAS DE PRESTAMO ---");
                System.out.println("LIBRO   : " + rs.getInt("dias_libro") + " días");
                System.out.println("REVISTA : " + rs.getInt("dias_revista") + " días");
                System.out.println("CD      : " + rs.getInt("dias_cd") + " días");
                System.out.println("DVD     : " + rs.getInt("dias_dvd") + " días");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}