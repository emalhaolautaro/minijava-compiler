///[Error:a|6]
class Main{
    int a;

    static void metodoStatic(){
        a = 10; // Error: acceso a atributo no static desde metodo static
    }

    static void main(){}
}