package com.quicklibrary.ui;

import com.quicklibrary.modelos.EstadoLibro;
import com.quicklibrary.modelos.Libro;
import com.quicklibrary.modelos.SolicitudPrestamo;
import com.quicklibrary.servicios.BibliotecaServicio;
import com.quicklibrary.utilidades.Validador;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Vista de interfaz por consola para interactuar con el sistema QuickLibrary.
 */
public class MenuConsola {
    private final BibliotecaServicio servicio;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuConsola(BibliotecaServicio servicio) {
        this.servicio = servicio;
    }

    /**
     * Inicia el bucle principal del menú de la consola.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = Validador.leerEnteroRango("Seleccione una opción (1-16): ", 1, 16);
            procesarOpcion(opcion);
            } while (opcion != 16);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=========================================");
        System.out.println("              QUICKLIBRARY               ");
        System.out.println("=========================================");
        System.out.println("1. Registrar libro");
        System.out.println("2. Mostrar todos los libros");
        System.out.println("3. Buscar libro (Código / Título / Autor)");
        System.out.println("4. Buscar libros por categoría");
        System.out.println("5. Modificar libro");
        System.out.println("6. Eliminar libro");
        System.out.println("7. Registrar solicitud de préstamo");
        System.out.println("8. Mostrar cola de solicitudes");
        System.out.println("9. Ver siguiente solicitud en espera");
        System.out.println("10. Atender siguiente solicitud");
        System.out.println("11. Cancelar siguiente solicitud (sin atender)");
        System.out.println("12. Cancelar solicitud de un estudiante específico");
        System.out.println("13. Vaciar cola de solicitudes");
        System.out.println("14. Registrar devolución");
        System.out.println("15. Mostrar reporte");
        System.out.println("16. Salir");
        System.out.println("=========================================");
    }
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                ejecutarRegistrarLibro();
                break;
            case 2:
                servicio.mostrarTodosLosLibros();
                break;
            case 3:
                ejecutarSubmenuBusqueda();
                break;
            case 4:
                ejecutarBuscarPorCategoria();
                break;
            case 5:
                ejecutarModificarLibro();
                break;
            case 6:
                ejecutarEliminarLibro();
                break;
            case 7:
                ejecutarRegistrarSolicitud();
                break;
            case 8:
                servicio.mostrarColaSolicitudes();
                break;
            case 9:
                ejecutarVerSiguienteSolicitud();
                break;
            case 10:
                servicio.atenderSiguienteSolicitud();
                break;
            case 11:
                ejecutarCancelarSiguienteSolicitud();
                break;
            case 12:
                ejecutarCancelarSolicitudPorEstudiante();
                break;
            case 13:
                ejecutarVaciarColaSolicitudes();
                break;
            case 14:
                ejecutarRegistrarDevolucion();
                break;
            case 15:
                servicio.mostrarReporte();
                break;
            case 16:
                System.out.println("\nGracias por usar QuickLibrary. ¡Hasta pronto!");
                break;
        }
    }

    // ==========================================
    // Mapeo de Operaciones del Menú
    // ==========================================

    private void ejecutarRegistrarLibro() {
        System.out.println("\n--- REGISTRAR NUEVO LIBRO ---");
        int codigo = Validador.leerEntero("Ingrese el código del libro (entero): ");
        String titulo = Validador.leerCadena("Ingrese el título: ");
        String autor = Validador.leerCadena("Ingrese el autor principal: ");
        String categoria = Validador.leerCadena("Ingrese la categoría: ");
        int anio = Validador.leerAnio("Ingrese el año de publicación: ");

        Libro nuevo = new Libro(codigo, titulo, autor, categoria, anio, EstadoLibro.DISPONIBLE);
        if (servicio.registrarLibro(nuevo)) {
            System.out.println("Éxito: Libro registrado y almacenado correctamente en el ABB.");
        } else {
            System.out.println("Error: Ya existe un libro en el catálogo con el código " + codigo);
        }
    }

    private void ejecutarSubmenuBusqueda() {
        System.out.println("\n--- CRITERIO DE BÚSQUEDA ---");
        System.out.println("1. Buscar por Código (Árbol - O(log n))");
        System.out.println("2. Buscar por Título (Filtro - O(n))");
        System.out.println("3. Buscar por Autor (Filtro - O(n))");
        int criterio = Validador.leerEnteroRango("Seleccione criterio de búsqueda (1-3): ", 1, 3);

        switch (criterio) {
            case 1:
                int codigo = Validador.leerEntero("Ingrese el código a buscar: ");
                Libro libro = servicio.buscarLibroPorCodigo(codigo);
                if (libro != null) {
                    System.out.println("\nLibro Encontrado:");
                    System.out.println(libro);
                } else {
                    System.out.println("Error: No se encontró ningún libro con el código " + codigo);
                }
                break;
            case 2:
                String titulo = Validador.leerCadena("Ingrese parte del título a buscar: ");
                servicio.buscarLibrosPorTitulo(titulo);
                break;
            case 3:
                String autor = Validador.leerCadena("Ingrese parte del nombre del autor: ");
                servicio.buscarLibrosPorAutor(autor);
                break;
        }
    }

    private void ejecutarBuscarPorCategoria() {
        System.out.println("\n--- BUSCAR POR CATEGORÍA ---");
        String categoria = Validador.leerCadena("Ingrese la categoría: ");
        servicio.buscarLibrosPorCategoria(categoria);
    }

    private void ejecutarModificarLibro() {
        System.out.println("\n--- MODIFICAR DATOS DE LIBRO ---");
        int codigo = Validador.leerEntero("Ingrese el código del libro a modificar: ");
        Libro libro = servicio.buscarLibroPorCodigo(codigo);
        if (libro == null) {
            System.out.println("Error: El libro con el código especificado no existe.");
            return;
        }

        System.out.println("Datos actuales: " + libro);
        String titulo = Validador.leerCadena("Ingrese el nuevo título: ");
        String autor = Validador.leerCadena("Ingrese el nuevo autor: ");
        String categoria = Validador.leerCadena("Ingrese la nueva categoría: ");
        int anio = Validador.leerAnio("Ingrese el nuevo año de publicación: ");

        if (servicio.modificarLibro(codigo, titulo, autor, categoria, anio)) {
            System.out.println("Éxito: Los datos del libro han sido actualizados.");
        } else {
            System.out.println("Error: Ocurrió un fallo al intentar modificar el libro.");
        }
    }

    private void ejecutarEliminarLibro() {
        System.out.println("\n--- ELIMINAR LIBRO DEL CATÁLOGO ---");
        int codigo = Validador.leerEntero("Ingrese el código del libro a eliminar: ");
        Libro libro = servicio.buscarLibroPorCodigo(codigo);
        if (libro == null) {
            System.out.println("Error: El libro con el código especificado no existe.");
            return;
        }

        System.out.println("Se eliminará: " + libro.getTitulo());
        String confirmacion = Validador.leerCadena("¿Está seguro? (S/N): ");
        if (confirmacion.equalsIgnoreCase("S")) {
            if (servicio.eliminarLibro(codigo)) {
                System.out.println("Éxito: El libro ha sido retirado físicamente del ABB.");
            } else {
                System.out.println("Error: No se pudo eliminar el libro.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void ejecutarRegistrarSolicitud() {
        System.out.println("\n--- REGISTRAR SOLICITUD DE PRÉSTAMO ---");
        String codEstudiante = Validador.leerCodigoEstudiante("Ingrese el código del estudiante (8 caracteres alfanuméricos): ");
        String nomEstudiante = Validador.leerCadena("Ingrese el nombre completo del estudiante: ");
        int codLibro = Validador.leerEntero("Ingrese el código del libro solicitado: ");

        // Comprobación rápida para alertar al usuario si el libro no existe o no está disponible
        Libro libro = servicio.buscarLibroPorCodigo(codLibro);
        if (libro == null) {
            System.out.println("Advertencia: El libro solicitado no existe en el catálogo.");
        } else if (libro.getEstado() == EstadoLibro.PRESTADO) {
            System.out.println("Advertencia: El libro solicitado está actualmente prestado.");
        }

        String fecha = LocalDate.now().format(formatter);
        SolicitudPrestamo solicitud = new SolicitudPrestamo(codEstudiante, nomEstudiante, codLibro, fecha);
        servicio.registrarSolicitud(solicitud);
        System.out.println("Éxito: Solicitud agregada a la cola de espera de préstamos.");
    }

    private void ejecutarRegistrarDevolucion() {
        System.out.println("\n--- REGISTRAR DEVOLUCIÓN DE LIBRO ---");
        int codigo = Validador.leerEntero("Ingrese el código del libro devuelto: ");
        servicio.registrarDevolucion(codigo);
    }
    
    /**
     * Opción 9: Muestra la solicitud al frente de la cola sin retirarla (peek).
     */
    private void ejecutarVerSiguienteSolicitud() {
        System.out.println("\n--- SIGUIENTE SOLICITUD EN ESPERA ---");
        SolicitudPrestamo siguiente = servicio.consultarSiguienteSolicitud();
        if (siguiente == null) {
            System.out.println("No hay solicitudes pendientes en la cola.");
        } else {
            System.out.println(siguiente);
        }
    }
    
}
