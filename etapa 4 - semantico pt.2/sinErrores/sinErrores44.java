///[SinErrores]
class Vehiculo {
    void acelerar() {
        System.printIln(1);
    }

    void frenar() {
        System.printIln(10);
    }

    void prenderLuces() {
        System.printIln(100);
    }
}

class Auto extends Vehiculo {
    void acelerar() {
        System.printIln(2);
        prenderLuces();
    }
}

class Deportivo extends Auto {
    void acelerar() {
        System.printIln(3);
        frenar();
    }

    void frenar() {
        System.printIln(20);
    }
}

class TestHerencia {
    Vehiculo v;
    Auto a;
    Deportivo d;

    void andar() {
        v = new Vehiculo();
        a = new Auto();
        d = new Deportivo();

        a.acelerar();
        v.frenar();
        d.acelerar();
    }

    static void main() {
        var t = new TestHerencia();
        t.andar();
    }
}