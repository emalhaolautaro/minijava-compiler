///[Error:toString|15]
class Persona {
    int edad;
    String nombre;
    String toString() {
        return nombre;
    }
}

class Principal {
    static void main() {
        var p = new Persona();

        // Error: No se puede encadenar sobre un tipo primitivo (int)
        var s = p.edad.toString();
    }
}