///[Error:=|7]
class A { }

class Main {
    static void main() {
        var obj = new A();
        new A() = obj;
    }
}
