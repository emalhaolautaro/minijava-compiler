///30&exitosamente

class MathTest {

    // Método estático simple para asegurar que las variables locales
    // (x, y, z) se almacenen en offsets negativos (ej. -1, -2, -3)
    // sin pisar la metadata del método.
    static int calculate() {
        var x = 5;  // <-- Offset 0
        var y = 10; // <-- Offset -1

        // Prueba la precedencia: se evalúa la multiplicación primero.
        var z = x + y * 2;

        // 5 + (10 * 2) = 25
        return z;
    }

    static void main() {
        // La salida debe ser el resultado del cálculo.
        System.printIln(MathTest.calculate());
    }
}