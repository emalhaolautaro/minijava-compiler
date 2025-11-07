///[SinErrores]
// Prueba la inicializacion de una variable local.

class PruebaInicializacion {
    int a;
    int b;
    int c;
    int d = 20;

    void metodo(){
        a = d;
        if(d == 20){
            d = d - 5;
        }
    }
}