package com.mediateca.dao;

import com.mediateca.db.DatabaseConnection;
import java.sql.*;

public class ConfiguracionDAO {
    
    public int getMaxEjemplaresPrestamo() {
        String sql = "SELECT max_ejemplares_prestamo FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("max_ejemplares_prestamo");
        } catch (SQLException e) { e.printStackTrace(); }
        return 3;
    }
    
    public double getMoraPorDia() {
        String sql = "SELECT mora_por_dia FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("mora_por_dia");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.5;
    }
    
    public double getMoraPorAnio() {
        String sql = "SELECT mora_por_anio FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("mora_por_anio");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.25;
    }
}