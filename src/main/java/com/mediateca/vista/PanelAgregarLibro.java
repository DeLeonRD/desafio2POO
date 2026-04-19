package com.mediateca.vista;

import javax.swing.*;
import java.awt.*;
import com.mediateca.control.LibroController;

public class PanelAgregarLibro extends JPanel {

    private JTextField txtTitulo, txtAutor, txtAnio;
    private LibroController controller;

    public PanelAgregarLibro() {

        controller = new LibroController();
        setLayout(new GridLayout(4,2));

        add(new JLabel("Título"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Autor"));
        txtAutor = new JTextField();
        add(txtAutor);

        add(new JLabel("Año"));
        txtAnio = new JTextField();
        add(txtAnio);

        JButton btn = new JButton("Guardar");
        add(btn);

        btn.addActionListener(e ->
            controller.guardarLibro(
                txtTitulo.getText(),
                txtAutor.getText(),
                txtAnio.getText()
            )
        );
    }
}