package com.mediateca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection conexion;
    private String url = "jdbc:mysql://localhost:3306/mediateca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private String user = "root";
    private String password = "323564.f";

    private DatabaseConnection() {
        conectar();
    }

    private void conectar() {
        try {
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la BD");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
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