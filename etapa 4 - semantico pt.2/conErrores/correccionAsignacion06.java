///[Error:=|14]
class A {
    int a;
    int m1() { return 5; }
}

class B {
    A a3;
}

class Main {
    static void main() {
        var b = new B();
        (b.a3.m1()) = 7;
    }
}