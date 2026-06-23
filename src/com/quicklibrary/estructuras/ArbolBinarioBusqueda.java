package com.quicklibrary.estructuras;

import java.util.function.Consumer;

/**
 * Estructura de datos no lineal tipo Árbol Binario de Búsqueda (ABB) genérico.
 * 
 * @param <T> Tipo de dato de los elementos del árbol, debe ser Comparable.
 */
public class ArbolBinarioBusqueda<T extends Comparable<T>> {

    // Clase interna para representar los nodos del árbol
    private static class NodoArbol<T> {
        T dato;
        NodoArbol<T> izquierdo;
        NodoArbol<T> derecho;

        NodoArbol(T dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private NodoArbol<T> raiz;
    private int contadorElementos;

    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.contadorElementos = 0;
    }

    /**
     * Verifica si el árbol está vacío.
     */
    public boolean isEmpty() {
        return raiz == null;
    }

    /**
     * Devuelve la cantidad de elementos en el árbol.
     */
    public int contarElementos() {
        return contadorElementos;
    }

    /**
     * Inserta un elemento en el árbol.
     * Complejidad: O(log n) promedio, O(n) peor caso.
     */
    public void insertar(T dato) {
        if (dato == null) return;
        raiz = insertarRecursivo(raiz, dato);
    }

    private NodoArbol<T> insertarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) {
            contadorElementos++;
            return new NodoArbol<>(dato);
        }

        int comparacion = dato.compareTo(actual.dato);
        if (comparacion < 0) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, dato);
        } else if (comparacion > 0) {
            actual.derecho = insertarRecursivo(actual.derecho, dato);
        }
        // Si es igual, no se inserta (evita duplicados exactos)
        return actual;
    }

    /**
     * Busca un elemento en el árbol que coincida con el molde provisto.
     * Complejidad: O(log n) promedio, O(n) peor caso.
     */
    public T buscar(T molde) {
        if (molde == null || raiz == null) return null;
        NodoArbol<T> nodoEncontrado = buscarRecursivo(raiz, molde);
        return nodoEncontrado != null ? nodoEncontrado.dato : null;
    }

    private NodoArbol<T> buscarRecursivo(NodoArbol<T> actual, T molde) {
        if (actual == null) return null;

        int comparacion = molde.compareTo(actual.dato);
        if (comparacion == 0) {
            return actual;
        } else if (comparacion < 0) {
            return buscarRecursivo(actual.izquierdo, molde);
        } else {
            return buscarRecursivo(actual.derecho, molde);
        }
    }

    /**
     * Elimina un elemento del árbol.
     * Complejidad: O(log n) promedio, O(n) peor caso.
     */
    public void eliminar(T dato) {
        if (dato == null || raiz == null) return;
        boolean[] eliminado = new boolean[1]; // Flag para saber si realmente se eliminó
        raiz = eliminarRecursivo(raiz, dato, eliminado);
        if (eliminado[0]) {
            contadorElementos--;
        }
    }

    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> actual, T dato, boolean[] eliminado) {
        if (actual == null) {
            return null;
        }

        int comparacion = dato.compareTo(actual.dato);
        if (comparacion < 0) {
            actual.izquierdo = eliminarRecursivo(actual.izquierdo, dato, eliminado);
        } else if (comparacion > 0) {
            actual.derecho = eliminarRecursivo(actual.derecho, dato, eliminado);
        } else {
            // Nodo encontrado
            eliminado[0] = true;

            // Caso 1: Sin hijos o 1 hijo
            if (actual.izquierdo == null) {
                return actual.derecho;
            } else if (actual.derecho == null) {
                return actual.izquierdo;
            }

            // Caso 2: Dos hijos. Obtener el sucesor inorden (el menor en el subárbol derecho)
            actual.dato = encontrarMinimo(actual.derecho);

            // Eliminar el sucesor inorden
            actual.derecho = eliminarRecursivo(actual.derecho, actual.dato, new boolean[1]);
        }
        return actual;
    }

    private T encontrarMinimo(NodoArbol<T> actual) {
        T min = actual.dato;
        while (actual.izquierdo != null) {
            min = actual.izquierdo.dato;
            actual = actual.izquierdo;
        }
        return min;
    }

    /**
     * Realiza un recorrido inorden del árbol y aplica el consumidor a cada elemento.
     * Permite listar los elementos de manera ordenada.
     * Complejidad: O(n)
     */
    public void recorridoInorden(Consumer<T> accion) {
        recorridoInordenRecursivo(raiz, accion);
    }

    private void recorridoInordenRecursivo(NodoArbol<T> actual, Consumer<T> accion) {
        if (actual != null) {
            recorridoInordenRecursivo(actual.izquierdo, accion);
            accion.accept(actual.dato);
            recorridoInordenRecursivo(actual.derecho, accion);
        }
    }
}
