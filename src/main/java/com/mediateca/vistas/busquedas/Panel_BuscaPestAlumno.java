package com.mediateca.vistas.busquedas;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.model.Usuario;
import com.mediateca.util.SessionManager;
import com.mediateca.vistas.Panel_Alumno;
import com.mediateca.vistas.Ventana_PPAL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Panel_BuscaPestAlumno extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(Panel_BuscaPestAlumno.class.getName());

    public Panel_BuscaPestAlumno() {
        initComponents();
        configurarTabla();
        cablearEventos();
        cargarMisPrestamos();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Material", "Tipo", "Fecha Préstamo", "Fecha Esperada", "Estado", "Mora USD"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPrestamos.setModel(modelo);

        tblPrestamos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblPrestamos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblPrestamos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblPrestamos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    private void cargarMisPrestamos() {
        Usuario usuarioActual = SessionManager.getUsuarioActual();
        
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No hay sesión activa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idUsuario = usuarioActual.getIdUsuario();
        DefaultTableModel modelo = (DefaultTableModel) tblPrestamos.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT p.id_prestamo, m.titulo, m.tipo, p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado, " +
                     "COALESCE(p.mora_total, 0) as mora_total " +
                     "FROM prestamos p " +
                     "JOIN material m ON p.id_material = m.id " +
                     "WHERE p.id_usuario = ? " +
                     "ORDER BY p.id_prestamo DESC";
        
    double moraPorDia = obtenerMoraPorDia();
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            
            while (rs.next()) {
                int idPrestamo = rs.getInt("id_prestamo");
                String material = rs.getString("titulo");
                String tipo = rs.getString("tipo");
                Date fechaPrestamo = rs.getDate("fecha_prestamo");
                Date fechaEsperada = rs.getDate("fecha_devolucion_esperada");
                String estado = rs.getString("estado");
                double moraRegistrada = rs.getDouble("mora_total");
                
                String estadoMostrar;
                double mora;
                
                switch (estado) {
                    case "ACTIVO":
                        estadoMostrar = "EN CIRCULACION";
                        LocalDate fechaVencimiento = fechaEsperada.toLocalDate();
                        LocalDate hoy = LocalDate.now();
                        long diasRetraso = hoy.isAfter(fechaVencimiento) ? ChronoUnit.DAYS.between(fechaVencimiento, hoy) : 0;
                        mora = diasRetraso * moraPorDia;
                        break;
                    case "DEVUELTO":
                        estadoMostrar = "DEVUELTO";
                        mora = moraRegistrada;
                        break;
                    default:
                        estadoMostrar = estado;
                        mora = moraRegistrada;
                }
                
                String moraStr = String.format("$%.2f", mora);
                
                modelo.addRow(new Object[]{
                    idPrestamo, material, tipo, fechaPrestamo, fechaEsperada, estadoMostrar, moraStr
                });
            }
            
            lblResultados.setText("Total de pr\u00e9stamos: " + modelo.getRowCount());
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al cargar préstamos", ex);
            JOptionPane.showMessageDialog(this,
                "Error al consultar la base de datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double obtenerMoraPorDia() {
        String sql = "SELECT mora_por_dia FROM configuracion WHERE id_config = 1";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("mora_por_dia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.5;
    }

    private void refrescar() {
        cargarMisPrestamos();
    }

    private void volverAlMenu() {
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_Alumno());
        }
    }

    private void cablearEventos() {
        btnRefrescar.addActionListener(e -> refrescar());
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        btnRefrescar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        lblResultados = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setBackground(new java.awt.Color(8, 20, 55));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 28));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("MIS PRÉSTAMOS");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Consulta de tus préstamos activos y devueltos");

        tblPrestamos.setFont(new java.awt.Font("Arial", 0, 12));
        jScrollPane1.setViewportView(tblPrestamos);

        btnRefrescar.setBackground(new java.awt.Color(0, 102, 204));
        btnRefrescar.setFont(new java.awt.Font("Arial", 1, 14));
        btnRefrescar.setForeground(new java.awt.Color(255, 255, 255));
        btnRefrescar.setText("REFRESCAR");
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.setPreferredSize(new java.awt.Dimension(150, 40));

        btnVolver.setBackground(new java.awt.Color(204, 0, 0));
        btnVolver.setFont(new java.awt.Font("Arial", 1, 14));
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("VOLVER");
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new java.awt.Dimension(120, 40));

        lblResultados.setFont(new java.awt.Font("Arial", 0, 12));
        lblResultados.setForeground(new java.awt.Color(200, 200, 200));
        lblResultados.setText("Total de pr\u00e9stamos: 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
                    .addComponent(lblResultados)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(450, 450, 450)
                        .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblResultados)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }

    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblResultados;
    private javax.swing.JTable tblPrestamos;
}
