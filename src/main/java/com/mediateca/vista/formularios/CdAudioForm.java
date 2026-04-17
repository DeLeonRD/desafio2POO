package com.mediateca.vista.formularios;

import com.mediateca.util.ValidacionUtil;
import javax.swing.*;

/**
 * - Formulario para la gestión de CDs de Audio.
 * - Hereda de MaterialForm. Estructura basada en MaterialAudiovisual.
 */
public class CdAudioForm extends MaterialForm {

    private JTextField txtId;
    private JTextField txtArtista;
    private JTextField txtDuracion;

    public CdAudioForm() {
        super("Registro de CD de Audio");
        inicializarCampos();
    }

    private void inicializarCampos() {
        txtId = new JTextField(10);
        txtArtista = new JTextField(20);
        txtDuracion = new JTextField(10);

        agregarCampo("ID Material:", txtId, 0);
        agregarCampo("Artista:", txtArtista, 1);
        agregarCampo("Duración (min):", txtDuracion, 2);
    }

    @Override
    public void limpiarCampos() {
        txtId.setText("");
        txtArtista.setText("");
        txtDuracion.setText("");
    }

    public boolean validarDatos() {
        if (ValidacionUtil.esTextoVacio(txtId.getText(), "ID", this))
            return false;
        if (ValidacionUtil.esTextoVacio(txtArtista.getText(), "Artista", this))
            return false;
        if (!ValidacionUtil.esNumeroValido(txtDuracion.getText(), "Duración", this))
            return false;
        return true;
    }

    public String getId() {
        return txtId.getText();
    }

    public String getArtista() {
        return txtArtista.getText();
    }

    public String getDuracion() {
        return txtDuracion.getText();
    }
}
