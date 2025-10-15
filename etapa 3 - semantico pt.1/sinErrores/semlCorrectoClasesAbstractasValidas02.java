///[SinErrores]
// Clase abstracta que hereda de otra abstracta
abstract class A {
    abstract void m();
}
abstract class B extends A {
    void n() {}
}
class Init { static void main() {} }