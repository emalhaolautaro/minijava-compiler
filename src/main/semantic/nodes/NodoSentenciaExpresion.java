package main.semantic.nodes;

public class NodoSentenciaExpresion extends NodoSentencia{
    private NodoExpresion expresion;

    public NodoSentenciaExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public NodoExpresion obtenerExpresion() {
        return expresion;
    }

    public void setearExpresion(NodoExpresion exp){
        expresion = exp;
    }

    @Override
    public void chequear() {

    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Sentencia de expresión:");
        expresion.imprimirAST(nivel + 1);
    }
}
