/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas.formularios;
 
import com.mediateca.db.DatabaseConnection;
import com.mediateca.service.ConfiguracionService;
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
 * Panel para calcular la mora de préstamos activos.
 * 
 * Flujo del panel:
 *   1. El usuario ingresa el carnet (ID de usuario) y hace clic en BUSCAR.
 *      Se cargan los préstamos activos o vencidos del usuario en la tabla.
 *   2. El usuario selecciona una fila de la tabla y hace clic en "Seleccionar".
 *      Se calculan los días de retraso y el costo de mora, mostrándolos en los campos inferiores.
 * 
 * La mora se calcula como: días de retraso × valor de mora por día (configurado en el sistema).
 */
public class Panel_Calcular_Mora extends javax.swing.JPanel {
 
    private static final Logger logger = Logger.getLogger(Panel_Calcular_Mora.class.getName());
 
    private static final String[] COLUMNAS = {
        "ID Préstamo", "ID Material", "Título", "Fecha Préstamo",
        "Fecha Devolución Esperada", "Estado"
    };
 
    /**
     * Creates new form Panel_Calcular_Mora
     */
    public Panel_Calcular_Mora() {
        initComponents();
        configurarTabla();
        jTextField2.setEditable(false);
        jLabel5.setText("");
        cablearEventos();
    }
 
    /**
     * Configura la tabla con las columnas definidas.
     */
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(modelo);
    }
 
    /**
     * Conecta los botones del panel con sus respectivas acciones.
     */
    private void cablearEventos() {
        jButton1.addActionListener(e -> buscarPrestamos());
 
        // Los tres botones "Seleccionar" realizan la misma acción
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
                "Ingresa el carnet (ID de usuario).",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(carnetTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El carnet debe ser un número entero.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        jTextField2.setText("");
        jLabel5.setText("");
 
        String sql = "SELECT p.id_prestamo, p.id_material, m.titulo, " +
                     "p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado " +
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
                        "El usuario no tiene préstamos activos o vencidos.",
                        "Sin préstamos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al buscar préstamos del usuario", ex);
            JOptionPane.showMessageDialog(this,
                "Error al consultar la base de datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Toma la fila seleccionada del JTable, calcula los días de retraso
     * y aplica la fórmula de mora: días retraso × valor mora por día.
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
            Object fechaObj = modelo.getValueAt(fila, 4);
 
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
 
            // Obtener el valor de mora por día desde la configuración
            double valorMoraPorDia = ConfiguracionService.getMoraPorDia();
            
            // Fórmula: mora = días de retraso × valor de mora por día
            double mora = diasRetraso * valorMoraPorDia;
 
            jTextField2.setText(String.valueOf(diasRetraso));
            jLabel5.setText(String.format("$%.2f", mora));
 
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al calcular mora", ex);
            JOptionPane.showMessageDialog(this,
                "Error al calcular la mora: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Regresa al menú principal del administrador.
     */
    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }
 
    @SuppressWarnings("unchecked")
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

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 36));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CALCULAR MORA");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel1.setText("Carnet");

        jTextField1.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setText("Dias de Mora");

        jTextField2.setFont(new java.awt.Font("Arial", 0, 14));

        jTable1.setFont(new java.awt.Font("Arial", 0, 14));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] { {null, null, null, null}, {null, null, null, null}, {null, null, null, null} },
            new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton5.setFont(new java.awt.Font("Arial", 0, 14));
        jButton5.setText("Seleccionar");

        jButton6.setFont(new java.awt.Font("Arial", 0, 14));
        jButton6.setText("Seleccionar");

        jButton7.setFont(new java.awt.Font("Arial", 0, 14));
        jButton7.setText("Seleccionar");

        jButton1.setFont(new java.awt.Font("Arial", 0, 14));
        jButton1.setText("BUSCAR");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18));
        jLabel3.setText("Costo Mora");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18));
        jLabel5.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addComponent(jButton6)
                                    .addComponent(jButton7))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(94, 94, 94)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))))
                .addContainerGap(108, Short.MAX_VALUE))
        );
    }

    // Variables declaration - do not modify
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
    // End of variables declaration
}