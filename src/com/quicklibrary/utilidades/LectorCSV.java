package com.quicklibrary.utilidades;

import com.quicklibrary.modelos.EstadoLibro;
import com.quicklibrary.modelos.Libro;
import com.quicklibrary.estructuras.ArbolBinarioBusqueda;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/**
 * Utilidad para cargar el catálogo inicial de libros.
 * Proporciona soporte para leer un archivo CSV y un catálogo de respaldo con 30 libros predefinidos.
 */
public class LectorCSV {

    /**
     * Carga libros en el árbol provisto. Intenta leer del archivo CSV, y si falla o no existe,
     * carga 30 libros de prueba predefinidos en código.
     */
    public static void cargarCatalogoInicial(ArbolBinarioBusqueda<Libro> catalogo, String rutaCSV) {
        File archivo = new File(rutaCSV);
        if (archivo.exists() && archivo.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                // Omitir cabecera si existe
                boolean primeraLinea = true;
                while ((linea = br.readLine()) != null) {
                    if (primeraLinea) {
                        if (linea.toLowerCase().contains("codigo") || linea.toLowerCase().contains("título")) {
                            primeraLinea = false;
                            continue;
                        }
                        primeraLinea = false;
                    }
                    String[] datos = linea.split(",");
                    if (datos.length >= 6) {
                        try {
                            int codigo = Integer.parseInt(datos[0].trim());
                            String titulo = datos[1].trim();
                            String autor = datos[2].trim();
                            String categoria = datos[3].trim();
                            int anio = Integer.parseInt(datos[4].trim());
                            EstadoLibro estado = datos[5].trim().equalsIgnoreCase("Prestado") 
                                    ? EstadoLibro.PRESTADO 
                                    : EstadoLibro.DISPONIBLE;
                            
                            catalogo.insertar(new Libro(codigo, titulo, autor, categoria, anio, estado));
                        } catch (NumberFormatException e) {
                            // Ignorar línea mal formateada
                        }
                    }
                }
                System.out.println("Catálogo cargado con éxito desde el archivo CSV: " + rutaCSV);
                return;
            } catch (IOException e) {
                System.out.println("Advertencia: No se pudo leer el archivo CSV. Cargando libros por defecto.");
            }
        }

