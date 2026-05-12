/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import javax.swing.JOptionPane;
import com.mediateca.vistas.busquedas.Panel_BuscaPest;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class Panel_administrador extends javax.swing.JPanel {

    public Panel_administrador() {
        initComponents();
        cablearEventos();
    }

    private void cablearEventos() {
        btnGestionUsuarios.addActionListener(e -> abrirGestionUsuarios());
        btnGestionMateriales.addActionListener(e -> abrirGestionMateriales());
        boton_busca_prestam.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_BuscaPest()));
        boton_configadmin.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_Configadmin()));
        btnReporteDisponibilidad.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_ReporteDisponibilidad()));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void abrirGestionUsuarios() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_GestionUsuarios());
    }

    private void abrirGestionMateriales() {
        Ventana_PPAL.getInstancia().mostrar(new Panel_GestionMateriales());
    }

    private void cerrarSesion() {
        com.mediateca.util.SessionManager.cerrarSesion();
        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
        if (ventana != null) {
            ventana.mostrar(new Panel_Bienvenida());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlMenu = new javax.swing.JPanel();
        pnlContenido = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblBienvenida = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        boton_busca_prestam = new javax.swing.JButton();
        boton_configadmin = new javax.swing.JButton();
        btnGestionUsuarios = new javax.swing.JButton();
        btnGestionMateriales = new javax.swing.JButton();
        btnReporteDisponibilidad = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();

        setBackground(new Color(5, 15, 45));
        setLayout(new BorderLayout());

        pnlMenu.setBackground(new Color(5, 15, 45));
        pnlMenu.setPreferredSize(new Dimension(300, 720));
        pnlMenu.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 10, 25);

        javax.swing.JPanel panelSuperior = new javax.swing.JPanel();
        panelSuperior.setLayout(new GridBagLayout());
        panelSuperior.setBackground(new Color(5, 15, 45));
        GridBagConstraints gbcSuperior = new GridBagConstraints();
        gbcSuperior.gridx = 0;
        gbcSuperior.fill = GridBagConstraints.HORIZONTAL;
        gbcSuperior.insets = new Insets(10, 25, 10, 25);
        gbcSuperior.weightx = 1.0;
        gbcSuperior.gridy = 0;

        lblTitulo.setText("Mediateca Don Bosco");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 120, 255));
        panelSuperior.add(lblTitulo, gbcSuperior);

        gbcSuperior.gridy = 1;
        gbcSuperior.insets = new Insets(0, 25, 35, 25);
        lblSubtitulo.setText("Administrador");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        lblSubtitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblSubtitulo, gbcSuperior);

        gbcSuperior.insets = new Insets(10, 25, 10, 25);

        configurarBoton(btnGestionUsuarios, "Gestión de Usuarios");
        gbcSuperior.gridy = 2;
        panelSuperior.add(btnGestionUsuarios, gbcSuperior);

        configurarBoton(btnGestionMateriales, "Gestión de Materiales");
        gbcSuperior.gridy = 3;
        panelSuperior.add(btnGestionMateriales, gbcSuperior);

        configurarBoton(boton_busca_prestam, "Préstamos");
        gbcSuperior.gridy = 4;
        panelSuperior.add(boton_busca_prestam, gbcSuperior);

        configurarBoton(boton_configadmin, "Configuración");
        gbcSuperior.gridy = 5;
        panelSuperior.add(boton_configadmin, gbcSuperior);

        configurarBoton(btnReporteDisponibilidad, "📊 Ver Disponibilidad");
        gbcSuperior.gridy = 6;
        panelSuperior.add(btnReporteDisponibilidad, gbcSuperior);

        GridBagConstraints gbcGlue = new GridBagConstraints();
        gbcGlue.gridx = 0;
        gbcGlue.gridy = 7;
        gbcGlue.weighty = 1.0;
        gbcGlue.fill = GridBagConstraints.VERTICAL;
        panelSuperior.add(new javax.swing.JPanel(), gbcGlue);

        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        pnlMenu.add(panelSuperior, gbc);

        configurarBoton(btnCerrarSesion, "Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(204, 0, 0));
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 25, 25, 25);
        pnlMenu.add(btnCerrarSesion, gbc);

        add(pnlMenu, BorderLayout.WEST);

        pnlContenido.setBackground(new Color(5, 15, 45));
        pnlContenido.setLayout(new GridBagLayout());
        GridBagConstraints center = new GridBagConstraints();
        center.gridx = 0;
        center.insets = new Insets(15, 15, 15, 15);

        lblBienvenida.setText("Panel Administrativo");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 42));
        lblBienvenida.setForeground(Color.WHITE);
        center.gridy = 0;
        pnlContenido.add(lblBienvenida, center);

        lblDescripcion.setText(
                "<html><div style='text-align:center;'>"
                + "Bienvenido al sistema de gesti\u00f3n de la Mediateca Don Bosco.<br><br>"
                + "Desde este panel puede administrar usuarios, materiales,<br>"
                + "pr\u00e9stamos y configuraciones del sistema."
                + "</div></html>"
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 22));
        lblDescripcion.setForeground(new Color(220, 220, 220));
        center.gridy = 1;
        pnlContenido.add(lblDescripcion, center);

        add(pnlContenido, BorderLayout.CENTER);
    }

    private void configurarBoton(javax.swing.JButton boton, String texto) {
        boton.setText(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 102, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(220, 42));
    }

    // Variables declaration
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnGestionMateriales;
    private javax.swing.JButton btnGestionUsuarios;
    private javax.swing.JButton btnReporteDisponibilidad;
    private javax.swing.JButton boton_busca_prestam;
    private javax.swing.JButton boton_configadmin;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlMenu;
    // End of variables declaration
}