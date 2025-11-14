///2&100&10&3&10&exitosamente
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

    void andar() {
        var v = new Vehiculo();
        var a = new Auto();
        var d = new Deportivo();

        //a.acelerar();
        //v.frenar();
        d.acelerar();
    }

    static void main() {
        var t = new TestHerencia();
        t.andar();
    }
}