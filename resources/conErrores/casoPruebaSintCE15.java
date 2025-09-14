///[Error:(|5]
// Falta identificador después del punto en llamada a método estático
class MiClase {
    static void metodo() {
        MiClase.();
    }
}