package com.mediateca.util;

import com.mediateca.model.Usuario;

/**
 * Mantiene la sesion del usuario actualmente logueado, accesible desde
 * cualquier panel de la aplicacion.
 *
 * Uso tipico:
 *   // al hacer login exitoso:
 *   SessionManager.iniciarSesion(usuario);
 *
 *   // desde cualquier panel:
 *   Usuario actual = SessionManager.getUsuarioActual();
 *
 *   // al cerrar sesion:
 *   SessionManager.cerrarSesion();
 */
public final class SessionManager {

    private static Usuario usuarioActual;

    private SessionManager() {
        // Clase utilitaria: no instanciable.
    }

    /** Marca al usuario como logueado en la sesion actual. */
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    /** Devuelve el usuario logueado, o {@code null} si no hay sesion activa. */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /** {@code true} si hay un usuario logueado. */
    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }

    /** Limpia la sesion actual (usado al cerrar sesion). */
    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
