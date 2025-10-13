///[SinErrores]
// Atributo con el mismo nombre que uno en superclase

class A {
    int x;
}

class B extends A {
    char y;
}

class C extends B{}

class Init {
    static void main() {}
}
