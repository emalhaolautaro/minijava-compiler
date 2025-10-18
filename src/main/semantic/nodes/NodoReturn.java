package main.semantic.nodes;

import main.semantic.symboltable.Tipo;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion retorno;
    private Tipo tipoMetodo;

    public NodoReturn(NodoExpresion expresion) {
        retorno = expresion;
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
        Tipo tipoRet = retorno.chequear();
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Return:");
        retorno.imprimirAST(nivel + 1);
    }
}
