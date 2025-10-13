package main.semantic.nodes;

import main.utils.TokenImpl;
import main.utils.Token;
import main.semantic.symboltable.Tipo;

public class NodoLiteralChar extends NodoExpresion{

    public NodoLiteralChar(Token valor) {
        super(valor);
    }

/*
    public Tipo chequear() {
        // Tipo semántico: char
        return new Tipo(new TokenImpl("palabra reservada", "char", valor.obtenerLinea()));
    }
*/
}
