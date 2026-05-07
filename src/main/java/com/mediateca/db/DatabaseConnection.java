package com.mediateca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection conexion;
    private String url, user, password;

    private DatabaseConnection() {
        conectar();
    }

    private void conectar() {
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
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                url = "jdbc:mysql://localhost:3306/mediateca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                user = "root";
                password = "root123";
                return;
            }
            props.load(input);
            url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/mediateca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            user = props.getProperty("db.user", "root");
            password = props.getProperty("db.password", "root123");
        }
    }

    public static DatabaseConnection getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            conectar();
        }
        return conexion;
    }
}