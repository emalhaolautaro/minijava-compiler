///[Error:this|8]
class Persona {
    int edad;

    void metodoInutil() {
        // Error: Esta expresión no es una sentencia válida.
        // No es una llamada, ni una asignación.
        this;
    }
}

class Principal { static void main() {} }