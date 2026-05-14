package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;

public class MaterialService {
    
    public static boolean existeMaterial(int idMaterial) {
        String sql = "SELECT COUNT(*) FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean tieneDisponibilidad(int idMaterial) {
        String sql = "SELECT cantidad_disponible FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad_disponible") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int getCantidadDisponible(int idMaterial) {
        String sql = "SELECT cantidad_disponible FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad_disponible");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static boolean reducirDisponibilidad(int idMaterial) {
        String sql = "UPDATE material SET cantidad_disponible = cantidad_disponible - 1 WHERE id = ? AND cantidad_disponible > 0";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean aumentarDisponibilidad(int idMaterial) {
        String sql = "UPDATE material SET cantidad_disponible = cantidad_disponible + 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
