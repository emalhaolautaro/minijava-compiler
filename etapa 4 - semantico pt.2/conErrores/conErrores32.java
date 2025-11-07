///[Error:cambiarNombre|16]
class Persona {
    int edad;
    String nombre;

    void cambiarNombre(String nuevo) {
        nombre = nuevo;
    }
}

class Principal {
    static void main() {
        var p = new Persona();

        // Error: El tipo del argumento (int) no conforma con 'String'
        p.cambiarNombre(42);
    }
}