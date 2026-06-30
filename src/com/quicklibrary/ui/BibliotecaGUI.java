package com.quicklibrary.ui;

import com.quicklibrary.modelos.EstadoLibro;
import com.quicklibrary.modelos.Libro;
import com.quicklibrary.modelos.SolicitudPrestamo;
import com.quicklibrary.servicios.BibliotecaServicio;
import com.quicklibrary.utilidades.LectorCSV;
import com.quicklibrary.utilidades.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Interfaz Gráfica de Usuario (GUI) para el sistema QuickLibrary.
 */
public class BibliotecaGUI extends JFrame {
    private final BibliotecaServicio servicio;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Componentes de la interfaz
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JTextArea areaCola;
    private JTextArea areaConsola;
    
    // Estado de filtros visuales
    private String filtroCategoria = null;
    
    public BibliotecaGUI(BibliotecaServicio servicio) {
        this.servicio = servicio;
        
        setTitle("QuickLibrary - Sistema de Gestión de Biblioteca");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Agregar confirmación al cerrar la ventana desde la X
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmarSalida();
            }
        });

        inicializarComponentes();
        redirigirConsola();
        actualizarTablasYVistas();
    }

    private void inicializarComponentes() {
        // Contenedor principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(240, 244, 248));

        // 1. Encabezado
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(41, 128, 185));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("QUICKLIBRARY - CONTROL DE BIBLIOTECA");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // 2. Panel Lateral de Botones (Acciones) con Scrollbar
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 5, 5));
        panelBotones.setBackground(new Color(240, 244, 248));

        // Subtítulos para categorizar las acciones
        agregarSeccionBoton(panelBotones, "--- GESTIÓN DE LIBROS ---");
        agregarBotonAccion(panelBotones, "1. Registrar Libro", e -> ejecutarRegistrarLibro());
        agregarBotonAccion(panelBotones, "2. Mostrar todos los Libros", e -> mostrarTodosLosLibros());
        agregarBotonAccion(panelBotones, "3. Buscar Libro", e -> ejecutarBuscarLibro());
        agregarBotonAccion(panelBotones, "4. Buscar por Categoría", e -> ejecutarBuscarPorCategoria());
        agregarBotonAccion(panelBotones, "5. Modificar Libro", e -> ejecutarModificarLibro());
        agregarBotonAccion(panelBotones, "6. Eliminar Libro", e -> ejecutarEliminarLibro());
        agregarBotonAccion(panelBotones, "14. Registrar Devolución", e -> ejecutarRegistrarDevolucion());

        agregarSeccionBoton(panelBotones, "--- SOLICITUDES (COLA) ---");
        agregarBotonAccion(panelBotones, "7. Registrar Solicitud", e -> ejecutarRegistrarSolicitud());
        agregarBotonAccion(panelBotones, "8. Mostrar Cola", e -> ejecutarMostrarCola());
        agregarBotonAccion(panelBotones, "9. Ver Siguiente en Espera", e -> ejecutarVerSiguiente());
        agregarBotonAccion(panelBotones, "10. Atender Siguiente", e -> ejecutarAtenderSiguiente());
        agregarBotonAccion(panelBotones, "11. Cancelar Siguiente", e -> ejecutarCancelarSiguiente());
        agregarBotonAccion(panelBotones, "12. Cancelar por Estudiante", e -> ejecutarCancelarPorEstudiante());
        agregarBotonAccion(panelBotones, "13. Vaciar Cola", e -> ejecutarVaciarCola());

        agregarSeccionBoton(panelBotones, "--- SISTEMA ---");
        agregarBotonAccion(panelBotones, "15. Mostrar Reporte", e -> ejecutarMostrarReporte());
        
        JButton btnSalir = new JButton("16. Salir y Guardar");
        btnSalir.setBackground(new Color(192, 57, 43));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSalir.addActionListener(e -> confirmarSalida());
        panelBotones.add(btnSalir);

        JScrollPane scrollBotones = new JScrollPane(panelBotones);
        scrollBotones.setPreferredSize(new Dimension(260, 0));
        scrollBotones.setBorder(BorderFactory.createTitledBorder("Panel de Operaciones"));
        panelPrincipal.add(scrollBotones, BorderLayout.WEST);

        // 3. Panel Central de Visualización (Pestañas)
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Pestaña 1: Tabla del Catálogo de Libros
        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Título", "Autor", "Categoría", "Año", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición directa
            }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setRowHeight(22);
        tablaLibros.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollTabla = new JScrollPane(tablaLibros);
        pestañas.addTab("Catálogo de Libros", scrollTabla);

        // Pestaña 2: Cola de Solicitudes
        areaCola = new JTextArea();
        areaCola.setEditable(false);
        areaCola.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollCola = new JScrollPane(areaCola);
        pestañas.addTab("Cola de Espera", scrollCola);

        // Pestaña 3: Registro de Actividad y Logs
        areaConsola = new JTextArea();
        areaConsola.setEditable(false);
        areaConsola.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaConsola.setBackground(Color.BLACK);
        areaConsola.setForeground(new Color(46, 204, 113)); // Verde consola
        JScrollPane scrollConsola = new JScrollPane(areaConsola);
        pestañas.addTab("Consola / Logs", scrollConsola);

        panelPrincipal.add(pestañas, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private void agregarSeccionBoton(JPanel panel, String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(127, 140, 141));
        panel.add(label);
    }

    private void agregarBotonAccion(JPanel panel, String texto, java.awt.event.ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boton.setBackground(Color.WHITE);
        boton.addActionListener(listener);
        panel.add(boton);
    }

    /**
     * Redirige System.out al JTextArea de la pestaña de consola.
     */
    private void redirigirConsola() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                areaConsola.append(String.valueOf((char) b));
                areaConsola.setCaretPosition(areaConsola.getDocument().getLength());
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    /**
     * Actualiza la tabla del catálogo y el visor de la cola de solicitudes.
     */
    private void actualizarTablasYVistas() {
        // 1. Actualizar Tabla de Libros respetando el filtro de categoría
        modeloTabla.setRowCount(0);
        servicio.getCatalogo().recorridoInorden(libro -> {
            if (filtroCategoria == null || libro.getCategoria().equalsIgnoreCase(filtroCategoria)) {
                modeloTabla.addRow(new Object[]{
                        libro.getCodigo(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getCategoria(),
                        libro.getAnio(),
                        libro.getEstado()
                });
            }
        });

        // 2. Actualizar Vista de Cola
        areaCola.setText("");
        if (servicio.getColaSolicitudes().isEmpty()) {
            areaCola.setText("No hay solicitudes de préstamo pendientes en la cola.");
        } else {
            // Captura temporalmente la salida del método mostrar
            PrintStream originalOut = System.out;
            StringBuilder sb = new StringBuilder();
            OutputStream tempOut = new OutputStream() {
                @Override
                public void write(int b) {
                    sb.append((char) b);
                }
            };
            System.setOut(new PrintStream(tempOut));
            servicio.getColaSolicitudes().mostrar();
            System.setOut(originalOut);
            areaCola.setText(sb.toString());
        }
    }

    // ==========================================
    // LÓGICA DE LAS ACCIONES DE LA INTERFAZ
    // ==========================================

    private void mostrarTodosLosLibros() {
        System.out.println("\n--- LISTADO COMPLETO DEL CATÁLOGO ---");
        this.filtroCategoria = null; // Limpiar filtro al mostrar todos
        servicio.mostrarTodosLosLibros();
        actualizarTablasYVistas();
    }

    private void ejecutarRegistrarLibro() {
        try {
            String codStr = JOptionPane.showInputDialog(this, "Ingrese el código del libro (entero):", "Registrar Libro", JOptionPane.QUESTION_MESSAGE);
            if (codStr == null) return;
            int codigo = Integer.parseInt(codStr.trim());

            String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro:", "Registrar Libro", JOptionPane.QUESTION_MESSAGE);
            if (titulo == null || titulo.trim().isEmpty()) return;

            String autor = JOptionPane.showInputDialog(this, "Ingrese el autor principal:", "Registrar Libro", JOptionPane.QUESTION_MESSAGE);
            if (autor == null || autor.trim().isEmpty()) return;

            String categoria = JOptionPane.showInputDialog(this, "Ingrese la categoría:", "Registrar Libro", JOptionPane.QUESTION_MESSAGE);
            if (categoria == null || categoria.trim().isEmpty()) return;

            String anioStr = JOptionPane.showInputDialog(this, "Ingrese el año de publicación:", "Registrar Libro", JOptionPane.QUESTION_MESSAGE);
            if (anioStr == null) return;
            int anio = Integer.parseInt(anioStr.trim());
            int anioActual = java.time.Year.now().getValue();
            if (anio < 1000 || anio > anioActual) {
                JOptionPane.showMessageDialog(this, "Error: Año de publicación inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Libro nuevo = new Libro(codigo, titulo.trim(), autor.trim(), categoria.trim(), anio, EstadoLibro.DISPONIBLE);
            if (servicio.registrarLibro(nuevo)) {
                System.out.println("Éxito: Libro \"" + nuevo.getTitulo() + "\" registrado en el ABB.");
                actualizarTablasYVistas();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Ya existe un libro con el código " + codigo, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese valores numéricos válidos para código y año.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarBuscarLibro() {
        String[] opciones = {"Código", "Título", "Autor"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione el criterio de búsqueda:", 
                "Buscar Libro", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        
        if (seleccion == 0) { // Código
            String codStr = JOptionPane.showInputDialog(this, "Ingrese el código a buscar:");
            if (codStr == null) return;
            try {
                int codigo = Integer.parseInt(codStr.trim());
                Libro libro = servicio.buscarLibroPorCodigo(codigo);
                if (libro != null) {
                    System.out.println("\nLibro Encontrado por Código:\n" + libro);
                    JOptionPane.showMessageDialog(this, libro.toString(), "Libro Encontrado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("\nError: No se encontró libro con código " + codigo);
                    JOptionPane.showMessageDialog(this, "No se encontró el libro.", "Resultado", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (seleccion == 1) { // Título
            String titulo = JOptionPane.showInputDialog(this, "Ingrese parte del título a buscar:");
            if (titulo == null || titulo.trim().isEmpty()) return;
            servicio.buscarLibrosPorTitulo(titulo.trim());
        } else if (seleccion == 2) { // Autor
            String autor = JOptionPane.showInputDialog(this, "Ingrese parte del nombre del autor:");
            if (autor == null || autor.trim().isEmpty()) return;
            servicio.buscarLibrosPorAutor(autor.trim());
        }
    }

    private void ejecutarBuscarPorCategoria() {
        // Recolectar categorías únicas del catálogo (ordenadas alfabéticamente)
        java.util.Set<String> categorias = new java.util.TreeSet<>();
        servicio.getCatalogo().recorridoInorden(libro -> {
            if (libro.getCategoria() != null && !libro.getCategoria().trim().isEmpty()) {
                categorias.add(libro.getCategoria().trim());
            }
        });

        if (categorias.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay categorías registradas en el catálogo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Convertir a un arreglo para mostrarlo en el dropdown del diálogo
        String[] opciones = categorias.toArray(new String[0]);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione la categoría a buscar:",
                "Buscar por Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            System.out.println("\nBuscando libros de la categoría: " + seleccion);
            this.filtroCategoria = seleccion; // Aplicar filtro en la tabla
            servicio.buscarLibrosPorCategoria(seleccion);
            actualizarTablasYVistas();
        }
    }

    private void ejecutarModificarLibro() {
        String codStr = JOptionPane.showInputDialog(this, "Ingrese el código del libro a modificar:");
        if (codStr == null) return;
        try {
            int codigo = Integer.parseInt(codStr.trim());
            Libro libro = servicio.buscarLibroPorCodigo(codigo);
            if (libro == null) {
                JOptionPane.showMessageDialog(this, "El libro no existe en el catálogo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String titulo = JOptionPane.showInputDialog(this, "Modificar título:", libro.getTitulo());
            if (titulo == null || titulo.trim().isEmpty()) return;

            String autor = JOptionPane.showInputDialog(this, "Modificar autor:", libro.getAutor());
            if (autor == null || autor.trim().isEmpty()) return;

            String categoria = JOptionPane.showInputDialog(this, "Modificar categoría:", libro.getCategoria());
            if (categoria == null || categoria.trim().isEmpty()) return;

            String anioStr = JOptionPane.showInputDialog(this, "Modificar año de publicación:", String.valueOf(libro.getAnio()));
            if (anioStr == null) return;
            int anio = Integer.parseInt(anioStr.trim());

            if (servicio.modificarLibro(codigo, titulo.trim(), autor.trim(), categoria.trim(), anio)) {
                System.out.println("Éxito: Datos del libro " + codigo + " modificados.");
                actualizarTablasYVistas();
            } else {
                System.out.println("Error al intentar modificar el libro.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores numéricos incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarEliminarLibro() {
        String codStr = JOptionPane.showInputDialog(this, "Ingrese el código del libro a eliminar:");
        if (codStr == null) return;
        try {
            int codigo = Integer.parseInt(codStr.trim());
            Libro libro = servicio.buscarLibroPorCodigo(codigo);
            if (libro == null) {
                JOptionPane.showMessageDialog(this, "El libro especificado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tarea 12: Bloqueo de seguridad si está prestado
            if (libro.getEstado() == EstadoLibro.PRESTADO) {
                JOptionPane.showMessageDialog(this, 
                        "ADVERTENCIA DE SEGURIDAD: El libro \"" + libro.getTitulo() + "\" está PRESTADO.\n" +
                        "No se puede eliminar del catálogo hasta que sea devuelto.", 
                        "Eliminación Bloqueada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int seguro = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar físicamente el libro \"" + libro.getTitulo() + "\"?", 
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (seguro == JOptionPane.YES_OPTION) {
                if (servicio.eliminarLibro(codigo)) {
                    System.out.println("Éxito: Libro " + codigo + " eliminado físicamente del ABB.");
                    actualizarTablasYVistas();
                } else {
                    System.out.println("Error al intentar eliminar el libro.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarRegistrarSolicitud() {
        // Tarea 11: Validar formato del código de estudiante
        String codEstudiante = JOptionPane.showInputDialog(this, "Ingrese el código del estudiante (8 caracteres alfanuméricos):");
        if (codEstudiante == null) return;
        codEstudiante = codEstudiante.trim();
        if (codEstudiante.length() != 8 || !codEstudiante.matches("^[a-zA-Z0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Error: El código de estudiante debe tener exactamente 8 caracteres alfanuméricos.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomEstudiante = JOptionPane.showInputDialog(this, "Ingrese el nombre del estudiante:");
        if (nomEstudiante == null || nomEstudiante.trim().isEmpty()) return;

        String codLibroStr = JOptionPane.showInputDialog(this, "Ingrese el código del libro solicitado:");
        if (codLibroStr == null) return;
        try {
            int codLibro = Integer.parseInt(codLibroStr.trim());
            Libro libro = servicio.buscarLibroPorCodigo(codLibro);
            if (libro == null) {
                JOptionPane.showMessageDialog(this, "Advertencia: El libro solicitado no existe en el catálogo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (libro.getEstado() == EstadoLibro.PRESTADO) {
                JOptionPane.showMessageDialog(this, "Advertencia: El libro solicitado está actualmente prestado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            String fecha = LocalDate.now().format(formatter);
            SolicitudPrestamo solicitud = new SolicitudPrestamo(codEstudiante, nomEstudiante.trim(), codLibro, fecha);
            servicio.registrarSolicitud(solicitud);
            System.out.println("Éxito: Solicitud encolada para: " + nomEstudiante);
            actualizarTablasYVistas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código de libro inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarMostrarCola() {
        System.out.println("\n--- COLA DE SOLICITUDES PENDIENTES ---");
        servicio.mostrarColaSolicitudes();
        actualizarTablasYVistas();
    }

    private void ejecutarVerSiguiente() {
        SolicitudPrestamo sig = servicio.consultarSiguienteSolicitud();
        if (sig != null) {
            System.out.println("\nSiguiente solicitud en cola:\n" + sig);
            JOptionPane.showMessageDialog(this, sig.toString(), "Siguiente Solicitud (Peek)", JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("\nNo hay solicitudes en la cola.");
            JOptionPane.showMessageDialog(this, "La cola está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ejecutarAtenderSiguiente() {
        if (servicio.getColaSolicitudes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay solicitudes pendientes por atender.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        servicio.atenderSiguienteSolicitud();
        actualizarTablasYVistas();
    }

    private void ejecutarCancelarSiguiente() {
        if (servicio.getColaSolicitudes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cola ya está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SolicitudPrestamo retirado = servicio.getColaSolicitudes().dequeue();
        System.out.println("Cancelada (Retirada de cola sin atender): " + retirado);
        actualizarTablasYVistas();
    }

    private void ejecutarCancelarPorEstudiante() {
        if (servicio.getColaSolicitudes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cola está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del estudiante a remover de la cola:");
        if (codigo == null || codigo.trim().isEmpty()) return;
        codigo = codigo.trim();

        // Reconstrucción de la cola omitiendo las solicitudes de ese estudiante
        com.quicklibrary.estructuras.Cola<SolicitudPrestamo> colaAux = new com.quicklibrary.estructuras.Cola<>();
        boolean encontrado = false;
        
        while (!servicio.getColaSolicitudes().isEmpty()) {
            SolicitudPrestamo sol = servicio.getColaSolicitudes().dequeue();
            if (sol.getCodigoEstudiante().equalsIgnoreCase(codigo)) {
                encontrado = true;
                System.out.println("Cancelada la solicitud del estudiante: " + sol.getNombreEstudiante());
            } else {
                colaAux.enqueue(sol);
            }
        }

        // Devolver los elementos que no se eliminaron a la cola original
        while (!colaAux.isEmpty()) {
            servicio.getColaSolicitudes().enqueue(colaAux.dequeue());
        }

        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Solicitud(es) del estudiante cancelada(s).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna solicitud para el código " + codigo, "Resultado", JOptionPane.WARNING_MESSAGE);
        }
        actualizarTablasYVistas();
    }

    private void ejecutarVaciarCola() {
        if (servicio.getColaSolicitudes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cola ya está vacía.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int seguro = JOptionPane.showConfirmDialog(this, "¿Está seguro de vaciar todas las solicitudes pendientes?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (seguro == JOptionPane.YES_OPTION) {
            while (!servicio.getColaSolicitudes().isEmpty()) {
                servicio.getColaSolicitudes().dequeue();
            }
            System.out.println("Cola de solicitudes vaciada por completo.");
            actualizarTablasYVistas();
        }
    }

    private void ejecutarRegistrarDevolucion() {
        String codStr = JOptionPane.showInputDialog(this, "Ingrese el código del libro devuelto:");
        if (codStr == null) return;
        try {
            int codigo = Integer.parseInt(codStr.trim());
            if (servicio.registrarDevolucion(codigo)) {
                actualizarTablasYVistas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la devolución (verificar código o estado del libro).", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarMostrarReporte() {
        System.out.println("\nGenerando Reporte Estadístico...");
        servicio.mostrarReporte();
        actualizarTablasYVistas();
        JOptionPane.showMessageDialog(this, "Reporte generado. Revisa la pestaña 'Consola / Logs' o el archivo 'reporte_biblioteca.txt'.", "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmarSalida() {
        int seguro = JOptionPane.showConfirmDialog(this, 
                "¿Desea cerrar el sistema? Se guardarán todos los cambios en libros.csv.", 
                "Salir de QuickLibrary", JOptionPane.YES_NO_OPTION);
        if (seguro == JOptionPane.YES_OPTION) {
            System.out.println("\nGuardando cambios en el catálogo...");
            LectorCSV.guardarCatalogo(servicio.getCatalogo(), "libros.csv");
            System.exit(0);
        }
    }
}
