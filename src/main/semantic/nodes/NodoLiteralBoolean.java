package main.semantic.nodes;

import main.utils.Token;
import main.semantic.symboltable.Tipo;
import main.utils.TokenImpl;

public class NodoLiteralBoolean extends NodoExpresion{
    private Token valor;

    public NodoLiteralBoolean(Token valor) {
        super(valor);
    }

    public Token obtenerValor() {
        return valor;
    }

    /*
    public Tipo chequear() {
        // Tipo semántico: boolean
        return new Tipo(new TokenImpl("palabra reservada", "boolean", valor.obtenerLinea()));
    }
     */
}
