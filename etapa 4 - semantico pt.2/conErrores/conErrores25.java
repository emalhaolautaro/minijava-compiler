///[Error:sumar|10]
class Utilidades {
    int sumar(int a, int b) {
        return a + b;
    }
}

class Principal extends Utilidades{
    static void main() {
        sumar("hola", 3);
    }
}
