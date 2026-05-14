/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas.busquedas;

import com.mediateca.service.PrestamoService;
import com.mediateca.service.TipoMaterialService;
import com.mediateca.model.Usuario;
import com.mediateca.util.SessionManager;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Panel_Alumno;
import com.mediateca.vistas.Panel_Bienvenida;
import com.mediateca.vistas.Panel_Docente;
import com.mediateca.vistas.Ventana_PPAL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Panel_BuscaPest extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(Panel_BuscaPest.class.getName());

    public Panel_BuscaPest() {
        initComponents();
        configurarTabla();
        cargarTiposMateriales();
        cablearEventos();
        limpiar();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "ID Usuario", "Usuario", "Tipo", "Material", "Fecha Préstamo", "Fecha Esperada", "Estado", "Mora USD"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPrestamos.setModel(modelo);

        tblPrestamos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblPrestamos.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblPrestamos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblPrestamos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblPrestamos.getColumnModel().getColumn(4).setPreferredWidth(180);
        tblPrestamos.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblPrestamos.getColumnModel().getColumn(8).setPreferredWidth(80);
    }

    private void cargarTiposMateriales() {
        cmbTipo.removeAllItems();
        cmbTipo.addItem("-- Todos --");
        List<String> tipos = TipoMaterialService.listarTipos();
        for (String tipo : tipos) {
            cmbTipo.addItem(tipo);
        }
    }

    private void cablearEventos() {
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    private void buscar() {
        String carnetTxt = txtCarnet.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if ("-- Todos --".equals(tipo)) {
            tipo = null;
        }

        int idUsuario = -1;
        if (!carnetTxt.isEmpty()) {
            try {
                idUsuario = Integer.parseInt(carnetTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "El carnet debe ser un número (ID de usuario).",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            List<Object[]> filas = PrestamoService.buscarPrestamos(idUsuario, nombre.isEmpty() ? null : nombre, tipo);

            DefaultTableModel modelo = (DefaultTableModel) tblPrestamos.getModel();
            modelo.setRowCount(0);

            for (Object[] fila : filas) {
                String estado = (String) fila[7];
                String estadoMostrar;
                switch (estado) {
                    case "ACTIVO":
                        estadoMostrar = "EN CIRCULACION";
                        break;
                    case "DEVUELTO":
                        estadoMostrar = "DEVUELTO";
                        break;
                    case "VENCIDO":
                        estadoMostrar = "VENCIDO";
                        break;
                    default:
                        estadoMostrar = estado;
                }

                double mora = (double) fila[8];
                String moraStr = String.format("$%.2f", mora);

                modelo.addRow(new Object[]{
                    fila[0], fila[1], fila[2], fila[3], fila[4],
                    fila[5], fila[6], estadoMostrar, moraStr
                });
            }

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron préstamos con esos filtros.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

            lblResultados.setText("Resultados: " + modelo.getRowCount() + " préstamos encontrados");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al buscar préstamos", ex);
            JOptionPane.showMessageDialog(this,
                "Error al consultar la BD: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtCarnet.setText("");
        txtNombre.setText("");
        cmbTipo.setSelectedIndex(0);
        ((DefaultTableModel) tblPrestamos.getModel()).setRowCount(0);
        lblResultados.setText("Resultados: 0 préstamos encontrados");
        txtCarnet.requestFocus();
    }

    private void volverAlMenu() {
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana == null) return;

        Usuario usuarioActual = SessionManager.getUsuarioActual();

        if (usuarioActual == null) {
            ventana.mostrar(new Panel_Bienvenida());
            return;
        }

        String tipo = usuarioActual.getTipo();

        switch (tipo) {
            case "ADMIN":
                ventana.mostrar(new Panel_administrador());
                break;
            case "EMPLEADO":
            case "PROFESOR":
                ventana.mostrar(new Panel_Docente());
                break;
            case "ALUMNO":
                ventana.mostrar(new Panel_Alumno());
                break;
            default:
                ventana.mostrar(new Panel_Bienvenida());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCarnet = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        cmbTipo = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        lblResultados = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setBackground(new java.awt.Color(8, 20, 55));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 28));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("BÚSQUEDA DE PRÉSTAMOS");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID Usuario (Carnet):");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre del Usuario:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tipo de Material:");

        txtCarnet.setFont(new java.awt.Font("Arial", 0, 14));
        txtCarnet.setPreferredSize(new java.awt.Dimension(200, 30));

        txtNombre.setFont(new java.awt.Font("Arial", 0, 14));
        txtNombre.setPreferredSize(new java.awt.Dimension(250, 30));

        cmbTipo.setFont(new java.awt.Font("Arial", 0, 14));
        cmbTipo.setPreferredSize(new java.awt.Dimension(150, 30));

        btnBuscar.setBackground(new java.awt.Color(0, 102, 204));
        btnBuscar.setFont(new java.awt.Font("Arial", 1, 14));
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("BUSCAR");
        btnBuscar.setBorderPainted(false);
        btnBuscar.setPreferredSize(new java.awt.Dimension(120, 35));

        btnLimpiar.setBackground(new java.awt.Color(102, 102, 102));
        btnLimpiar.setFont(new java.awt.Font("Arial", 1, 14));
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setPreferredSize(new java.awt.Dimension(120, 35));

        btnVolver.setBackground(new java.awt.Color(204, 0, 0));
        btnVolver.setFont(new java.awt.Font("Arial", 1, 14));
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("VOLVER");
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new java.awt.Dimension(120, 35));

        tblPrestamos.setFont(new java.awt.Font("Arial", 0, 12));
        jScrollPane1.setViewportView(tblPrestamos);

        lblResultados.setFont(new java.awt.Font("Arial", 0, 12));
        lblResultados.setForeground(new java.awt.Color(200, 200, 200));
        lblResultados.setText("Resultados: 0 préstamos encontrados");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(10, 10, 10)
                                .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jLabel2)
                                .addGap(10, 10, 10)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jLabel3)
                                .addGap(10, 10, 10)
                                .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(200, 200, 200)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
                    .addComponent(lblResultados))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblResultados)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }

    // Variables declaration
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblResultados;
    private javax.swing.JTable tblPrestamos;
    private javax.swing.JTextField txtCarnet;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration
}