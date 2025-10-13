package main.semantic.nodes;

import main.utils.Token;
import main.semantic.symboltable.Tipo;
import main.utils.TokenImpl;

public class NodoNull extends NodoOperando{
    private Token valor;

    public NodoNull(Token valor) {
        super(valor);
    }

    public Token obtenerValor() {
        return valor;
    }

    /*
    public Tipo chequear() {
        // Tipo semántico: null
        return new Tipo(new TokenImpl("palabra reservada", "null", valor.obtenerLinea()));
    }
    */
}
