///[Error:X|2]
class X extends Z{}
class Y extends Z{}
class Z extends X{}
class D extends C {}
class A extends C {}
class C extends B{}
class B extends A {}

class Init { static void main() {} }
