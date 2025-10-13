///[SinErrores]
// Herencia en cadena con algunos métodos redefinidos y otros heredados

class A {
    int a;

    void m1() {
        Object.debugPrint(1);
    }

    int m2(int x) {
        return x + 1;
    }

    void m3() {
        Object.debugPrint(3);
    }
}

class B extends A {
    int b;

    // Redefine solo uno de los métodos de A
    int m2(int x) {
        return x + 2;
    }

    void m4() {
        Object.debugPrint(4);
    }
}

class C extends B {
    int c;

    // Redefine un método heredado de A (a través de B)
    void m1() {
        Object.debugPrint(10);
    }

    // No redefine m2() ni m3() → los hereda automáticamente
    void m5() {
        Object.debugPrint(5);
    }
}

class Init {
    static void main() {
        var c = new C();
        c.m1(); // redefinido en C
        Object.debugPrint(c.m2(10)); // heredado de B
        c.m3(); // heredado de A
        c.m4(); // heredado de B
        c.m5(); // propio de C
    }
}
