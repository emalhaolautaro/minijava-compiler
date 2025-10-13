package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import java.util.List;

public class NodoMetodoEncadenado extends NodoEncadenado{
    private List<NodoExpresion> args; // lista de argumentos de la llamada

    public NodoMetodoEncadenado(NodoExpresion izquierda, Token id, List<NodoExpresion> args) {
        super(izquierda, id);
        this.args = args;
    }

    public List<NodoExpresion> getArgs() {
        return args;
    }

    @Override
    public Tipo chequear(TablaSimbolos tabla) throws SemanticException {
        //TODO
        return null;
    }
}
