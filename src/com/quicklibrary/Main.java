package com.quicklibrary;

import com.quicklibrary.servicios.BibliotecaServicio;
import com.quicklibrary.ui.MenuConsola;
import com.quicklibrary.utilidades.LectorCSV;

/**
 * Clase de entrada principal del sistema QuickLibrary.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("     Iniciando Sistema QuickLibrary...   ");
        System.out.println("=========================================");

        // 1. Inicializar el servicio principal
        BibliotecaServicio servicio = new BibliotecaServicio();

        // 2. Cargar catálogo de 30 libros base
        // Se intenta cargar de 'libros.csv' en la raíz del proyecto.
        // Si no existe, LectorCSV cargará automáticamente los 30 libros por defecto.
        LectorCSV.cargarCatalogoInicial(servicio.getCatalogo(), "libros.csv");

        // 3. Lanzar la interfaz gráfica (GUI)
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel nativo del sistema operativo para mejor estética
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Ignorar si no se puede aplicar
            }
            new com.quicklibrary.ui.BibliotecaGUI(servicio).setVisible(true);
        });
    }
}
