///[SinErrores]
class TestPrimitivosAislados {
    static void main() {
        var i = 1;
        var j = 2;
        var c = 'a';
        var b = true;

        var res_int = 5 * (i + j) / 2; // OK: int * (int + int) / int
        var res_bool = 10 % 3 != 1;    // OK: int != int

        res_bool = i <= j;         // OK: int <= int

        // B. Operaciones Booleanas (Solo bool)
        res_bool = (true && !false) || b; // OK: bool && bool || bool

        // C. Operaciones de Igualdad Primitivas (Mismo Tipo Primitivo)
        res_bool = i == j;   // OK: int == int
        res_bool = c != 'b'; // OK: char != char
        res_bool = b == false; // OK: bool == bool
    }
}