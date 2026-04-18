package com.mediateca.vista.tablas;

import com.mediateca.model.Material;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 * -Modelo de tabla generico para la visualización de materiales.
 * -Proporciona una base sólida para que otros integrantes puedan consultar
 * los objetos directamente sin lidiar con indices de arreglos complejos.
 */
public class MaterialTableModel extends AbstractTableModel {

    private final String[] columnas = { "ID", "Tipo", "Detalle del Material", "Acción" };
    private List<Material> materiales;

    public MaterialTableModel() {
        this.materiales = new ArrayList<>();
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
        // Notificamos a la vista que los datos han cambiado para refrescar la JTable
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return materiales.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (materiales.isEmpty() || rowIndex >= materiales.size()) {
            return null;
        }

        Material material = materiales.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return material.getId();
            case 1:
                return material.getTipo();
            case 2:
                return material.toString();
            case 3:
                return "Gestionar";
            default:
                return null;
        }
    }

    /**
     * - Devuelve el objeto Material completo de la fila seleccionada.
     * - Util para la integracion con los procesos de busqueda y borrado.
     */
    public Material getMaterialAt(int row) {
        if (row >= 0 && row < materiales.size()) {
            return materiales.get(row);
        }
        return null;
    }
}
