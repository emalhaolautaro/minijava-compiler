///[Error:apellido|12]
class Persona {
    int edad;
    String nombre;
}

class Principal {
    static void main() {
        var p = new Persona();

        // Error: 'apellido' no es un atributo de Persona
        var a = p.apellido;
    }
}