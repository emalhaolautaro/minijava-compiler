///500Valor final: &1000&exitosamente

class Nivel3 {
    int valor;

    void setValor(int v) {
        valor = v;
    }

    int getValor() {
        return valor;
    }
}

class Nivel2 {
    Nivel3 interno;

    public Nivel2() {
        interno = new Nivel3();
    }

    Nivel3 getInterno() {
        return interno;
    }
}

class Nivel1 {
    Nivel2 medio;

    public Nivel1() {
        medio = new Nivel2();
    }
}

class Main {
    static void main() {
        var base = new Nivel1();

        // PRUEBA DE FUEGO PARA TU COMPILADOR
        // Acceso mixto: atributo -> metodo -> atributo
        // base.medio (Atributo) -> .getInterno() (Metodo) -> .valor (Atributo)

        base.medio.getInterno().valor = 500;

        // Verificar
        debugPrint(base.medio.getInterno().valor); // 500

        // Sobrescribir usando el camino largo
        base.medio.interno.valor = 1000;

        System.printS("Valor final: ");
        System.printIln(base.medio.getInterno().getValor());
    }
}