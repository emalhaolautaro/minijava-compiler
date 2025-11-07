///[SinErrores]

class A {
    static int a(){ return 0; }
    final boolean b(){}

    void m1(){
        a = null;
        b = true;
    }
}