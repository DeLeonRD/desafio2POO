/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediateca.util;

import com.mediateca.model.Usuario;

/**
 * Administra la sesión del usuario actual en la aplicación.
 * 
 * @author Mediateca System
 */
public class SessionManager {
    
    private static Usuario usuarioActual = null;
    
    /**
     * Inicia sesión con el usuario proporcionado.
     * 
     * @param usuario Usuario que ha iniciado sesión
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
        System.out.println("Sesión iniciada para: " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
    }
    
    /**
     * Cierra la sesión actual.
     */
    public static void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada para: " + usuarioActual.getNombre());
            usuarioActual = null;
        }
    }
    
    /**
     * Obtiene el usuario actualmente logueado.
     * 
     * @return Usuario actual, o null si no hay sesión activa
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Verifica si hay una sesión activa.
     * 
     * @return true si hay un usuario logueado, false en caso contrario
     */
    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    /**
     * Obtiene el tipo de usuario actual.
     * 
     * @return Tipo del usuario actual (ADMIN, PROFESOR, ALUMNO, EMPLEADO), 
     *         o null si no hay sesión activa
     */
    public static String getTipoUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getTipo() : null;
    }
    
    /**
     * Obtiene el ID del usuario actual.
     * 
     * @return ID del usuario actual, o -1 si no hay sesión activa
     */
    public static int getIdUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : -1;
    }
    
    /**
     * Obtiene el nombre del usuario actual.
     * 
     * @return Nombre del usuario actual, o null si no hay sesión activa
     */
    public static String getNombreUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getNombre() : null;
    }
}