package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoExpresionBinaria extends NodoExpresion{
    private Token opBin;
    private NodoExpresion expIzq;
    private NodoExpresion expDer;

    public NodoExpresionBinaria(Token opBin, NodoExpresion expIzq, NodoExpresion expDer) {
        this.opBin = opBin;
        this.expIzq = expIzq;
        this.expDer = expDer;
    }

    public Token obtenerValor(){
        return opBin;
    }

    public NodoExpresion obtenerExpresionIzquierda(){
        return expIzq;
    }

    public NodoExpresion obtenerExpresionDerecha(){
        return expDer;
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Expresion Binaria: " + opBin.obtenerLexema());
        expIzq.imprimirAST(i + 1);
        expDer.imprimirAST(i + 1);
    }

    @Override
    public Tipo chequear() {
        Tipo expresionIzquierda = expIzq.chequear();
        Tipo expresionDerecha = expDer.chequear();

        // Identificador del operador para simplificar
        String operador = opBin.obtenerTipo();

        // --- Operadores Booleanos: && y || ---
        if(operador.equals("OrCortocircuito") || operador.equals("AndCortocircuito")){
            if(expresionIzquierda instanceof TipoBool && expresionDerecha instanceof TipoBool)
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }
        // --- Operadores Matemáticos: +, -, *, /, % ---
        else if(operador.equals("Mas") || operador.equals("Menos") || operador.equals("Por") || operador.equals("Dividir") || operador.equals("Modulo")){
            // ESTRICTO: SÓLO int y devuelve int.
            if(expresionIzquierda instanceof TipoInt && expresionDerecha instanceof TipoInt)
                return new TipoInt(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }
        // --- Operadores Relacionales: <, <=, >=, > ---
        else if(operador.equals("Menor") || operador.equals("Mayor") || operador.equals("MenorIgual") || operador.equals("MayorIgual")){
            // ESTRICTO: SÓLO int y devuelve boolean.
            if(expresionIzquierda instanceof TipoInt && expresionDerecha instanceof TipoInt)
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }
        // --- Operadores de Igualdad: == y != ---
        else if(operador.equals("IgualIgual") || operador.equals("NotIgual")){

            // 1. Detectar si son tipos primitivos (int, bool, char)
            boolean esPrimitivoIzq = expresionIzquierda instanceof TipoInt ||
                    expresionIzquierda instanceof TipoBool ||
                    expresionIzquierda instanceof TipoChar;

            boolean esPrimitivoDer = expresionDerecha instanceof TipoInt ||
                    expresionDerecha instanceof TipoBool ||
                    expresionDerecha instanceof TipoChar;

            // 2. ERROR: Si hay mezcla de primitivos con referencias (Clase/String/Null)
            if (esPrimitivoIzq != esPrimitivoDer) {
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(
                        expresionIzquierda.obtenerNombre().obtenerLexema(),
                        expresionDerecha.obtenerNombre().obtenerLexema(),
                        opBin.obtenerLexema(), opBin
                ));
            }

            // 3. OK: Si son del mismo grupo (ambos primitivos o ambos referencias), verificar la compatibilidad flexible (herencia, null, misma clase, etc.)
            // Usa OR (||) para permitir la relación en CUALQUIER dirección (A compatible con B O B compatible con A).
            if(expresionIzquierda.esCompatible(expresionDerecha) || expresionDerecha.esCompatible(expresionIzquierda))
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }

        return new TipoNull(opBin);
    }
}
