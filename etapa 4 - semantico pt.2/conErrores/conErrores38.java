///[Error:otroMetodo|22]
class Persona {
    int edad;
    String nombre;

    public Persona() {
        edad = 25;
        nombre = "Juan";
    }

    void metodoVoid() { }
}

class Principal {
    static void main() {
        // Test: Encadenado sobre un constructor,
        // pero a través de un atributo primitivo.

        // new Persona() -> OK (Tipo Persona)
        // .edad         -> OK (Tipo int)
        // .otroMetodo() -> ¡ERROR! No se puede encadenar sobre int.
        var x = new Persona().edad.otroMetodo();
    }
}