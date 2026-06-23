package com.quicklibrary.modelos;

/**
 * Representa la entidad Libro dentro de la biblioteca.
 * Implementa Comparable para permitir su indexación ordenada en el Árbol Binario de Búsqueda.
 */
public class Libro implements Comparable<Libro> {
    private int codigo;
    private String titulo;
    private String autor;
    private String categoria;
    private int anio;
    private EstadoLibro estado;

    public Libro(int codigo, String titulo, String autor, String categoria, int anio, EstadoLibro estado) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anio = anio;
        this.estado = estado;
    }

    // Constructor de búsqueda (molde que solo contiene el código para buscar en el ABB)
    public Libro(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    /**
     * Compara libros basándose en su código.
     * Esto permite organizar el ABB de menor a mayor código.
     */
    @Override
    public int compareTo(Libro otro) {
        return Integer.compare(this.codigo, otro.codigo);
    }

    @Override
    public String toString() {
        return String.format("Código: %d | Título: %-30s | Autor: %-20s | Categoría: %-15s | Año: %d | Estado: %s",
                codigo, 
                titulo.length() > 30 ? titulo.substring(0, 27) + "..." : titulo, 
                autor.length() > 20 ? autor.substring(0, 17) + "..." : autor, 
                categoria, 
                anio, 
                estado);
    }
}
