///[Error:multiplicar|11]
class A{
    static int sumar(int a, int b){
        return a + b;
    }
}

class B{
    void metodo(){
        var resultado = A.sumar(5, 10);
        resultado = resultado + A.multiplicar(2, 3);
    }

    static void main(){}
}