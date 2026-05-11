
package com.mediateca.vistas.formularios;
 
import com.mediateca.dao.UsuarioDAO;
import com.mediateca.model.Usuario;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Ventana_PPAL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
 
/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 *
 * MAPEO DE CAMPOS (basado en orden de creación y labels del form):
 *   jTextField1 -> Carnet           (no se guarda en BD)
 *   jTextField2 -> Nombre Completo  -> Usuario.nombre
 *   jTextField3 -> Correo           -> Usuario.email
 *   jTextField4 -> Telefono         -> Usuario.telefono
 *   jTextField5 -> Usuario          (no se guarda en BD)
 *   jTextField6 -> Contraseña       -> Usuario.contrasena
 *   jTextField8 -> Estado           (no se guarda en BD)
 *   jTextField9 -> Escuela          -> Usuario.carrera
 *
 *   jButton1 -> REGISTRAR
 *   jButton2 -> LIMPIAR
 *   jButton3 -> REVISION
 *
 * RECOMENDACIÓN: renombrar los campos en NetBeans (clic derecho > Change
 * Variable Name) a algo como campo_carnet, campo_nombrecomp, etc. para
 * facilitar mantenimiento. Si se renombran, hay que actualizar este archivo.
 */
public class Panel_ingresodocente extends javax.swing.JPanel {
 
    private static final Logger logger = Logger.getLogger(Panel_ingresodocente.class.getName());
    private static final String REGEX_EMAIL = "^[\\w.\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$";
 
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
 
    /**
     * Creates new form Panel_ingresoalum
     */
    public Panel_ingresodocente() {
        initComponents();
        cablearEventos();
    }
 
    private void cablearEventos() {
        jButton1.addActionListener(e -> registrar());        // REGISTRAR
        jButton2.addActionListener(e -> limpiarCampos());    // LIMPIAR
        jButton3.addActionListener(e -> revisarEmailEnBD()); // REVISION
        // No hay boton de cancelar visible en este form; si se agrega, cablear aquí.
    }
 
    /**
     * Valida campos y registra un nuevo docente.
     * Campos sin backend (no se persisten): carnet, usuario, estado.
     * Se guardan: nombre, email, contrasena, tipo (=PROFESOR), carrera (=Escuela), telefono.
     */
    private void registrar() {
        String nombre   = jTextField2.getText().trim();   // Nombre Completo
        String email    = jTextField3.getText().trim();   // Correo
        String telefono = jTextField4.getText().trim();   // Telefono
        String password = jTextField6.getText().trim();   // Contraseña
        String escuela  = jTextField9.getText().trim();   // Escuela -> carrera
 
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
            if (usuarioDAO.existeEmail(email)) {
                mostrarError("Ya existe un usuario con ese correo.");
                return;
            }
 
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setEmail(email);
            nuevo.setContrasena(password);
            nuevo.setTipo("PROFESOR");
            nuevo.setCarrera(escuela); // "Escuela" del docente se guarda en columna carrera
            nuevo.setTelefono(telefono);
 
            if (usuarioDAO.insertar(nuevo)) {
                JOptionPane.showMessageDialog(this,
                    "Docente registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                mostrarError("No se pudo registrar el docente. Revisa los logs.");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al registrar docente", ex);
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }
 
    private void revisarEmailEnBD() {
        String email = jTextField3.getText().trim();
        if (email.isEmpty()) {
            mostrarError("Ingresa un correo para revisarlo.");
            return;
        }
        try {
            boolean existe = usuarioDAO.existeEmail(email);
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
        jTextField1.setText(""); // Carnet
        jTextField2.setText(""); // Nombre Completo
        jTextField3.setText(""); // Correo
        jTextField4.setText(""); // Telefono
        jTextField5.setText(""); // Usuario
        jTextField6.setText(""); // Contraseña
        jTextField8.setText(""); // Estado
        jTextField9.setText(""); // Escuela
        jTextField2.requestFocus();
    }
 
    @SuppressWarnings("unused")
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Carnet");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Nombre Completo");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Correo");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Telefono");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Usuario");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Contraseña");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Estado");

        jTextField1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jTextField4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jTextField6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jTextField8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jButton1.setText("REGISTRAR");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("LIMPIAR");

        jButton3.setText("REVISION");

        jLabel12.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel12.setText("REGISTRO DE NUEVO DOCENTE");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Escuela");

        jTextField9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(87, 87, 87)
                            .addComponent(jButton3)
                            .addGap(53, 53, 53)
                            .addComponent(jButton1)
                            .addGap(53, 53, 53)
                            .addComponent(jButton2))))
                .addContainerGap(426, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(164, 164, 164)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(58, 58, 58))
        );

        getAccessibleContext().setAccessibleName("Panel_ingresouser");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
