package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;

public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;

    public NodoWhile(NodoExpresion condicion, NodoSentencia sentencia) {
        this.condicion = condicion;
        this.sentencia = sentencia;
    }

    public NodoExpresion obtenerCondicion(){
        return condicion;
    }

    public NodoSentencia obtenerSentencia(){
        return sentencia;
    }

    @Override
    public void chequear() {
        if(!condicion.chequear().esCompatible(new TipoBool(null)))
            throw new SemanticException(SemanticTwoErrorMessages.WHILE_COND_NO_BOOL(condicion));
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "While:");
        condicion.imprimirAST(nivel + 1);
        sentencia.imprimirAST(nivel + 1);
    }
}
