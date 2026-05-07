package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;

public class DocumentoDAO {
    
    public List<Material> listarMateriales() {
        List<Material> materiales = new ArrayList<>();
        String sql = "SELECT id, tipo, anio_publicacion FROM material";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id"));
                m.setTipo(rs.getString("tipo"));
                m.setAnioPublicacion(rs.getInt("anio_publicacion"));
                materiales.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return materiales;
    }
    
    public int getAnioPublicacion(int idMaterial) {
        String sql = "SELECT anio_publicacion FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("anio_publicacion");
        } catch (SQLException e) { e.printStackTrace(); }
        return Year.now().getValue();
    }
}