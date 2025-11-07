///[Error:a1|6]
class A { int a1; }
class Main {
    static A m2() { return new A(); }
    static void main() {
        m2().a1; // ❌ No es ni llamada ni asignación
    }
}