package com.quicklibrary.estructuras;

/**
 * Estructura de datos lineal tipo Cola (FIFO) implementada mediante nodos enlazados.
 * 
 * @param <T> Tipo de dato de los elementos de la cola.
 */
public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamano;

    public Cola() {
        this.primero = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    /**
     * Inserta un elemento al final de la cola.
     * Complejidad temporal: O(1)
     */
    public void enqueue(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (isEmpty()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        tamano++;
    }

    /**
     * Retira y devuelve el elemento al frente de la cola.
     * Complejidad temporal: O(1)
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T dato = primero.getDato();
        primero = primero.getSiguiente();
        tamano--;
        if (primero == null) {
            ultimo = null;
        }
        return dato;
    }

    /**
     * Devuelve el elemento al frente de la cola sin retirarlo.
     * Complejidad temporal: O(1)
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return primero.getDato();
    }

    /**
     * Verifica si la cola está vacía.
     */
    public boolean isEmpty() {
        return primero == null;
    }

    /**
     * Devuelve el número de elementos en la cola.
     */
    public int size() {
        return tamano;
    }

    /**
     * Muestra en consola los elementos de la cola.
     */
    public void mostrar() {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }
        Nodo<T> actual = primero;
        int index = 1;
        while (actual != null) {
            System.out.println(index + ". " + actual.getDato().toString());
            actual = actual.getSiguiente();
            index++;
        }
    }
}
