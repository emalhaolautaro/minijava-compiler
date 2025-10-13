package main.semantic.nodes;

import main.utils.Token;

public class NodoExpresion {
    private Token valor;

    public NodoExpresion(){}

    public NodoExpresion(Token token){
        valor = token;
    }

    public Token obtenerValor(){
        return valor;
    }

    public void chequear(){

    }
}
