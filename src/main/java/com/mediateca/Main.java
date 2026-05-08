package com.mediateca;

import com.mediateca.controller.PrestamoController;
import com.mediateca.dao.ConfiguracionDAO;
import com.mediateca.dao.ConfiguracionPrestamoDAO;
import com.mediateca.dao.DocumentoDAO;
import com.mediateca.dao.UsuarioDAO;
import com.mediateca.dao.MaterialDAO;
import com.mediateca.dao.TipoMaterialDAO;
import com.mediateca.dao.TipoUsuarioDAO;
import com.mediateca.db.DatabaseConnection;
import com.mediateca.model.Usuario;
import com.mediateca.model.Material;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.Year;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static PrestamoController prestamoController = new PrestamoController();
    private static ConfiguracionDAO configDAO = new ConfiguracionDAO();
    private static ConfiguracionPrestamoDAO configPrestamoDAO = new ConfiguracionPrestamoDAO();
    private static DocumentoDAO documentoDAO = new DocumentoDAO();
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static MaterialDAO<Material> materialDAO = new MaterialDAO<Material>() {
        @Override public void insertar(Material obj) {}
        @Override public void listar() {}
        @Override public void actualizar(int id, Material obj) {}
        @Override public void eliminar(int id) {}
    };
    private static TipoMaterialDAO tipoMaterialDAO = new TipoMaterialDAO();
    private static TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
    
    private static Usuario usuarioActual = null;
    private static final int MAX_INTENTOS_LOGIN = 3;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("     SISTEMA DE PRESTAMOS - MEDIATECA      ");
        System.out.println("===========================================");
        
        try {
            if (DatabaseConnection.getInstancia().getConexion() != null) {
                System.out.println("[OK] Conexion a base de datos: EXITOSA");
            } else {
                System.out.println("[ERROR] Conexion a base de datos: FALLIDA");
                return;
            }
            
            boolean ejecutando = true;
            while (ejecutando) {
                if (!login()) {
                    System.out.println("[ERROR] No se pudo iniciar sesion. Saliendo...");
                    break;
                }
                
                boolean salirMenu = false;
                while (!salirMenu) {
                    switch (usuarioActual.getTipo()) {
                        case "ALUMNO":
                        case "PROFESOR":
                            salirMenu = menuUsuario();
                            break;
                        case "EMPLEADO":
                            salirMenu = menuEmpleado();
                            break;
                        case "ADMIN":
                            salirMenu = menuAdministrador();
                            break;
                        default:
                            System.out.println("[ERROR] Rol no reconocido: " + usuarioActual.getTipo());
                            salirMenu = true;
                    }
                }
                
                System.out.print("\n¿Desea salir del sistema? (S/N): ");
                String respuesta = scanner.next().toUpperCase();
                if (respuesta.equals("S")) {
                    ejecutando = false;
                }
            }
        } finally {
            try {
                if (DatabaseConnection.getInstancia().getConexion() != null && 
                    !DatabaseConnection.getInstancia().getConexion().isClosed()) {
                    DatabaseConnection.getInstancia().getConexion().close();
                    System.out.println("[OK] Conexion a BD cerrada correctamente.");
                }
            } catch (SQLException e) {
                System.out.println("[WARN] No se pudo cerrar la conexion: " + e.getMessage());
            }
            scanner.close();
        }
        System.out.println("[OK] Programa finalizado.");
    }
    
    // ==================== LOGIN ====================
    private static boolean login() {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS_LOGIN) {
            System.out.println("\n--- INICIO DE SESION ---");
            System.out.println("Intento " + (intentos + 1) + " de " + MAX_INTENTOS_LOGIN);
            
            if (intentos == 0) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
            
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine().trim();
            
            if (email.isEmpty() || pass.isEmpty()) {
                System.out.println("[ERROR] Email y contraseña son requeridos.");
                intentos++;
                if (intentos < MAX_INTENTOS_LOGIN) {
                    System.out.print("\nPresione Enter para continuar...");
                    scanner.nextLine();
                }
                continue;
            }
            
            if (usuarioDAO.validarCredenciales(email, pass)) {
                usuarioActual = usuarioDAO.obtenerPorEmail(email);
                System.out.println("\n[OK] Bienvenido " + usuarioActual.getNombre() + " (" + usuarioActual.getTipo() + ")");
                System.out.print("\nPresione Enter para continuar...");
                scanner.nextLine();
                return true;
            } else {
                System.out.println("[ERROR] Credenciales incorrectas.");
                intentos++;
                if (intentos < MAX_INTENTOS_LOGIN) {
                    System.out.print("\nPresione Enter para continuar...");
                    scanner.nextLine();
                }
            }
        }
        
        System.out.println("\n[ERROR] Ha superado el número máximo de intentos (" + MAX_INTENTOS_LOGIN + ").");
        return false;
    }
    
    // ==================== MENU USUARIO ====================
    private static boolean menuUsuario() {
        int opcion = 0;
        while (opcion != 4) {
            System.out.println("\n===========================================");
            System.out.println("       MENU USUARIO - " + usuarioActual.getTipo());
            System.out.println("===========================================");
            System.out.println("   1. Listar materiales disponibles por tipo");
            System.out.println("   2. Buscar material disponible por titulo");
            System.out.println("   3. Listar mis prestamos");
            System.out.println("   4. Cerrar sesion");
            System.out.print("\nSeleccione una opcion: ");
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1: listarMaterialesPorTipo(); break;
                case 2: buscarMaterialPorTitulo(); break;
                case 3: listarMisPrestamosTabla(); break;
                case 4: return true;
                default: System.out.println("[ERROR] Opcion no valida.");
            }
        }
        return true;
    }
    
    // ==================== MENU EMPLEADO ====================
    private static boolean menuEmpleado() {
        int opcion = 0;
        while (opcion != 11) {
            System.out.println("\n===========================================");
            System.out.println("            MENU EMPLEADO                  ");
            System.out.println("===========================================");
            System.out.println("CONSULTAS:");
            System.out.println("   1. Listar materiales disponibles");
            System.out.println("   2. Verificar si un usuario tiene mora");
            System.out.println("   3. Ver limite de prestamos por usuario");
            System.out.println("   4. Calcular mora de un prestamo");
            System.out.println("   5. Listar materiales por tipo");
            System.out.println("   6. Listar todos los prestamos");
            System.out.println("   7. Buscar material por ID");
            System.out.println("   8. Ver prestamos por usuario");
            System.out.println("\nPRESTAMOS:");
            System.out.println("   9. Registrar nuevo prestamo");
            System.out.println("  10. Registrar devolucion");
            System.out.println("\n  11. Cerrar sesion");
            System.out.print("\nSeleccione una opcion: ");
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1: listarMateriales(); break;
                case 2: verificarMora(); break;
                case 3: verLimitePrestamos(); break;
                case 4: calcularMora(); break;
                case 5: listarMaterialesPorTipo(); break;
                case 6: listarTodosPrestamosTabla(); break;
                case 7: buscarMaterialPorIdEmpleado(); break;
                case 8: verPrestamosPorUsuario(); break;
                case 9: registrarPrestamo(); break;
                case 10: registrarDevolucion(); break;
                case 11: return true;
                default: System.out.println("[ERROR] Opcion no valida.");
            }
        }
        return true;
    }
    
    // ==================== MENU ADMINISTRADOR ====================
    private static boolean menuAdministrador() {
        int opcion = 0;
        while (opcion != 17) {
            System.out.println("\n===========================================");
            System.out.println("          MENU ADMINISTRADOR               ");
            System.out.println("===========================================");
            System.out.println("CONSULTAS:");
            System.out.println("   1. Listar usuarios");
            System.out.println("   2. Buscar usuario por ID");
            System.out.println("   3. Listar todos los prestamos");
            System.out.println("   4. Listar materiales por estado");
            System.out.println("\nPROCESOS:");
            System.out.println("   5. Crear usuario");
            System.out.println("   6. Editar usuario");
            System.out.println("   7. Eliminar usuario");
            System.out.println("   8. Crear nuevo material");
            System.out.println("   9. Editar material");
            System.out.println("  10. Eliminar material");
            System.out.println("  11. Editar prestamo");
            System.out.println("\nCONFIGURACIONES:");
            System.out.println("  12. Configurar mora por dia");
            System.out.println("  13. Configurar dias de prestamo por tipo");
            System.out.println("  14. Ver configuraciones actuales");
            System.out.println("  15. Configurar tipos de materiales disponibles");
            System.out.println("  16. Configurar tipos de usuarios disponibles");
            System.out.println("\n  17. Cerrar sesion");
            System.out.print("\nSeleccione una opcion: ");
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1: listarUsuarios(); break;
                case 2: buscarUsuarioPorId(); break;
                case 3: listarTodosPrestamosTabla(); break;
                case 4: listarMaterialesPorEstado(); break;
                case 5: crearUsuarioAdmin(); break;
                case 6: editarUsuario(); break;
                case 7: eliminarUsuario(); break;
                case 8: crearMaterial(); break;
                case 9: editarMaterial(); break;
                case 10: eliminarMaterial(); break;
                case 11: editarPrestamo(); break;
                case 12: configurarMoraPorDia(); break;
                case 13: configurarDiasPrestamo(); break;
                case 14: verConfiguraciones(); break;
                case 15: menuConfigurarTiposMateriales(); break;
                case 16: menuConfigurarTiposUsuarios(); break;
                case 17: return true;
                default: System.out.println("[ERROR] Opcion no valida.");
            }
        }
        return true;
    }
    
    // ==================== CONFIGURACION DE TIPOS ====================
    
    private static void menuConfigurarTiposMateriales() {
        int opcion = 0;
        while (opcion != 4) {
            tipoMaterialDAO.mostrarTipos();
            System.out.println("\n--- CONFIGURAR TIPOS DE MATERIALES ---");
            System.out.println("   1. Crear nuevo tipo de material");
            System.out.println("   2. Editar tipo de material existente");
            System.out.println("   3. Ver tipos de materiales");
            System.out.println("   4. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1: crearTipoMaterial(); break;
                case 2: editarTipoMaterial(); break;
                case 3: tipoMaterialDAO.mostrarTipos(); pausar(); break;
                case 4: System.out.println("Volviendo..."); break;
                default: System.out.println("[ERROR] Opcion no valida.");
            }
        }
    }
    
    private static void menuConfigurarTiposUsuarios() {
        int opcion = 0;
        while (opcion != 4) {
            tipoUsuarioDAO.mostrarTipos();
            System.out.println("\n--- CONFIGURAR TIPOS DE USUARIOS ---");
            System.out.println("   1. Crear nuevo tipo de usuario");
            System.out.println("   2. Editar tipo de usuario existente");
            System.out.println("   3. Ver tipos de usuarios");
            System.out.println("   4. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1: crearTipoUsuario(); break;
                case 2: editarTipoUsuario(); break;
                case 3: tipoUsuarioDAO.mostrarTipos(); pausar(); break;
                case 4: System.out.println("Volviendo..."); break;
                default: System.out.println("[ERROR] Opcion no valida.");
            }
        }
    }
    
    private static void crearTipoMaterial() {
        System.out.println("\n--- CREAR NUEVO TIPO DE MATERIAL ---");
        scanner.nextLine();
        System.out.print("Nombre del nuevo tipo: ");
        String nombre = scanner.nextLine().trim().toUpperCase();
        System.out.print("Dias de prestamo para este tipo: ");
        int dias = scanner.nextInt();
        
        if (tipoMaterialDAO.crearTipo(nombre, dias)) {
            System.out.println("[OK] Tipo de material '" + nombre + "' creado con " + dias + " dias de prestamo.");
        } else {
            System.out.println("[ERROR] No se pudo crear. Puede que ya exista.");
        }
        pausar();
    }
    
    private static void editarTipoMaterial() {
        System.out.println("\n--- EDITAR TIPO DE MATERIAL ---");
        tipoMaterialDAO.mostrarTipos();
        scanner.nextLine();
        System.out.print("Nombre del tipo a editar: ");
        String nombreAntiguo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombreNuevo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Nuevos dias de prestamo (0 para mantener): ");
        int dias = scanner.nextInt();
        
        if (nombreNuevo.isEmpty()) {
            nombreNuevo = nombreAntiguo;
        }
        
        if (tipoMaterialDAO.editarTipo(nombreAntiguo, nombreNuevo, dias)) {
            System.out.println("[OK] Tipo de material actualizado.");
        } else {
            System.out.println("[ERROR] No se pudo actualizar.");
        }
        pausar();
    }
    
    private static void crearTipoUsuario() {
        System.out.println("\n--- CREAR NUEVO TIPO DE USUARIO ---");
        scanner.nextLine();
        System.out.print("Nombre del nuevo tipo: ");
        String nombre = scanner.nextLine().trim().toUpperCase();
        
        if (tipoUsuarioDAO.crearTipo(nombre)) {
            System.out.println("[OK] Tipo de usuario '" + nombre + "' creado.");
        } else {
            System.out.println("[ERROR] No se pudo crear. Puede que ya exista.");
        }
        pausar();
    }
    
    private static void editarTipoUsuario() {
        System.out.println("\n--- EDITAR TIPO DE USUARIO ---");
        tipoUsuarioDAO.mostrarTipos();
        scanner.nextLine();
        System.out.print("Nombre del tipo a editar: ");
        String nombreAntiguo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Nuevo nombre: ");
        String nombreNuevo = scanner.nextLine().trim().toUpperCase();
        
        if (tipoUsuarioDAO.editarTipo(nombreAntiguo, nombreNuevo)) {
            System.out.println("[OK] Tipo de usuario actualizado.");
        } else {
            System.out.println("[ERROR] No se pudo actualizar.");
        }
        pausar();
    }
    
    // ==================== CONFIGURACIONES BASICAS ====================
    
    private static void configurarMoraPorDia() {
        System.out.println("\n--- CONFIGURAR MORA POR DIA ---");
        System.out.println("Mora actual por dia: $" + configDAO.getMoraPorDia());
        System.out.print("Ingrese nuevo valor (USD): ");
        double nuevaMora = scanner.nextDouble();
        
        String sql = "UPDATE configuracion SET mora_por_dia = ? WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevaMora);
            if (pstmt.executeUpdate() > 0) {
                System.out.println("[OK] Mora por dia actualizada a $" + nuevaMora);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    private static void configurarDiasPrestamo() {
        System.out.println("\n--- CONFIGURAR DIAS DE PRESTAMO POR TIPO ---");
        configPrestamoDAO.mostrarConfiguracion();
        
        System.out.println("\n¿Qué tipo desea modificar?");
        System.out.println("   1. LIBRO");
        System.out.println("   2. REVISTA");
        System.out.println("   3. CD");
        System.out.println("   4. DVD");
        System.out.print("Seleccione una opcion: ");
        int tipoOpcion = scanner.nextInt();
        
        String tipo = "";
        switch (tipoOpcion) {
            case 1: tipo = "LIBRO"; break;
            case 2: tipo = "REVISTA"; break;
            case 3: tipo = "CD"; break;
            case 4: tipo = "DVD"; break;
            default: System.out.println("[ERROR] Opcion no valida."); pausar(); return;
        }
        
        System.out.print("Ingrese nuevo numero de dias para " + tipo + ": ");
        int nuevosDias = scanner.nextInt();
        
        if (configPrestamoDAO.actualizarDias(tipo, nuevosDias)) {
            System.out.println("[OK] Dias de prestamo para " + tipo + " actualizados a " + nuevosDias + " dias.");
        } else {
            System.out.println("[ERROR] No se pudo actualizar.");
        }
        pausar();
    }
    
    private static void verConfiguraciones() {
        System.out.println("\n--- VER CONFIGURACIONES ---");
        System.out.println("\n[CONFIGURACION DE MORA]");
        System.out.println("Mora por dia: $" + configDAO.getMoraPorDia());
        System.out.println("\n[CONFIGURACION DE DIAS DE PRESTAMO]");
        configPrestamoDAO.mostrarConfiguracion();
        System.out.println("\n[TIPOS DE MATERIALES]");
        tipoMaterialDAO.mostrarTipos();
        System.out.println("\n[TIPOS DE USUARIOS]");
        tipoUsuarioDAO.mostrarTipos();
        pausar();
    }
    
    // ==================== FUNCIONES DE PAUSA ====================
    private static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
        scanner.nextLine();
    }
    
    // ==================== MATERIALES ====================
    
    private static void mostrarUsuariosDisponibles() {
        System.out.println("\nUsuarios disponibles en el sistema:");
        System.out.println("+----+------------------+--------------------------+----------+");
        System.out.println("| ID | Nombre           | Email                    | Tipo     |");
        System.out.println("+----+------------------+--------------------------+----------+");
        for (Usuario u : usuarioDAO.listarTodos()) {
            System.out.printf("| %2d | %-16s | %-24s | %-8s |\n", 
                u.getIdUsuario(), 
                u.getNombre().length() > 16 ? u.getNombre().substring(0,13) + "..." : u.getNombre(),
                u.getEmail().length() > 24 ? u.getEmail().substring(0,21) + "..." : u.getEmail(),
                u.getTipo());
        }
        System.out.println("+----+------------------+--------------------------+----------+");
    }
    
    private static void listarMateriales() {
        System.out.println("\n--- MATERIALES DISPONIBLES ---");
        System.out.println("+----+----------+--------------------------+------+------------+");
        System.out.println("| ID | Tipo     | Titulo                   | Año  | Disponibles|");
        System.out.println("+----+----------+--------------------------+------+------------+");
        List<com.mediateca.model.Material> materiales = documentoDAO.listarMateriales();
        for (com.mediateca.model.Material m : materiales) {
            int disponibles = materialDAO.getCantidadDisponible(m.getId());
            String titulo = obtenerTituloMaterial(m.getId());
            String tituloCorto = titulo.length() > 24 ? titulo.substring(0,21) + "..." : titulo;
            System.out.printf("| %2d | %-8s | %-24s | %4d | %10d |\n", 
                m.getId(), m.getTipo(), tituloCorto, m.getAnioPublicacion(), disponibles);
        }
        System.out.println("+----+----------+--------------------------+------+------------+");
        pausar();
    }
    
    private static void listarMaterialesPorTipo() {
        System.out.println("\nSeleccione el tipo de material:");
        List<String> tipos = tipoMaterialDAO.listarTipos();
        for (int i = 0; i < tipos.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + tipos.get(i));
        }
        System.out.print("Opcion: ");
        int tipoOpcion = scanner.nextInt();
        
        if (tipoOpcion < 1 || tipoOpcion > tipos.size()) {
            System.out.println("[ERROR] Opcion no valida.");
            pausar();
            return;
        }
        
        String tipo = tipos.get(tipoOpcion - 1);
        
        System.out.println("\n--- MATERIALES TIPO: " + tipo + " ---");
        System.out.println("+----+--------------------------+------+------------+");
        System.out.println("| ID | Titulo                   | Año  | Disponibles|");
        System.out.println("+----+--------------------------+------+------------+");
        String sql = "SELECT id, titulo, anio_publicacion, cantidad_disponible FROM material WHERE tipo = ? AND cantidad_disponible > 0";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String tituloCorto = titulo != null && titulo.length() > 24 ? titulo.substring(0,21) + "..." : (titulo != null ? titulo : "Sin titulo");
                System.out.printf("| %2d | %-24s | %4d | %10d |\n", 
                    rs.getInt("id"), tituloCorto, rs.getInt("anio_publicacion"), rs.getInt("cantidad_disponible"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------------+------+------------+");
        pausar();
    }
    
    private static void buscarMaterialPorTitulo() {
        System.out.print("\nIngrese titulo a buscar: ");
        String titulo = "%" + scanner.next() + "%";
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("+----+----------+--------------------------+------+------------+");
        System.out.println("| ID | Tipo     | Titulo                   | Año  | Disponibles|");
        System.out.println("+----+----------+--------------------------+------+------------+");
        String sql = "SELECT id, tipo, titulo, anio_publicacion, cantidad_disponible FROM material WHERE titulo LIKE ? AND cantidad_disponible > 0";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tituloMat = rs.getString("titulo");
                String tituloCorto = tituloMat != null && tituloMat.length() > 24 ? tituloMat.substring(0,21) + "..." : (tituloMat != null ? tituloMat : "Sin titulo");
                System.out.printf("| %2d | %-8s | %-24s | %4d | %10d |\n", 
                    rs.getInt("id"), rs.getString("tipo"), tituloCorto, rs.getInt("anio_publicacion"), rs.getInt("cantidad_disponible"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+----------+--------------------------+------+------------+");
        pausar();
    }
    
    private static void listarMaterialesPorEstado() {
        System.out.println("\n--- MATERIALES DISPONIBLES ---");
        System.out.println("+----+----------+--------------------------+------------+");
        System.out.println("| ID | Tipo     | Titulo                   | Disponibles|");
        System.out.println("+----+----------+--------------------------+------------+");
        String sql = "SELECT id, tipo, titulo, cantidad_disponible FROM material WHERE cantidad_disponible > 0";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String tituloCorto = titulo != null && titulo.length() > 24 ? titulo.substring(0,21) + "..." : (titulo != null ? titulo : "Sin titulo");
                System.out.printf("| %2d | %-8s | %-24s | %10d |\n", 
                    rs.getInt("id"), rs.getString("tipo"), tituloCorto, rs.getInt("cantidad_disponible"));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+----------+--------------------------+------------+");
        
        System.out.println("\n--- MATERIALES EN CIRCULACION (PRESTADOS) ---");
        System.out.println("+----+----------+--------------------------+");
        System.out.println("| ID | Tipo     | Titulo                   |");
        System.out.println("+----+----------+--------------------------+");
        String sql2 = "SELECT DISTINCT m.id, m.tipo, m.titulo FROM material m JOIN prestamos p ON m.id = p.id_material WHERE p.estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql2)) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String tituloCorto = titulo != null && titulo.length() > 24 ? titulo.substring(0,21) + "..." : (titulo != null ? titulo : "Sin titulo");
                System.out.printf("| %2d | %-8s | %-24s |\n", 
                    rs.getInt("id"), rs.getString("tipo"), tituloCorto);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+----------+--------------------------+");
        pausar();
    }
    
    private static void buscarMaterialPorIdEmpleado() {
        System.out.print("\nIngrese ID del material: ");
        int id = scanner.nextInt();
        if (materialDAO.existeMaterial(id)) {
            String titulo = obtenerTituloMaterial(id);
            System.out.println("\n--- DATOS DEL MATERIAL ---");
            System.out.println("ID: " + id);
            System.out.println("Titulo: " + titulo);
            System.out.println("Año: " + materialDAO.getAnioPublicacion(id));
            System.out.println("Disponibles: " + materialDAO.getCantidadDisponible(id));
        } else {
            System.out.println("[ERROR] Material no existe.");
        }
        pausar();
    }
    
    private static String obtenerTituloMaterial(int idMaterial) {
        String sql = "SELECT titulo FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("titulo") != null ? rs.getString("titulo") : "Sin titulo";
            }
        } catch (SQLException e) {
            return "Error";
        }
        return "Sin titulo";
    }
    
    // ==================== PRESTAMOS ====================
    
    private static void listarMisPrestamosTabla() {
        System.out.println("\n--- MIS PRESTAMOS ---");
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        System.out.println("| ID | Material                 | Tipo     | Prestamo    | Vencimiento | Dias Vencidos  | Mora USD   |");
        System.out.println("|    |                          |          | Fecha       |             |                |            |");
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        
        String sql = "SELECT p.id_prestamo, p.id_material, p.fecha_prestamo, p.fecha_devolucion_esperada, m.tipo " +
                     "FROM prestamos p " +
                     "JOIN material m ON p.id_material = m.id " +
                     "WHERE p.id_usuario = ? AND p.estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioActual.getIdUsuario());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String titulo = obtenerTituloMaterial(rs.getInt("id_material"));
                String tituloCorto = titulo.length() > 22 ? titulo.substring(0,19) + "..." : titulo;
                String tipo = rs.getString("tipo");
                Date fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                LocalDate fechaVencimiento = fechaEsperada.toLocalDate();
                LocalDate hoy = LocalDate.now();
                long diasRetraso = hoy.isAfter(fechaVencimiento) ? ChronoUnit.DAYS.between(fechaVencimiento, hoy) : 0;
                double mora = diasRetraso * configDAO.getMoraPorDia();
                String diasVencimientoStr = (diasRetraso > 0) ? diasRetraso + " días" : "Vigente";
                System.out.printf("| %2d | %-24s | %-8s | %-11s | %-11s | %14s | %10.2f |\n", 
                    rs.getInt("id_prestamo"), tituloCorto, tipo,
                    rs.getDate("fecha_prestamo"), fechaEsperada, diasVencimientoStr, mora);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        pausar();
    }
    
    // ==================== TABLA DETALLADA DE PRESTAMOS ====================
    private static void listarTodosPrestamosTabla() {
        System.out.println("\n--- TODOS LOS PRESTAMOS ---");
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+------------+----------+");
        System.out.println("| ID | Material                 | Usuario          | Prestamo    | Vencimiento | Dias Vencidos  | Mora USD   | Estado   |");
        System.out.println("|    |                          |                  | Fecha       |             |                |            |          |");
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+------------+----------+");
        
        double moraPorDia = configDAO.getMoraPorDia();
        
        String sql = "SELECT p.id_prestamo, p.id_material, p.id_usuario, p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado, " +
                     "m.titulo, u.nombre " +
                     "FROM prestamos p " +
                     "JOIN material m ON p.id_material = m.id " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY p.id_prestamo DESC";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String tituloCorto = titulo != null && titulo.length() > 22 ? titulo.substring(0,19) + "..." : (titulo != null ? titulo : "Sin titulo");
                String nombre = rs.getString("nombre");
                String nombreCorto = nombre != null && nombre.length() > 16 ? nombre.substring(0,13) + "..." : (nombre != null ? nombre : "Desconocido");
                Date fechaPrestamo = rs.getDate("fecha_prestamo");
                Date fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                String estado = rs.getString("estado");
                
                long diasRetraso = 0;
                double mora = 0;
                String estadoMostrar = "";
                
                switch (estado) {
                    case "ACTIVO":
                        estadoMostrar = "EN CIRCULACION";
                        if (fechaEsperada != null) {
                            LocalDate fechaVencimiento = fechaEsperada.toLocalDate();
                            LocalDate hoy = LocalDate.now();
                            diasRetraso = hoy.isAfter(fechaVencimiento) ? ChronoUnit.DAYS.between(fechaVencimiento, hoy) : 0;
                            mora = diasRetraso * moraPorDia;
                        }
                        break;
                    case "DEVUELTO":
                        estadoMostrar = "DEVUELTO";
                        diasRetraso = 0;
                        mora = 0;
                        break;
                    case "VENCIDO":
                        estadoMostrar = "VENCIDO";
                        break;
                    default:
                        estadoMostrar = estado;
                }
                String diasVencimientoStr = (diasRetraso > 0) ? diasRetraso + " días" : "Vigente";
                
                System.out.printf("| %2d | %-24s | %-16s | %-11s | %-11s | %14s | %10.2f | %-8s |\n", 
                    rs.getInt("id_prestamo"), tituloCorto, nombreCorto,
                    fechaPrestamo, fechaEsperada, diasVencimientoStr, mora, estadoMostrar);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+------------+----------+");
        pausar();
    }
    
    private static void verPrestamosPorUsuario() {
        System.out.print("\nIngrese ID de usuario: ");
        int idUsuario = scanner.nextInt();
        
        if (!usuarioDAO.existeUsuario(idUsuario)) {
            System.out.println("[ERROR] Usuario no existe. IDs disponibles: 9,10,11,12,13,14,15");
            pausar();
            return;
        }
        
        Usuario usuario = usuarioDAO.obtenerPorId(idUsuario);
        String nombreUsuario = usuario != null ? usuario.getNombre() : "Desconocido";
        
        System.out.println("\n--- PRESTAMOS DEL USUARIO: " + nombreUsuario.toUpperCase() + " ---");
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        System.out.println("| ID | Material                 | Tipo     | Prestamo    | Vencimiento | Dias Vencidos  | Mora USD   |");
        System.out.println("|    |                          |          | Fecha       |             |                |            |");
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        
        double moraPorDia = configDAO.getMoraPorDia();
        String sql = "SELECT p.id_prestamo, p.id_material, p.fecha_prestamo, p.fecha_devolucion_esperada, m.tipo " +
                     "FROM prestamos p " +
                     "JOIN material m ON p.id_material = m.id " +
                     "WHERE p.id_usuario = ? AND p.estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            boolean hayPrestamos = false;
            while (rs.next()) {
                hayPrestamos = true;
                String titulo = obtenerTituloMaterial(rs.getInt("id_material"));
                String tituloCorto = titulo.length() > 22 ? titulo.substring(0,19) + "..." : titulo;
                String tipo = rs.getString("tipo");
                Date fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                LocalDate fechaVencimiento = fechaEsperada.toLocalDate();
                LocalDate hoy = LocalDate.now();
                long diasRetraso = hoy.isAfter(fechaVencimiento) ? ChronoUnit.DAYS.between(fechaVencimiento, hoy) : 0;
                double mora = diasRetraso * moraPorDia;
                String diasVencimientoStr = (diasRetraso > 0) ? diasRetraso + " días" : "Vigente";
                System.out.printf("| %2d | %-24s | %-8s | %-11s | %-11s | %14s | %10.2f |\n", 
                    rs.getInt("id_prestamo"), tituloCorto, tipo,
                    rs.getDate("fecha_prestamo"), fechaEsperada, diasVencimientoStr, mora);
            }
            if (!hayPrestamos) {
                System.out.println("|                         No hay prestamos activos para este usuario                         |");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------------+----------+-------------+-------------+----------------+------------+");
        pausar();
    }
    
    // ==================== PRESTAMOS FUNCIONES BASE ====================
    
    private static void verificarMora() {
        System.out.print("\nIngrese ID de usuario: ");
        int idUser = scanner.nextInt();
        if (!usuarioDAO.existeUsuario(idUser)) {
            System.out.println("[ERROR] Usuario no existe. IDs disponibles: 9,10,11,12,13,14,15");
            pausar();
            return;
        }
        boolean tieneMora = prestamoController.usuarioTieneMora(idUser);
        System.out.println("Tiene mora activa: " + (tieneMora ? "SI" : "NO"));
        System.out.println("Puede realizar prestamo: " + (prestamoController.puedeRealizarPrestamo(idUser) ? "SI" : "NO"));
        pausar();
    }
    
    private static void verLimitePrestamos() {
        System.out.print("\nIngrese ID de usuario: ");
        int idUser = scanner.nextInt();
        if (!usuarioDAO.existeUsuario(idUser)) {
            System.out.println("[ERROR] Usuario no existe. IDs disponibles: 9,10,11,12,13,14,15");
            pausar();
            return;
        }
        int activos = prestamoController.contarPrestamosActivos(idUser);
        int maximo = configDAO.getMaxEjemplaresPrestamo();
        System.out.println("Prestamos activos: " + activos + " / " + maximo);
        System.out.println("Puede solicitar otro: " + (activos < maximo ? "SI" : "NO"));
        pausar();
    }
    
    private static void calcularMora() {
        System.out.println("\n--- CALCULO DE MORA ---");
        try {
            List<Integer> prestamosIds = new ArrayList<>();
            List<Date> fechasEsperadas = new ArrayList<>();
            List<Integer> materialesIds = new ArrayList<>();
            
            System.out.println("+----+--------------------------+-------------+");
            System.out.println("| ID | Material                 | Fecha       |");
            System.out.println("|    |                          | Esperada    |");
            System.out.println("+----+--------------------------+-------------+");
            String sql = "SELECT id_prestamo, id_material, fecha_devolucion_esperada FROM prestamos WHERE estado = 'ACTIVO'";
            try (Statement stmt = DatabaseConnection.getInstancia().getConexion().createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    prestamosIds.add(rs.getInt("id_prestamo"));
                    materialesIds.add(rs.getInt("id_material"));
                    fechasEsperadas.add(rs.getDate("fecha_devolucion_esperada"));
                    String titulo = obtenerTituloMaterial(rs.getInt("id_material"));
                    String tituloCorto = titulo.length() > 24 ? titulo.substring(0,21) + "..." : titulo;
                    System.out.printf("| %2d | %-24s | %-11s |\n", 
                        rs.getInt("id_prestamo"), tituloCorto, rs.getDate("fecha_devolucion_esperada"));
                }
            }
            System.out.println("+----+--------------------------+-------------+");
            
            if (prestamosIds.isEmpty()) {
                System.out.println("No hay prestamos activos.");
                pausar();
                return;
            }
            
            System.out.print("\nIngrese ID del prestamo: ");
            int idPrestamo = scanner.nextInt();
            
            int index = -1;
            for (int i = 0; i < prestamosIds.size(); i++) {
                if (prestamosIds.get(i) == idPrestamo) {
                    index = i;
                    break;
                }
            }
            
            if (index == -1) {
                System.out.println("[ERROR] Prestamo no encontrado.");
                pausar();
                return;
            }
            
            int idMaterial = materialesIds.get(index);
            Date fechaEsperada = fechasEsperadas.get(index);
            
            double mora = prestamoController.calcularMora(idPrestamo, idMaterial, fechaEsperada);
            long diasRetraso = ChronoUnit.DAYS.between(fechaEsperada.toLocalDate(), LocalDate.now());
            if (diasRetraso < 0) diasRetraso = 0;
            int antiguedad = Year.now().getValue() - materialDAO.getAnioPublicacion(idMaterial);
            
            System.out.println("\n--- DETALLE DEL CALCULO ---");
            System.out.println("Dias de retraso: " + diasRetraso);
            System.out.println("Antiguedad del material: " + antiguedad + " años");
            System.out.println("Mora por dia: $" + configDAO.getMoraPorDia());
            System.out.println("Mora por año: $" + configDAO.getMoraPorAnio());
            System.out.println("\n>>> MORA TOTAL: $" + String.format("%.2f", mora));
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    // ==================== REGISTRAR PRESTAMO ====================
    private static void registrarPrestamo() {
        System.out.println("\n--- REGISTRAR PRESTAMO ---");
        
        mostrarUsuariosDisponibles();
        
        System.out.print("ID Usuario: ");
        int idUsuario = scanner.nextInt();
        System.out.print("ID Material: ");
        int idMaterial = scanner.nextInt();
        
        if (!usuarioDAO.existeUsuario(idUsuario)) {
            System.out.println("[ERROR] Usuario no existe. Use uno de los IDs de la lista.");
            pausar();
            return;
        }
        if (!materialDAO.existeMaterial(idMaterial)) {
            System.out.println("[ERROR] Material no existe.");
            pausar();
            return;
        }
        if (!materialDAO.tieneDisponibilidad(idMaterial)) {
            System.out.println("[ERROR] Material no disponible.");
            pausar();
            return;
        }
        if (prestamoController.usuarioTieneMora(idUsuario)) {
            System.out.println("[ERROR] Usuario tiene mora.");
            pausar();
            return;
        }
        int activos = prestamoController.contarPrestamosActivos(idUsuario);
        int maximo = configDAO.getMaxEjemplaresPrestamo();
        if (activos >= maximo) {
            System.out.println("[ERROR] Limite alcanzado (" + maximo + ")");
            pausar();
            return;
        }
        
        Usuario usuario = usuarioDAO.obtenerPorId(idUsuario);
        String nombreUsuario = usuario != null ? usuario.getNombre() : "Desconocido";
        String tituloMaterial = obtenerTituloMaterial(idMaterial);
        
        String tipoMaterial = "";
        String sqlTipo = "SELECT tipo FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sqlTipo)) {
            pstmt.setInt(1, idMaterial);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tipoMaterial = rs.getString("tipo");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        
        int diasPrestamo = tipoMaterialDAO.getDiasPrestamo(tipoMaterial);
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(diasPrestamo);
        
        String sql = "INSERT INTO prestamos (id_usuario, id_material, fecha_prestamo, fecha_devolucion_esperada, estado) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), 'ACTIVO')";
        int idPrestamoGenerado = 0;
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idMaterial);
            pstmt.setInt(3, diasPrestamo);
            if (pstmt.executeUpdate() > 0) {
                materialDAO.reducirDisponibilidad(idMaterial);
                
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPrestamoGenerado = generatedKeys.getInt(1);
                }
                
                System.out.println("\n[OK] PRESTAMO REGISTRADO EXITOSAMENTE");
                System.out.println("\n--- DETALLE DEL PRESTAMO ---");
                System.out.println("+----+--------------------------+------------------+-------------+-------------+");
                System.out.println("| ID | Material                 | Usuario          | Prestamo    | Devolucion  |");
                System.out.println("|    |                          |                  | Fecha       | Esperada    |");
                System.out.println("+----+--------------------------+------------------+-------------+-------------+");
                String tituloCorto = tituloMaterial.length() > 24 ? tituloMaterial.substring(0,21) + "..." : tituloMaterial;
                String nombreCorto = nombreUsuario.length() > 16 ? nombreUsuario.substring(0,13) + "..." : nombreUsuario;
                System.out.printf("| %2d | %-24s | %-16s | %-11s | %-11s |\n", 
                    idPrestamoGenerado, tituloCorto, nombreCorto, fechaPrestamo, fechaDevolucion);
                System.out.println("+----+--------------------------+------------------+-------------+-------------+");
                System.out.println("Fecha de devolucion esperada: " + fechaDevolucion + " (" + diasPrestamo + " dias a partir de hoy)");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    // ==================== REGISTRAR DEVOLUCION ====================
    private static void registrarDevolucion() {
        System.out.println("\n--- REGISTRAR DEVOLUCION ---");
        
        System.out.println("Prestamos activos en el sistema:");
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+");
        System.out.println("| ID | Material                 | Usuario          | Prestamo    | Vencimiento | Dias Vencidos  |");
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+");
        
        String sqlPrestamos = "SELECT p.id_prestamo, p.id_material, p.id_usuario, p.fecha_prestamo, p.fecha_devolucion_esperada, " +
                               "m.titulo, u.nombre " +
                               "FROM prestamos p " +
                               "JOIN material m ON p.id_material = m.id " +
                               "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                               "WHERE p.estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlPrestamos)) {
            
            boolean hayPrestamos = false;
            while (rs.next()) {
                hayPrestamos = true;
                String titulo = rs.getString("titulo");
                String tituloCorto = titulo != null && titulo.length() > 22 ? titulo.substring(0,19) + "..." : (titulo != null ? titulo : "Sin titulo");
                String nombre = rs.getString("nombre");
                String nombreCorto = nombre != null && nombre.length() > 16 ? nombre.substring(0,13) + "..." : (nombre != null ? nombre : "Desconocido");
                Date fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                LocalDate fechaVencimiento = fechaEsperada.toLocalDate();
                LocalDate hoy = LocalDate.now();
                long diasRetraso = hoy.isAfter(fechaVencimiento) ? ChronoUnit.DAYS.between(fechaVencimiento, hoy) : 0;
                String diasVencimientoStr = (diasRetraso > 0) ? diasRetraso + " días" : "Vigente";
                
                System.out.printf("| %2d | %-24s | %-16s | %-11s | %-11s | %14s |\n", 
                    rs.getInt("id_prestamo"), tituloCorto, nombreCorto,
                    rs.getDate("fecha_prestamo"), fechaEsperada, diasVencimientoStr);
            }
            if (!hayPrestamos) {
                System.out.println("|                     No hay prestamos activos para devolver                     |");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        System.out.println("+----+--------------------------+------------------+-------------+-------------+----------------+");
        
        System.out.print("\nIngrese ID del prestamo a devolver: ");
        int idPrestamo = scanner.nextInt();
        
        String checkSql = "SELECT id_material, id_usuario, fecha_devolucion_esperada FROM prestamos WHERE id_prestamo = ? AND estado = 'ACTIVO'";
        int idMaterial = -1;
        int idUsuario = -1;
        Date fechaEsperada = null;
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setInt(1, idPrestamo);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println("[ERROR] Prestamo no existe o ya fue devuelto.");
                pausar();
                return;
            }
            idMaterial = rs.getInt("id_material");
            idUsuario = rs.getInt("id_usuario");
            fechaEsperada = rs.getDate("fecha_devolucion_esperada");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
            pausar();
            return;
        }
        
        long diasRetraso = ChronoUnit.DAYS.between(fechaEsperada.toLocalDate(), LocalDate.now());
        if (diasRetraso < 0) diasRetraso = 0;
        double mora = diasRetraso * configDAO.getMoraPorDia();
        
        System.out.println("\n--- RESUMEN DE DEVOLUCION ---");
        System.out.println("ID Prestamo: " + idPrestamo);
        System.out.println("Dias de retraso: " + diasRetraso);
        System.out.println("Mora por dia: $" + configDAO.getMoraPorDia());
        System.out.println("Mora a pagar: $" + String.format("%.2f", mora));
        
        System.out.print("\n¿Confirmar devolucion? (S/N): ");
        String confirm = scanner.next().toUpperCase();
        if (!confirm.equals("S")) {
            System.out.println("Devolucion cancelada.");
            pausar();
            return;
        }
        
        String sql = "UPDATE prestamos SET estado = 'DEVUELTO', fecha_devolucion_real = CURDATE(), mora_total = ? WHERE id_prestamo = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, mora);
            pstmt.setInt(2, idPrestamo);
            if (pstmt.executeUpdate() > 0) {
                materialDAO.aumentarDisponibilidad(idMaterial);
                System.out.println("\n[OK] DEVOLUCION REGISTRADA EXITOSAMENTE");
                System.out.println("Mora cobrada: $" + String.format("%.2f", mora));
            } else {
                System.out.println("[ERROR] No se pudo registrar la devolucion.");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    private static void editarPrestamo() {
        System.out.print("ID Prestamo: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nuevo estado (ACTIVO/DEVUELTO/VENCIDO): ");
        String estado = scanner.nextLine().toUpperCase();
        
        String sql = "UPDATE prestamos SET estado = ? WHERE id_prestamo = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setInt(2, id);
            if (pstmt.executeUpdate() > 0) {
                System.out.println("[OK] Prestamo actualizado.");
                if ("DEVUELTO".equals(estado)) {
                    String sqlMat = "SELECT id_material FROM prestamos WHERE id_prestamo = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sqlMat)) {
                        pstmt2.setInt(1, id);
                        ResultSet rs = pstmt2.executeQuery();
                        if (rs.next()) {
                            materialDAO.aumentarDisponibilidad(rs.getInt("id_material"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    // ==================== CREAR MATERIAL ====================
    
    private static void crearMaterial() {
        System.out.println("\n--- CREAR MATERIAL ---");
        scanner.nextLine();
        
        List<String> tipos = tipoMaterialDAO.listarTipos();
        System.out.println("Seleccione el tipo de material:");
        for (int i = 0; i < tipos.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + tipos.get(i));
        }
        System.out.print("Opcion: ");
        int tipoOpcion = scanner.nextInt();
        scanner.nextLine();
        
        if (tipoOpcion < 1 || tipoOpcion > tipos.size()) {
            System.out.println("[ERROR] Opcion no valida.");
            pausar();
            return;
        }
        
        String tipo = tipos.get(tipoOpcion - 1);
        
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String autor = scanner.nextLine().trim();
        System.out.print("Año: ");
        int anio = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ubicacion: ");
        String ubicacion = scanner.nextLine().trim();
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        
        String sql = "INSERT INTO material (tipo, titulo, autor, anio_publicacion, ubicacion, cantidad_total, cantidad_disponible) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            pstmt.setString(2, titulo);
            pstmt.setString(3, autor);
            pstmt.setInt(4, anio);
            pstmt.setString(5, ubicacion);
            pstmt.setInt(6, cantidad);
            pstmt.setInt(7, cantidad);
            pstmt.executeUpdate();
            System.out.println("[OK] Material creado.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    private static void editarMaterial() {
        System.out.print("ID Material: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nuevo titulo (Enter mantener): ");
        String titulo = scanner.nextLine();
        System.out.print("Nuevo autor (Enter mantener): ");
        String autor = scanner.nextLine();
        
        String sql = "UPDATE material SET titulo = COALESCE(NULLIF(?, ''), titulo), autor = COALESCE(NULLIF(?, ''), autor) WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("[OK] Material actualizado.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        pausar();
    }
    
    private static void eliminarMaterial() {
        System.out.print("ID Material: ");
        int id = scanner.nextInt();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.next();
        if (confirm.equalsIgnoreCase("S")) {
            String sql = "DELETE FROM material WHERE id = ?";
            try (Connection conn = DatabaseConnection.getInstancia().getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                System.out.println("[OK] Material eliminado.");
            } catch (SQLException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        pausar();
    }
    
    // ==================== USUARIOS ====================
    
    private static void listarUsuarios() {
        System.out.println("\n--- USUARIOS ---");
        System.out.println("+----+------------------+--------------------------+----------+");
        System.out.println("| ID | Nombre           | Email                    | Tipo     |");
        System.out.println("+----+------------------+--------------------------+----------+");
        for (Usuario u : usuarioDAO.listarTodos()) {
            String nombre = u.getNombre();
            String nombreCorto = nombre != null && nombre.length() > 16 ? nombre.substring(0,13) + "..." : (nombre != null ? nombre : "N/A");
            String email = u.getEmail();
            String emailCorto = email != null && email.length() > 24 ? email.substring(0,21) + "..." : (email != null ? email : "N/A");
            System.out.printf("| %2d | %-16s | %-24s | %-8s |\n", 
                u.getIdUsuario(), nombreCorto, emailCorto, u.getTipo());
        }
        System.out.println("+----+------------------+--------------------------+----------+");
        pausar();
    }
    
    private static void buscarUsuarioPorId() {
        System.out.print("ID Usuario: ");
        int id = scanner.nextInt();
        Usuario u = usuarioDAO.obtenerPorId(id);
        if (u != null) {
            System.out.println("\n--- DATOS DEL USUARIO ---");
            System.out.println("ID: " + u.getIdUsuario());
            System.out.println("Nombre: " + u.getNombre());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Tipo: " + u.getTipo());
            System.out.println("Carrera: " + (u.getCarrera() != null ? u.getCarrera() : "No especificada"));
            System.out.println("Telefono: " + (u.getTelefono() != null ? u.getTelefono() : "No especificado"));
        } else {
            System.out.println("[ERROR] No existe.");
        }
        pausar();
    }
    
    private static void crearUsuarioAdmin() {
        System.out.println("\n--- CREAR USUARIO ---");
        scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (usuarioDAO.existeEmail(email)) {
            System.out.println("[ERROR] Email ya registrado.");
            pausar();
            return;
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine().trim();
        
        List<String> tipos = tipoUsuarioDAO.listarTipos();
        System.out.println("\nSeleccione el tipo de usuario (ingrese el número):");
        for (int i = 0; i < tipos.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + tipos.get(i));
        }
        System.out.print("Opcion: ");
        
        int tipoOpcion = 0;
        try {
            tipoOpcion = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("[ERROR] Debe ingresar un número válido.");
            scanner.nextLine();
            pausar();
            return;
        }
        scanner.nextLine();
        
        if (tipoOpcion < 1 || tipoOpcion > tipos.size()) {
            System.out.println("[ERROR] Opcion no valida. Debe ser un número entre 1 y " + tipos.size());
            pausar();
            return;
        }
        
        String tipo = tipos.get(tipoOpcion - 1);
        
        System.out.print("Carrera (opcional): ");
        String carrera = scanner.nextLine().trim();
        System.out.print("Telefono (opcional): ");
        String telefono = scanner.nextLine().trim();
        
                Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setContrasena(pass);
        u.setTipo(tipo);
        u.setCarrera(carrera.isEmpty() ? null : carrera);
        u.setTelefono(telefono.isEmpty() ? null : telefono);
        
        if (usuarioDAO.insertar(u)) {
            System.out.println("[OK] Usuario creado.");
        } else {
            System.out.println("[ERROR] No se pudo crear.");
        }
        pausar();
    }
    
    private static void editarUsuario() {
        System.out.print("ID Usuario: ");
        int id = scanner.nextInt();
        Usuario u = usuarioDAO.obtenerPorId(id);
        if (u == null) {
            System.out.println("[ERROR] No existe.");
            return;
        }
        scanner.nextLine();
        System.out.print("Nuevo nombre (Enter mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) u.setNombre(nombre);
        System.out.print("Nuevo email (Enter mantener): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) u.setEmail(email);
        System.out.print("Nuevo tipo (Enter mantener): ");
        String tipo = scanner.nextLine().toUpperCase();
        if (!tipo.isEmpty()) u.setTipo(tipo);
        
        if (usuarioDAO.actualizar(u)) {
            System.out.println("[OK] Usuario actualizado.");
        }
        pausar();
    }
    
    private static void eliminarUsuario() {
        System.out.print("ID Usuario: ");
        int id = scanner.nextInt();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.next();
        if (confirm.equalsIgnoreCase("S")) {
            if (usuarioDAO.eliminar(id)) {
                System.out.println("[OK] Usuario eliminado.");
            }
        }
        pausar();
    }
}