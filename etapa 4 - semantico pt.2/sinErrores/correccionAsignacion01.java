///[SinErrores]
class A {
    int attrA;
    B m1() {
        return new B();
    }
}

class B extends A {
    int attrB;
}

class Main {
    static void main() {
        var objA = new A();
        var objB = new B();
        var i = 10;

        // A. Asignaciones Simples y Directas
        objA.attrA = 1;
        i = objB.attrB;

        // B. Asignaciones con Encadenamiento Complejo (Válidas con la corrección)

        // OK: Comienza con llamada a método, termina en atributo (m1().attrB = ...)
        objA.m1().attrB = 2;

        // OK: Comienza con constructor (new B()), termina en atributo
        (new B()).attrA = 3;

        // OK: Comienza con expresión parentetizada, termina en atributo
        (objB.m1()).attrB = 4;

        // OK: Cadena larga.
        objA.m1().m1().attrB = 5;
    }
}