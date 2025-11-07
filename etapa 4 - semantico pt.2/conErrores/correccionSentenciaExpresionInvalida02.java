///[Error:a2|15]
// Sentencia de expresión no válida: termina en un acceso a atributo en lugar de una llamada a método
class A {
    int a2;
}

class B {
    A a1;
}

class Main {
    static B m2() { return new B(); }

    static void main() {
        m2().a1.a2; // ❌ Error: no es ni asignación ni llamada
    }
}
