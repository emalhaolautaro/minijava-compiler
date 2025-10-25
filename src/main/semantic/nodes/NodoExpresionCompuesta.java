package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoExpresionCompuesta extends NodoExpresion{

    public NodoExpresionCompuesta(Token token) {
        super(token);
    }

    @Override
    public Tipo chequear() {
        return null;
    }
}
