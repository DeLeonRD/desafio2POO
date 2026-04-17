package com.mediateca.vista.formularios;

import com.mediateca.util.ValidacionUtil;
import javax.swing.*;

/**
 * - Formulario específico para la gestión de Libros.
 * - Hereda de MaterialForm para mantener la consistencia visual y
 * comportamiento.
 */
public class LibroForm extends MaterialForm {

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnio;

    public LibroForm() {
        super("Registro de Nuevo Libro");
        inicializarCampos();
    }

    private void inicializarCampos() {
        txtId = new JTextField(10);
        txtTitulo = new JTextField(20);
        txtAutor = new JTextField(20);
        txtAnio = new JTextField(5);

        // Usamos la utilidad de la clase base para organizar los campos
        agregarCampo("ID Material:", txtId, 0);
        agregarCampo("Título del Libro:", txtTitulo, 1);
        agregarCampo("Autor:", txtAutor, 2);
        agregarCampo("Año de Publicación:", txtAnio, 3);
    }

    @Override
    public void limpiarCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
    }

    /**
     * - Valida los campos antes de proceder con el guardado.
     * - Implementa las reglas de negocio.
     */
    public boolean validarDatos() {
        if (ValidacionUtil.esTextoVacio(txtId.getText(), "ID", this))
            return false;
        if (ValidacionUtil.esTextoVacio(txtTitulo.getText(), "Título", this))
            return false;
        if (ValidacionUtil.esTextoVacio(txtAutor.getText(), "Autor", this))
            return false;
        if (!ValidacionUtil.esNumeroValido(txtAnio.getText(), "Año", this))
            return false;

        return true;
    }

    // Getters para que el controlador pueda extraer la informacion
    public String getId() {
        return txtId.getText();
    }

    public String getTitulo() {
        return txtTitulo.getText();
    }

    public String getAutor() {
        return txtAutor.getText();
    }

    public String getAnio() {
        return txtAnio.getText();
    }
}
