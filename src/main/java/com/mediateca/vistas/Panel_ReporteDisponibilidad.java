package com.mediateca.vistas;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.util.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;

public class Panel_ReporteDisponibilidad extends JPanel {
    
    private JTable tblDisponibilidad;
    private DefaultTableModel model;
    
    public Panel_ReporteDisponibilidad() {
        initComponents();
        configurarTabla();
        cargarTodosMateriales();
    }
    
    private void configurarTabla() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        model.setColumnIdentifiers(new String[]{"ID", "Tipo", "Título", "Autor", "Año", "Disponible/Total", "Estado"});
        tblDisponibilidad.setModel(model);
        
        tblDisponibilidad.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblDisponibilidad.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblDisponibilidad.getColumnModel().getColumn(2).setPreferredWidth(350);
        tblDisponibilidad.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblDisponibilidad.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblDisponibilidad.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblDisponibilidad.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblDisponibilidad.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblDisponibilidad.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblDisponibilidad.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }
    
    private void cargarTodosMateriales() {
        model.setRowCount(0);
        String sql = "SELECT id, tipo, titulo, autor, anio_publicacion, cantidad_total, cantidad_disponible FROM material ORDER BY tipo, titulo";
        ejecutarConsulta(sql);
    }
    
    private void cargarMaterialesDisponibles() {
        model.setRowCount(0);
        String sql = "SELECT id, tipo, titulo, autor, anio_publicacion, cantidad_total, cantidad_disponible FROM material WHERE cantidad_disponible > 0 ORDER BY tipo, titulo";
        ejecutarConsulta(sql);
    }
    
    private void cargarMaterialesNoDisponibles() {
        model.setRowCount(0);
        String sql = "SELECT id, tipo, titulo, autor, anio_publicacion, cantidad_total, cantidad_disponible FROM material WHERE cantidad_disponible = 0 ORDER BY tipo, titulo";
        ejecutarConsulta(sql);
    }
    
    private void cargarMaterialesParciales() {
        model.setRowCount(0);
        String sql = "SELECT id, tipo, titulo, autor, anio_publicacion, cantidad_total, cantidad_disponible FROM material WHERE cantidad_disponible > 0 AND cantidad_disponible < cantidad_total ORDER BY tipo, titulo";
        ejecutarConsulta(sql);
    }
    
    private void ejecutarConsulta(String sql) {
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int anio = rs.getInt("anio_publicacion");
                int total = rs.getInt("cantidad_total");
                int disponible = rs.getInt("cantidad_disponible");
                
                String disponibilidad = disponible + " / " + total;
                String estado;
                
                if (disponible == 0) {
                    estado = "🔴 NO DISPONIBLE";
                } else if (disponible == total) {
                    estado = "🟢 TODOS DISPONIBLES";
                } else {
                    estado = "🟡 PARCIALMENTE DISPONIBLE";
                }
                
                model.addRow(new Object[]{id, tipo, titulo, autor, anio, disponibilidad, estado});
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
    
    private void volverAlMenu() {
        com.mediateca.model.Usuario usuarioActual = SessionManager.getUsuarioActual();
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        
        if (usuarioActual == null) {
            ventana.mostrar(new Panel_Bienvenida());
            return;
        }
        
        String tipo = usuarioActual.getTipo();
        
        if ("ADMIN".equals(tipo)) {
            ventana.mostrar(new Panel_administrador());
        } else if ("EMPLEADO".equals(tipo) || "PROFESOR".equals(tipo)) {
            ventana.mostrar(new Panel_Docente());
        } else {
            ventana.mostrar(new Panel_Alumno());
        }
    }
    
    private void initComponents() {
        setBackground(new Color(11, 19, 43));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setBackground(new Color(11, 19, 43));
        JLabel lblTitulo = new JLabel("REPORTE DE DISPONIBILIDAD DE MATERIALES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);
        
        JPanel pnlFiltros = new JPanel();
        pnlFiltros.setBackground(new Color(11, 19, 43));
        pnlFiltros.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnTodos = crearBoton("📋 TODOS", new Color(0, 102, 204));
        JButton btnDisponibles = crearBoton("🟢 DISPONIBLES", new Color(0, 153, 0));
        JButton btnParciales = crearBoton("🟡 PARCIALES", new Color(255, 153, 0));
        JButton btnNoDisponibles = crearBoton("🔴 NO DISPONIBLES", new Color(204, 0, 0));
        JButton btnVolver = crearBoton("◀ VOLVER", new Color(102, 102, 102));
        
        btnTodos.addActionListener(e -> cargarTodosMateriales());
        btnDisponibles.addActionListener(e -> cargarMaterialesDisponibles());
        btnParciales.addActionListener(e -> cargarMaterialesParciales());
        btnNoDisponibles.addActionListener(e -> cargarMaterialesNoDisponibles());
        btnVolver.addActionListener(e -> volverAlMenu());
        
        pnlFiltros.add(btnTodos);
        pnlFiltros.add(btnDisponibles);
        pnlFiltros.add(btnParciales);
        pnlFiltros.add(btnNoDisponibles);
        pnlFiltros.add(btnVolver);
        add(pnlFiltros, BorderLayout.CENTER);
        
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBackground(new Color(8, 20, 55));
        pnlTabla.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        tblDisponibilidad = new JTable();
        tblDisponibilidad.setFont(new Font("Arial", Font.PLAIN, 12));
        tblDisponibilidad.setRowHeight(28);
        tblDisponibilidad.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblDisponibilidad.getTableHeader().setBackground(new Color(0, 102, 204));
        tblDisponibilidad.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tblDisponibilidad);
        pnlTabla.add(scrollPane, BorderLayout.CENTER);
        add(pnlTabla, BorderLayout.SOUTH);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(170, 40));
        return boton;
    }
}