package com.quicklibrary.servicios;

import com.quicklibrary.estructuras.ArbolBinarioBusqueda;
import com.quicklibrary.estructuras.Cola;
import com.quicklibrary.modelos.EstadoLibro;
import com.quicklibrary.modelos.Libro;
import com.quicklibrary.modelos.SolicitudPrestamo;

/**
 * Controlador central que gestiona las operaciones lógicas de la biblioteca.
 * Vincula el Árbol Binario de Búsqueda (catálogo) con la Cola (solicitudes).
 */
public class BibliotecaServicio {
    private final ArbolBinarioBusqueda<Libro> catalogo;
    private final Cola<SolicitudPrestamo> colaSolicitudes;

    public BibliotecaServicio() {
        this.catalogo = new ArbolBinarioBusqueda<>();
        this.colaSolicitudes = new Cola<>();
    }

    public ArbolBinarioBusqueda<Libro> getCatalogo() {
        return catalogo;
    }

    public Cola<SolicitudPrestamo> getColaSolicitudes() {
        return colaSolicitudes;
    }

    // ==========================================
    // RF01. Gestión de Libros
    // ==========================================

    /**
     * Registra un libro nuevo en el catálogo.
     */
    public boolean registrarLibro(Libro libro) {
        if (buscarLibroPorCodigo(libro.getCodigo()) != null) {
            return false; // Código duplicado
        }
        catalogo.insertar(libro);
        return true;
    }

    /**
     * Muestra todos los libros almacenados en el catálogo (Recorrido Inorden).
     */
    public void mostrarTodosLosLibros() {
        if (catalogo.isEmpty()) {
            System.out.println("No hay libros registrados en el catálogo.");
            return;
        }
        System.out.println("\n--- LISTADO DE LIBROS (ORDENADO POR CÓDIGO) ---");
        catalogo.recorridoInorden(libro -> System.out.println(libro));
    }

    /**
     * Modifica los datos de un libro existente.
     */
    public boolean modificarLibro(int codigo, String nuevoTitulo, String nuevoAutor, String nuevaCategoria, int nuevoAnio) {
        Libro libro = buscarLibroPorCodigo(codigo);
        if (libro != null) {
            libro.setTitulo(nuevoTitulo);
            libro.setAutor(nuevoAutor);
            libro.setCategoria(nuevaCategoria);
            libro.setAnio(nuevoAnio);
            return true;
        }
        return false;
    }

    /**
     * Elimina un libro del catálogo.
     */
    public boolean eliminarLibro(int codigo) {
        Libro libro = buscarLibroPorCodigo(codigo);
        if (libro != null) {
            catalogo.eliminar(libro);
            return true;
        }
        return false;
    }

