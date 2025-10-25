package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;

public class NodoAsignacion extends NodoExpresion{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;

    public NodoAsignacion(NodoExpresion izquierda, NodoExpresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public Tipo chequear() {
        Tipo tipoIzquierda = izquierda.chequear();
        Tipo tipoDerecha = derecha.chequear();

        if (!tipoIzquierda.esCompatible(tipoDerecha)) {
            throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_ASIGNACION(tipoIzquierda.obtenerTipo(),
                    tipoDerecha.obtenerNombre()));
        }

        return tipoIzquierda;
    }

    @Override
    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Asignacion");
        izquierda.imprimirAST(nivel + 1);
        derecha.imprimirAST(nivel + 1);
    }
}
