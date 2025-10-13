package main.semantic.nodes;

import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;
import main.utils.Token;
import main.errorhandling.exceptions.SemanticException;

public class NodoVariable extends NodoOperando {
    NodoEncadenado cad;

    public NodoVariable(Token token) {
        super(token);
    }

    public NodoVariable(Token token, Tipo tipo) {
        super(token, tipo);
    }

    public NodoVariable(Token token, Tipo tipo, NodoEncadenado cad) {
        super(token, tipo);
    }

    public NodoEncadenado obtenerCadena(){
        return cad;
    }
}
