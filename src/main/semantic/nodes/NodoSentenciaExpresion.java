package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;

public class NodoSentenciaExpresion extends NodoSentencia{
    private NodoExpresion expresion;

    public NodoSentenciaExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public NodoExpresion obtenerExpresion() {
        return expresion;
    }

    public void setearExpresion(NodoExpresion exp){
        expresion = exp;
    }

    @Override
    public void chequear() {
        expresion.chequear();

        //TODO: AGREGAR CHEQUEOS A MEDIDA QUE CREO LAS COSAS

        if(!(expresion instanceof NodoAsignacion)
        && !(expresion instanceof NodoLlamadaMetodo)
        && !(expresion instanceof NodoLlamadaMetodoEstatico)
        && !(expresion instanceof NodoLlamadaConstructor)){
            throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(expresion.obtenerValor()));
        }
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Sentencia de expresión:");
        expresion.imprimirAST(nivel + 1);
    }
}