    /**
     * Muestra los libros disponibles.
     */
    public void mostrarLibrosDisponibles() {
        System.out.println("\n--- LIBROS DISPONIBLES ---");
        boolean[] tieneElementos = {false};
        catalogo.recorridoInorden(libro -> {
            if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
                System.out.println(libro);
                tieneElementos[0] = true;
            }
        });
        if (!tieneElementos[0]) {
            System.out.println("No hay libros disponibles en este momento.");
        }
    }

    /**
     * Muestra los libros prestados.
     */
    public void mostrarLibrosPrestados() {
        System.out.println("\n--- LIBROS PRESTADOS ---");
        boolean[] tieneElementos = {false};
        catalogo.recorridoInorden(libro -> {
            if (libro.getEstado() == EstadoLibro.PRESTADO) {
                System.out.println(libro);
                tieneElementos[0] = true;
            }
        });
        if (!tieneElementos[0]) {
            System.out.println("No hay libros prestados actualmente.");
        }
    }

    // ==========================================
    // RF02. Búsqueda de Libros
    // ==========================================

    /**
     * Busca un libro por su código utilizando el ABB.
     */
    public Libro buscarLibroPorCodigo(int codigo) {
        return catalogo.buscar(new Libro(codigo));
    }

    /**
     * Busca y muestra libros por coincidencia en el título.
     */
    public void buscarLibrosPorTitulo(String titulo) {
        System.out.printf("%n--- RESULTADOS PARA TÍTULO: \"%s\" ---%n", titulo);
        String termino = titulo.toLowerCase();
        boolean[] encontrado = {false};
        catalogo.recorridoInorden(libro -> {
            if (libro.getTitulo().toLowerCase().contains(termino)) {
                System.out.println(libro);
                encontrado[0] = true;
            }
        });
        if (!encontrado[0]) {
            System.out.println("No se encontraron libros que coincidan con ese título.");
        }
    }

    /**
     * Busca y muestra libros por coincidencia en el autor.
     */
    public void buscarLibrosPorAutor(String autor) {
        System.out.printf("%n--- RESULTADOS PARA AUTOR: \"%s\" ---%n", autor);
        String termino = autor.toLowerCase();
        boolean[] encontrado = {false};
        catalogo.recorridoInorden(libro -> {
            if (libro.getAutor().toLowerCase().contains(termino)) {
                System.out.println(libro);
                encontrado[0] = true;
            }
        });
        if (!encontrado[0]) {
            System.out.println("No se encontraron libros de ese autor.");
        }
    }

    /**
     * Busca y muestra libros por coincidencia en la categoría.
     */
    public void buscarLibrosPorCategoria(String categoria) {
        System.out.printf("%n--- RESULTADOS PARA CATEGORÍA: \"%s\" ---%n", categoria);
        String termino = categoria.toLowerCase();
        boolean[] encontrado = {false};
        catalogo.recorridoInorden(libro -> {
            if (libro.getCategoria().toLowerCase().contains(termino)) {
                System.out.println(libro);
                encontrado[0] = true;
            }
        });
        if (!encontrado[0]) {
            System.out.println("No se encontraron libros en esa categoría.");
        }
    }

    // ==========================================
    // RF03. Solicitudes de Préstamo
    // ==========================================

    /**
     * Encola una solicitud de préstamo.
     */
    public void registrarSolicitud(SolicitudPrestamo solicitud) {
        colaSolicitudes.enqueue(solicitud);
    }

    /**
     * Muestra la cola de solicitudes pendientes.
     */
    public void mostrarColaSolicitudes() {
        System.out.println("\n--- COLA DE SOLICITUDES PENDIENTES ---");
        colaSolicitudes.mostrar();
    }

    /**
     * Muestra el elemento al frente de la cola de solicitudes sin extraerlo.
     */
    public SolicitudPrestamo cancelarSiguienteSolicitud() {
        if (colaSolicitudes.isEmpty()) {
            return null;
        }
        return colaSolicitudes.dequeue();
    }
    // ==========================================
    // RF04. Préstamo de Libros
    // ==========================================

    /**
     * Atiende la solicitud al frente de la cola de préstamos.
     * Realiza las verificaciones de existencia, disponibilidad y actualiza estados.
     */
    public void atenderSiguienteSolicitud() {
        if (colaSolicitudes.isEmpty()) {
            System.out.println("Error: No hay solicitudes de préstamo en la cola.");
            return;
        }

        // Consultamos la solicitud sin retirarla todavía
        SolicitudPrestamo solicitud = colaSolicitudes.peek();
        System.out.println("\nProcesando solicitud de: " + solicitud.getNombreEstudiante());
        
        // 1. Verificar existencia del libro
        Libro libro = buscarLibroPorCodigo(solicitud.getCodigoLibro());
        if (libro == null) {
            System.out.printf("Resultado: RECHAZADA. El libro con código %d no existe en el catálogo.%n", 
                    solicitud.getCodigoLibro());
            // Retirar la solicitud no viable de la cola
            colaSolicitudes.dequeue();
            return;
        }

        // 2. Comprobar disponibilidad
        if (libro.getEstado() == EstadoLibro.PRESTADO) {
            System.out.printf("Resultado: RECHAZADA. El libro \"%s\" (Código: %d) ya está prestado.%n", 
                    libro.getTitulo(), libro.getCodigo());
            // Retirar la solicitud no viable de la cola
            colaSolicitudes.dequeue();
            return;
        }

        // 3. Cambiar estado a PRESTADO
        libro.setEstado(EstadoLibro.PRESTADO);

        // 4. Retirar de la cola
        colaSolicitudes.dequeue();

        // 5. Mostrar resultado exitoso
        System.out.printf("Resultado: APROBADA. El libro \"%s\" ha sido prestado con éxito a %s.%n", 
                libro.getTitulo(), solicitud.getNombreEstudiante());
    }

    // ==========================================
    // RF05. Devolución de Libros
    // ==========================================

    /**
     * Registra la devolución de un libro y cambia su estado a DISPONIBLE.
     */
    public boolean registrarDevolucion(int codigo) {
        Libro libro = buscarLibroPorCodigo(codigo);
        if (libro == null) {
            System.out.println("Error: El libro no existe en el catálogo.");
            return false;
        }

        if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
            System.out.println("Advertencia: El libro ya se encuentra disponible en la biblioteca.");
            return false;
        }

        libro.setEstado(EstadoLibro.DISPONIBLE);
        System.out.printf("Confirmación: El libro \"%s\" ha sido devuelto y está nuevamente disponible.%n", 
                libro.getTitulo());
        return true;
    }

    // ==========================================
    // RF06. Reporte Básico
    // ==========================================

    /**
     * Despliega las estadísticas cuantitativas del sistema.
     */
    public void mostrarReporte() {
        int[] conteos = new int[2]; // [0] = disponibles, [1] = prestados
        
        catalogo.recorridoInorden(libro -> {
            if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
                conteos[0]++;
            } else {
                conteos[1]++;
            }
        });

        int totalLibros = catalogo.contarElementos();
        int pendientes = colaSolicitudes.size();

        System.out.println("\n=========================================");
        System.out.println("            REPORTE GENERAL              ");
        System.out.println("=========================================");
        System.out.printf("Cantidad total de libros:       %d%n", totalLibros);
        System.out.printf("Cantidad de libros disponibles: %d%n", conteos[0]);
        System.out.printf("Cantidad de libros prestados:   %d%n", conteos[1]);
        System.out.printf("Solicitudes de préstamo en cola: %d%n", pendientes);
        System.out.println("=========================================");
    }
}
