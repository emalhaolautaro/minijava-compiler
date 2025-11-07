///[Error:f|16]
class Persona {
    int edad;
    String nombre;

    void saludar() {
        System.printSln("Hola!");
    }
}

class Principal {
    static void main() {
        var p = new Persona();

        // Error: No se puede asignar 'void' a una variable
        var f = p.saludar();
    }
}