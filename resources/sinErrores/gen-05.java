///Valor Final: 100

class Objeto {
    int valor; // Atributo que será el destino final

    public Objeto(int v) {
        valor = v;
    }

    int getValor() {
        return valor;
    }
}

class Caja {
    Objeto b;

    public Caja() {
        b = new Objeto(0); // Inicializa el objeto anidado
    }

    // Retorna el objeto anidado (actúa como el eslabón intermedio)
    Objeto getB() {
        return this.b; // Prueba acceso a 'this'
    }

    // Método para cambiar el valor del objeto anidado
    void setValorB(int v) {
        // Asignación con this implícito: this.b.valor = v;
        // Esta línea es la clave para la correcta carga de 'this'.
        this.getB().valor = v; // L-VALUE: getB().valor
    }
}

class Main {
    static void main() {
        var x = new Caja();

        // 1. Llamada a un método cuya expresión de retorno
        //    se convierte en el puntero base para la asignación.
        x.setValorB(100);

        // 2. Verificación
        System.printS("Valor Final: ");
        System.printIln(x.getB().getValor());
    }
}