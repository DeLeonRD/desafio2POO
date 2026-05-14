package com.mediateca.vistas.busquedas;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.service.TipoMaterialService;
import com.mediateca.vistas.Panel_administrador;
import com.mediateca.vistas.Ventana_PPAL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 *
 * Panel de búsqueda de materiales (libros, revistas, CDs, DVDs, tesis).
 * Filtros disponibles: título (LIKE), tipo (combobox) y código/ID.
 * Los resultados se muestran en el JTable.
 *
 * Nota: los botones "Modificar" (5 de ellos en el form) y los botones
 * tipo jButton6/7/8 quedan SIN cablear porque la funcionalidad de modificar
 * material desde la búsqueda requiere paneles de edición que aún no existen.
 */
public class Panel_BuscaMat extends javax.swing.JPanel {
 
    private static final Logger logger = Logger.getLogger(Panel_BuscaMat.class.getName());
 
    private static final String[] COLUMNAS = {
        "ID", "Tipo", "Título", "Autor", "Año", "Ubicación", "Total", "Disponibles"
    };
 
    /**
     * Creates new form Panel_BuscaMat
     */
    public Panel_BuscaMat() {
        initComponents();
        configurarTabla();
        cargarTiposMateriales();
        cablearEventos();
    }
 
    /** Reemplaza el modelo placeholder por uno con columnas reales y filas vacías. */
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // la tabla es solo de lectura
            }
        };
        jTable1.setModel(modelo);
    }
 
    /** Carga los tipos de materiales desde la base de datos */
    private void cargarTiposMateriales() {
        combobox_selecciónmaterial_busqueda.removeAllItems();
        combobox_selecciónmaterial_busqueda.addItem("-- Todos --");
        List<String> tipos = TipoMaterialService.listarTipos();
        for (String tipo : tipos) {
            combobox_selecciónmaterial_busqueda.addItem(tipo);
        }
    }
 
    private void cablearEventos() {
        boton_buscar_material_tabla.addActionListener(e -> buscar());
        boton_limpiar_campos.addActionListener(e -> limpiar());
        boton_regresar_menu_anterior.addActionListener(e -> volverAlMenu());
 
        // Los botones "Modificar" del form no tienen funcionalidad de backend
        // todavía. Muestran un aviso para no dejar al usuario sin feedback.
        java.awt.event.ActionListener pendiente = e -> JOptionPane.showMessageDialog(
            this,
            "Funcionalidad de modificar material aún no implementada.",
            "Pendiente", JOptionPane.INFORMATION_MESSAGE);
        botn_modificar_primerelementolista.addActionListener(pendiente);
        botn_modificar_segndoelementolista.addActionListener(pendiente);
        jButton6.addActionListener(pendiente);
        jButton7.addActionListener(pendiente);
        jButton8.addActionListener(pendiente);
    }
 
    /** Ejecuta la búsqueda con los filtros llenos y vuelca el resultado a la tabla. */
    private void buscar() {
        String titulo = campo_titulomaterial_busqueda.getText().trim();
        String tipo = (String) combobox_selecciónmaterial_busqueda.getSelectedItem();
        String codTxt = campo_codigomaterial_busqueda.getText().trim();
        
        if ("-- Todos --".equals(tipo)) {
            tipo = null;
        }
 
        int idMaterial = -1;
        if (!codTxt.isEmpty()) {
            try {
                idMaterial = Integer.parseInt(codTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "El código debe ser un número entero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
 
        try {
            List<Object[]> filas = buscarMateriales(titulo, tipo, idMaterial);
 
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.setRowCount(0); // limpiar tabla
            for (Object[] fila : filas) {
                modelo.addRow(fila);
            }
 
            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron materiales con esos filtros.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al buscar materiales", ex);
            JOptionPane.showMessageDialog(this,
                "Error al consultar la BD: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /** Método que realiza la consulta SQL directa a la base de datos */
    private List<Object[]> buscarMateriales(String titulo, String tipo, int idMaterial) {
        List<Object[]> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT id, tipo, titulo, autor, anio_publicacion, ubicacion, cantidad_total, cantidad_disponible " +
            "FROM material WHERE 1=1 "
        );
        
        List<Object> params = new ArrayList<>();
        
        if (idMaterial > 0) {
            sql.append("AND id = ? ");
            params.add(idMaterial);
        }
        
        if (titulo != null && !titulo.isEmpty()) {
            sql.append("AND titulo LIKE ? ");
            params.add("%" + titulo + "%");
        }
        
        if (tipo != null && !tipo.isEmpty()) {
            sql.append("AND tipo = ? ");
            params.add(tipo);
        }
        
        sql.append("ORDER BY tipo, titulo");
        
        try (Connection conn = DatabaseConnection.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("tipo");
                fila[2] = rs.getString("titulo");
                fila[3] = rs.getString("autor");
                fila[4] = rs.getInt("anio_publicacion");
                fila[5] = rs.getString("ubicacion");
                fila[6] = rs.getInt("cantidad_total");
                fila[7] = rs.getInt("cantidad_disponible");
                resultados.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }
 
    private void limpiar() {
        campo_titulomaterial_busqueda.setText("");
        campo_codigomaterial_busqueda.setText("");
        combobox_selecciónmaterial_busqueda.setSelectedIndex(0);
        ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        campo_titulomaterial_busqueda.requestFocus();
    }
 
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        boton_buscar_material_tabla = new javax.swing.JButton();
        boton_limpiar_campos = new javax.swing.JButton();
        boton_regresar_menu_anterior = new javax.swing.JButton();
        botn_modificar_primerelementolista = new javax.swing.JButton();
        botn_modificar_segndoelementolista = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        combobox_selecciónmaterial_busqueda = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        campo_titulomaterial_busqueda = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campo_codigomaterial_busqueda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1075, 655));
        setMinimumSize(new java.awt.Dimension(1075, 655));

        jTable1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        boton_buscar_material_tabla.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boton_buscar_material_tabla.setText("BUSCAR");

        boton_limpiar_campos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boton_limpiar_campos.setText("LIMPIAR");

        boton_regresar_menu_anterior.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boton_regresar_menu_anterior.setText("CANCELAR");

        botn_modificar_primerelementolista.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botn_modificar_primerelementolista.setText("Modificar");

        botn_modificar_segndoelementolista.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botn_modificar_segndoelementolista.setText("Modificar");

        jButton6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton6.setText("Modificar");

        jButton7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton7.setText("Modificar");

        jButton8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton8.setText("Modificar");

        combobox_selecciónmaterial_busqueda.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Titulo");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Codigo");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Tipo");

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("BUSQUEDA DE MATERIAL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(combobox_selecciónmaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(campo_titulomaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(69, 69, 69)
                                                .addComponent(jLabel2))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(218, 218, 218)
                                                .addComponent(boton_buscar_material_tabla)
                                                .addGap(55, 55, 55)
                                                .addComponent(boton_limpiar_campos)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(boton_regresar_menu_anterior)
                                            .addComponent(campo_codigomaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 945, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botn_modificar_segndoelementolista)
                                    .addComponent(botn_modificar_primerelementolista)
                                    .addComponent(jButton6)
                                    .addComponent(jButton7)
                                    .addComponent(jButton8))))
                        .addGap(0, 21, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(campo_titulomaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(campo_codigomaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combobox_selecciónmaterial_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_limpiar_campos)
                    .addComponent(boton_buscar_material_tabla)
                    .addComponent(boton_regresar_menu_anterior))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botn_modificar_primerelementolista)
                        .addGap(18, 18, 18)
                        .addComponent(botn_modificar_segndoelementolista)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8)
                        .addGap(132, 132, 132))))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botn_modificar_primerelementolista;
    private javax.swing.JButton botn_modificar_segndoelementolista;
    private javax.swing.JButton boton_buscar_material_tabla;
    private javax.swing.JButton boton_limpiar_campos;
    private javax.swing.JButton boton_regresar_menu_anterior;
    private javax.swing.JTextField campo_codigomaterial_busqueda;
    private javax.swing.JTextField campo_titulomaterial_busqueda;
    private javax.swing.JComboBox<String> combobox_selecciónmaterial_busqueda;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}