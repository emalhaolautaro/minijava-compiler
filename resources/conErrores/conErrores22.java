///[Error:ConErrores22|16]
class ConErrores22 {
    String a;
    public ConErrores22(String p1) {
        a = p1;
    }

    static void metodo() {
        var x = 0;
    }
}

class Prueba{
    static void main() {
        var a = 5;
        var obj = new ConErrores22(a);
    }
}