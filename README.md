# QuickLibrary - Sistema de Gestión de Préstamos de Libros

QuickLibrary es una aplicación de escritorio desarrollada en **Java** diseñada para gestionar el catálogo de libros de una biblioteca universitaria y administrar la cola de solicitudes de préstamos de los estudiantes de forma eficiente y ordenada. 

Este proyecto ha sido desarrollado aplicando estructuras de datos lineales y no lineales genéricas implementadas a mano (sin utilizar librerías nativas de colección de Java como `java.util.LinkedList` o `java.util.TreeMap`).

---

## 🚀 Características Principales

*   **Gestión de Catálogo (Árbol Binario de Búsqueda):** Los libros se organizan de manera óptima utilizando un **ABB genérico** indexado por el código único del libro.
*   **Gestión de Solicitudes (Cola Enlazada):** Las solicitudes de préstamo se atienden de forma justa respetando el orden de llegada (**FIFO**) utilizando una **Cola genérica** basada en nodos enlazados.
*   **Búsquedas Avanzadas:** Consulta rápida de libros por código (mediante árbol, $O(\log n)$ promedio), título, autor o categoría.
*   **Validación de Datos:** Protección contra entradas vacías, tipos de datos incorrectos y códigos de libros duplicados.
*   **Reportes en Tiempo Real:** Estadísticas del total de libros, cantidad de ejemplares disponibles, prestados y solicitudes en cola de espera.

---

## 📂 Estructura del Proyecto

El código está estructurado de forma modular en paquetes bien definidos:

```text
src/
└── com/
    └── quicklibrary/
        ├── Main.java                 # Clase principal de ejecución
        ├── estructuras/              # Estructuras de datos propias genéricas
        │   ├── Nodo.java             # Nodo genérico para estructuras enlazadas
        │   ├── Cola.java             # Cola FIFO genérica
        │   └── ArbolBinarioBusqueda.java # Árbol Binario de Búsqueda genérico
        ├── modelos/                  # Clases de entidad (POJOs)
        │   ├── Libro.java            # Modelo de Libro (Comparable)
        │   ├── SolicitudPrestamo.java # Modelo de Solicitud de Préstamo
        │   └── EstadoLibro.java      # Enum (DISPONIBLE, PRESTADO)
        ├── servicios/                # Lógica de control y reglas de negocio
        │   └── BibliotecaServicio.java # Gestor de la biblioteca
        ├── utilidades/               # Funciones auxiliares
        │   ├── Validador.java        # Validación de entradas de teclado
        │   └── LectorCSV.java        # Carga de datos iniciales (opcional)
        └── ui/                       # Interfaz de usuario
            └── MenuConsola.java      # Menú interactivo por consola
```

---

## 🛠️ Requisitos e Instalación

### Requisitos Previos
*   **Java Development Kit (JDK):** Versión 17 o superior.
*   Un IDE compatible (Eclipse, IntelliJ IDEA, VS Code) o acceso a una terminal de comandos con `javac` y `java` configurados.

### Instrucciones de Ejecución (Línea de Comandos)

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/QuickLibrary.git
    cd QuickLibrary
    ```

2.  **Compilar el proyecto:**
    Desde la raíz del proyecto, compila todas las clases en la carpeta de binarios:
    ```bash
    javac -d bin src/com/quicklibrary/**/*.java src/com/quicklibrary/*.java
    ```

3.  **Ejecutar la aplicación:**
    ```bash
    java -cp bin com.quicklibrary.Main
    ```

---

## 📋 Menú del Sistema

Al iniciar la aplicación, se desplegará el siguiente menú interactivo por consola:

```text
=========================================
              QUICKLIBRARY               
=========================================
1. Registrar libro
2. Mostrar todos los libros
3. Buscar libro por código
4. Buscar libros por categoría
5. Modificar datos de un libro
6. Eliminar libro del catálogo
7. Registrar solicitud de préstamo
8. Mostrar cola de solicitudes
9. Atender siguiente solicitud
10. Registrar devolución de libro
11. Mostrar reporte general
12. Salir
=========================================
Seleccione una opción: 
```

---

## 👥 Organización del Equipo de Trabajo

*   **Integrante 1 (Estructuras de Datos):** Desarrollo de las clases de almacenamiento core (`Nodo.java`, `Cola.java`, `ArbolBinarioBusqueda.java`). Redacción del análisis de complejidad algorítmica.
*   **Integrante 2 (Lógica del Sistema):** Implementación de modelos (`Libro.java`, `SolicitudPrestamo.java`) y el controlador principal (`BibliotecaServicio.java`). Carga de los 30 libros iniciales de prueba.
*   **Integrante 3 (Interfaz y Pruebas):** Diseño del menú interactivo (`MenuConsola.java`), el sistema de validación robusto (`Validador.java`), la suite de pruebas funcionales y la documentación del proyecto.

---

## 📊 Análisis de Complejidad Temporal (Resumen)

| Operación | Estructura Utilizada | Complejidad Promedio | Peor Caso |
| :--- | :--- | :--- | :--- |
| **Registrar Solicitud (Encolar)** | Cola Enlazada | $O(1)$ | $O(1)$ |
| **Atender Solicitud (Desencolar)** | Cola Enlazada | $O(1)$ | $O(1)$ |
| **Buscar Libro por Código** | Árbol Binario (ABB) | $O(\log n)$ | $O(n)$ (Árbol degenerado) |
| **Insertar Libro al Catálogo** | Árbol Binario (ABB) | $O(\log n)$ | $O(n)$ (Árbol degenerado) |
| **Listar Libros (Recorrido Inorden)** | Árbol Binario (ABB) | $O(n)$ | $O(n)$ |
| **Búsqueda Secundaria (Categoría)** | Recorrido ABB | $O(n)$ | $O(n)$ |
