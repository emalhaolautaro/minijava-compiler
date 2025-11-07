///[Error:metodo|10]
class Calculadora {
    int valor = 5;

    int obtenerValor() { return valor; }

    void calcular() {
        // Error: 'this.obtenerValor()' retorna 'int'.
        // No se puede encadenar sobre un tipo primitivo (int).
        var x = this.obtenerValor().metodo();
    }
}

class Principal { static void main() {} }