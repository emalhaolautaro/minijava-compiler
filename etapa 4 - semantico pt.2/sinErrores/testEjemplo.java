///[SinErrores]

class A {
    int x;
    boolean b;
    char c;

    public A() {
        x = 10;
        b = true;
        c = 'z';
    }

    int inc(int v) {
        return v + x;
    }

    int mix() {
        var x = 3;
        var r = x + this.x;
        if (b && (r > 5)) {
            r = r + inc(r);
        }
        return r;
    }
}

class B extends A {
    int y;

    public B() {
        y = 100;
    }

    int inc(int v) {
        return v + y;
    }

    int calc() {
        var z = mix();
        // CORRECCIÓN 1: No se permite -= en MiniJava
        z = z - inc(3);
        return z;
    }
}

class C extends B {
    int w;

    public C() {
        w = 7;
    }

    int deep() {
        var t = calc();
        var a = new A();
        var k = a.inc(2);
        if ((k < 20) || !false) {
            t = t + k * w;
        }
        while (t > 0) {
            // CORRECCIÓN 2: No se permite -= en MiniJava
            t = t - 5;
        }
        return t; // Debe retornar -3
    }
}

class Init {
    static void main() {
        var c = new C();

        var r = c.deep(); // = -3

        // La salida compleja debe coincidir con: -3, false, !, END, 1234
        System.printIln(r);                // -3
        System.printBln(r > 0);            // false
        System.printCln('!');              // !
        System.printSln(" END ");          //  END
        Object.debugPrint(1234);           // 1234
    }
}