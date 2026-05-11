package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para consultas sobre préstamos.
 *
 * Esta clase complementa a {@link com.mediateca.controller.PrestamoController}
 * — que se encarga de la lógica de validación (mora, límites, etc.) — con
 * operaciones de consulta para alimentar la UI de búsqueda.
 */
public class PrestamoDAO {

    private static final Logger logger = Logger.getLogger(PrestamoDAO.class.getName());

    /**
     * Busca préstamos aplicando los filtros que estén presentes. Cualquier
     * filtro {@code null}, vacío o &le; 0 (para el id) se ignora.
     *
     * @param idUsuario     id del usuario; -1 o menor para ignorar
     * @param nombreUsuario fragmento del nombre (LIKE %nombre%); null/empty para ignorar
     * @param tipoMaterial  tipo exacto (LIBRO, REVISTA, CD, DVD, TESIS); null/empty para ignorar
     * @return filas con columnas: id_prestamo, id_usuario, nombre_usuario,
     *         tipo_material, titulo_material, fecha_prestamo,
     *         fecha_devolucion_esperada, estado, mora_total
     */
    public List<Object[]> buscarPrestamos(int idUsuario, String nombreUsuario, String tipoMaterial) {
        List<Object[]> filas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.id_prestamo, p.id_usuario, u.nombre AS nombre_usuario, " +
            "       m.tipo AS tipo_material, m.titulo AS titulo_material, " +
            "       p.fecha_prestamo, p.fecha_devolucion_esperada, " +
            "       p.estado, p.mora_total " +
            "FROM prestamos p " +
            "JOIN usuarios u ON u.id_usuario = p.id_usuario " +
            "JOIN material m ON m.id = p.id_material " +
            "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (idUsuario > 0) {
            sql.append("AND p.id_usuario = ? ");
            params.add(idUsuario);
        }
        if (nombreUsuario != null && !nombreUsuario.isBlank()) {
            sql.append("AND u.nombre LIKE ? ");
            params.add("%" + nombreUsuario + "%");
        }
        if (tipoMaterial != null && !tipoMaterial.isBlank()) {
            sql.append("AND m.tipo = ? ");
            params.add(tipoMaterial);
        }
        sql.append("ORDER BY p.id_prestamo DESC");

        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("tipo_material"),
                        rs.getString("titulo_material"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion_esperada"),
                        rs.getString("estado"),
                        rs.getDouble("mora_total")
                    });
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar préstamos", e);
        }
        return filas;
    }
}