package com.mediateca.util;

import java.io.IOException;
import java.util.logging.*;
import java.util.Date;

/**
 * Configuración centralizada del sistema de logs.
 * Configura los handlers para consola y archivo.
 * Filtra los mensajes de bajo nivel (FINEST, FINER, FINE) para que solo
 * se registren mensajes de nivel INFO, WARNING, SEVERE.
 * 
 * @author Mediateca Don Bosco
 */
public class LoggerConfig {

    /**
     * Configura el logger global de la aplicación.
     * - ConsoleHandler: muestra logs en consola
     * - FileHandler: guarda logs en archivo (logs/mediateca_%g.log)
     */
    public static void configurarLogger() {

        try {
            // Crear la carpeta logs si no existe
            java.io.File logDir = new java.io.File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Obtener el logger raíz
            Logger logger = Logger.getLogger("");

            // Remover handlers existentes para evitar duplicados
            for (Handler handler : logger.getHandlers()) {
                logger.removeHandler(handler);
            }

            // Deshabilitar el uso del handler padre
            logger.setUseParentHandlers(false);

            // =====================================================
            // HANDLER PARA CONSOLA
            // =====================================================
            ConsoleHandler console = new ConsoleHandler();
            console.setLevel(Level.INFO);  // Solo INFO, WARNING, SEVERE
            console.setFormatter(new SimpleFormatter());
            logger.addHandler(console);

            // =====================================================
            // HANDLER PARA ARCHIVO CON FORMATO PERSONALIZADO
            // =====================================================
            FileHandler archivo = new FileHandler(
                    "logs/mediateca_%g.log",
                    1024 * 1024,  // 1 MB
                    5,            // 5 archivos rotativos
                    true          // append mode
            );

            // Formatter personalizado que genera logs en UNA sola línea
            archivo.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format(
                        "%1$tF %1$tT %2$-7s %3$s %4$s%n",
                        new Date(record.getMillis()),
                        record.getLevel().getName(),
                        record.getLoggerName() != null ? record.getLoggerName() : "unknown",
                        record.getMessage() != null ? record.getMessage() : ""
                    );
                }
            });
            
            // Solo registrar niveles INFO, WARNING, SEVERE
            archivo.setLevel(Level.INFO);
            logger.addHandler(archivo);

            // Establecer nivel global
            logger.setLevel(Level.INFO);  // Cambiado de ALL a INFO

            System.out.println("Sistema de logs configurado correctamente.");
            System.out.println("Archivos de log en: logs/mediateca_*.log");
            System.out.println("Formato de log: FECHA HORA NIVEL CLASE MENSAJE");
            System.out.println("Niveles registrados: INFO, WARNING, SEVERE");

        } catch (IOException e) {
            System.err.println("Error al configurar el sistema de logs:");
            e.printStackTrace();
        }
    }
}