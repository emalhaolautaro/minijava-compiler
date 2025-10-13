package main.semantic.nodes;

import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;
import main.utils.Token;
import main.errorhandling.exceptions.SemanticException;

public class NodoVariableEncadenada extends NodoEncadenado {

    public NodoVariableEncadenada(NodoExpresion izquierda, Token id) {
        super(izquierda, id);
    }

    @Override
    public Tipo chequear(TablaSimbolos tabla) throws SemanticException {
        //TODO
        return null;
    }
}
