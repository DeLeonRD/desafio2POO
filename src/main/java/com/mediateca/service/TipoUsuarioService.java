package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoUsuarioService {
    
    public static List<String> listarTipos() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre FROM tipo_usuario ORDER BY nombre";
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
    
    public static boolean crearTipo(String nombre) {
        String sql = "INSERT INTO tipo_usuario (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean editarTipo(String nombreAntiguo, String nombreNuevo) {
        String sql = "UPDATE tipo_usuario SET nombre = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo);
            pstmt.setString(2, nombreAntiguo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
