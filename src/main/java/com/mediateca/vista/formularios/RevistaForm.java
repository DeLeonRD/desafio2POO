package com.mediateca.vista.formularios;

import com.mediateca.util.ValidacionUtil;
import javax.swing.*;

/**
 * - Formulario para la gestión de Revistas.
 * - Nota para De Leon: La clase Revista hereda directamente de Material según
 * el modelado de Franky.
 */
public class RevistaForm extends MaterialForm {

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEdicion;

    public RevistaForm() {
        super("Registro de Revista");
        inicializarCampos();
    }

    private void inicializarCampos() {
        txtId = new JTextField(10);
        txtNombre = new JTextField(20);
        txtEdicion = new JTextField(10);

        agregarCampo("ID Material:", txtId, 0);
        agregarCampo("Nombre Revista:", txtNombre, 1);
        agregarCampo("Edición:", txtEdicion, 2);
    }

    @Override
    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEdicion.setText("");
    }

    public boolean validarDatos() {
        if (ValidacionUtil.esTextoVacio(txtId.getText(), "ID", this))
            return false;
        if (ValidacionUtil.esTextoVacio(txtNombre.getText(), "Nombre", this))
            return false;
        if (!ValidacionUtil.esNumeroValido(txtEdicion.getText(), "Edición", this))
            return false;
        return true;
    }

    public String getId() {
        return txtId.getText();
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getEdicion() {
        return txtEdicion.getText();
    }
}