        // Carga de respaldo: 30 libros por defecto para cumplir con la especificación
        cargarLibrosPorDefecto(catalogo);
        System.out.println("Catálogo de respaldo cargado (30 libros registrados por defecto).");
    }

    private static void cargarLibrosPorDefecto(ArbolBinarioBusqueda<Libro> catalogo) {
        Object[][] datos = {
            {101, "Programación en Java", "Herbert Schildt", "Programación", 2022, EstadoLibro.DISPONIBLE},
            {102, "Estructuras de Datos", "Mark Allen Weiss", "Computación", 2021, EstadoLibro.DISPONIBLE},
            {103, "Introducción a los Algoritmos", "Thomas Cormen", "Algoritmos", 2022, EstadoLibro.PRESTADO},
            {104, "Patrones de Diseño", "Erich Gamma", "Programación", 2015, EstadoLibro.DISPONIBLE},
            {105, "Código Limpio", "Robert C. Martin", "Programación", 2008, EstadoLibro.DISPONIBLE},
            {106, "El Programador Pragmático", "Andrew Hunt", "Programación", 2019, EstadoLibro.DISPONIBLE},
            {107, "Inteligencia Artificial", "Stuart Russell", "Computación", 2020, EstadoLibro.DISPONIBLE},
            {108, "Redes de Computadoras", "Andrew S. Tanenbaum", "Redes", 2013, EstadoLibro.DISPONIBLE},
            {109, "Sistemas Operativos Modernos", "Andrew S. Tanenbaum", "Sistemas", 2015, EstadoLibro.DISPONIBLE},
            {110, "Base de Datos Conceptos", "Abraham Silberschatz", "Bases de Datos", 2019, EstadoLibro.DISPONIBLE},
            {111, "Compiladores: Principios", "Alfred Aho", "Computación", 2006, EstadoLibro.DISPONIBLE},
            {112, "Ingeniería de Software", "Ian Sommerville", "Ingeniería", 2018, EstadoLibro.DISPONIBLE},
            {113, "Diseño de Interfaces", "Ben Shneiderman", "Diseño", 2016, EstadoLibro.DISPONIBLE},
            {114, "Criptografía Aplicada", "Bruce Schneier", "Seguridad", 2015, EstadoLibro.DISPONIBLE},
            {115, "Fundamentos de SQL", "Itzik Ben-Gan", "Bases de Datos", 2016, EstadoLibro.DISPONIBLE},
            {116, "Desarrollo Web Ágil", "Sam Ruby", "Desarrollo Web", 2017, EstadoLibro.DISPONIBLE},
            {117, "Git y GitHub Práctico", "Richard Silver", "Herramientas", 2021, EstadoLibro.DISPONIBLE},
            {118, "Machine Learning Guía", "Aurélien Géron", "Algoritmos", 2019, EstadoLibro.DISPONIBLE},
            {119, "Pensar en Python", "Allen B. Downey", "Programación", 2015, EstadoLibro.DISPONIBLE},
            {120, "El Arte de Programar", "Donald Knuth", "Computación", 2011, EstadoLibro.DISPONIBLE},
            {121, "C++ Lenguaje Estándar", "Bjarne Stroustrup", "Programación", 2013, EstadoLibro.DISPONIBLE},
            {122, "Java Efectivo", "Joshua Bloch", "Programación", 2018, EstadoLibro.DISPONIBLE},
            {123, "Refactorización", "Martin Fowler", "Programación", 2018, EstadoLibro.DISPONIBLE},
            {124, "Arquitectura Limpia", "Robert C. Martin", "Programación", 2017, EstadoLibro.DISPONIBLE},
            {125, "Desarrollo de Algoritmos", "Jon Kleinberg", "Algoritmos", 2005, EstadoLibro.DISPONIBLE},
            {126, "Seguridad en Redes", "William Stallings", "Seguridad", 2017, EstadoLibro.DISPONIBLE},
            {127, "Sistemas Distribuidos", "Maarten van Steen", "Sistemas", 2017, EstadoLibro.DISPONIBLE},
            {128, "Metodologías Ágiles", "Ken Schwaber", "Gestión", 2020, EstadoLibro.DISPONIBLE},
            {129, "Deep Learning", "Ian Goodfellow", "Algoritmos", 2016, EstadoLibro.DISPONIBLE},
            {130, "NoSQL Esencial", "Pramod J. Sadalage", "Bases de Datos", 2012, EstadoLibro.DISPONIBLE}
        };

        for (Object[] fila : datos) {
            catalogo.insertar(new Libro(
                (int) fila[0],
                (String) fila[1],
                (String) fila[2],
                (String) fila[3],
                (int) fila[4],
                (EstadoLibro) fila[5]
            ));
        }
    }

    /**
     * Guarda el catálogo completo de libros desde el ABB hacia el archivo CSV.
     */
    public static void guardarCatalogo(ArbolBinarioBusqueda<Libro> catalogo, String rutaCSV) {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(rutaCSV))) {
            // Escribir cabecera
            pw.println("codigo,titulo,autor,categoria,anio,estado");
            
            // Recorrer el ABB y escribir cada libro
            catalogo.recorridoInorden(libro -> {
                String estadoStr = libro.getEstado() == EstadoLibro.PRESTADO ? "Prestado" : "Disponible";
                pw.printf("%d,%s,%s,%s,%d,%s%n",
                    libro.getCodigo(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getCategoria(),
                    libro.getAnio(),
                    estadoStr
                );
            });
            System.out.println("Catálogo guardado con éxito en el archivo CSV: " + rutaCSV);
        } catch (IOException e) {
            System.out.println("Error: No se pudo guardar el catálogo en el archivo CSV. " + e.getMessage());
        }
    }
}

