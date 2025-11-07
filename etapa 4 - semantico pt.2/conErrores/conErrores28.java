///[Error:getNombre|12]
class Persona {
    int edad;
    String nombre;
}

class Principal {
    static void main() {
        var p = new Persona();

        // Error: 'getNombre' no es un método de Persona
        var n = p.getNombre();
    }
}