///[Error:5|5]
// Error Semántico: No se puede retornar una expresión desde un método void.
class A {
    void m1() {
        return 5; // ❌ error
    }

    static void main() {}
}