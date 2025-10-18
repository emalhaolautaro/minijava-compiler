package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;

public class NodoIf extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentenciaThen;
    private NodoSentencia sentenciaElse;

    public NodoIf(NodoExpresion condicion, NodoSentencia sentencia, NodoSentencia sentenciaElse) {
        this.condicion = condicion;
        this.sentenciaThen = sentencia;
        this.sentenciaElse = sentenciaElse;
    }

    public NodoExpresion obtenerCondicion(){
        return condicion;
    }

    public NodoSentencia obtenerSentencia(){
        return sentenciaThen;
    }

    public NodoSentencia obtenerSentenciaElse(){
        return sentenciaElse;
    }

    @Override
    public void chequear() {
        boolean cond = condicion.chequear().esCompatible(new TipoBool(null));
        if(!cond) throw new SemanticException(SemanticTwoErrorMessages.IF_COND_NO_BOOL(condicion));
        sentenciaThen.chequear();
        if(sentenciaElse != null) sentenciaElse.chequear();
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "If:");
        condicion.imprimirAST(nivel + 1);
        sentenciaThen.imprimirAST(nivel + 1);
        sentenciaElse.imprimirAST(nivel + 1);
    }
}
