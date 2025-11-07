package main.semantic.nodes;

public class NodoSentenciaVacia extends NodoSentencia {
    @Override
    public void chequear() {

    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "NodoSentenciaVacia");
    }
}
