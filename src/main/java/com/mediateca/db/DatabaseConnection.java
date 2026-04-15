package com.mediateca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection conexion;
    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        try {
            cargarConfiguracion();
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la BD");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    private void cargarConfiguracion() throws Exception {
        Properties props = new Properties();
        String configFile = "config.properties";
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                System.out.println("No se encontró " + configFile + " en el classpath");
                System.out.println("Usando valores por defecto...");
                
                // Valores por defecto (para desarrollo)
                url = "jdbc:mysql://localhost:3306/mediateca?useSSL=false&serverTimezone=UTC";
                user = "root";
                password = "tu_contraseña_aqui";
                return;
            }
            
            props.load(input);
            url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/mediateca?useSSL=false&serverTimezone=UTC");
            user = props.getProperty("db.user", "root");
            password = props.getProperty("db.password");
            
            if (password == null || password.equals("tu_contraseña_aqui")) {
                System.out.println("⚠️ ADVERTENCIA: Debes configurar tu contraseña en config.properties");
            }
        }
    }

    public static DatabaseConnection getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
    
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}