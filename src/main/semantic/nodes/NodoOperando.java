package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoOperando extends NodoExpresionCompuesta{
    private Tipo tipo;

    public NodoOperando(Token token, Tipo type) {
        super(token);
        tipo = type;
    }

    public NodoOperando(Token token){
        super(token);
    }

    public Tipo obtenerTipo(){
        return tipo;
    }
}