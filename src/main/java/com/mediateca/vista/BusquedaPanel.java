package com.mediateca.vista;

import com.mediateca.vista.tablas.MaterialTableModel;
import javax.swing.*;
import java.awt.*;

/**
 * Panel central para la búsqueda y visualización de materiales.
 * Integra la JTable con el modelo reutilizable MaterialTableModel.
 * Proporciona una interfaz limpia para que De Leon integre las búsquedas reales.
 */
public class BusquedaPanel extends JPanel {

    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JTable tablaMateriales;
    private MaterialTableModel modeloTabla;

    public BusquedaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Inicializar componentes
        modeloTabla = new MaterialTableModel();
        tablaMateriales = new JTable(modeloTabla);
        
        // Estilización básica de la tabla
        tablaMateriales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMateriales.getTableHeader().setReorderingAllowed(false);

        // Armar el panel
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(tablaMateriales), BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(new JLabel("Criterio de búsqueda:"));
        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnActualizar = new JButton("Limpiar Búsqueda");
        
        panel.add(txtBusqueda);
        panel.add(btnBuscar);
        panel.add(btnActualizar);
        
        return panel;
    }

    /**
     * Permite al controlador externo asignar la lógica de búsqueda.
     * Nota para De Leon: Aquí se debería llamar al DAO de Ricardo.
     */
    public void setAccionBuscar(java.awt.event.ActionListener listener) {
        btnBuscar.addActionListener(listener);
    }

    public void setAccionActualizar(java.awt.event.ActionListener listener) {
        btnActualizar.addActionListener(listener);
    }

    /**
     * Facilita la obtención del texto de búsqueda y limpieza.
     */
    public String getTextoBusqueda() {
        return txtBusqueda.getText();
    }

    public void limpiarBusqueda() {
        txtBusqueda.setText("");
    }

    /**
     * Acceso directo al modelo para cargar datos desde el DAO.
     */
    public MaterialTableModel getModeloTabla() {
        return modeloTabla;
    }

    /**
     * Obtiene el ID del material seleccionado en la tabla.
     */
    public int getIdSeleccionado() {
        int fila = tablaMateriales.getSelectedRow();
        if (fila != -1) {
            return (int) modeloTabla.getValueAt(fila, 0);
        }
        return -1;
    }
}
