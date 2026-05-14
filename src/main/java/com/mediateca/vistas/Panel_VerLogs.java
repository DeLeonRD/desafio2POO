package com.mediateca.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Panel para visualizar los logs del sistema.
 * Muestra los registros de CREACION, ELIMINACION, MODIFICACION, CONSULTA y PRESTAMO.
 */
public class Panel_VerLogs extends javax.swing.JPanel {
    
    private DefaultTableModel model;
    
    // Palabras clave para filtrar solo operaciones de interés
    private static final String[] PALABRAS_CLAVE = {
        "creado", "registrado", "insertado", "nuevo", "crear",
        "eliminado", "borrado", "delete", "eliminar",
        "actualizado", "modificado", "update", "editar", "cambio",
        "buscar", "consultar", "listar", "obtener",
        "prestamo", "prestado", "devolucion", "mora"
    };
    
    public Panel_VerLogs() {
        initComponents();
        configurarTabla();
        cargarLogs();
        cablearEventos();
    }
    
    private void configurarTabla() {
        model = new DefaultTableModel(
            new String[]{"Fecha/Hora", "Nivel", "Clase", "Mensaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLogs.setModel(model);
        
        tblLogs.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLogs.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblLogs.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblLogs.getColumnModel().getColumn(3).setPreferredWidth(500);
    }
    
    private void cargarLogs() {
        model.setRowCount(0);
        
        String logPath = "logs/mediateca_0.log";
        File logFile = new File(logPath);
        
        if (!logFile.exists() || logFile.length() == 0) {
            lblInfo.setText("No se encontró el archivo de log o está vacío");
            return;
        }
        
        try {
            // Leer con BufferedReader usando el charset por defecto del sistema
            List<String> lineas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                }
            }
            Collections.reverse(lineas); // Mostrar más recientes primero
            
            // Patrón para el formato: 2026-05-14 14:18:31 INFO clase mensaje
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "^(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2}:\\d{2})\\s+(\\S+)\\s+(\\S+)\\s+(.*)$"
            );
            
            int count = 0;
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                
                java.util.regex.Matcher matcher = pattern.matcher(linea);
                
                if (matcher.matches()) {
                    String fecha = matcher.group(1) + " " + matcher.group(2);
                    String nivel = matcher.group(3);
                    String clase = matcher.group(4);
                    String mensaje = matcher.group(5);
                    
                    // Verificar si el mensaje contiene alguna palabra clave de interés
                    boolean esRelevante = false;
                    for (String clave : PALABRAS_CLAVE) {
                        if (mensaje.toLowerCase().contains(clave.toLowerCase())) {
                            esRelevante = true;
                            break;
                        }
                    }
                    
                    // Solo mostrar registros de operaciones relevantes
                    if (esRelevante) {
                        model.addRow(new Object[]{fecha, nivel, clase, mensaje});
                        count++;
                    }
                }
            }
            
            lblInfo.setText("Total de registros: " + count);
            
        } catch (IOException e) {
            lblInfo.setText("Error al leer el archivo de log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void refrescar() {
        cargarLogs();
    }
    
    private void volverAlMenu() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_administrador());
    }
    
    private void cablearEventos() {
        btnRefrescar.addActionListener(e -> refrescar());
        btnVolver.addActionListener(e -> volverAlMenu());
    }
    
    private void initComponents() {
        setBackground(new Color(11, 19, 43));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout(10, 10));

        // Panel superior - Título
        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setBackground(new Color(11, 19, 43));
        JLabel lblTitulo = new JLabel("REGISTRO DE OPERACIONES DEL SISTEMA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel pnlBotones = new JPanel();
        pnlBotones.setBackground(new Color(11, 19, 43));
        pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnRefrescar = new JButton("REFRESCAR");
        btnRefrescar.setBackground(new Color(0, 102, 204));
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setPreferredSize(new Dimension(150, 40));
        btnRefrescar.setBorderPainted(false);
        
        btnVolver = new JButton("VOLVER");
        btnVolver.setBackground(new Color(102, 102, 102));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(120, 40));
        btnVolver.setBorderPainted(false);
        
        pnlBotones.add(btnRefrescar);
        pnlBotones.add(btnVolver);
        add(pnlBotones, BorderLayout.CENTER);

        // Tabla de logs
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBackground(new Color(8, 20, 55));
        pnlTabla.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        tblLogs = new JTable();
        tblLogs.setFont(new Font("Arial", Font.PLAIN, 12));
        tblLogs.setRowHeight(25);
        tblLogs.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblLogs.getTableHeader().setBackground(new Color(0, 102, 204));
        tblLogs.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tblLogs);
        pnlTabla.add(scrollPane, BorderLayout.CENTER);
        
        lblInfo = new JLabel("Total de registros: 0");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(200, 200, 200));
        pnlTabla.add(lblInfo, BorderLayout.SOUTH);
        
        add(pnlTabla, BorderLayout.SOUTH);
    }

    private JButton btnRefrescar;
    private JButton btnVolver;
    private JLabel lblInfo;
    private JTable tblLogs;
}