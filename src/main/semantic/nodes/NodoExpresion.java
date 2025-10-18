package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public abstract class NodoExpresion {
    private Token valor;

    public NodoExpresion(){}

    public NodoExpresion(Token token){
        valor = token;
    }

    public Token obtenerValor(){
        return valor;
    }

    public abstract Tipo chequear();

    public void imprimirAST(int i) {
        System.out.print("- ".repeat(i) + "Expresion: " + valor.obtenerLexema());
    }
}
