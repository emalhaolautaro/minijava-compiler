/*
 * Caso de prueba integral VÁLIDO.
 *
 * Este código está diseñado para pasar el análisis semántico y prueba:
 * 1. Clases abstractas y concretas.
 * 2. Herencia simple (Auto hereda de Vehiculo).
 * 3. Atributos en superclase y subclase.
 * 4. Un método abstracto (acelerar) implementado en la subclase.
 * 5. Un método concreto (getVelocidad) sobrescrito en la subclase.
 * 6. Un método 'final' (encenderLuces) que no puede ser sobrescrito.
 * 7. Un constructor explícito y válido en la clase Auto.
 * 8. Herencia del método estático 'debugPrint' de la clase Object.
 */
abstract class Vehiculo {
    int velocidad;

    // Método abstracto que debe ser implementado por las subclases.
    abstract void acelerar(int cantidad);

    // Método concreto que puede ser sobrescrito.
    int getVelocidad() {
        return velocidad;
    }

    // Método final que no puede ser sobrescrito en las subclases.
    final void encenderLuces() {
        // Lógica para encender luces...
    }
}

class Auto extends Vehiculo {
    boolean tieneTurbo;

    // Constructor explícito y válido (los constructores sí llevan 'public').
    public Auto() {
        velocidad = 0;
        tieneTurbo = true;
    }

    // Implementación obligatoria del método abstracto heredado.
    void acelerar(int cantidad) {
        if (tieneTurbo) {
            velocidad = velocidad + (cantidad * 2);
        } else {
            velocidad = velocidad + cantidad;
        }
    }

    // Sobrescritura válida de un método de la superclase.
    // Misma firma y tipo de retorno.
    int getVelocidad() {
        return velocidad;
    }
}

class Main {
    // Método principal del programa.
    void main() {
        var miAuto = null;
        var velActual = null;

        miAuto = new Auto();
        miAuto.acelerar(50);
        miAuto.encenderLuces(); // Llama a un método 'final' heredado.

        velActual = miAuto.getVelocidad();

        // Llama al método estático 'debugPrint' heredado implícitamente de la clase Object.
        // Esto prueba que la consolidación de la herencia desde Object funciona.
        Object.debugPrint(velActual); // Debería imprimir 100 por la salida estándar.
    }
}