package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

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

    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Variable: " + obtenerValor().obtenerLexema());
    }
}
