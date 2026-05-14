package com.mediateca.vistas;

import com.mediateca.service.UsuarioService;
import com.mediateca.model.Usuario;
import com.mediateca.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Panel_Bienvenida extends javax.swing.JPanel {

    private int intentos = 0;
    private static final int MAX_INTENTOS = 3;

    public Panel_Bienvenida() {
        initComponents();
        configurarEventos();
        limpiar();
    }

    private void configurarEventos() {
        btnLogin.addActionListener(e -> hacerLogin());
        btnLimpiar.addActionListener(e -> limpiar());

        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    hacerLogin();
                }
            }
        });

        txtEmail.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                }
            }
        });
    }

    private void hacerLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("⚠️ Email y contraseña son requeridos");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        btnLogin.setEnabled(false);

        try {
            if (UsuarioService.validarCredenciales(email, password)) {
                Usuario usuario = UsuarioService.obtenerPorEmail(email);
                SessionManager.iniciarSesion(usuario);

                JOptionPane.showMessageDialog(this, 
                    "Bienvenido " + usuario.getNombre(),
                    "Login Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);

                Ventana_PPAL ventana = Ventana_PPAL.getInstancia();
                if (ventana != null) {
                    switch (usuario.getTipo()) {
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
                            ventana.mostrar(new Panel_administrador());
                    }
                }
            } else {
                intentos++;
                int restantes = MAX_INTENTOS - intentos;
                lblMensaje.setText("❌ Credenciales incorrectas. Intentos restantes: " + restantes);
                lblMensaje.setForeground(Color.RED);
                txtPassword.setText("");
                txtPassword.requestFocus();

                if (intentos >= MAX_INTENTOS) {
                    JOptionPane.showMessageDialog(this, 
                        "Ha superado el número máximo de intentos. La aplicación se cerrará.",
                        "Acceso Denegado", 
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error de conexión: " + ex.getMessage());
            lblMensaje.setForeground(Color.RED);
            ex.printStackTrace();
        } finally {
            btnLogin.setEnabled(true);
        }
    }

    private void limpiar() {
        txtEmail.setText("");
        txtPassword.setText("");
        lblMensaje.setText("");
        intentos = 0;
        txtEmail.requestFocus();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblMensaje = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new Color(5, 15, 45));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        jPanel1.setBackground(new Color(8, 20, 55));
        jPanel1.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        jPanel1.setPreferredSize(new Dimension(400, 350));
        jPanel1.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.insets = new Insets(10, 20, 10, 20);

        jLabel4.setFont(new Font("Arial", Font.BOLD, 22));
        jLabel4.setForeground(Color.WHITE);
        jLabel4.setText("MEDIATECA DON BOSCO");
        g.gridy = 0;
        jPanel1.add(jLabel4, g);

        jLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setText("Email:");
        g.gridy = 1;
        jPanel1.add(jLabel1, g);

        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(280, 35));
        g.gridy = 2;
        jPanel1.add(txtEmail, g);

        jLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setText("Contraseña:");
        g.gridy = 3;
        jPanel1.add(jLabel2, g);

        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(280, 35));
        g.gridy = 4;
        jPanel1.add(txtPassword, g);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlBotones.setBackground(new Color(8, 20, 55));

        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setText("INGRESAR");
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        pnlBotones.add(btnLogin);

        btnLimpiar.setBackground(new Color(102, 102, 102));
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.setPreferredSize(new Dimension(120, 40));
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setFocusPainted(false);
        pnlBotones.add(btnLimpiar);

        g.gridy = 5;
        jPanel1.add(pnlBotones, g);

        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 6;
        jPanel1.add(lblMensaje, g);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(jPanel1, gbc);
    }

    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
}
