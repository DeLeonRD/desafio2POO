package com.mediateca.util;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * - Utilería para validaciones de interfaz de usuario.
 * - Centraliza la lógica de mensajes para mantener consistencia en la
 * aplicación.
 */
public class ValidacionUtil {

    /**
     * - Verifica si un campo está vacío y muestra un mensaje de advertencia.
     */
    public static boolean esTextoVacio(String texto, String nombreCampo, Component padre) {
        if (texto == null || texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(padre,
                    "El campo " + nombreCampo + " es obligatorio",
                    "Validación de Formulario",
                    JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * - Valida si la entrada es un número entero válido.
     */
    public static boolean esNumeroValido(String texto, String nombreCampo, Component padre) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            // Nota para De Leon: Aquí se podría disparar un log de Log4J si se considera
            // crítico
            JOptionPane.showMessageDialog(padre,
                    "El campo " + nombreCampo + " debe ser un numero entero",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
