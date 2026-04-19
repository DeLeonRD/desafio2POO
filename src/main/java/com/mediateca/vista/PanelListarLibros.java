package com.mediateca.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.mediateca.control.LibroController;
import com.mediateca.model.Libro;

public class PanelListarLibros extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private LibroController controller;

    public PanelListarLibros() {

        controller = new LibroController();
        setLayout(new BorderLayout());

        // cl
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Autor");
        modelo.addColumn("Año");

        // tb
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // bt n
        JButton btn = new JButton("Cargar datos");
        add(btn, BorderLayout.SOUTH);

        btn.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {

        modelo.setRowCount(0); // pa limp tl

        List<Libro> lista = controller.listarLibros();

        for (Libro l : lista) {
            modelo.addRow(new Object[]{
                l.getId(),
                l.getTitulo(),
                l.getAutor(),
                l.getAnio()
            });
        }
    }
}