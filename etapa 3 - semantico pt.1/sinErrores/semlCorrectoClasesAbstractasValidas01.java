///[SinErrores]
abstract class A{
    abstract void m1();
    abstract int m2();
}

abstract class B extends A{
    void m1(){}
    int m2(){ return 0; }
    abstract void m3();
}

class C extends B{
    void m3(){}
    void m4(){}
}