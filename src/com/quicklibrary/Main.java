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

        // 3. Lanzar la interfaz de consola
        MenuConsola menu = new MenuConsola(servicio);
        menu.iniciar();

        // 4. Guardar catálogo al salir del programa
        System.out.println("\nGuardando cambios en el catálogo...");
        LectorCSV.guardarCatalogo(servicio.getCatalogo(), "libros.csv");
    }
}
