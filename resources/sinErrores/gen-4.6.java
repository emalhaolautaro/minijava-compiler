///El nodo 1 no tiene siguiente.
///El nodo 2 se llama Nodo Dos
///--------------------

// Una clase simple para la prueba
class Nodo {
    String nombre;
    Nodo siguiente; // Un atributo de tipo clase

    public Nodo(String n) {
        nombre = n;
        siguiente = null; // Prueba de asignación 'null' en constructor
    }

    void setSiguiente(Nodo s) {
        siguiente = s;
    }

    Nodo getSiguiente() {
        return siguiente; // Prueba de retorno de 'null'
    }

    String getNombre() {
        return nombre;
    }
}

class Main {

    // Método que acepta 'null' como parámetro
    static void imprimirNodo(Nodo n) {
        if (n == null) { // Prueba '== null'
            System.printSln("El nodo es nulo.");
        } else {
            System.printS("El nodo se llama ");
            System.printSln(n.getNombre());
        }
    }

    static void main() {
        var n1 = new Nodo("Nodo Uno");
        var n2 = new Nodo("Nodo Dos");

        // 1. Prueba de asignación 'null' explícita
        n1.setSiguiente(null);

        // 2. Prueba de comparación '== null'
        if (n1.getSiguiente() == null) {
            System.printSln("El nodo 1 no tiene siguiente.");
        } else {
            System.printSln("El nodo 1 tiene siguiente.");
        }

        // 3. Prueba de parámetro 'null' (implícito)
        // imprimirNodo(n2.getSiguiente()); // n2.siguiente es 'null'

        // 4. Prueba de parámetro no-null
        imprimirNodo(n2);

        System.printSln("--------------------");
    }
}