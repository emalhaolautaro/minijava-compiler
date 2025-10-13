///[SinErrores]
// Prueba de control de flujo anidado y sentencias complejas.

class ControlFlujo {
    static void prueba() {
        var i = 0;
        if (i < 10) {
            while (i > 0) {
                if (i == 5) {
                    return;
                } else {
                    ; // Sentencia vacia
                }
            }
        } else {
            i = 100;
        }
    }
}