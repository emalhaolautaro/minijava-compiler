///[Error:==|9]
class TestPrimitivosAislados {
    static void main() {
        var i = 1;
        var j = 2;
        var c = 'a';
        var b = true;

        var error_1 = i == null;
        // ERROR: int (primitivo) == null (referencia)
    }
}