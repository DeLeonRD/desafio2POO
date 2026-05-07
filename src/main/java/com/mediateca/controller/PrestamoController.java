package com.mediateca.controller;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.dao.DocumentoDAO;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.Year;

public class PrestamoController {
    
    public boolean usuarioTieneMora(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado = 'ACTIVO' AND fecha_devolucion_esperada < CURDATE()";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    public boolean puedeRealizarPrestamo(int idUsuario) {
        return !usuarioTieneMora(idUsuario);
    }
    
    public int contarPrestamosActivos(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    public boolean respetaLimitePrestamos(int idUsuario) {
        return contarPrestamosActivos(idUsuario) < 3;
    }
    
    public double calcularMora(int idPrestamo, int idMaterial, Date fechaEsperadaSQL) {
        try {
            double moraPorDia = 0.5;
            double moraPorAnio = 0.25;
            
            LocalDate fechaEsperada = fechaEsperadaSQL.toLocalDate();
            LocalDate hoy = LocalDate.now();
            long diasRetraso = hoy.isAfter(fechaEsperada) ? ChronoUnit.DAYS.between(fechaEsperada, hoy) : 0;
            
            int anioPublicacion = new DocumentoDAO().getAnioPublicacion(idMaterial);
            int antiguedad = Math.max(0, Year.now().getValue() - anioPublicacion);
            
            return (diasRetraso * moraPorDia) + (antiguedad * moraPorAnio);
        } catch (Exception e) { return 0; }
    }
}