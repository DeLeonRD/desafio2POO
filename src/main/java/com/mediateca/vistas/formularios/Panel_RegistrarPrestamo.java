package com.mediateca.vistas.formularios;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.service.PrestamoService;
import com.mediateca.vistas.Ventana_PPAL;
import com.mediateca.vistas.Panel_Docente;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Panel_RegistrarPrestamo extends JPanel {
    
    private JTable tblMateriales;
    private DefaultTableModel model;
    private JComboBox<String> cmbUsuario;
    private JSpinner spnDias;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private JLabel lblMensaje;
    
    private int materialSeleccionadoId = -1;
    private String materialSeleccionadoTitulo = "";
    
    public Panel_RegistrarPrestamo() {
        initComponents();
        cargarMaterialesDisponibles();
        cargarUsuarios();
    }
    
    private void initComponents() {
        setBackground(new Color(8, 20, 55));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("REGISTRAR NUEVO PRÉSTAMO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        add(lblTitulo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(8, 20, 55));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblMateriales = new JLabel("Materiales Disponibles:");
        lblMateriales.setFont(new Font("Arial", Font.BOLD, 16));
        lblMateriales.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(lblMateriales, gbc);
        
        model = new DefaultTableModel(new String[]{"ID", "Título", "Tipo", "Disponibles"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblMateriales = new JTable(model);
        tblMateriales.setFont(new Font("Arial", Font.PLAIN, 14));
        tblMateriales.setRowHeight(25);
        tblMateriales.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblMateriales.getSelectedRow();
                if (row >= 0) {
                    materialSeleccionadoId = (int) model.getValueAt(row, 0);
                    materialSeleccionadoTitulo = (String) model.getValueAt(row, 1);
                    lblMensaje.setText("Material seleccionado: " + materialSeleccionadoTitulo);
                }
            }
        });
        
        JScrollPane scrollMateriales = new JScrollPane(tblMateriales);
        scrollMateriales.setPreferredSize(new Dimension(600, 300));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelCentral.add(scrollMateriales, gbc);
        
        JLabel lblUsuario = new JLabel("Seleccionar Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(lblUsuario, gbc);
        
        cmbUsuario = new JComboBox<>();
        cmbUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbUsuario.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        panelCentral.add(cmbUsuario, gbc);
        
        JLabel lblDias = new JLabel("Días de Préstamo:");
        lblDias.setFont(new Font("Arial", Font.BOLD, 14));
        lblDias.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelCentral.add(lblDias, gbc);
        
        spnDias = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        spnDias.setFont(new Font("Arial", Font.PLAIN, 14));
        spnDias.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 1;
        panelCentral.add(spnDias, gbc);
        
        lblMensaje = new JLabel("Seleccione un material y un usuario");
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 12));
        lblMensaje.setForeground(new Color(200, 200, 200));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelCentral.add(lblMensaje, gbc);
        
        add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(8, 20, 55));
        
        btnRegistrar = new JButton("REGISTRAR PRÉSTAMO");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(0, 102, 204));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setPreferredSize(new Dimension(200, 45));
        btnRegistrar.addActionListener(e -> registrarPrestamo());
        
        btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setBackground(new Color(102, 102, 102));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(150, 45));
        btnVolver.addActionListener(e -> volver());
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarMaterialesDisponibles() {
        model.setRowCount(0);
        String sql = "SELECT id, titulo, tipo, cantidad_disponible FROM material WHERE cantidad_disponible > 0 ORDER BY titulo";
        
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("tipo"),
                    rs.getInt("cantidad_disponible")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar materiales: " + e.getMessage());
        }
    }
    
    private void cargarUsuarios() {
        cmbUsuario.removeAllItems();
        String sql = "SELECT id_usuario, nombre, email FROM usuarios WHERE tipo IN ('ALUMNO', 'DOCENTE') ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cmbUsuario.addItem(rs.getInt("id_usuario") + " - " + rs.getString("nombre") + " (" + rs.getString("email") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void registrarPrestamo() {
        if (materialSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un material de la lista.");
            return;
        }
        
        if (cmbUsuario.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }
        
        String usuarioSeleccionado = (String) cmbUsuario.getSelectedItem();
        int idUsuario = Integer.parseInt(usuarioSeleccionado.split(" - ")[0]);
        int diasPrestamo = (int) spnDias.getValue();
        
        int prestamosActivos = PrestamoService.contarPrestamosActivos(idUsuario);
        int maxPrestamos = obtenerMaxPrestamos();
        
        if (prestamosActivos >= maxPrestamos) {
            JOptionPane.showMessageDialog(this, 
                "El usuario ya tiene " + prestamosActivos + " préstamos activos.\n" +
                "Límite máximo: " + maxPrestamos,
                "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (PrestamoService.usuarioTieneMora(idUsuario)) {
            JOptionPane.showMessageDialog(this,
                "El usuario tiene préstamos vencidos. No puede realizar nuevos préstamos.",
                "Mora pendiente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Confirmar préstamo?\n\n" +
            "Usuario: " + usuarioSeleccionado.split(" - ")[1] + "\n" +
            "Material: " + materialSeleccionadoTitulo + "\n" +
            "Días: " + diasPrestamo,
            "Confirmar Préstamo", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = PrestamoService.crearPrestamo(idUsuario, materialSeleccionadoId, diasPrestamo);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente.");
                cargarMaterialesDisponibles();
                limpiarSeleccion();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el préstamo.");
            }
        }
    }
    
    private int obtenerMaxPrestamos() {
        String sql = "SELECT valor FROM configuracion WHERE clave = 'max_prestamos_activos'";
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("valor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3;
    }
    
    private void limpiarSeleccion() {
        materialSeleccionadoId = -1;
        materialSeleccionadoTitulo = "";
        tblMateriales.clearSelection();
        lblMensaje.setText("Seleccione un material y un usuario");
    }
    
    private void volver() {
        String rol = SessionManager.getUsuarioActual() != null ? 
                     SessionManager.getUsuarioActual().getTipo() : "";
        
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            if ("ADMIN".equals(rol)) {
                ventana.mostrar(new Panel_administrador());
            } else {
                ventana.mostrar(new Panel_Docente());
            }
        }
    }
}