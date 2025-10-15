package main.semantic.nodes;

import main.utils.Token;

public class NodoExpresionUnaria{
    private NodoOperando operando;
    private Token operador;

    public NodoExpresionUnaria(NodoOperando operando, Token operador){
        this.operando = operando;
        this.operador = operador;
    }

    public NodoOperando obtenerOperando(){
        return operando;
    }

    public Token obtenerOperador(){
        return operador;
    }

    public void chequear() {
        // Implementar la lógica de chequeo para la expresión unaria
        // Por ejemplo, verificar que el tipo del operando sea compatible
    }
}
