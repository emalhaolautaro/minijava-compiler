///[Error:this|9]
class Principal {
    int valorInstancia = 10;

    static void main() {

        // Error: No se puede usar 'this' en un contexto estático.
        // 'this' no existe aquí.
        var x = this.valorInstancia;
    }
}