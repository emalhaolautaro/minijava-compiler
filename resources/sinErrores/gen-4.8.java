///10&exitosamente

class Box {
    int value;

    public Box(int v) {
        value = v;
    }

    int getValue() {
        return value;
    }
}

class Main {
    static void main() {
        // La expresión interna '(new Box(10))' es evaluada primero.
        // El puntero resultante es pasado al encadenado '.getValue()'.
        System.printIln((new Box(10)).getValue());
    }
}