///[Error:m1|13]
class A { }
class B extends A { }

class Test {

    // Este método espera un 'B' (el subtipo)
    void m1(B param) { }

    void m2() {
        var miA = new A(); // Creamos una 'A' (el supertipo)

        m1(miA);
    }
}

class Init{
    static void main()
    { }
}