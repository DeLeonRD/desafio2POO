/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.service.ConfiguracionService;
import com.mediateca.vistas.busquedas.Panel_BuscaPest;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Panel principal para usuarios con rol Docente o Empleado.
 * Permite consultar préstamos, registrar devoluciones y ver disponibilidad de materiales.
 */
public class Panel_Docente extends javax.swing.JPanel {

    public Panel_Docente() {
        initComponents();
        cablearEventos();
    }

    /**
     * Conecta los botones del menú con sus respectivas acciones.
     */
    private void cablearEventos() {
        boton_busca_prestam.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_BuscaPest()));
        btnRegistrarDevolucion.addActionListener(e -> mostrarPanelDevolucion());
        btnReporteDisponibilidad.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_ReporteDisponibilidad()));
        btnRegistrarPrestamo.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new com.mediateca.vistas.formularios.Panel_RegistrarPrestamo()));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    /**
     * Muestra el panel de devolución de préstamos.
     */
    private void mostrarPanelDevolucion() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_Devolucion());
    }

    /**
     * Cierra la sesión actual y vuelve a la pantalla de bienvenida.
     */
    private void cerrarSesion() {
        com.mediateca.util.SessionManager.cerrarSesion();
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_Bienvenida());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlMenu = new javax.swing.JPanel();
        pnlContenido = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblBienvenida = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        boton_busca_prestam = new javax.swing.JButton();
        btnRegistrarDevolucion = new javax.swing.JButton();
        btnReporteDisponibilidad = new javax.swing.JButton();
        btnRegistrarPrestamo = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();

        setBackground(new Color(5, 15, 45));
        setLayout(new BorderLayout());

        // =====================================================
        // MENU LATERAL
        // =====================================================
        pnlMenu.setBackground(new Color(5, 15, 45));
        pnlMenu.setPreferredSize(new Dimension(300, 720));
        pnlMenu.setLayout(new BorderLayout());

        javax.swing.JPanel panelSuperior = new javax.swing.JPanel();
        panelSuperior.setLayout(new javax.swing.BoxLayout(panelSuperior, javax.swing.BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(5, 15, 45));
        panelSuperior.setBorder(new EmptyBorder(25, 25, 10, 25));

        lblTitulo.setText("Mediateca Don Bosco");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 120, 255));
        lblTitulo.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(lblTitulo);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(10));

        lblSubtitulo.setText("Docente / Empleado");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(lblSubtitulo);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(35));

        configurarBoton(boton_busca_prestam, "Consultar Préstamo");
        boton_busca_prestam.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(boton_busca_prestam);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(12));

        configurarBoton(btnRegistrarDevolucion, "Registrar Devolución");
        btnRegistrarDevolucion.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(btnRegistrarDevolucion);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(12));

        configurarBoton(btnReporteDisponibilidad, "📊 Ver Disponibilidad");
        btnReporteDisponibilidad.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(btnReporteDisponibilidad);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(12));

        configurarBoton(btnRegistrarPrestamo, "Registrar Préstamo");
        btnRegistrarPrestamo.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panelSuperior.add(btnRegistrarPrestamo);
        panelSuperior.add(javax.swing.Box.createVerticalStrut(12));

        panelSuperior.add(javax.swing.Box.createVerticalGlue());

        javax.swing.JPanel panelInferior = new javax.swing.JPanel();
        panelInferior.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        panelInferior.setBackground(new Color(5, 15, 45));
        panelInferior.setBorder(new EmptyBorder(10, 25, 25, 25));

        configurarBoton(btnCerrarSesion, "Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(204, 0, 0));
        btnCerrarSesion.setPreferredSize(new Dimension(220, 42));
        panelInferior.add(btnCerrarSesion);

        pnlMenu.add(panelSuperior, BorderLayout.CENTER);
        pnlMenu.add(panelInferior, BorderLayout.SOUTH);

        add(pnlMenu, BorderLayout.WEST);

        // =====================================================
        // PANEL DE CONTENIDO PRINCIPAL
        // =====================================================
        pnlContenido.setBackground(new Color(8, 20, 55));
        pnlContenido.setLayout(new GridBagLayout());
        GridBagConstraints center = new GridBagConstraints();
        center.gridx = 0;
        center.insets = new Insets(15, 15, 15, 15);

        lblBienvenida.setText("Panel Docente / Empleado");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 36));
        lblBienvenida.setForeground(Color.WHITE);
        center.gridy = 0;
        pnlContenido.add(lblBienvenida, center);

        lblDescripcion.setText(
                "<html><div style='text-align:center;'>"
                + "Desde este panel puede consultar pr\u00e9stamos,<br>"
                + "registrar devoluciones y ver disponibilidad de materiales."
                + "</div></html>"
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 20));
        lblDescripcion.setForeground(new Color(220, 220, 220));
        center.gridy = 1;
        pnlContenido.add(lblDescripcion, center);

        add(pnlContenido, BorderLayout.CENTER);
    }

    /**
     * Configura la apariencia de los botones del menú lateral.
     * 
     * @param boton  botón a configurar
     * @param texto  texto que mostrará el botón
     */
    private void configurarBoton(javax.swing.JButton boton, String texto) {
        boton.setText(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 102, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 80, 200), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        boton.setPreferredSize(new Dimension(220, 45));
        boton.setMaximumSize(new Dimension(220, 45));
        boton.setMinimumSize(new Dimension(220, 45));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0, 120, 255));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0, 102, 255));
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnReporteDisponibilidad;
    private javax.swing.JButton btnRegistrarPrestamo;
    private javax.swing.JButton boton_busca_prestam;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlMenu;
    // End of variables declaration
}

