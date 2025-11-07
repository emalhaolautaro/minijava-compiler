///[SinErrores]
// Caso para detectar error de duplicado en consolidación de herencia
class A {
    int x;
}
class B extends A {
    char y;
}
class C extends B {
    boolean z;
}
class Init {
    static void main() {}
}
