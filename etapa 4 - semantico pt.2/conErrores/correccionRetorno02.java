///[Error:return|5]
// Error Semántico: Método con tipo de retorno no void y return vacío.
class A {
    int m1() {
        return; // ❌ error
    }

    static void main() {}
}
