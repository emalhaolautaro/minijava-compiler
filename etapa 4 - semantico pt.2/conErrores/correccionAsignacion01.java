///[Error:=|10]
class A {
    int a;
    int m1() { return 5; }
}

class Main {
    static void main() {
        var obj = new A();
        obj.m1() = 10;
    }
}