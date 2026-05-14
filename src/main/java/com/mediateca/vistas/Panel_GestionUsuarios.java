/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.model.Usuario;
import com.mediateca.service.TipoUsuarioService;
import com.mediateca.service.UsuarioService;
import com.mediateca.vistas.Ventana_PPAL;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Panel_GestionUsuarios extends javax.swing.JPanel {

    private int usuarioSeleccionadoId = -1;

    public Panel_GestionUsuarios() {
        initComponents();
        cargarTablaUsuarios();
        cargarComboTipos();
        limpiarFormulario();
    }

    private void cargarTablaUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tblUsuarios.getModel();
        model.setRowCount(0);
        
        List<Usuario> usuarios = UsuarioService.listarTodos();
        for (Usuario u : usuarios) {
            model.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombre(),
                u.getEmail(),
                u.getTipo(),
                u.getCarrera() != null ? u.getCarrera() : "N/A",
                u.getTelefono() != null ? u.getTelefono() : "N/A"
            });
        }
    }

    private void cargarComboTipos() {
        cmbTipo.removeAllItems();
        List<String> tipos = TipoUsuarioService.listarTipos();
        for (String tipo : tipos) {
            cmbTipo.addItem(tipo);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtCarrera.setText("");
        txtTelefono.setText("");
        cmbTipo.setSelectedIndex(0);
        usuarioSeleccionadoId = -1;
        btnEliminar.setEnabled(false);
    }

    private void cargarUsuarioEnFormulario(Usuario u) {
        txtId.setText(String.valueOf(u.getIdUsuario()));
        txtNombre.setText(u.getNombre());
        txtEmail.setText(u.getEmail());
        txtPassword.setText(u.getContrasena());
        txtCarrera.setText(u.getCarrera() != null ? u.getCarrera() : "");
        txtTelefono.setText(u.getTelefono() != null ? u.getTelefono() : "");
        cmbTipo.setSelectedItem(u.getTipo());
        usuarioSeleccionadoId = u.getIdUsuario();
        btnEliminar.setEnabled(true);
    }

    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String carrera = txtCarrera.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, email y contraseña son obligatorios.");
            return;
        }

        if (usuarioSeleccionadoId == -1) {
            // Nuevo usuario
            if (UsuarioService.existeEmail(email)) {
                JOptionPane.showMessageDialog(this, "El email ya está registrado.");
                return;
            }
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setContrasena(password);
            u.setTipo(tipo);
            u.setCarrera(carrera.isEmpty() ? null : carrera);
            u.setTelefono(telefono.isEmpty() ? null : telefono);
            if (UsuarioService.insertar(u)) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
                limpiarFormulario();
                cargarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear usuario.");
            }
        } else {
            // Editar usuario existente
            Usuario u = UsuarioService.obtenerPorId(usuarioSeleccionadoId);
            if (u != null) {
                u.setNombre(nombre);
                u.setEmail(email);
                if (!password.isEmpty()) {
                    u.setContrasena(password);
                }
                u.setTipo(tipo);
                u.setCarrera(carrera.isEmpty() ? null : carrera);
                u.setTelefono(telefono.isEmpty() ? null : telefono);
                if (UsuarioService.actualizar(u)) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
                    limpiarFormulario();
                    cargarTablaUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar usuario.");
                }
            }
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionadoId == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (UsuarioService.eliminar(usuarioSeleccionadoId)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                limpiarFormulario();
                cargarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario.");
            }
        }
    }

    private void restablecerContrasena() {
        if (usuarioSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero.");
            return;
        }
        String nuevaPass = JOptionPane.showInputDialog(this, "Ingrese nueva contraseña:");
        if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
            // Actualizar directamente en BD
            String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
            try (java.sql.Connection conn = com.mediateca.db.DatabaseConnection.getInstancia().getConexion();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nuevaPass);
                pstmt.setInt(2, usuarioSeleccionadoId);
                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada.");
                    txtPassword.setText(nuevaPass);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar contraseña.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void volverAlMenu() {
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_administrador());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtCarrera = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnRestablecerPass = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setBackground(new java.awt.Color(11, 19, 43));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GESTIÓN DE USUARIOS");

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nombre", "Email", "Tipo", "Carrera", "Teléfono"}
        ));
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblUsuarios.getSelectedRow();
                if (row >= 0) {
                    int id = (int) tblUsuarios.getValueAt(row, 0);
                    Usuario u = UsuarioService.obtenerPorId(id);
                    if (u != null) {
                        cargarUsuarioEnFormulario(u);
                    }
                }
            }
        });
        jScrollPane1.setViewportView(tblUsuarios);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID:");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre:");

        txtNombre.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contraseña:");

        txtPassword.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tipo:");

        cmbTipo.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Carrera:");

        txtCarrera.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Teléfono:");

        txtTelefono.setFont(new java.awt.Font("Arial", 0, 14));

        btnGuardar.setBackground(new java.awt.Color(0, 102, 204));
        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12));
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(e -> guardarUsuario());

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Arial", 1, 12));
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarUsuario());

        btnLimpiar.setBackground(new java.awt.Color(102, 102, 102));
        btnLimpiar.setFont(new java.awt.Font("Arial", 1, 12));
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnRestablecerPass.setBackground(new java.awt.Color(255, 102, 0));
        btnRestablecerPass.setFont(new java.awt.Font("Arial", 1, 12));
        btnRestablecerPass.setForeground(new java.awt.Color(255, 255, 255));
        btnRestablecerPass.setText("Restablecer Contraseña");
        btnRestablecerPass.setBorderPainted(false);
        btnRestablecerPass.addActionListener(e -> restablecerContrasena());

        btnCerrar.setBackground(new java.awt.Color(102, 102, 102));
        btnCerrar.setFont(new java.awt.Font("Arial", 1, 12));
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Volver");
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> volverAlMenu());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(10, 10, 10)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(10, 10, 10)
                        .addComponent(txtCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestablecerPass, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnRestablecerPass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }

    // Variables declaration
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRestablecerPass;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtCarrera;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration
}