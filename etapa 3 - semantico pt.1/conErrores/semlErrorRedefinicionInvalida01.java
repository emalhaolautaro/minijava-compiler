///[Error:m1|10]
class A{
    void m1(int p1, String p2){}
    void m2(int p1){}
    void m3(){}
}

class B extends A{
    void m2(int p1){}
    void m1(boolean p1, String p2){}
}