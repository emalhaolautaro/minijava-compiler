package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion retorno;
    private Tipo tipoMetodo;

    public NodoReturn(NodoExpresion expresion, Tipo tipoRet) {
        retorno = expresion;
        tipoMetodo = tipoRet;
    }

    public NodoExpresion obtenerRetorno(){
        return retorno;
    }

    public Tipo obtenerTipoMetodo(){
        return tipoMetodo;
    }

    public void setearTipoMetodo(Tipo t){
        tipoMetodo = t;
    }

    @Override
    public void chequear() {
        Tipo tipoRetorno = retorno.chequear();
        if (!tipoRetorno.esCompatible(tipoMetodo))
            throw new SemanticException(SemanticTwoErrorMessages.RETORNO_NO_COMPATIBLE(retorno, tipoMetodo, tipoRetorno));

    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Return:");
        retorno.imprimirAST(nivel + 1);
    }
}
