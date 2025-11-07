///[SinErrores]
class A {
    void m2() { }
}

class B {
    A m3() { return new A(); }
}

class Main {
    B p1;

    void m3(){
        (p1).m3().m2();
    }

    static void main() {}
}
