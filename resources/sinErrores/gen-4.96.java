///25&exitosamente

class PrecedenceTest {

    static int calculate() {
        var x = 5;  // <-- Local en offset 0
        var y = 10; // <-- Local en offset -1
        var z = 2;  // <-- Local en offset -2
        // PRUEBA CLAVE: Multiplicación antes de la suma.
        // Debe ser 5 + (10 * 2) = 25.
        var r = x + (y * z);

        return r;
    }

    static void main() {
        System.printIln(PrecedenceTest.calculate());
    }
}