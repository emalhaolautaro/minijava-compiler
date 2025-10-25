package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoVarLocal extends NodoSentencia{
    private Token var;
    private NodoExpresion expresionAsignada;
    private Tipo tipoVar;

    public NodoVarLocal(Token var, NodoExpresion expresion){
        this.var = var;
        this.expresionAsignada = expresion;
    }

    public Token obtenerVar(){
        return var;
    }
    public NodoExpresion obtenerExpresion(){
        return expresionAsignada;
    }
    public Tipo obtenerTipo(){
        return tipoVar;
    }

    public void chequear() throws SemanticException {
        this.tipoVar = expresionAsignada.chequear();
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "VarLocal:");
        System.out.println("- ".repeat(nivel + 1) + "Variable: " + var.obtenerLexema());
        expresionAsignada.imprimirAST(nivel + 2);
    }
}
