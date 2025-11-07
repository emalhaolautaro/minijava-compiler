///[SinErrores]
class A {
    int a;
}

class B {
    A a3;
}

class Main {
    static void main() {
        var obj = new A();
        var b = new B();

        obj.a = 1;            // OK
        (b.a3).a = 2;         // OK
        new B().a3.a = 3;     // OK
    }
}