package main.semantic.nodes;

import main.utils.TokenImpl;
import main.utils.Token;
import main.semantic.symboltable.Tipo;

public class NodoLiteralChar extends NodoExpresion{

    public NodoLiteralChar(Token valor) {
        super(valor);
    }

    public Tipo chequear() {
        // Tipo semántico: char
        return new TipoChar(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Literal char: " + obtenerValor().obtenerLexema());
    }
}
