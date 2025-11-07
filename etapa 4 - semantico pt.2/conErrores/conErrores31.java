///[Error:nombre|15]
class Persona {
    int edad;
    String nombre;

    void saludar() { }
}

class Principal {
    static void main() {
        var p = new Persona();
        p.saludar(); // Esto es válido

        // Error: 'p.nombre' no es una sentencia válida
        p.nombre;
    }
}