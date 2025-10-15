package main.semantic.nodes;

import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoExpresionCompuesta extends NodoExpresion{
    public NodoExpresionCompuesta(Token token) {
        super(token);
    }

    public Tipo chequear(TablaSimbolos tabla) {
        return null;
    }
}
