///[Error:m1|9]
class A {

    void m1() { // m1() es un método de INSTANCIA (no-estático)
        System.printSln("hola");
    }

    static void m2() {
        m1();
    }
}

class Init{
    static void main()
    { }
}