///[SinErrores]
abstract class A {
    abstract void m1();
}

class B extends A {
    final void m1() { }
}

class C {
    static int contador(){ return 0; }
    static void sumar() { contador = contador + 1; }
}

class Init {
    static void main() {
        C.sumar();
    }
}
