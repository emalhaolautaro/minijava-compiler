///[SinErrores]
class Padre {
    void saludar() {
        // Imprimir "Hola desde el padre"
    }
}

class Hijo extends Padre {
    void saludar() {
        // Imprimir "Hola desde el hijo"
    }

    void main() {
        var h = new Hijo();
        h.saludar();
    }
}