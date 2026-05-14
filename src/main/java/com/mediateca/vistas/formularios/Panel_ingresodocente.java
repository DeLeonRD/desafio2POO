package com.mediateca.vistas.formularios;

import com.mediateca.service.UsuarioService;
import com.mediateca.model.Usuario;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Ventana_PPAL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Panel para el registro de nuevos docentes en el sistema.
 * Permite ingresar nombre, correo, contraseña, teléfono y carrera del docente.
 */
public class Panel_ingresodocente extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(Panel_ingresodocente.class.getName());
    private static final String REGEX_EMAIL = "^[\\w.\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$";

    public Panel_ingresodocente() {
        initComponents();
        cablearEventos();
    }

    /**
     * Conecta los botones con sus respectivas acciones.
     */
    private void cablearEventos() {
        botn_registrar.addActionListener(e -> registrar());
        botn_limpiarcampos.addActionListener(e -> limpiarCampos());
        botn_cancelar.addActionListener(e -> volverAlMenu());
        botn_revisar_incongrunDB.addActionListener(e -> revisarEmailEnBD());
    }

    /**
     * Valida los campos y registra un nuevo docente en la base de datos.
     */
    private void registrar() {
        String nombre = campo_nombre.getText().trim();
        String email = campo_correo.getText().trim();
        String password = campo_contraseña.getText().trim();
        String telefono = campo_telefono.getText().trim();
        String carrera = campo_carrera.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mostrarError("Nombre, correo y contraseña son obligatorios.");
            return;
        }
        if (!email.matches(REGEX_EMAIL)) {
            mostrarError("El correo no tiene un formato válido.");
            return;
        }
        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        try {
            if (UsuarioService.existeEmail(email)) {
                mostrarError("Ya existe un usuario con ese correo.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setEmail(email);
            nuevo.setContrasena(password);
            nuevo.setTipo("PROFESOR");
            nuevo.setCarrera(carrera);
            nuevo.setTelefono(telefono);

            if (UsuarioService.insertar(nuevo)) {
                JOptionPane.showMessageDialog(this, "Docente registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                mostrarError("No se pudo registrar el docente.");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al registrar docente", ex);
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }

    /**
     * Verifica si el correo ingresado ya existe en la base de datos.
     */
    private void revisarEmailEnBD() {
        String email = campo_correo.getText().trim();
        if (email.isEmpty()) {
            mostrarError("Ingresa un correo para revisarlo.");
            return;
        }
        try {
            boolean existe = UsuarioService.existeEmail(email);
            JOptionPane.showMessageDialog(this,
                existe ? "El correo YA está registrado en la BD." : "El correo está libre, puede usarse.",
                "Revisión de correo",
                existe ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al revisar email", ex);
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        campo_carnet.setText("");
        campo_nombre.setText("");
        campo_correo.setText("");
        campo_telefono.setText("");
        campo_contraseña.setText("");
        campo_carrera.setText("");
        campo_nombre.requestFocus();
    }

    /**
     * Regresa al menú principal del administrador.
     */
    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        campo_carnet = new javax.swing.JTextField();
        campo_nombre = new javax.swing.JTextField();
        campo_correo = new javax.swing.JTextField();
        campo_telefono = new javax.swing.JTextField();
        campo_contraseña = new javax.swing.JTextField();
        campo_carrera = new javax.swing.JTextField();
        botn_registrar = new javax.swing.JButton();
        botn_limpiarcampos = new javax.swing.JButton();
        botn_revisar_incongrunDB = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        botn_cancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(11, 19, 43));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Carnet");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre Completo");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Correo");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Telefono");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Contraseña");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Carrera");

        campo_carnet.setFont(new java.awt.Font("Arial", 0, 14));
        campo_nombre.setFont(new java.awt.Font("Arial", 0, 14));
        campo_correo.setFont(new java.awt.Font("Arial", 0, 14));
        campo_telefono.setFont(new java.awt.Font("Arial", 0, 14));
        campo_contraseña.setFont(new java.awt.Font("Arial", 0, 14));
        campo_carrera.setFont(new java.awt.Font("Arial", 0, 14));

        botn_registrar.setText("REGISTRAR");
        botn_limpiarcampos.setText("LIMPIAR");
        botn_revisar_incongrunDB.setText("REVISION");

        jLabel12.setFont(new java.awt.Font("Arial Black", 0, 24));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("REGISTRO DE NUEVO DOCENTE");

        botn_cancelar.setText("CANCELAR");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(campo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(campo_carrera, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                    .addComponent(campo_contraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                    .addComponent(campo_correo, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                    .addComponent(campo_carnet, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campo_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(botn_revisar_incongrunDB)
                                .addGap(53, 53, 53)
                                .addComponent(botn_registrar)
                                .addGap(53, 53, 53)
                                .addComponent(botn_limpiarcampos)
                                .addGap(36, 36, 36)
                                .addComponent(botn_cancelar)))))
                .addContainerGap(410, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel12)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(campo_carnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(campo_correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campo_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campo_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(campo_carrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(168, 168, 168)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botn_revisar_incongrunDB)
                    .addComponent(botn_registrar)
                    .addComponent(botn_limpiarcampos)
                    .addComponent(botn_cancelar))
                .addContainerGap(84, Short.MAX_VALUE))
        );
    }

    private javax.swing.JButton botn_cancelar;
    private javax.swing.JButton botn_limpiarcampos;
    private javax.swing.JButton botn_registrar;
    private javax.swing.JButton botn_revisar_incongrunDB;
    private javax.swing.JTextField campo_carnet;
    private javax.swing.JTextField campo_carrera;
    private javax.swing.JTextField campo_contraseña;
    private javax.swing.JTextField campo_correo;
    private javax.swing.JTextField campo_nombre;
    private javax.swing.JTextField campo_telefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
}