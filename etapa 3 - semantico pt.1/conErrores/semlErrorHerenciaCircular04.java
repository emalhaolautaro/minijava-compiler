///[Error:A|2]
class A extends C {}
class C extends D{}
class D extends E{}
class E extends F{}
class F extends B{}
class B extends A {}

class Init { static void main() {} }
