///[Error:metodoA|10]
class A{
    void metodoA(){
        metodoB();
    }

    void metodoB(){}

    static void main(){
        metodoA(1);
    }
}