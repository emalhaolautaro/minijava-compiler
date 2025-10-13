package main.semantic.nodes;

import main.utils.Token;

public class NodoExpresionBinaria extends NodoExpresionCompuesta{
    private NodoExpresionCompuesta izquierda;
    private NodoExpresionCompuesta derecha;
    private Token operador;

    public NodoExpresionBinaria(NodoExpresionCompuesta izquierda, NodoExpresionCompuesta derecha, Token operador){
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.operador = operador;
    }

    public NodoExpresionCompuesta obtenerIzquierda(){
        return izquierda;
    }

    public NodoExpresionCompuesta obtenerDerecha(){
        return derecha;
    }

    public Token obtenerOperador(){
        return operador;
    }

    @Override
    public void chequear() {
        // Implementar la lógica de chequeo para la expresión binaria
        // Por ejemplo, verificar que los tipos de izquierda y derecha sean compatibles
    }
}
