///[SinErrores]
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

        // Caso 1: Comparación válida por herencia (B es subtipo de A)
        // El compilador actual falla aquí con el error: "El tipo A no es compatible con el tipo B"


        // Caso 2: Comparación válida con Null
        // El compilador actual podría fallar aquí si es estricto con los tipos de referencia
        resultado = a == null;
        resultado = null == b;

        // Caso 3: Comparación válida si ambos son del mismo tipo (ejemplo control)
        a = new A();
        var a2 = new A();
        resultado = a == a2;
    }
}