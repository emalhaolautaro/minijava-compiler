///[Error:=|8]
class Persona {
    int edad;

    void cambiarInstancia() {
        // Error: El lado izquierdo de una asignación no puede ser 'this'.
        // 'this' no es una variable, es una referencia.
        this = new Persona();
    }
}

class Principal { static void main() {} }