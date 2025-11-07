package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoAsignacion extends NodoExpresion{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;
    private Token operadorAsignacion;

    public NodoAsignacion(NodoExpresion izquierda, NodoExpresion derecha, Token operadorAsignacion) {
        this.operadorAsignacion = operadorAsignacion;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public Tipo chequear() {
        if (!esDestinoValido(izquierda)) {
            Token tokenParaError;

            if (!(izquierda instanceof NodoLlamadaMetodo) && izquierda.obtenerValor() != null) {
                tokenParaError = izquierda.obtenerValor();
            } else if (izquierda instanceof NodoLlamadaMetodo) {
                tokenParaError = ((NodoLlamadaMetodo) izquierda).obtenerNombre();
            } else {
                tokenParaError = operadorAsignacion;
            }

            throw new SemanticException(
                    SemanticTwoErrorMessages.IZQUIERDA_ASIGNACION_NO_VALIDA(tokenParaError, operadorAsignacion)
            );
        }

        Tipo tipoDestino = izquierda.chequear();
        Tipo tipoOrigen = derecha.chequear();

        if (!tipoOrigen.esCompatible(tipoDestino)) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_ASIGNACION(tipoDestino.obtenerNombre().obtenerLexema(),
                            tipoOrigen.obtenerNombre(),
                            operadorAsignacion
                    ));
        }

        return tipoDestino;
    }

    private boolean esDestinoValido(NodoExpresion izquierda) {

        if (izquierda instanceof NodoAccesoVar) {
            NodoAccesoVar acceso = (NodoAccesoVar) izquierda;
            NodoEncadenado enc = acceso.obtenerEncadenado();

            if (enc == null || enc instanceof NodoEncadenadoVacio) {
                return true;
            } else {
                return esUltimoEncadenadoUnAtributo(enc);
            }

        } else if (izquierda instanceof NodoThis) {
            NodoThis nodoThis = (NodoThis) izquierda;
            NodoEncadenado enc = nodoThis.obtenerEncadenado();

            if (enc == null || enc instanceof NodoEncadenadoVacio) {
                return false;
            } else {
                return esUltimoEncadenadoUnAtributo(enc);
            }
        }else if (izquierda instanceof NodoExpresionParentizada) {
            NodoExpresionParentizada nodoPar = (NodoExpresionParentizada) izquierda;

            // Si tiene encadenado, verificamos si termina en atributo
            if (nodoPar.obtenerEncadenado() != null && !(nodoPar.obtenerEncadenado() instanceof NodoEncadenadoVacio)) {
                return esUltimoEncadenadoUnAtributo(nodoPar.obtenerEncadenado());
            }

            // Sino, miramos lo que hay dentro
            return esDestinoValido(nodoPar.getExpresion());
        } else if (izquierda instanceof NodoLlamadaMetodo) {
            NodoLlamadaMetodo llamada = (NodoLlamadaMetodo) izquierda;
            NodoEncadenado enc = llamada.obtenerEncadenado();

            if (enc != null && !(enc instanceof NodoEncadenadoVacio)) {
                return esUltimoEncadenadoUnAtributo(enc);
            }

            return false; // llamada sin encadenado no es asignable
        } else if (izquierda instanceof NodoLlamadaConstructor) {
            NodoLlamadaConstructor llamada = (NodoLlamadaConstructor) izquierda;
            NodoEncadenado enc = llamada.obtenerEncadenado();

            if (enc != null && !(enc instanceof NodoEncadenadoVacio)) {
                return esUltimoEncadenadoUnAtributo(enc);
            }

            return false; // "new B()" solo no es asignable
        }

        return false;
    }

    private boolean esUltimoEncadenadoUnAtributo(NodoEncadenado enc) {
        while (enc.obtenerEncadenado() != null && !(enc.obtenerEncadenado() instanceof NodoEncadenadoVacio)) {
            enc = enc.obtenerEncadenado();
        }
        NodoExpresion ultimoNodo = enc.obtenerIzquierda();
        return (ultimoNodo instanceof NodoAccesoVar);
    }

    @Override
    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Asignacion");
        izquierda.imprimirAST(nivel + 1);
        derecha.imprimirAST(nivel + 1);
    }
}
