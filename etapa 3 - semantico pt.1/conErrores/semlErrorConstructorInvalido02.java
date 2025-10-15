///[Error:B|4]
class A {
    public A() {}      // ✅ correcto
    public B() {}      // ❌ error: constructor debe llamarse A
}
class B {}
class Init { static void main() {} }
