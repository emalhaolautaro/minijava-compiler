///[SinErrores]
class Taller {

    void getReporte(int id) {
        Taller.generarReporteBase(id);
        System.printSln(" (revisado)");
    }

    static void generarReporteBase(int id) {
        System.printS("Reporte Nro: ");
        System.printI(id);
    }
}

class Principal {
    static void main() {
        Taller.generarReporteBase(123);
        System.println();

        var t = new Taller();
        t.getReporte(123);
    }
}