///[SinErrores]
class Motor {
    int cilindros = 8;
    int getCilindros() { return cilindros; }
}

class Auto {
    Motor miMotor;

    public Auto() {
        miMotor = new Motor();
    }

    Motor getMotor() {
        return miMotor;
    }
}

class Deportivo extends Auto {
    // Constructor de Deportivo (llama implícitamente a super())
}

class Principal {
    static void main() {
        // 1. Test de encadenado complejo sobre 'new'
        // Prueba: NodoLlamadaConstructor -> encadenado -> encadenado
        var c = new Deportivo().getMotor().getCilindros();

        // 2. Test de conformidad (subtipo a supertipo)
        var miAuto = new Deportivo(); // Válido: Deportivo ES-UN Auto

        // 3. Test de encadenado sobre variable con tipo de superclase
        var m = miAuto.getMotor();
    }
}