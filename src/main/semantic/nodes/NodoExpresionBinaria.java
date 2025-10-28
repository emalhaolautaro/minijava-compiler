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

        if(opBin.obtenerTipo().equals("OrCortocircuito") || opBin.obtenerTipo().equals("AndCortocircuito")){
            if(expresionIzquierda instanceof TipoBool && expresionDerecha instanceof TipoBool)
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }else if(opBin.obtenerTipo().equals("Mas") || opBin.obtenerTipo().equals("Menos") || opBin.obtenerTipo().equals("Por") || opBin.obtenerTipo().equals("Dividir") || opBin.obtenerTipo().equals("Modulo")){
            if(expresionIzquierda instanceof TipoInt && expresionDerecha instanceof TipoInt)
                return new TipoInt(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }
        else if(opBin.obtenerTipo().equals("IgualIgual") || opBin.obtenerTipo().equals("NotIgual")){
            if(expresionIzquierda.esCompatible(expresionDerecha) && expresionDerecha.esCompatible(expresionIzquierda))
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }else if(opBin.obtenerTipo().equals("Menor") || opBin.obtenerTipo().equals("Mayor") || opBin.obtenerTipo().equals("MenorIgual") || opBin.obtenerTipo().equals("MayorIgual")){
            if(expresionIzquierda instanceof TipoInt && expresionDerecha instanceof TipoInt)
                return new TipoBool(opBin);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_EXPRESION(expresionIzquierda.obtenerNombre().obtenerLexema(), expresionDerecha.obtenerNombre().obtenerLexema(), opBin.obtenerLexema(), opBin));
        }

        return new TipoNull(opBin);
    }
}
