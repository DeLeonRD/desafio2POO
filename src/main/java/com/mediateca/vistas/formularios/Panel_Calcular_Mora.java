/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas.formularios;
 
import com.mediateca.controller.PrestamoController;
import com.mediateca.db.DatabaseConnection;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Ventana_PPAL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
 
/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 *
 * Flujo del panel:
 *   1. Usuario ingresa el carnet (id_usuario) y clic en BUSCAR -> carga sus
 *      préstamos activos/vencidos en la tabla.
 *   2. Usuario selecciona una fila y hace clic en "Seleccionar" -> calcula
 *      días de retraso y costo de mora, los muestra en los campos inferiores.
 *
 *   jTextField1 -> Carnet (id_usuario)
 *   jTextField2 -> Días de Mora (output, no editable)
 *   jLabel5     -> Costo de Mora (output)
 *   jTable1     -> Préstamos del usuario
 *   jButton1    -> BUSCAR
 *   jButton5, jButton6, jButton7 -> Seleccionar (los 3 hacen lo mismo: calcular
 *                                                mora para la fila seleccionada)
 */
public class Panel_Calcular_Mora extends javax.swing.JPanel {
 
    private static final Logger logger = Logger.getLogger(Panel_Calcular_Mora.class.getName());
 
    private static final String[] COLUMNAS = {
        "ID Préstamo", "ID Material", "Título", "Fecha Préstamo",
        "Fecha Devolución Esperada", "Estado"
    };
 
    private final PrestamoController prestamoController = new PrestamoController();
 
    /**
     * Creates new form Panel_Calcular_Mora
     */
    public Panel_Calcular_Mora() {
        initComponents();
        configurarTabla();
        jTextField2.setEditable(false);
        jLabel5.setText(""); // limpiar placeholder "jLabel5"
        cablearEventos();
    }
 
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(modelo);
    }
 
    private void cablearEventos() {
        jButton1.addActionListener(e -> buscarPrestamos());
 
        // Los 3 botones "Seleccionar" hacen lo mismo: calcular mora para la
        // fila actualmente seleccionada en la tabla.
        java.awt.event.ActionListener calcularSelec = e -> calcularMoraDeSeleccionada();
        jButton5.addActionListener(calcularSelec);
        jButton6.addActionListener(calcularSelec);
        jButton7.addActionListener(calcularSelec);
    }
 
    /**
     * Carga en la tabla los préstamos activos y vencidos del usuario.
     */
    private void buscarPrestamos() {
        String carnetTxt = jTextField1.getText().trim();
        if (carnetTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingresa el carnet (id de usuario).",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(carnetTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El carnet debe ser numérico.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        jTextField2.setText("");
        jLabel5.setText("");
 
        String sql = "SELECT p.id_prestamo, p.id_material, m.titulo, " +
                     "       p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado " +
                     "FROM prestamos p " +
                     "JOIN material m ON m.id = p.id_material " +
                     "WHERE p.id_usuario = ? AND p.estado IN ('ACTIVO', 'VENCIDO') " +
                     "ORDER BY p.fecha_devolucion_esperada";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_material"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion_esperada"),
                        rs.getString("estado")
                    });
                    count++;
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this,
                        "El usuario no tiene préstamos activos.",
                        "Sin préstamos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al buscar préstamos del usuario", ex);
            JOptionPane.showMessageDialog(this,
                "Error de BD: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Toma la fila seleccionada del JTable y calcula la mora usando
     * {@link PrestamoController#calcularMora}.
     */
    private void calcularMoraDeSeleccionada() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una fila de la tabla primero.",
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        try {
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            int idPrestamo = (Integer) modelo.getValueAt(fila, 0);
            int idMaterial = (Integer) modelo.getValueAt(fila, 1);
            Object fechaObj = modelo.getValueAt(fila, 4); // Fecha Devolución Esperada
 
            // El ResultSet devuelve java.sql.Date — lo convertimos a LocalDate
            // para los días de retraso, y lo pasamos directo al controller.
            Date fechaSQL;
            LocalDate fechaEsperada;
            if (fechaObj instanceof Date) {
                fechaSQL = (Date) fechaObj;
                fechaEsperada = fechaSQL.toLocalDate();
            } else if (fechaObj instanceof java.util.Date) {
                fechaSQL = new Date(((java.util.Date) fechaObj).getTime());
                fechaEsperada = fechaSQL.toLocalDate();
            } else {
                throw new IllegalStateException("Tipo de fecha inesperado: " + fechaObj.getClass());
            }
 
            LocalDate hoy = LocalDate.now();
            long diasRetraso = hoy.isAfter(fechaEsperada)
                ? ChronoUnit.DAYS.between(fechaEsperada, hoy) : 0;
 
            double mora = prestamoController.calcularMora(idPrestamo, idMaterial, fechaSQL);
 
            jTextField2.setText(String.valueOf(diasRetraso));
            jLabel5.setText(String.format("$%.2f", mora));
 
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al calcular mora", ex);
            JOptionPane.showMessageDialog(this,
                "Error al calcular mora: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    @SuppressWarnings("unused")
    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1075, 650));

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CALCULAR MORA");

        jLabel1.setText("Carnet");

        jLabel2.setText("Dias de Mora");

        jTable1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton5.setText("Seleccionar");

        jButton6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton6.setText("Seleccionar");

        jButton7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton7.setText("Seleccionar");

        jButton1.setText("BUSCAR");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Costo Mora");

        jLabel5.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1065, Short.MAX_VALUE)
                .addGap(4, 4, 4))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addComponent(jButton6)
                                    .addComponent(jButton7)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(226, 226, 226)
                                .addComponent(jLabel3)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel5)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(64, 64, 64)
                        .addComponent(jButton1)
                        .addGap(691, 691, 691))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(94, 94, 94)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(108, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
