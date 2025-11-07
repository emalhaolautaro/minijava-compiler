///[Error:errorDeParser|7]
class A {
    int a1;

    void m1() {
        var a = 10;
        a = (5 + 5).errorDeParser;
    }
}

class Init {
    static void main() {}
}