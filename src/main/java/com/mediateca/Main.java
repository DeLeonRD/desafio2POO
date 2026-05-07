package com.mediateca;

import com.mediateca.controller.PrestamoController;
import com.mediateca.dao.ConfiguracionDAO;
import com.mediateca.dao.DocumentoDAO;
import com.mediateca.db.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.Year;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PrestamoController prestamoController = new PrestamoController();
        ConfiguracionDAO configDAO = new ConfiguracionDAO();
        DocumentoDAO documentoDAO = new DocumentoDAO();
        
        System.out.println("=====================================");
        System.out.println("  SISTEMA DE PRÉSTAMOS - MEDIATECA   ");
        System.out.println("=====================================");
        
        // Probar conexión a BD
        if (DatabaseConnection.getInstancia().getConexion() != null) {
            System.out.println("✅ Conexión a base de datos: EXITOSA");
        } else {
            System.out.println("❌ Conexión a base de datos: FALLIDA");
        }
        
        int opcion = 0;
        while (opcion != 6) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ver configuración del sistema");
            System.out.println("2. Listar materiales disponibles");
            System.out.println("3. Verificar si un usuario tiene mora");
            System.out.println("4. Ver límite de préstamos de un usuario");
            System.out.println("5. Calcular mora de un préstamo");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                String entradaInvalida = scanner.next();
                System.out.println("❌ Error: '" + entradaInvalida + "' no es un número válido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    try {
                        System.out.println("\n--- CONFIGURACIÓN ACTUAL ---");
                        System.out.println("Máximo de ejemplares a prestar: " + configDAO.getMaxEjemplaresPrestamo());
                        System.out.println("Mora por día: USD " + configDAO.getMoraPorDia());
                        System.out.println("Mora por año de antigüedad: USD " + configDAO.getMoraPorAnio());
                    } catch (Exception e) {
                        System.out.println("Error al leer configuración: " + e.getMessage());
                    }
                    break;
                    
                case 2:
                    try {
                        System.out.println("\n--- MATERIALES DISPONIBLES ---");
                        documentoDAO.listarMateriales().forEach(m -> 
                            System.out.println("ID: " + m.getId() + " | Tipo: " + m.getTipo() + " | Año: " + m.getAnioPublicacion()));
                    } catch (Exception e) {
                        System.out.println("Error al listar materiales: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    try {
                        System.out.print("\nIngrese ID de usuario: ");
                        if (scanner.hasNextInt()) {
                            int idUser = scanner.nextInt();
                            boolean tieneMora = prestamoController.usuarioTieneMora(idUser);
                            System.out.println("¿El usuario tiene mora activa? " + (tieneMora ? "SÍ" : "NO"));
                            System.out.println("¿Puede realizar préstamo? " + (prestamoController.puedeRealizarPrestamo(idUser) ? "SÍ" : "NO (tiene mora)"));
                        } else {
                            System.out.println("❌ ID de usuario inválido");
                            scanner.next();
                        }
                    } catch (Exception e) {
                        System.out.println("Error al verificar mora: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    try {
                        System.out.print("\nIngrese ID de usuario: ");
                        if (scanner.hasNextInt()) {
                            int idUserLimite = scanner.nextInt();
                            int activos = prestamoController.contarPrestamosActivos(idUserLimite);
                            int maximo = configDAO.getMaxEjemplaresPrestamo();
                            System.out.println("Préstamos activos actuales: " + activos);
                            System.out.println("Límite permitido: " + maximo);
                            System.out.println("¿Puede solicitar otro préstamo? " + (prestamoController.respetaLimitePrestamos(idUserLimite) ? "SÍ" : "NO (alcanzó el límite)"));
                        } else {
                            System.out.println("❌ ID de usuario inválido");
                            scanner.next();
                        }
                    } catch (Exception e) {
                        System.out.println("Error al verificar límite: " + e.getMessage());
                    }
                    break;
                    
                case 5:
                    System.out.println("\n--- CÁLCULO DE MORA REAL ---");
                    try {
                        System.out.println("Préstamos activos en el sistema:");
                        String sqlPrestamos = "SELECT id_prestamo, id_usuario, id_material, fecha_prestamo, fecha_devolucion_esperada FROM prestamos WHERE estado = 'ACTIVO'";
                        try (Statement stmt = DatabaseConnection.getInstancia().getConexion().createStatement();
                             ResultSet rs = stmt.executeQuery(sqlPrestamos)) {
                            while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("id_prestamo") + 
                                                   " | Usuario: " + rs.getInt("id_usuario") +
                                                   " | Material: " + rs.getInt("id_material") +
                                                   " | Fecha esperada: " + rs.getDate("fecha_devolucion_esperada"));
                            }
                        }
                        
                        System.out.print("\nIngrese ID del préstamo para calcular mora: ");
                        int idPrestamo = scanner.nextInt();
                        
                        String sqlPrestamo = "SELECT id_usuario, id_material, fecha_devolucion_esperada FROM prestamos WHERE id_prestamo = ?";
                        int idUsuario = 0;
                        int idMaterial = 0;
                        Date fechaEsperada = null;
                        
                        try (PreparedStatement pstmt = DatabaseConnection.getInstancia().getConexion().prepareStatement(sqlPrestamo)) {
                            pstmt.setInt(1, idPrestamo);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                idUsuario = rs.getInt("id_usuario");
                                idMaterial = rs.getInt("id_material");
                                fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                            } else {
                                System.out.println("❌ No se encontró el préstamo con ID " + idPrestamo);
                                break;
                            }
                        }
                        
                        double mora = prestamoController.calcularMora(idPrestamo, idMaterial, fechaEsperada);
                        
                        LocalDate fechaEsperadaLocal = fechaEsperada.toLocalDate();
                        LocalDate hoy = LocalDate.now();
                        long diasRetraso = 0;
                        if (hoy.isAfter(fechaEsperadaLocal)) {
                            diasRetraso = ChronoUnit.DAYS.between(fechaEsperadaLocal, hoy);
                        }
                        
                        int anioPublicacion = documentoDAO.getAnioPublicacion(idMaterial);
                        int anioActual = Year.now().getValue();
                        int antiguedad = Math.max(0, anioActual - anioPublicacion);
                        
                        System.out.println("\n--- DETALLE DEL CÁLCULO ---");
                        System.out.println("ID Préstamo: " + idPrestamo);
                        System.out.println("ID Usuario: " + idUsuario);
                        System.out.println("ID Material: " + idMaterial);
                        System.out.println("Días de retraso: " + diasRetraso);
                        System.out.println("Mora por día: USD " + configDAO.getMoraPorDia());
                        System.out.println("Año publicación del material: " + anioPublicacion);
                        System.out.println("Antigüedad: " + antiguedad + " años");
                        System.out.println("Mora por año: USD " + configDAO.getMoraPorAnio());
                        System.out.println("\n💰 **MORA TOTAL CALCULADA: USD " + String.format("%.2f", mora) + "**");
                        if (mora > 0) {
                            System.out.println("⚠️ El usuario tiene una mora activa de USD " + String.format("%.2f", mora));
                        } else {
                            System.out.println("✅ El usuario no tiene mora.");
                        }
                        
                    } catch (Exception e) {
                        System.out.println("Error al calcular mora: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                    
                case 6:
                    System.out.println("\nSaliendo del sistema...");
                    break;
                    
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
        System.out.println("✅ Programa finalizado.");
    }
}