/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.service.ConfiguracionService;
import com.mediateca.service.TipoMaterialService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 */
public class Panel_Configadmin extends javax.swing.JPanel {

    public Panel_Configadmin() {
        initComponents();
        cargarConfiguraciones();
        cargarTablaDiasPrestamo();
    }

    private void cargarConfiguraciones() {
        txtMoraPorDia.setText(String.valueOf(ConfiguracionService.getMoraPorDia()));
        txtMaxPrestamos.setText(String.valueOf(ConfiguracionService.getMaxEjemplaresPrestamo()));
    }

    private void cargarTablaDiasPrestamo() {
        DefaultTableModel model = (DefaultTableModel) tblDiasPrestamo.getModel();
        model.setRowCount(0);
        
        // Cargar días de préstamo usando TipoMaterialService
        String[] tipos = {"LIBRO", "REVISTA", "CD", "DVD"};
        for (String tipo : tipos) {
            int dias = TipoMaterialService.getDiasPrestamo(tipo);
            model.addRow(new Object[]{tipo, dias});
        }
    }

    private void guardarMoraPorDia() {
        try {
            double nuevaMora = Double.parseDouble(txtMoraPorDia.getText().trim());
            if (ConfiguracionService.actualizarMoraPorDia(nuevaMora)) {
                JOptionPane.showMessageDialog(this, "Mora por día actualizada a $" + nuevaMora);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la mora por día");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void guardarMaxPrestamos() {
        try {
            int maxPrestamos = Integer.parseInt(txtMaxPrestamos.getText().trim());
            String sql = "UPDATE configuracion SET max_ejemplares_prestamo = ? WHERE id_config = 1";
            try (java.sql.Connection conn = com.mediateca.db.DatabaseConnection.getInstancia().getConexion();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maxPrestamos);
                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Límite de préstamos actualizado a " + maxPrestamos);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void guardarDiasPrestamo() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblDiasPrestamo.getModel();
            String[] tipos = {"LIBRO", "REVISTA", "CD", "DVD"};
            
            for (int i = 0; i < tipos.length; i++) {
                int dias = Integer.parseInt(model.getValueAt(i, 1).toString());
                ConfiguracionService.actualizarDiasPrestamo(tipos[i], dias);
            }
            
            JOptionPane.showMessageDialog(this, "Días de préstamo actualizados correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMoraPorDia = new javax.swing.JTextField();
        txtMaxPrestamos = new javax.swing.JTextField();
        btnGuardarMora = new javax.swing.JButton();
        btnGuardarMaxPrestamos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDiasPrestamo = new javax.swing.JTable();
        btnGuardarDias = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(11, 19, 43));
        setPreferredSize(new java.awt.Dimension(1075, 560));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CONFIGURACIÓN DEL SISTEMA");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mora por día (USD):");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Máximo de préstamos por usuario:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setText("DÍAS DE PRÉSTAMO POR TIPO DE MATERIAL");

        txtMoraPorDia.setFont(new java.awt.Font("Arial", 0, 14));
        txtMoraPorDia.setPreferredSize(new java.awt.Dimension(100, 30));

        txtMaxPrestamos.setFont(new java.awt.Font("Arial", 0, 14));
        txtMaxPrestamos.setPreferredSize(new java.awt.Dimension(100, 30));

        btnGuardarMora.setBackground(new java.awt.Color(0, 102, 204));
        btnGuardarMora.setFont(new java.awt.Font("Arial", 1, 12));
        btnGuardarMora.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarMora.setText("Guardar");
        btnGuardarMora.setBorderPainted(false);
        btnGuardarMora.addActionListener(e -> guardarMoraPorDia());

        btnGuardarMaxPrestamos.setBackground(new java.awt.Color(0, 102, 204));
        btnGuardarMaxPrestamos.setFont(new java.awt.Font("Arial", 1, 12));
        btnGuardarMaxPrestamos.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarMaxPrestamos.setText("Guardar");
        btnGuardarMaxPrestamos.setBorderPainted(false);
        btnGuardarMaxPrestamos.addActionListener(e -> guardarMaxPrestamos());

        tblDiasPrestamo.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] {"Tipo de Material", "Días de Préstamo"}
        ));
        jScrollPane1.setViewportView(tblDiasPrestamo);

        btnGuardarDias.setBackground(new java.awt.Color(0, 102, 204));
        btnGuardarDias.setFont(new java.awt.Font("Arial", 1, 12));
        btnGuardarDias.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarDias.setText("Guardar Días de Préstamo");
        btnGuardarDias.setBorderPainted(false);
        btnGuardarDias.addActionListener(e -> guardarDiasPrestamo());

        btnCerrar.setBackground(new java.awt.Color(204, 0, 0));
        btnCerrar.setFont(new java.awt.Font("Arial", 1, 12));
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Cerrar");
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> VolverAlMenu());

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12));
        jLabel5.setForeground(new java.awt.Color(150, 150, 150));
        jLabel5.setText("Nota: Los días de préstamo se aplican al registrar nuevos préstamos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMoraPorDia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarMora)
                            .addComponent(btnGuardarMaxPrestamos)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardarDias, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(500, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMoraPorDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardarMora))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtMaxPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardarMaxPrestamos))
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnGuardarDias)
                .addGap(5, 5, 5)
                .addComponent(jLabel5)
                .addGap(20, 20, 20)
                .addComponent(btnCerrar)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }

    private void VolverAlMenu() {
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_administrador());
        }
    }

    // Variables declaration
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnGuardarDias;
    private javax.swing.JButton btnGuardarMaxPrestamos;
    private javax.swing.JButton btnGuardarMora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDiasPrestamo;
    private javax.swing.JTextField txtMaxPrestamos;
    private javax.swing.JTextField txtMoraPorDia;
    // End of variables declaration
}