package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoLiteralBoolean extends NodoExpresion{
    public NodoLiteralBoolean(Token valor) {
        super(valor);
    }

    public Tipo chequear() {
        // Tipo semántico: boolean true
        return new TipoBool(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Literal booleano: " + obtenerValor().obtenerLexema());
    }
}
