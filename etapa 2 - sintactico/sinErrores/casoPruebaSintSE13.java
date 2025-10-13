///[SinErrores]
// Llamadas a métodos y constructores
class MiClase {
    static void metodo() {
        var obj = new MiClase();
        obj.metodo();
        MiClase.metodoEstatico();
        this.atributo = 5;
    }
}