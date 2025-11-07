///[SinErrores]
class A {
    static void m1() {
        m2(); // llamada directa
    }

    static void m2() { debugPrint(1234); }

    static void main(){}
}
