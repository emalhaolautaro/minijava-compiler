package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;
import main.utils.TokenImpl;

public class NodoLiteralEntero extends NodoExpresion{
    private Token valor;

    public NodoLiteralEntero(Token valor) {
        super(valor);
    }

    public Token obtenerValor() {
        return valor;
    }

    /*
    public Tipo chequear() {
        // Tipo semántico: int
        return new Tipo(new TokenImpl("palabra reservada", "int", valor.obtenerLinea()));
    }
     */
}
