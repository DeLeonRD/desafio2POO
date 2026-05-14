package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoService {

    public static boolean usuarioTieneMora(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado = 'ACTIVO' AND fecha_devolucion_esperada < CURDATE()";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int contarPrestamosActivos(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Object[]> buscarPrestamos(int idUsuario, String nombre, String tipo) {
        List<Object[]> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.id_prestamo, p.id_usuario, u.nombre, m.tipo, m.titulo, p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado, " +
            "COALESCE(p.mora_total, 0) as mora_total " +
            "FROM prestamos p " +
            "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
            "JOIN material m ON p.id_material = m.id " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (idUsuario > 0) {
            sql.append("AND p.id_usuario = ? ");
            params.add(idUsuario);
        }

        if (nombre != null && !nombre.isEmpty()) {
            sql.append("AND u.nombre LIKE ? ");
            params.add("%" + nombre + "%");
        }

        if (tipo != null && !tipo.isEmpty()) {
            sql.append("AND m.tipo = ? ");
            params.add(tipo);
        }

        sql.append("ORDER BY p.fecha_prestamo DESC");

        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[9];
                fila[0] = rs.getInt("id_prestamo");
                fila[1] = rs.getInt("id_usuario");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getString("tipo");
                fila[4] = rs.getString("titulo");
                fila[5] = rs.getDate("fecha_prestamo");
                fila[6] = rs.getDate("fecha_devolucion_esperada");
                fila[7] = rs.getString("estado");
                fila[8] = rs.getDouble("mora_total");
                resultados.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public static boolean crearPrestamo(int idUsuario, int idMaterial, int diasPrestamo) {
        String sql = "INSERT INTO prestamos (id_usuario, id_material, fecha_prestamo, fecha_devolucion_esperada, estado, mora_total) " +
                     "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), 'ACTIVO', 0)";
        
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idMaterial);
            pstmt.setInt(3, diasPrestamo);
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                String sqlUpdateMaterial = "UPDATE material SET cantidad_disponible = cantidad_disponible - 1 WHERE id = ? AND cantidad_disponible > 0";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdateMaterial)) {
                    pstmt2.setInt(1, idMaterial);
                    pstmt2.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}