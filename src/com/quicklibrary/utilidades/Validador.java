package com.quicklibrary.utilidades;

import java.util.Scanner;

/**
 * Utilidad defensiva para leer y validar datos de entrada por consola.
 */
public class Validador {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee una cadena de texto no vacía.
     */
    public static String leerCadena(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Error: El campo no puede estar vacío. Intente de nuevo.");
        }
    }

    /**
     * Lee un número entero.
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un número entero dentro de un rango inclusivo.
     */
    public static int leerEnteroRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Error: El valor debe estar entre %d y %d.%n", min, max);
        }
    }

    /**
     * Lee un año de publicación lógico.
     */
    public static int leerAnio(String mensaje) {
        int anioActual = java.time.Year.now().getValue();
        return leerEnteroRango(mensaje, 1000, anioActual);
    }
}
