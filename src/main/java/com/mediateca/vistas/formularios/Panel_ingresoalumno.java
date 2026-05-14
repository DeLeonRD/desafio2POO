/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas.formularios;
 
import com.mediateca.service.UsuarioService;
import com.mediateca.model.Usuario;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Ventana_PPAL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
 
/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 */
public class Panel_ingresoalumno extends javax.swing.JPanel {
 
    private static final Logger logger = Logger.getLogger(Panel_ingresoalumno.class.getName());
    private static final String REGEX_EMAIL = "^[\\w.\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$";
 
    /**
     * Creates new form Panel_ingresoalum
     */
    public Panel_ingresoalumno() {
        initComponents();
        cablearEventos();
    }
 
    private void cablearEventos() {
        botn_registrar.addActionListener(e -> registrar());
        botn_limpiarcampos.addActionListener(e -> limpiarCampos());
        botn_cancelar.addActionListener(e -> volverAlMenu());
        botn_revisar_incongrunDB.addActionListener(e -> revisarEmailEnBD());
    }
 
    /**
     * Valida campos, verifica que el email no exista, y guarda el alumno.
     * Nota: los campos carnet, estrato y anio de ingreso no se persisten porque
     * la tabla `usuarios` no tiene esas columnas. Solo se guardan: nombre,
     * email, contrasena, tipo (=ALUMNO), carrera, telefono.
     */
    private void registrar() {
        String nombre   = campo_nombre.getText().trim();
        String email    = campo_correo.getText().trim();
        String password = campo_contraseña.getText().trim();
        String telefono = campo_telefono.getText().trim();
        String carrera  = campo_carrera.getText().trim();
 
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
            nuevo.setTipo("ALUMNO");
            nuevo.setCarrera(carrera);
            nuevo.setTelefono(telefono);
 
            if (UsuarioService.insertar(nuevo)) {
                JOptionPane.showMessageDialog(this,
                    "Alumno registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                mostrarError("No se pudo registrar el alumno. Revisa los logs.");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al registrar alumno", ex);
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }
 
    /** Verifica si el correo ingresado ya existe en la BD. */
    private void revisarEmailEnBD() {
        String email = campo_correo.getText().trim();
        if (email.isEmpty()) {
            mostrarError("Ingresa un correo para revisarlo.");
            return;
        }
        try {
            boolean existe = UsuarioService.existeEmail(email);
            JOptionPane.showMessageDialog(this,
                existe ? "El correo YA está registrado en la BD."
                       : "El correo está libre, puede usarse.",
                "Revisión de correo",
                existe ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al revisar email", ex);
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }
 
    private void limpiarCampos() {
        campo_carnet.setText("");
        campo_nombre.setText("");
        campo_correo.setText("");
        campo_telefono.setText("");
        campo_contraseña.setText("");
        campo_estrado.setText("");
        campo_carrera.setText("");
        campo_anioingreso.setText("");
        campo_nombre.requestFocus();
    }
 
    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }
 
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        campo_carnet = new javax.swing.JTextField();
        campo_nombre = new javax.swing.JTextField();
        campo_correo = new javax.swing.JTextField();
        campo_telefono = new javax.swing.JTextField();
        campo_contraseña = new javax.swing.JTextField();
        campo_estrado = new javax.swing.JTextField();
        campo_carrera = new javax.swing.JTextField();
        campo_anioingreso = new javax.swing.JTextField();
        botn_registrar = new javax.swing.JButton();
        botn_limpiarcampos = new javax.swing.JButton();
        botn_revisar_incongrunDB = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        botn_cancelar = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Carnet");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Nombre Completo");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Correo");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Telefono");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Contraseña");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Estado");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Carrera");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setText("Año de Ingreso");

        campo_carnet.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campo_carnet.addActionListener(this::campo_carnetActionPerformed);

        campo_nombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_correo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_telefono.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_contraseña.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_estrado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_carrera.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        campo_anioingreso.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        botn_registrar.setText("REGISTRAR");
        botn_registrar.addActionListener(this::botn_registrarActionPerformed);

        botn_limpiarcampos.setText("LIMPIAR");

        botn_revisar_incongrunDB.setText("REVISION");

        jLabel12.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel12.setText("REGISTRO DE NUEVO ALUMNO");

        botn_cancelar.setText("cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(campo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(campo_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campo_carnet, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campo_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(17, 17, 17)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel8))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(campo_contraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                        .addComponent(campo_estrado, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel9))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(campo_carrera, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(campo_anioingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(botn_revisar_incongrunDB)
                        .addGap(53, 53, 53)
                        .addComponent(botn_registrar)
                        .addGap(53, 53, 53)
                        .addComponent(botn_limpiarcampos)
                        .addGap(31, 31, 31)
                        .addComponent(botn_cancelar)))
                .addContainerGap(415, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(80, 80, 80)
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
                    .addComponent(jLabel8)
                    .addComponent(campo_estrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(campo_carrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(campo_anioingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botn_revisar_incongrunDB)
                    .addComponent(botn_registrar)
                    .addComponent(botn_limpiarcampos)
                    .addComponent(botn_cancelar))
                .addGap(58, 58, 58))
        );

        getAccessibleContext().setAccessibleName("Panel_ingresouser");
    }// </editor-fold>//GEN-END:initComponents

    private void botn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botn_registrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botn_registrarActionPerformed

    private void campo_carnetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo_carnetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campo_carnetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botn_cancelar;
    private javax.swing.JButton botn_limpiarcampos;
    private javax.swing.JButton botn_registrar;
    private javax.swing.JButton botn_revisar_incongrunDB;
    private javax.swing.JTextField campo_anioingreso;
    private javax.swing.JTextField campo_carnet;
    private javax.swing.JTextField campo_carrera;
    private javax.swing.JTextField campo_contraseña;
    private javax.swing.JTextField campo_correo;
    private javax.swing.JTextField campo_estrado;
    private javax.swing.JTextField campo_nombre;
    private javax.swing.JTextField campo_telefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}