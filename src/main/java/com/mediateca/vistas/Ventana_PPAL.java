/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mediateca.vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import javax.swing.JPanel;

/**
 *
 * @author Francisco De la O Gonzalez - DG200722
 */
public class Ventana_PPAL extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Ventana_PPAL.class.getName());

    // =====================================================
    // INSTANCIA GLOBAL
    // =====================================================

    private static Ventana_PPAL instancia;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Ventana_PPAL() {

        initComponents();

        instancia = this;

        // Centrar ventana
        setLocationRelativeTo(null);

        // Layout principal
        getContentPane().setLayout(new BorderLayout());

        // Layout del panel contenedor
        jPanel1.setLayout(new BorderLayout());

        // Mostrar login al iniciar
        mostrar(new Panel_Bienvenida());
    }

    // =====================================================
    // OBTENER INSTANCIA
    // =====================================================

    public static Ventana_PPAL getInstancia() {

        return instancia;
    }

    // =====================================================
    // CAMBIAR PANEL
    // =====================================================

    public void mostrar(JPanel panel) {

        jPanel1.removeAll();

        jPanel1.add(panel, BorderLayout.CENTER);

        jPanel1.revalidate();

        jPanel1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setTitle("BIBLIOTECA VIRTUAL_UDB");

        // CAMBIADO: de 11,19,43 a 5,15,45
        setBackground(new Color(5, 15, 45));

        setMinimumSize(new java.awt.Dimension(1280, 720));

        setPreferredSize(new java.awt.Dimension(1280, 720));

        setResizable(false);

        // =====================================================
        // PANEL PRINCIPAL
        // =====================================================

        // CAMBIADO: de 11,19,43 a 5,15,45
        jPanel1.setBackground(new Color(5, 15, 45));

        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));

        javax.swing.GroupLayout jPanel1Layout =
                new javax.swing.GroupLayout(jPanel1);

        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                .addGap(0, 1280, Short.MAX_VALUE)
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                .addGap(0, 720, Short.MAX_VALUE)
        );

        // =====================================================
        // LAYOUT GENERAL
        // =====================================================

        javax.swing.GroupLayout layout =
                new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                .addComponent(
                        jPanel1,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE
                )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                .addComponent(
                        jPanel1,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE
                )
        );

        pack();
    }// </editor-fold>

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // =====================================================
        // LOOK AND FEEL
        // =====================================================

        try {

            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {

                    javax.swing.UIManager.setLookAndFeel(
                            info.getClassName()
                    );

                    break;
                }
            }

        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {

            logger.log(Level.SEVERE, null, ex);
        }

        // =====================================================
        // EJECUTAR VENTANA
        // =====================================================

        java.awt.EventQueue.invokeLater(() -> {

            new Ventana_PPAL().setVisible(true);

        });
    }

    // =====================================================
    // VARIABLES
    // =====================================================

    // Variables declaration - do not modify
    private javax.swing.JPanel jPanel1;
    // End of variables declaration
}