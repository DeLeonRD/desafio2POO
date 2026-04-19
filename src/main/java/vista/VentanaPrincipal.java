package vista;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mediateca.model.Material; // Importación crítica añadida
import com.mediateca.vista.formularios.*;
import com.mediateca.vista.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelCentral;

    public VentanaPrincipal() {
        setTitle("Mediateca - Sistema de Gestion de Materiales");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ============================
        // CardLayout - Panel Central
        // ============================
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Paneles
        panelCentral.add(crearPanelInicio(), "INICIO");

        // Instanciar formularios para configurar sus botones
        LibroForm libForm = new LibroForm();
        RevistaForm revForm = new RevistaForm();
        CdAudioForm cdForm = new CdAudioForm();
        DvdForm dvdForm = new DvdForm();

        // Configurar navegación y validación para cada uno
        configurarEventosFormulario(libForm);
        configurarEventosFormulario(revForm);
        configurarEventosFormulario(cdForm);
        configurarEventosFormulario(dvdForm);

        panelCentral.add(libForm, "AGREGAR_LIBRO");
        panelCentral.add(revForm, "AGREGAR_REVISTA");
        panelCentral.add(cdForm, "AGREGAR_CD");
        panelCentral.add(dvdForm, "AGREGAR_DVD");

        // Panel de búsqueda y listado
        BusquedaPanel busquedaPanel = new BusquedaPanel();

        // Configurar botón "Limpiar Búsqueda"
        busquedaPanel.setAccionActualizar(e -> {
            busquedaPanel.limpiarBusqueda();
            // Nota para De Leon: Aquí se debe recargar el MaterialTableModel con todos los datos del DAO.
        });

        // Configurar botón "Editar Seleccionado" (Flujo: Tabla -> Formulario)
        busquedaPanel.setAccionEditar(e -> {
            Material seleccionado = busquedaPanel.getMaterialSeleccionado();
            if (seleccionado != null) {
                JOptionPane.showMessageDialog(this, 
                    "Editando: " + seleccionado.getId() + "\n(De Leon debe mapear los datos al formulario correspondiente)");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un material de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Configurar botón "Eliminar Seleccionado" (Flujo: Tabla -> DAO)
        busquedaPanel.setAccionEliminar(e -> {
            Material seleccionado = busquedaPanel.getMaterialSeleccionado();
            if (seleccionado != null) {
                int r = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el material " + seleccionado.getId() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    com.mediateca.control.LibroController controller = new com.mediateca.control.LibroController();
                    controller.eliminarLibro(seleccionado.getId());
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un material de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            }
        });

        panelCentral.add(busquedaPanel, "BUSCAR");

        add(panelCentral, BorderLayout.CENTER);

        // ============================
        // Menu Bar
        // ============================
        setJMenuBar(crearMenuBar());

        // Mostrar panel de inicio por defecto
        cardLayout.show(panelCentral, "INICIO");
    }

    // ============================
    // Crear el JMenuBar
    // ============================
    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menu Material ---
        JMenu menuMaterial = new JMenu("Material");

        JMenu subMenuAgregar = new JMenu("Agregar Nuevo");
        JMenuItem itemAddLibro = new JMenuItem("Libro");
        JMenuItem itemAddRevista = new JMenuItem("Revista");
        JMenuItem itemAddCD = new JMenuItem("CD Audio");
        JMenuItem itemAddDVD = new JMenuItem("DVD");

        subMenuAgregar.add(itemAddLibro);
        subMenuAgregar.add(itemAddRevista);
        subMenuAgregar.add(itemAddCD);
        subMenuAgregar.add(itemAddDVD);

        JMenuItem itemListar = new JMenuItem("Listar / Buscar");

        // Eventos - cambiar panel con CardLayout
        itemAddLibro.addActionListener(e -> cardLayout.show(panelCentral, "AGREGAR_LIBRO"));
        itemAddRevista.addActionListener(e -> cardLayout.show(panelCentral, "AGREGAR_REVISTA"));
        itemAddCD.addActionListener(e -> cardLayout.show(panelCentral, "AGREGAR_CD"));
        itemAddDVD.addActionListener(e -> cardLayout.show(panelCentral, "AGREGAR_DVD"));

        itemListar.addActionListener(e -> cardLayout.show(panelCentral, "BUSCAR"));

        menuMaterial.add(subMenuAgregar);
        menuMaterial.add(itemListar);

        // --- Menu Salir ---
        JMenu menuSalir = new JMenu("Opciones");

        JMenuItem itemInicio = new JMenuItem("Inicio");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemInicio.addActionListener(e -> cardLayout.show(panelCentral, "INICIO"));
        itemSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Esta seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuSalir.add(itemInicio);
        menuSalir.addSeparator();
        menuSalir.add(itemSalir);

        menuBar.add(menuMaterial);
        menuBar.add(menuSalir);

        return menuBar;
    }

    // ============================
    // Panel de Inicio (bienvenida)
    // ============================
    /**
     * - Conecta los botones de los formularios con la lógica de la ventana
     * - (Integración UI).
     * - Nota: El guardado real en DB lo integrará De Leon en la Fase 5.
     */
    private void configurarEventosFormulario(MaterialForm form) {
        // Al cancelar, volvemos al inicio
form.setAccionGuardar(e -> {
    if (form.validarDatos()) {

        // SOLO para LibroForm
        if (form instanceof LibroForm) {
            LibroForm libroForm = (LibroForm) form;

            com.mediateca.control.LibroController controller = new com.mediateca.control.LibroController();

            controller.guardarLibro(
                libroForm.getTxtTitulo().getText(),
                libroForm.getTxtAutor().getText(),
                libroForm.getTxtAnio().getText()
            );
        }

        JOptionPane.showMessageDialog(this,
                "Guardado correctamente",
                "Sistema",
                JOptionPane.INFORMATION_MESSAGE);

        form.limpiarCampos();
    }
        });
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Bienvenido a la Mediateca");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(31, 78, 121));
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestion de Materiales");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        JLabel lblInstruccion = new JLabel("Use el menu superior para navegar entre las opciones");
        lblInstruccion.setFont(new Font("Arial", Font.ITALIC, 13));
        lblInstruccion.setForeground(new Color(150, 150, 150));
        gbc.gridy = 2;
        panel.add(lblInstruccion, gbc);

        return panel;
    }

    // ============================
    // Panel placeholder (temporal)
    // Aqui despues se reemplazan con los paneles reales
    // ============================
    private JPanel crearPanelPlaceholder(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(new Color(46, 117, 182));
        panel.add(lbl, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("Panel en construccion...", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblInfo.setForeground(new Color(180, 180, 180));
        panel.add(lblInfo, BorderLayout.SOUTH);

        return panel;
    }

    // ============================
    // Metodo para reemplazar paneles placeholder
    // por los paneles reales cuando esten listos
    // ============================
    public void reemplazarPanel(String nombre, JPanel nuevoPanel) {
        // Buscar y remover el panel existente con ese nombre
        for (Component comp : panelCentral.getComponents()) {
            // No hay forma directa de obtener el nombre en CardLayout,
            // asi que removemos y volvemos a agregar
        }
        panelCentral.add(nuevoPanel, nombre);
        cardLayout.show(panelCentral, nombre);
    }

    // ============================
    // Getters para acceso externo
    // ============================
    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getPanelCentral() {
        return panelCentral;
    }

    // ============================
    // Main
    // ============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
