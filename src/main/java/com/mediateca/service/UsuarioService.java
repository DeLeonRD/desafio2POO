package com.mediateca.service;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servicio para la gestión de usuarios en la base de datos.
 * Proporciona métodos para validar credenciales, obtener, listar,
 * insertar, actualizar y eliminar usuarios.
 * 
 * @author Mediateca Don Bosco
 */
public class UsuarioService {
    
    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());
    
    /**
     * Valida las credenciales de un usuario en la base de datos.
     * 
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public static boolean validarCredenciales(String email, String password) {
        logger.info("Intentando validar credenciales para email: " + email);
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND contrasena = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean resultado = rs.getInt(1) > 0;
                if (resultado) {
                    logger.info("Credenciales válidas para email: " + email);
                } else {
                    logger.warning("Credenciales inválidas para email: " + email);
                }
                return resultado;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al validar credenciales para email: " + email, e);
        }
        return false;
    }
    
    /**
     * Obtiene un usuario por su correo electrónico.
     * 
     * @param email Correo electrónico del usuario
     * @return Objeto Usuario si existe, null en caso contrario
     */
    public static Usuario obtenerPorEmail(String email) {
        logger.fine("Buscando usuario por email: " + email);
        String sql = "SELECT id_usuario, nombre, email, tipo, carrera, telefono FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getString("tipo"));
                u.setCarrera(rs.getString("carrera"));
                u.setTelefono(rs.getString("telefono"));
                logger.fine("Usuario encontrado: " + email);
                return u;
            }
            logger.fine("Usuario no encontrado: " + email);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener usuario por email: " + email, e);
        }
        return null;
    }
    
    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return Objeto Usuario si existe, null en caso contrario
     */
    public static Usuario obtenerPorId(int id) {
        logger.fine("Buscando usuario por ID: " + id);
        String sql = "SELECT id_usuario, nombre, email, tipo, carrera, telefono FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getString("tipo"));
                u.setCarrera(rs.getString("carrera"));
                u.setTelefono(rs.getString("telefono"));
                logger.fine("Usuario encontrado por ID: " + id);
                return u;
            }
            logger.fine("Usuario no encontrado por ID: " + id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener usuario por ID: " + id, e);
        }
        return null;
    }
    
    /**
     * Lista todos los usuarios registrados en el sistema.
     * 
     * @return Lista de objetos Usuario
     */
    public static List<Usuario> listarTodos() {
        logger.info("Listando todos los usuarios");
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, email, tipo, carrera, telefono FROM usuarios";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getString("tipo"));
                u.setCarrera(rs.getString("carrera"));
                u.setTelefono(rs.getString("telefono"));
                usuarios.add(u);
            }
            logger.info("Total de usuarios listados: " + usuarios.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar usuarios", e);
        }
        return usuarios;
    }
    
    /**
     * Verifica si existe un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return true si existe, false en caso contrario
     */
    public static boolean existeUsuario(int id) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar existencia de usuario ID: " + id, e);
        }
        return false;
    }
    
    /**
     * Verifica si existe un usuario por su email.
     * 
     * @param email Correo electrónico del usuario
     * @return true si existe, false en caso contrario
     */
    public static boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar existencia de email: " + email, e);
        }
        return false;
    }
    
    /**
     * Registra un nuevo usuario en la base de datos.
     * 
     * @param usuario Objeto Usuario con los datos a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public static boolean insertar(Usuario usuario) {
        logger.info("Insertando nuevo usuario: " + usuario.getEmail());
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, tipo, carrera, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getTipo());
            pstmt.setString(5, usuario.getCarrera());
            pstmt.setString(6, usuario.getTelefono());
            boolean resultado = pstmt.executeUpdate() > 0;
            if (resultado) {
                logger.info("Usuario insertado exitosamente: " + usuario.getEmail());
            } else {
                logger.warning("No se pudo insertar el usuario: " + usuario.getEmail());
            }
            return resultado;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar usuario: " + usuario.getEmail(), e);
            return false;
        }
    }
    
    /**
     * Actualiza los datos de un usuario existente.
     * 
     * @param usuario Objeto Usuario con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public static boolean actualizar(Usuario usuario) {
        logger.info("Actualizando usuario ID: " + usuario.getIdUsuario());
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, tipo = ?, carrera = ?, telefono = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getTipo());
            pstmt.setString(4, usuario.getCarrera());
            pstmt.setString(5, usuario.getTelefono());
            pstmt.setInt(6, usuario.getIdUsuario());
            boolean resultado = pstmt.executeUpdate() > 0;
            if (resultado) {
                logger.info("Usuario actualizado exitosamente ID: " + usuario.getIdUsuario());
            } else {
                logger.warning("No se pudo actualizar el usuario ID: " + usuario.getIdUsuario());
            }
            return resultado;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar usuario ID: " + usuario.getIdUsuario(), e);
            return false;
        }
    }
    
    /**
     * Elimina un usuario de la base de datos.
     * 
     * @param id ID del usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public static boolean eliminar(int id) {
        logger.warning("Eliminando usuario ID: " + id);
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            boolean resultado = pstmt.executeUpdate() > 0;
            if (resultado) {
                logger.info("Usuario eliminado exitosamente ID: " + id);
            } else {
                logger.warning("No se pudo eliminar el usuario ID: " + id);
            }
            return resultado;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar usuario ID: " + id, e);
            return false;
        }
    }
}