package main.semantic.nodes;

import main.semantic.symboltable.Tipo;

public class NodoExpresionParentizada extends NodoExpresion{
    private NodoExpresion expresion;

    public NodoExpresionParentizada(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoExpresionParentizada: ");
        expresion.imprimirAST(nivel + 1);
    }

    @Override
    public Tipo chequear(){
        return expresion.chequear();
    }
}
