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

        // Error: Cantidad incorrecta de argumentos
        p.cambiarNombre();
    }
}