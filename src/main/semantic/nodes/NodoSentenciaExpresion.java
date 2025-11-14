package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;
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

        // Lógica para manejar expresiones parentetizadas con encadenamiento (ej: (p1).m3().m2();)
        if (expresion instanceof NodoExpresionParentizada) {
            NodoExpresionParentizada expPar = (NodoExpresionParentizada) expresion;
            NodoEncadenado enc = expPar.obtenerEncadenado();

            // Si hay encadenamiento, verifica si termina en una llamada a método.
            if (enc != null && !(enc instanceof NodoEncadenadoVacio)) {
                NodoEncadenado ultimoEnc = enc;
                while (ultimoEnc.obtenerEncadenado() != null && !(ultimoEnc.obtenerEncadenado() instanceof NodoEncadenadoVacio)) {
                    ultimoEnc = ultimoEnc.obtenerEncadenado();
                }

                // Si el último elemento es una llamada a método, la sentencia es válida.
                if (ultimoEnc.obtenerIzquierda() instanceof NodoLlamadaMetodo) {
                    return;
                }
            }

            // Si no hay encadenamiento, o no fue válido, la expresión a revisar
            // pasa a ser lo que estaba dentro del paréntesis (ej: p1 en (p1);)
            expRaiz = expPar.getExpresion();
        }

        if (expresion instanceof NodoAsignacion) {
            return;
        }

        if (expRaiz instanceof NodoLlamadaMetodo ||
                expRaiz instanceof NodoLlamadaMetodoEstatico ||
                expRaiz instanceof NodoLlamadaConstructor) {
            NodoEncadenado enc = null;

            if (expRaiz instanceof NodoLlamadaMetodo) {
                enc = ((NodoLlamadaMetodo) expRaiz).obtenerEncadenado();
            } else if (expRaiz instanceof NodoLlamadaMetodoEstatico) {
                enc = ((NodoLlamadaMetodoEstatico) expRaiz).getEncadenado();
            } else if (expRaiz instanceof NodoLlamadaConstructor) {
                enc = ((NodoLlamadaConstructor) expRaiz).obtenerEncadenado();
            }

            // Si no hay encadenado → llamada pura, válida
            if (enc == null || enc instanceof NodoEncadenadoVacio) {
                return;
            }

            // Avanzar hasta el último eslabón
            NodoEncadenado ultimo = enc;
            while (ultimo.obtenerEncadenado() != null && !(ultimo.obtenerEncadenado() instanceof NodoEncadenadoVacio)) {
                ultimo = ultimo.obtenerEncadenado();
            }

            NodoExpresion ultimoNodo = ultimo.obtenerIzquierda();

            // Si el encadenado termina en otra llamada → ok
            if (ultimoNodo instanceof NodoLlamadaMetodo ||
                    ultimoNodo instanceof NodoLlamadaMetodoEstatico) {
                return;
            }

            // Si termina en atributo u otra cosa → no tiene efecto → error
            throw new SemanticException(
                    SemanticTwoErrorMessages.SENTENCIA_EXPRESION_NO_VALIDA(ultimo.obtenerId())
            );
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

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        System.out.println(unidadActual.obtenerNombre().obtenerLexema());
        expresion.generar(output, unidadActual);
    }
}