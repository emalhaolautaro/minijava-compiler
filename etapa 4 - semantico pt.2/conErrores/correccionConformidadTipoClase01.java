///[Error:==|21]
class A {
    void metodoA() {}
}

class B extends A {
    void metodoB() {}
}

class C {
    void metodoC() {}
}

class Main {
    static void main() {
        var a = new A();
        var b = new B();
        var c = new C();
        var resultado = a == b;

        resultado = a == c; // ERROR ESPERADO
    }
}