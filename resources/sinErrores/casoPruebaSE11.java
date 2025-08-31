///SinErrores
// MiniJava Test Class: TestCompleto
// Este archivo prueba una amplia gama de casos de uso del analizador léxico,
// incluyendo literales, operadores, comentarios, identificadores y casos de borde.

/*
 * Prueba de Comentario de Bloque:
 * Esta sección prueba comentarios de varias líneas.
 * El analizador debe ignorar todo hasta el delimitador de cierre.
 * También debe manejar correctamente falsos delimitadores como * y / por separado.
 * O incluso un falso cierre como este: * /
 * Ahora sí, el cierre real.
 */

class TestCompleto {
    // --- 1. Prueba de Atributos y Literales ---
    private String cadena_compleja = "Esta cadena contiene \"comillas\" y \\barras\\. También ignora //comentarios y /*bloques*/.";
    private abstract boolean identificador_con_guion_bajo777;

    // --- 2. Prueba de Constructor y Creación de Objetos ---
    public TestCompleto() {
        identificador_con_guion_bajo777 = true;
    }

    // --- 3. Prueba de Métodos, Operadores y Flujo de Control ---
    private void probarMetodos() {

        // Llamada a método del propio objeto
        this.probarMetodos();
    }
}

// Esta es una prueba de comentario de línea al final del archivo, sin un salto de línea después.