/**
 * Panel interno para registrar la devolución de préstamos.
 * Muestra los préstamos activos y permite seleccionar uno para registrar su devolución.
 */
class Panel_Devolucion extends javax.swing.JPanel {

    private DefaultTableModel model;
    private int prestamoSeleccionadoId = -1;

    public Panel_Devolucion() {
        initComponents();
        cargarPrestamosActivos();
    }

    /**
     * Carga en la tabla los préstamos que están actualmente activos.
     */
    private void cargarPrestamosActivos() {
        model = (DefaultTableModel) tblPrestamos.getModel();
        model.setRowCount(0);

        String sql = "SELECT p.id_prestamo, u.nombre, m.titulo, p.fecha_prestamo, p.fecha_devolucion_esperada " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "JOIN material m ON p.id_material = m.id " +
                     "WHERE p.estado = 'ACTIVO'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_prestamo"),
                    rs.getString("nombre"),
                    rs.getString("titulo"),
                    rs.getDate("fecha_prestamo"),
                    rs.getDate("fecha_devolucion_esperada")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra la devolución del préstamo seleccionado, calcula la mora si corresponde
     * y actualiza la disponibilidad del material.
     */
    private void registrarDevolucion() {
        if (prestamoSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla.");
            return;
        }

        String sql = "SELECT id_material, fecha_devolucion_esperada FROM prestamos WHERE id_prestamo = ?";
        int idMaterial = -1;
        Date fechaEsperada = null;

        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prestamoSeleccionadoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idMaterial = rs.getInt("id_material");
                fechaEsperada = rs.getDate("fecha_devolucion_esperada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (idMaterial == -1) return;

        long diasRetraso = ChronoUnit.DAYS.between(fechaEsperada.toLocalDate(), LocalDate.now());
        if (diasRetraso < 0) diasRetraso = 0;
        double mora = diasRetraso * ConfiguracionService.getMoraPorDia();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Préstamo ID: " + prestamoSeleccionadoId + "\n" +
            "Días de retraso: " + diasRetraso + "\n" +
            "Mora a pagar: $" + mora + "\n\n" +
            "¿Confirmar devolución?",
            "Registrar Devolución",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sqlUpdate = "UPDATE prestamos SET estado = 'DEVUELTO', fecha_devolucion_real = CURDATE(), mora_total = ? WHERE id_prestamo = ?";
            try (Connection conn = DatabaseConnection.getInstancia().getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setDouble(1, mora);
                pstmt.setInt(2, prestamoSeleccionadoId);
                if (pstmt.executeUpdate() > 0) {
                    String sqlDisponibilidad = "UPDATE material SET cantidad_disponible = cantidad_disponible + 1 WHERE id = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sqlDisponibilidad)) {
                        pstmt2.setInt(1, idMaterial);
                        pstmt2.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(this, "Devolución registrada exitosamente.");
                    cargarPrestamosActivos();
                    limpiarSeleccion();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al registrar devolución: " + e.getMessage());
            }
        }
    }

    /**
     * Limpia la selección actual y el área de detalle.
     */
    private void limpiarSeleccion() {
        prestamoSeleccionadoId = -1;
        txtDetalle.setText("");
    }

    /**
     * Muestra los detalles del préstamo seleccionado en el área de texto.
     */
    private void mostrarDetalle() {
        int row = tblPrestamos.getSelectedRow();
        if (row >= 0) {
            prestamoSeleccionadoId = (int) model.getValueAt(row, 0);
            String usuario = (String) model.getValueAt(row, 1);
            String material = (String) model.getValueAt(row, 2);
            java.sql.Date fechaPrestamo = (java.sql.Date) model.getValueAt(row, 3);
            java.sql.Date fechaEsperada = (java.sql.Date) model.getValueAt(row, 4);

            long diasRetraso = ChronoUnit.DAYS.between(fechaEsperada.toLocalDate(), LocalDate.now());
            if (diasRetraso < 0) diasRetraso = 0;
            double mora = diasRetraso * ConfiguracionService.getMoraPorDia();

            txtDetalle.setText("Usuario: " + usuario + "\n" +
                              "Material: " + material + "\n" +
                              "Préstamo: " + fechaPrestamo + "\n" +
                              "Devolución esperada: " + fechaEsperada + "\n" +
                              "Días de retraso: " + diasRetraso + "\n" +
                              "Mora: $" + mora);
        }
    }

    /**
     * Regresa al panel principal de Docente/Empleado.
     */
    private void volver() {
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_Docente());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        btnDevolver = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        setBackground(new Color(8, 20, 55));
        setPreferredSize(new Dimension(1280, 720));

        jLabel1.setFont(new Font("Arial", 1, 24));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setText("REGISTRAR DEVOLUCIÓN");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tblPrestamos.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Usuario", "Material", "Fecha Préstamo", "Fecha Esperada"}
        ));
        tblPrestamos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarDetalle();
            }
        });
        jScrollPane1.setViewportView(tblPrestamos);

        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDetalle.setRows(10);
        jScrollPane2.setViewportView(txtDetalle);

        btnDevolver.setBackground(new Color(0, 102, 204));
        btnDevolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setText("REGISTRAR DEVOLUCIÓN");
        btnDevolver.addActionListener(e -> registrarDevolucion());

        btnVolver.setBackground(new Color(102, 102, 102));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setText("VOLVER");
        btnVolver.addActionListener(e -> volver());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }

    private javax.swing.JButton btnDevolver;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPrestamos;
    private javax.swing.JTextArea txtDetalle;
}