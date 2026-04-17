package com.mediateca.vista.formularios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * - Clase base abstracta para los formularios de la mediateca.
 * - Proporciona un diseño consistente con botones de acción predefinidos.
 */
public abstract class MaterialForm extends JPanel {

    protected JPanel panelCampos;
    protected JButton btnGuardar;
    protected JButton btnLimpiar;
    protected JButton btnCancelar;
    private JLabel lblTitulo;

    public MaterialForm(String titulo) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cabecera del formulario
        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central para campos especificos (se definira en las subclases)
        panelCampos = new JPanel(new GridBagLayout());
        add(new JScrollPane(panelCampos), BorderLayout.CENTER);

        // Panel inferior de botones
        add(crearPanelBotones(), BorderLayout.SOUTH);

        configurarEventosBase();
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnLimpiar);
        panel.add(btnCancelar);

        return panel;
    }

    private void configurarEventosBase() {
        // El boton limpiar es comun para todos: vacia los campos
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    /**
     * - Metodo obligatorio para que cada formulario limpie sus propios campos de
     * texto.
     */
    public abstract void limpiarCampos();

    /**
     * - Permite a la VentanaPrincipal o al controlador asignar acciones a los
     * botones.
     */
    public void setAccionGuardar(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    public void setAccionCancelar(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    /**
     * - Utilidad para agregar etiquetas y componentes al panel central de forma
     * ordenada.
     */
    protected void agregarCampo(String etiqueta, Component componente, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelCampos.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panelCampos.add(componente, gbc);
    }
}
