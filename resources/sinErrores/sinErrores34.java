///[Error:+|8]
// Variable local duplicada en el metodo m2
class A {
    static int metodo(){
        return 3+1;
    }

    static void main() {
        var x = 10;
        var y = 20;
        (A.metodo());
    }
}