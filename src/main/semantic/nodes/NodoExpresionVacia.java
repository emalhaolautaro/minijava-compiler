package main.semantic.nodes;

import main.semantic.symboltable.Tipo;

public class NodoExpresionVacia extends NodoExpresion{
    @Override
    public Tipo chequear() {
        return new TipoUniversal(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Expresion vacia");
    }
}
