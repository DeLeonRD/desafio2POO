/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mediateca.vistas;

import com.mediateca.dao.UsuarioDAO;
import com.mediateca.model.Usuario;
import com.mediateca.util.SessionManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Panel_Bienvenida extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(Panel_Bienvenida.class.getName());

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Panel_Bienvenida() {

        initComponents();

        cablearEventos();

        lblMensajeError.setText(" ");

        cargarLogo();
    }

    // =====================================================
    // CARGAR LOGO
    // =====================================================

    private void cargarLogo() {

        try {

            URL logoUrl = getClass().getResource("/imagenes/Logo UDB.png");

            if (logoUrl != null) {

                ImageIcon logoIcon = new ImageIcon(logoUrl);

                Image logo = logoIcon.getImage().getScaledInstance(
                        220,
                        220,
                        Image.SCALE_SMOOTH
                );

                lblLogo.setIcon(new ImageIcon(logo));

                lblLogo.setText("");

            } else {

                lblLogo.setText("📚");

                lblLogo.setFont(new java.awt.Font("Segoe UI Emoji", 1, 80));
            }

        } catch (Exception e) {

            lblLogo.setText("📚");

            lblLogo.setFont(new java.awt.Font("Segoe UI Emoji", 1, 80));
        }
    }

    // =====================================================
    // EVENTOS
    // =====================================================

    private void cablearEventos() {

        btnAcceder.addActionListener(e -> hacerLogin());

        txtPassword.addActionListener(e -> hacerLogin());
    }

    // =====================================================
    // LOGIN
    // =====================================================

    private void hacerLogin() {

        String email = txtUsuario.getText().trim();

        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            lblMensajeError.setText("Ingrese correo y contraseña.");

            return;
        }

        try {

            if (!usuarioDAO.validarCredenciales(email, password)) {

                lblMensajeError.setText("Credenciales incorrectas.");

                txtPassword.setText("");

                return;
            }

            Usuario u = usuarioDAO.obtenerPorEmail(email);

            if (u == null) {

                lblMensajeError.setText("No se pudo cargar el usuario.");

                return;
            }

            SessionManager.iniciarSesion(u);

            lblMensajeError.setText(" ");

            txtPassword.setText("");

            navegarSegunTipo(u);

        } catch (Exception ex) {

            logger.log(Level.SEVERE, "Error al hacer login", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // NAVEGACION
    // =====================================================

    private void navegarSegunTipo(Usuario u) {

        Ventana_PPAL ventana = Ventana_PPAL.getInstancia();

        if (ventana == null) {
            return;
        }

        String tipo = u.getTipo() == null
                ? ""
                : u.getTipo().toUpperCase();

        switch (tipo) {

            case "ADMIN":

                ventana.mostrar(new Panel_administrador());

                break;

            case "EMPLEADO":

            case "PROFESOR":

                ventana.mostrar(new Panel_Docente());

                break;

            case "ALUMNO":

                ventana.mostrar(new Panel_Alumno());

                break;

            default:

                lblMensajeError.setText("Rol no reconocido: " + tipo);

                SessionManager.cerrarSesion();
        }
    }

    // =====================================================
    // COMPONENTES
    // =====================================================

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // =====================================================
        // COMPONENTES
        // =====================================================

        pnlPrincipal = new javax.swing.JPanel();

        pnlFormulario = new javax.swing.JPanel();

        lblLogo = new javax.swing.JLabel();

        lblTituloSistema = new javax.swing.JLabel();

        lblTituloBienvenida = new javax.swing.JLabel();

        lblSubtitulo = new javax.swing.JLabel();

        txtDescripcion = new javax.swing.JTextArea();

        lblIniciarSesion = new javax.swing.JLabel();

        lblUsuario = new javax.swing.JLabel();

        txtUsuario = new javax.swing.JTextField();

        lblPassword = new javax.swing.JLabel();

        txtPassword = new javax.swing.JPasswordField();

        btnAcceder = new javax.swing.JButton();

        lblMensajeError = new javax.swing.JLabel();

        // =====================================================
        // PANEL PRINCIPAL
        // =====================================================

        setBackground(new java.awt.Color(5, 15, 45));

        setLayout(new BorderLayout());

        pnlPrincipal.setBackground(new java.awt.Color(5, 15, 45));

        pnlPrincipal.setLayout(new GridLayout(1, 2));

        // =====================================================
        // PANEL IZQUIERDO
        // =====================================================

        javax.swing.JPanel pnlIzquierdo = new javax.swing.JPanel();

        pnlIzquierdo.setBackground(new java.awt.Color(5, 15, 45));

        pnlIzquierdo.setLayout(new GridBagLayout());

        GridBagConstraints left = new GridBagConstraints();

        left.gridx = 0;

        left.fill = GridBagConstraints.HORIZONTAL;

        left.anchor = GridBagConstraints.WEST;

        left.insets = new Insets(10, 70, 10, 40);

        // =====================================================
        // LOGO
        // =====================================================

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        left.gridy = 0;

        pnlIzquierdo.add(lblLogo, left);

        // =====================================================
        // TITULO SISTEMA
        // =====================================================

        lblTituloSistema.setFont(new java.awt.Font("Arial", 0, 26));

        lblTituloSistema.setForeground(java.awt.Color.WHITE);

        lblTituloSistema.setText("Mediateca Don Bosco");

        left.gridy = 1;

        left.insets = new Insets(0, 70, 30, 40);

        pnlIzquierdo.add(lblTituloSistema, left);

        // =====================================================
        // BIENVENIDA
        // =====================================================

        lblTituloBienvenida.setFont(new java.awt.Font("Arial", 1, 50));

        lblTituloBienvenida.setForeground(java.awt.Color.WHITE);

        lblTituloBienvenida.setText("¡Bienvenido a tu");

        left.gridy = 2;

        left.insets = new Insets(25, 70, 5, 40);

        pnlIzquierdo.add(lblTituloBienvenida, left);

        // =====================================================
        // SUBTITULO
        // =====================================================

        lblSubtitulo.setFont(new java.awt.Font("Arial", 1, 50));

        lblSubtitulo.setForeground(new java.awt.Color(0, 102, 255));

        lblSubtitulo.setText("Sistema de Préstamos!");

        left.gridy = 3;

        left.insets = new Insets(0, 70, 35, 40);

        pnlIzquierdo.add(lblSubtitulo, left);

        // =====================================================
        // DESCRIPCION
        // =====================================================

        txtDescripcion.setBackground(new java.awt.Color(5, 15, 45));

        txtDescripcion.setForeground(java.awt.Color.WHITE);

        txtDescripcion.setFont(new java.awt.Font("Arial", 0, 20));

        txtDescripcion.setEditable(false);

        txtDescripcion.setBorder(
                BorderFactory.createLineBorder(
                        new java.awt.Color(70, 70, 70)
                )
        );

        txtDescripcion.setLineWrap(true);

        txtDescripcion.setWrapStyleWord(true);

        txtDescripcion.setText(
                "Gestione préstamos de libros, revistas,\n"
                + "CDs, DVDs y más. Controle devoluciones,\n"
                + "calcule moras y administre su mediateca\n"
                + "de manera eficiente."
        );

        txtDescripcion.setPreferredSize(new Dimension(540, 180));

        left.gridy = 4;

        left.insets = new Insets(0, 70, 20, 40);

        pnlIzquierdo.add(txtDescripcion, left);

        // =====================================================
        // PANEL DERECHO
        // =====================================================

        javax.swing.JPanel pnlDerecho = new javax.swing.JPanel();

        pnlDerecho.setBackground(new java.awt.Color(5, 15, 45));

        pnlDerecho.setLayout(new GridBagLayout());

        GridBagConstraints right = new GridBagConstraints();

        // =====================================================
        // PANEL FORMULARIO LOGIN
        // =====================================================

        pnlFormulario.setBackground(new java.awt.Color(5, 15, 45));

        pnlFormulario.setBorder(
                BorderFactory.createLineBorder(
                        new java.awt.Color(0, 102, 255),
                        2
                )
        );

        pnlFormulario.setPreferredSize(new Dimension(760, 450));

        pnlFormulario.setLayout(new GridBagLayout());

        GridBagConstraints form = new GridBagConstraints();

        form.insets = new Insets(22, 30, 22, 30);

        form.anchor = GridBagConstraints.WEST;

        form.fill = GridBagConstraints.HORIZONTAL;

        form.weightx = 1.0;

        // =====================================================
        // TITULO LOGIN
        // =====================================================

        lblIniciarSesion.setFont(new java.awt.Font("Arial", 1, 44));

        lblIniciarSesion.setForeground(java.awt.Color.WHITE);

        lblIniciarSesion.setText("Iniciar Sesión");

        form.gridx = 0;

        form.gridy = 0;

        form.gridwidth = 2;

        pnlFormulario.add(lblIniciarSesion, form);

        // =====================================================
        // LABEL USUARIO
        // =====================================================

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 22));

        lblUsuario.setForeground(java.awt.Color.WHITE);

        lblUsuario.setText("Correo Institucional");

        form.gridx = 0;

        form.gridy = 1;

        form.gridwidth = 1;

        pnlFormulario.add(lblUsuario, form);

        // =====================================================
        // TEXTFIELD USUARIO
        // =====================================================

        txtUsuario.setFont(new java.awt.Font("Arial", 0, 18));

        txtUsuario.setPreferredSize(new Dimension(420, 50));

        form.gridx = 1;

        pnlFormulario.add(txtUsuario, form);

        // =====================================================
        // LABEL PASSWORD
        // =====================================================

        lblPassword.setFont(new java.awt.Font("Arial", 0, 22));

        lblPassword.setForeground(java.awt.Color.WHITE);

        lblPassword.setText("Contraseña");

        form.gridx = 0;

        form.gridy = 2;

        pnlFormulario.add(lblPassword, form);

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        txtPassword.setFont(new java.awt.Font("Arial", 0, 18));

        txtPassword.setPreferredSize(new Dimension(420, 50));

        form.gridx = 1;

        pnlFormulario.add(txtPassword, form);

        // =====================================================
        // BOTON
        // =====================================================

        btnAcceder.setBackground(new java.awt.Color(0, 120, 255));

        btnAcceder.setForeground(java.awt.Color.WHITE);

        btnAcceder.setFont(new java.awt.Font("Arial", 1, 24));

        btnAcceder.setText("ACCEDER");

        btnAcceder.setFocusPainted(false);

        btnAcceder.setBorderPainted(false);

        btnAcceder.setPreferredSize(new Dimension(420, 55));

        form.gridx = 1;

        form.gridy = 3;

        form.insets = new Insets(35, 30, 20, 30);

        pnlFormulario.add(btnAcceder, form);

        // =====================================================
        // MENSAJE ERROR
        // =====================================================

        lblMensajeError.setForeground(new java.awt.Color(255, 120, 120));

        lblMensajeError.setFont(new java.awt.Font("Arial", 0, 14));

        form.gridy = 4;

        pnlFormulario.add(lblMensajeError, form);

        // =====================================================
        // AGREGAR FORMULARIO
        // =====================================================

        pnlDerecho.add(pnlFormulario, right);

        // =====================================================
        // AGREGAR TODO
        // =====================================================

        pnlPrincipal.add(pnlIzquierdo);

        pnlPrincipal.add(pnlDerecho);

        add(pnlPrincipal, BorderLayout.CENTER);
    }

    // =====================================================
    // VARIABLES
    // =====================================================

    private javax.swing.JButton btnAcceder;

    private javax.swing.JLabel lblIniciarSesion;

    private javax.swing.JLabel lblLogo;

    private javax.swing.JLabel lblMensajeError;

    private javax.swing.JLabel lblPassword;

    private javax.swing.JLabel lblSubtitulo;

    private javax.swing.JLabel lblTituloBienvenida;

    private javax.swing.JLabel lblTituloSistema;

    private javax.swing.JLabel lblUsuario;

    private javax.swing.JPanel pnlFormulario;

    private javax.swing.JPanel pnlPrincipal;

    private javax.swing.JTextArea txtDescripcion;

    private javax.swing.JPasswordField txtPassword;

    private javax.swing.JTextField txtUsuario;
}