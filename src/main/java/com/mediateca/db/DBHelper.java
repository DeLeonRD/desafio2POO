package com.mediateca.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase helper para operaciones comunes de base de datos.
 * Centraliza la apertura y cierre de conexiones, prepared statements y resultsets.
 * 
 * @author Mediateca Don Bosco
 */
public class DBHelper {
    
    private static final Logger logger = Logger.getLogger(DBHelper.class.getName());
    
    /**
     * Ejecuta una consulta SELECT y procesa el resultado con un callback.
     * 
     * @param sql Consulta SQL con parámetros opcionales (?)
     * @param params Parámetros para la consulta
     * @param callback Interfaz para procesar el ResultSet
     */
    public static void executeQuery(String sql, Object[] params, ResultSetCallback callback) {
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParameters(pstmt, params);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                callback.process(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en executeQuery: " + sql, e);
        }
    }
    
    /**
     * Ejecuta una consulta SELECT que devuelve un solo valor.
     * 
     * @param sql Consulta SQL con parámetros opcionales (?)
     * @param params Parámetros para la consulta
     * @param defaultValue Valor por defecto si no hay resultado
     * @return El valor de la primera columna de la primera fila
     */
    public static Object executeScalar(String sql, Object[] params, Object defaultValue) {
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParameters(pstmt, params);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en executeScalar: " + sql, e);
        }
        return defaultValue;
    }
    
    /**
     * Ejecuta una sentencia UPDATE, INSERT o DELETE.
     * 
     * @param sql Sentencia SQL con parámetros opcionales (?)
     * @param params Parámetros para la sentencia
     * @return Número de filas afectadas
     */
    public static int executeUpdate(String sql, Object[] params) {
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en executeUpdate: " + sql, e);
            return 0;
        }
    }
    
    /**
     * Ejecuta una sentencia UPDATE, INSERT o DELETE y devuelve la clave generada.
     * 
     * @param sql Sentencia SQL con parámetros opcionales (?)
     * @param params Parámetros para la sentencia
     * @return ID generado automáticamente, o -1 si no se generó
     */
    public static int executeUpdateWithGeneratedKey(String sql, Object[] params) {
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setParameters(pstmt, params);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en executeUpdateWithGeneratedKey: " + sql, e);
        }
        return -1;
    }
    
    /**
     * Asigna los parámetros a un PreparedStatement.
     * 
     * @param pstmt PreparedStatement a configurar
     * @param params Lista de parámetros
     * @throws SQLException Si hay error al asignar
     */
    private static void setParameters(PreparedStatement pstmt, Object[] params) throws SQLException {
        if (params == null) return;
        
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
    
    /**
     * Cierra silenciosamente un ResultSet.
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { logger.log(Level.WARNING, "Error closing ResultSet", e); }
        }
    }
    
    /**
     * Cierra silenciosamente un Statement.
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { logger.log(Level.WARNING, "Error closing Statement", e); }
        }
    }
    
    /**
     * Cierra silenciosamente una Connection.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { logger.log(Level.WARNING, "Error closing Connection", e); }
        }
    }
    
    /**
     * Interfaz para callback de procesamiento de ResultSet.
     */
    @FunctionalInterface
    public interface ResultSetCallback {
        void process(ResultSet rs) throws SQLException;
    }
}