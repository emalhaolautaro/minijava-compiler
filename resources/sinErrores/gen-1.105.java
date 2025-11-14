///1&30&100&2&exitosamente
class Adder {
    int total; // Atributo

    // Constructor: suma a y b, lo guarda en 'total'
    public Adder(int a, int b) {
        total = a + b; // Asignación a atributo (this.total implícito)

        //System.printIln(total);
    }

    int sumar(){
        return total;
    }
}

class TestAdder {
    static void main() {
        System.printIln(1); // Marcador de inicio

        // Crea el primer objeto.
        // El constructor debe imprimir 30.
        var a1 = new Adder(10, 20);
        System.printIln(a1.sumar());
        // Crea el segundo objeto.
        // El constructor debe imprimir 100.
        var a2 = new Adder(50, 50);
        System.printIln(a2.sumar());

        System.printIln(2); // Marcador de fin
    }
}