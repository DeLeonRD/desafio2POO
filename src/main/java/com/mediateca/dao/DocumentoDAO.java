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

    /**
     * Busca materiales aplicando los filtros que estén presentes.
     * Cualquier parámetro {@code null} o vacío se ignora.
     *
     * @param titulo  fragmento del título (LIKE %titulo%); null/empty para ignorar
     * @param tipo    tipo exacto (LIBRO, REVISTA, CD, DVD, TESIS); null/empty para ignorar
     * @param idMaterial id exacto; -1 o menor para ignorar
     * @return filas con columnas: id, tipo, titulo, autor, anio_publicacion,
     *         ubicacion, cantidad_total, cantidad_disponible
     */
    public List<Object[]> buscarMateriales(String titulo, String tipo, int idMaterial) {
        List<Object[]> filas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT id, tipo, titulo, autor, anio_publicacion, ubicacion, " +
            "       cantidad_total, cantidad_disponible " +
            "FROM material WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (titulo != null && !titulo.isBlank()) {
            sql.append("AND titulo LIKE ? ");
            params.add("%" + titulo + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            sql.append("AND tipo = ? ");
            params.add(tipo);
        }
        if (idMaterial > 0) {
            sql.append("AND id = ? ");
            params.add(idMaterial);
        }
        sql.append("ORDER BY id");

        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("anio_publicacion"),
                        rs.getString("ubicacion"),
                        rs.getInt("cantidad_total"),
                        rs.getInt("cantidad_disponible")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas;
    }
}