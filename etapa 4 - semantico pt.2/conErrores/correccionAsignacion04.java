///[Error:=|8]
class A {
    static int m1() { return 5; }
}

class Main {
    static void main() {
        (A.m1()) = 8;
    }
}
