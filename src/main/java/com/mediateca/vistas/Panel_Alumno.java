/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.vistas.busquedas.Panel_BuscaPest;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 */
public class Panel_Alumno extends javax.swing.JPanel {

    public Panel_Alumno() {
        initComponents();
        cablearEventos();
        configurarPanelContenido();
    }

    private void cablearEventos() {
        boton_busca_prestam.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_BuscaPest()));
        btnReporteDisponibilidad.addActionListener(e -> Ventana_PPAL.getInstancia().mostrar(new Panel_ReporteDisponibilidad()));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void configurarPanelContenido() {
        // Configurar el panel derecho (jPanel2) con mensaje de bienvenida
        jPanel2.setBackground(new Color(8, 20, 55));
        jPanel2.setLayout(new GridBagLayout());
        
        // Panel interno para el contenido centrado
        JPanel pnlMensaje = new JPanel();
        pnlMensaje.setBackground(new Color(8, 20, 55));
        pnlMensaje.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título de bienvenida
        JLabel lblTituloBienvenida = new JLabel("📚 ¡Bienvenido a la Biblioteca!");
        lblTituloBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblTituloBienvenida.setForeground(Color.WHITE);
        lblTituloBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        pnlMensaje.add(lblTituloBienvenida, gbc);
        
        // Mensaje de bienvenida
        JLabel lblMensaje = new JLabel("<html><div style='text-align:center; width:500px;'>"
                + "Te damos la más cordial bienvenida a la Biblioteca Virtual de la Mediateca Don Bosco.<br><br>"
                + "Aquí encontrarás una amplia colección de libros, revistas, CD<br>"
                + "y materiales audiovisuales para apoyar tu formación académica."
                + "</div></html>");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMensaje.setForeground(new Color(220, 220, 220));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        pnlMensaje.add(lblMensaje, gbc);
        
        // Recomendación - Cuidado de materiales
        JLabel lblRecomendacion = new JLabel("<html><div style='text-align:center; width:500px;'>"
                + "📖 <b>Recomendación importante:</b><br><br>"
                + "Por favor, cuida los materiales que solicites en préstamo.<br>"
                + "Devuélvelos en buen estado y en la fecha establecida<br>"
                + "para que otros compañeros también puedan disfrutarlos.<br><br>"
                + "✨ <b>¡La biblioteca es de todos, cuidémosla juntos!</b> ✨"
                + "</div></html>");
        lblRecomendacion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblRecomendacion.setForeground(new Color(200, 200, 200));
        lblRecomendacion.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecomendacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        pnlMensaje.add(lblRecomendacion, gbc);
        
        // Horario de atención
        JLabel lblHorario = new JLabel("<html><div style='text-align:center;'>"
                + "🕐 <b>Horario de atención:</b><br>"
                + "Lunes a Viernes: 8:00 AM - 8:00 PM<br>"
                + "Sábados: 9:00 AM - 2:00 PM"
                + "</div></html>");
        lblHorario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblHorario.setForeground(new Color(150, 150, 150));
        lblHorario.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 15, 15, 15);
        pnlMensaje.add(lblHorario, gbc);
        
        GridBagConstraints center = new GridBagConstraints();
        center.gridx = 0;
        center.gridy = 0;
        center.fill = GridBagConstraints.BOTH;
        center.weightx = 1.0;
        center.weighty = 1.0;
        jPanel2.add(pnlMensaje, center);
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

        jPanel1 = new javax.swing.JPanel();
        boton_busca_prestam = new javax.swing.JButton();
        btnReporteDisponibilidad = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCerrarSesion = new javax.swing.JButton();

        setBackground(new java.awt.Color(11, 19, 43));
        setForeground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(11, 19, 43));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        boton_busca_prestam.setText("Mis Préstamos");
        boton_busca_prestam.setFont(new java.awt.Font("Arial", 0, 14));

        btnReporteDisponibilidad.setText("📊 Ver Disponibilidad");
        btnReporteDisponibilidad.setFont(new java.awt.Font("Arial", 0, 14));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mediateca Don Bosco");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 16));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Bienvenido Alumno");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 16));
        jLabel4.setForeground(new java.awt.Color(200, 200, 200));
        jLabel4.setText("Préstamos");

        btnCerrarSesion.setBackground(new java.awt.Color(204, 0, 0));
        btnCerrarSesion.setFont(new java.awt.Font("Arial", 1, 12));
        btnCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.setBorderPainted(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGap(10, 10, 10)
                    .addComponent(jLabel4)
                    .addComponent(boton_busca_prestam, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReporteDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addGap(5, 5, 5)
                .addComponent(boton_busca_prestam, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnReporteDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(11, 30, 78));
        jPanel2.setLayout(new GridBagLayout()); // Se configurará en configurarPanelContenido()

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    // Variables declaration
    private javax.swing.JButton boton_busca_prestam;
    private javax.swing.JButton btnReporteDisponibilidad;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration
}