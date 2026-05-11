package com.mediateca;

import com.mediateca.db.DatabaseConnection;
import com.mediateca.vistas.Ventana_PPAL;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Punto de entrada de la aplicacion Mediateca.
 *
 * Responsabilidades:
 *   1. Configurar el Look & Feel (Nimbus si esta disponible).
 *   2. Verificar la conexion a la base de datos antes de mostrar la UI.
 *   3. Lanzar la ventana principal {@link Ventana_PPAL} en el Event Dispatch Thread.
 *
 * Nota: {@link DatabaseConnection} es un Singleton, por lo que las vistas y DAOs
 * obtienen la conexion bajo demanda; aqui solo se verifica al arranque para
 * fallar rapido y con un mensaje claro al usuario.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        configurarLookAndFeel();

        if (!verificarConexionBD()) {
            // Si no hay BD, no tiene sentido seguir: salimos limpiamente.
            System.exit(1);
        }

        // Toda construccion de Swing debe correr en el EDT
        SwingUtilities.invokeLater(() -> {
            Ventana_PPAL ventana = new Ventana_PPAL();
            ventana.setVisible(true);
        });
    }

    /**
     * Aplica el Look & Feel Nimbus si esta disponible. Si falla, deja el L&F
     * por defecto del sistema; no es un error fatal.
     */
    private static void configurarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.WARNING, "No se pudo aplicar Nimbus L&F, se usara el predeterminado", ex);
        }
    }

    /**
     * Verifica que la conexion a la BD se pueda establecer. Si falla, muestra
     * un dialogo de error y devuelve {@code false}.
     */
    private static boolean verificarConexionBD() {
        try {
            Connection conn = DatabaseConnection.getInstancia().getConexion();
            if (conn != null && !conn.isClosed()) {
                logger.info("Conexion a base de datos verificada correctamente.");
                return true;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al verificar conexion a BD", ex);
        }

        JOptionPane.showMessageDialog(
            null,
            "<html><b>No se pudo conectar a la base de datos.</b><br><br>"
            + "Verifica que:<br>"
            + " &nbsp;&nbsp;&bull; El servicio de MySQL este corriendo.<br>"
            + " &nbsp;&nbsp;&bull; La base de datos <i>mediateca</i> exista.<br>"
            + " &nbsp;&nbsp;&bull; Las credenciales en <i>config.properties</i> sean correctas.<br><br>"
            + "Revisa la consola para mas detalles.</html>",
            "Error de conexion - Mediateca",
            JOptionPane.ERROR_MESSAGE
        );
        return false;
    }
}
