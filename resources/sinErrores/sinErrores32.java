///[SinErrores]
class Motor {
    int cilindros;
    public Motor(int c) {
        cilindros = c;
    }
}

class Auto {
    Motor motor;
    int edad = 2024;

    public Auto(Motor m) {
        this.motor = m;
    }

    Motor getMotor() {
        return motor;
    }
}

class Principal {
    static void main() {
        var m = new Motor(8);
        var p = new Auto(m);

        var e = (p).edad;
        var motorDelAuto = (new Auto(m)).getMotor();
    }
}