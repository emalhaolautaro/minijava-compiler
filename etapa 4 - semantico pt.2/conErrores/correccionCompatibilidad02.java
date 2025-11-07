///[Error:!=|9]
class TestPrimitivosAislados {
    static void main() {
        var i = 1;
        var j = 2;
        var c = 'a';
        var b = true;

        var error_2 = c != null;
        // ERROR: char (primitivo) != null (referencia)
    }
}