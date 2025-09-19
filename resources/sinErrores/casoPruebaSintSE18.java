///[SinErrores]
// Prueba una clase final que hereda, con constructor, metodos estaticos y atributos.

class Base {
    public Base() {}
}

final class Derivada extends Base {

    int atributoEstatico;

    public Derivada() {
        // constructor
    }

    static void metodo() {
        return;
    }
}