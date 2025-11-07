///[SinErrores]
class TestAmbito {
    int x = 100; //

    void m1(int x) {

        var y = x;

        {
            var z = this.x;
            var w = 50;

            y = x;
            y = z;
        }

        y = x;
    }
}

class Principal {
    static void main() {
        var t = new TestAmbito();
        t.m1(10);
    }
}