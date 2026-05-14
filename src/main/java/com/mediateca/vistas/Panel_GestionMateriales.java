/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.service.TipoMaterialService;
import java.sql.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Panel_GestionMateriales extends javax.swing.JPanel {

    // Usando TipoMaterialService en lugar de DAO
    private int materialSeleccionadoId = -1;

    public Panel_GestionMateriales() {
        initComponents();
        cargarTablaMateriales();
        cargarComboTipos();
        limpiarFormulario();
    }

    private void cargarTablaMateriales() {
        DefaultTableModel model = (DefaultTableModel) tblMateriales.getModel();
        model.setRowCount(0);
        
        String sql = "SELECT id, tipo, titulo, autor, anio_publicacion, ubicacion, cantidad_total, cantidad_disponible FROM material ORDER BY id";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("anio_publicacion"),
                    rs.getString("ubicacion"),
                    rs.getInt("cantidad_total"),
                    rs.getInt("cantidad_disponible")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarComboTipos() {
        cmbTipo.removeAllItems();
        List<String> tipos = TipoMaterialService.listarTipos();
        for (String tipo : tipos) {
            cmbTipo.addItem(tipo);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        cmbTipo.setSelectedIndex(0);
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtUbicacion.setText("");
        txtCantidadTotal.setText("");
        txtCantidadDisponible.setText("");
        materialSeleccionadoId = -1;
        btnEliminar.setEnabled(false);
    }

    private void cargarMaterialEnFormulario(int id) {
        String sql = "SELECT * FROM material WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtId.setText(String.valueOf(rs.getInt("id")));
                cmbTipo.setSelectedItem(rs.getString("tipo"));
                txtTitulo.setText(rs.getString("titulo"));
                txtAutor.setText(rs.getString("autor"));
                txtAnio.setText(String.valueOf(rs.getInt("anio_publicacion")));
                txtUbicacion.setText(rs.getString("ubicacion"));
                txtCantidadTotal.setText(String.valueOf(rs.getInt("cantidad_total")));
                txtCantidadDisponible.setText(String.valueOf(rs.getInt("cantidad_disponible")));
                materialSeleccionadoId = id;
                btnEliminar.setEnabled(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarMaterial() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String anioStr = txtAnio.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String cantidadTotalStr = txtCantidadTotal.getText().trim();
        String cantidadDisponibleStr = txtCantidadDisponible.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || anioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título, autor y año son obligatorios.");
            return;
        }

        int anio, cantidadTotal = 1, cantidadDisponible = 1;
        try {
            anio = Integer.parseInt(anioStr);
            if (!cantidadTotalStr.isEmpty()) cantidadTotal = Integer.parseInt(cantidadTotalStr);
            if (!cantidadDisponibleStr.isEmpty()) cantidadDisponible = Integer.parseInt(cantidadDisponibleStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Año y cantidades deben ser números válidos.");
            return;
        }

        if (materialSeleccionadoId == -1) {
            // Insertar nuevo material
            String sql = "INSERT INTO material (tipo, titulo, autor, anio_publicacion, ubicacion, cantidad_total, cantidad_disponible) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getInstancia().getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tipo);
                pstmt.setString(2, titulo);
                pstmt.setString(3, autor);
                pstmt.setInt(4, anio);
                pstmt.setString(5, ubicacion);
                pstmt.setInt(6, cantidadTotal);
                pstmt.setInt(7, cantidadDisponible);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Material creado exitosamente.");
                limpiarFormulario();
                cargarTablaMateriales();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al crear material: " + e.getMessage());
            }
        } else {
            // Actualizar material existente
            String sql = "UPDATE material SET tipo=?, titulo=?, autor=?, anio_publicacion=?, ubicacion=?, cantidad_total=?, cantidad_disponible=? WHERE id=?";
            try (Connection conn = DatabaseConnection.getInstancia().getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tipo);
                pstmt.setString(2, titulo);
                pstmt.setString(3, autor);
                pstmt.setInt(4, anio);
                pstmt.setString(5, ubicacion);
                pstmt.setInt(6, cantidadTotal);
                pstmt.setInt(7, cantidadDisponible);
                pstmt.setInt(8, materialSeleccionadoId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Material actualizado exitosamente.");
                limpiarFormulario();
                cargarTablaMateriales();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar material: " + e.getMessage());
            }
        }
    }

    private void eliminarMaterial() {
        if (materialSeleccionadoId == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este material?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM material WHERE id = ?";
            try (Connection conn = DatabaseConnection.getInstancia().getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, materialSeleccionadoId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Material eliminado.");
                limpiarFormulario();
                cargarTablaMateriales();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMateriales = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCantidadTotal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCantidadDisponible = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setBackground(new java.awt.Color(11, 19, 43));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GESTIÓN DE MATERIALES");

        tblMateriales.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Tipo", "Título", "Autor", "Año", "Ubicación", "Total", "Disponible"}
        ));
        tblMateriales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblMateriales.getSelectedRow();
                if (row >= 0) {
                    int id = (int) tblMateriales.getValueAt(row, 0);
                    cargarMaterialEnFormulario(id);
                }
            }
        });
        jScrollPane1.setViewportView(tblMateriales);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID:");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tipo:");

        cmbTipo.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Título:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Autor:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Año:");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Ubicación:");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cantidad Total:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Cantidad Disponible:");

        btnGuardar.setBackground(new java.awt.Color(0, 102, 204));
        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12));
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(e -> guardarMaterial());

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Arial", 1, 12));
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarMaterial());

        btnLimpiar.setBackground(new java.awt.Color(102, 102, 102));
        btnLimpiar.setFont(new java.awt.Font("Arial", 1, 12));
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

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
                        .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(10, 10, 10)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(10, 10, 10)
                        .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10)
                        .addComponent(txtCantidadTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(10, 10, 10)
                        .addComponent(txtCantidadDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtCantidadTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtCantidadDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMateriales;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtCantidadDisponible;
    private javax.swing.JTextField txtCantidadTotal;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtTitulo;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration
}