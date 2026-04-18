package com.mediateca.vista.formularios;

import com.mediateca.util.ValidacionUtil;
import javax.swing.*;

/**
 * - Formulario para la gestión de DVDs.
 * - Permite capturar información de director y duración.
 */
public class DvdForm extends MaterialForm {

    private JTextField txtId;
    private JTextField txtDirector;
    private JTextField txtDuracion;

    public DvdForm() {
        super("Registro de DVD");
        inicializarCampos();
    }

    private void inicializarCampos() {
        txtId = new JTextField(10);
        txtDirector = new JTextField(20);
        txtDuracion = new JTextField(10);

        agregarCampo("ID Material:", txtId, 0);
        agregarCampo("Director:", txtDirector, 1);
        agregarCampo("Duración (min):", txtDuracion, 2);
    }

    @Override
    public void limpiarCampos() {
        txtId.setText("");
        txtDirector.setText("");
        txtDuracion.setText("");
    }

    public boolean validarDatos() {
        if (ValidacionUtil.esTextoVacio(txtId.getText(), "ID", this))
            return false;
        if (ValidacionUtil.esTextoVacio(txtDirector.getText(), "Director", this))
            return false;
        if (!ValidacionUtil.esNumeroValido(txtDuracion.getText(), "Duración", this))
            return false;
        return true;
    }

    public String getId() {
        return txtId.getText();
    }

    public String getDirector() {
        return txtDirector.getText();
    }

    public String getDuracion() {
        return txtDuracion.getText();
    }
}
