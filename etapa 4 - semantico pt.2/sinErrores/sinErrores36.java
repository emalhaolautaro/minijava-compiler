///[SinErrores]
class B {
    int b1 = 5;

    int m2() {
        return 20;
    }
}

class A {
    B a1; // Atributo de tipo clase

    public A() {
        this.a1 = new B();
    }

    B m1() {
        return this.a1;
    }

    void prueba() {

        var x = m1().b1;

        var y = m1().m2();
    }
}

class Init {
    static void main() {}
}