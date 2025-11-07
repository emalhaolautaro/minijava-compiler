///[Error:A|2]
class A extends C {}
class C extends D{}
class D extends E{}
class E extends F{}
class G extends F{}
class F extends H{}
class H extends A{}
class B extends A {}

class Init { static void main() {} }
