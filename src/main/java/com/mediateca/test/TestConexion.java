package com.mediateca.test;

import com.mediateca.db.DatabaseConnection;

import com.mediateca.dao.LibroDAO;
import com.mediateca.dao.RevistaDAO;
import com.mediateca.dao.CdAudioDAO;
import com.mediateca.dao.DvdDAO;

import com.mediateca.model.Libro;
import com.mediateca.model.Revista;
import com.mediateca.model.CdAudio;
import com.mediateca.model.Dvd;

import java.sql.Connection;
import java.sql.Statement;

public class TestConexion {

    public static void main(String[] args) {

        System.out.println("===== INICIO DE PRUEBAS CRUD =====");

        DatabaseConnection.getInstancia().getConexion();

        resetTablas();

        // ========================= LIBRO =========================
        LibroDAO libroDAO = new LibroDAO();

        System.out.println("\n[LIBRO] INSERTANDO...");
        Libro libro1 = new Libro();
        libro1.setTitulo("El Quijote");
        libro1.setAutor("Miguel de Cervantes");
        libro1.setAnio(1605);
        libroDAO.insertar(libro1);

        System.out.println("\n[LIBRO] LISTANDO...");
        libroDAO.listar();

        System.out.println("\n[LIBRO] ACTUALIZANDO...");
        libroDAO.actualizar(libro1.getId(), libro1);

        System.out.println("\n[LIBRO] ELIMINANDO...");
        libroDAO.eliminar(libro1.getId());

        // ========================= REVISTA =========================
        RevistaDAO revistaDAO = new RevistaDAO();

        System.out.println("\n[REVISTA] INSERTANDO...");
        Revista revista1 = new Revista();
        revista1.setNombre("National Geographic");
        revista1.setEdicion(202);   // ✔ CORREGIDO
        revistaDAO.insertar(revista1);

        System.out.println("\n[REVISTA] LISTANDO...");
        revistaDAO.listar();

        System.out.println("\n[REVISTA] ACTUALIZANDO...");
        revistaDAO.actualizar(revista1.getId(), revista1);

        System.out.println("\n[REVISTA] ELIMINANDO...");
        revistaDAO.eliminar(revista1.getId());

        // ========================= CD AUDIO =========================
        CdAudioDAO cdDAO = new CdAudioDAO();

        System.out.println("\n[CD] INSERTANDO...");
        CdAudio cd = new CdAudio();
        cd.setArtista("Michael Jackson");
        cd.setDuracion(60);
        cdDAO.insertar(cd);

        System.out.println("\n[CD] LISTANDO...");
        cdDAO.listar();

        System.out.println("\n[CD] ACTUALIZANDO...");
        cdDAO.actualizar(cd.getId(), cd);

        System.out.println("\n[CD] ELIMINANDO...");
        cdDAO.eliminar(cd.getId());

        // ========================= DVD =========================
        DvdDAO dvdDAO = new DvdDAO();

        System.out.println("\n[DVD] INSERTANDO...");
        Dvd dvd = new Dvd();
        dvd.setDirector("Christopher Nolan");
        dvd.setDuracion(120);
        dvdDAO.insertar(dvd);

        System.out.println("\n[DVD] LISTANDO...");
        dvdDAO.listar();

        System.out.println("\n[DVD] ACTUALIZANDO...");
        dvdDAO.actualizar(dvd.getId(), dvd);

        System.out.println("\n[DVD] ELIMINANDO...");
        dvdDAO.eliminar(dvd.getId());

        System.out.println("\n===== FIN DE PRUEBAS =====");
    }

    // ========================= RESET TABLAS =========================
    private static void resetTablas() {
        try {
            Connection conn = DatabaseConnection.getInstancia().getConexion();
            Statement st = conn.createStatement();

            st.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");

            st.executeUpdate("TRUNCATE TABLE libro");
            st.executeUpdate("TRUNCATE TABLE revista");
            st.executeUpdate("TRUNCATE TABLE cdaudio");
            st.executeUpdate("TRUNCATE TABLE dvd");

            st.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("🧹 TABLAS REINICIADAS");

        } catch (Exception e) {
            System.out.println("❌ Error reset tablas: " + e.getMessage());
        }
    }
}