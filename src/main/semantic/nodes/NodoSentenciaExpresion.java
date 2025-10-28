package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.utils.Token;

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

        NodoExpresion expRaiz = expresion;
        while (expRaiz instanceof NodoExpresionParentizada) {
            expRaiz = ((NodoExpresionParentizada) expRaiz).getExpresion();
        }

        if (expresion instanceof NodoAsignacion) {
            return;
        }

        if (expRaiz instanceof NodoLlamadaMetodo ||
                expRaiz instanceof NodoLlamadaMetodoEstatico ||
                expRaiz instanceof NodoLlamadaConstructor) {
            return;
        }

        if (expRaiz instanceof NodoExpresionUnaria) {
            NodoExpresionUnaria opUnaria = (NodoExpresionUnaria) expRaiz;
            if (opUnaria.obtenerOperador().obtenerLexema().equals("++") || opUnaria.obtenerOperador().obtenerLexema().equals("--")) {
                return;
            }
        }

        if (expRaiz instanceof NodoAccesoVar || expRaiz instanceof NodoThis) {

            NodoEncadenado enc;
            if (expRaiz instanceof NodoAccesoVar) {
                enc = ((NodoAccesoVar) expRaiz).obtenerEncadenado();
            } else {
                enc = ((NodoThis) expRaiz).obtenerEncadenado();
            }

            if (enc == null || enc instanceof NodoEncadenadoVacio) {
                throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(expRaiz.obtenerValor()));
            }

            while (enc.obtenerEncadenado() != null && !(enc.obtenerEncadenado() instanceof NodoEncadenadoVacio)) {
                enc = enc.obtenerEncadenado();
            }

            NodoExpresion ultimoNodo = enc.obtenerIzquierda();

            if (ultimoNodo instanceof NodoLlamadaMetodo) {
                return;
            } else {
                throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(enc.obtenerId()));
            }
        }

        if (expresion instanceof NodoExpresionBinaria) {
            NodoExpresionBinaria opBinaria = (NodoExpresionBinaria) expresion;
            Token operador = opBinaria.obtenerValor();
            throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(operador));
        }

        if (expresion instanceof NodoExpresionUnaria) {
            NodoExpresionUnaria opUnaria = (NodoExpresionUnaria) expresion;
            Token operador = opUnaria.obtenerOperador();
            throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(operador));
        }

        throw new SemanticException(SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(expresion.obtenerValor()));
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Sentencia de expresión:");
        expresion.imprimirAST(nivel + 1);
    }
}