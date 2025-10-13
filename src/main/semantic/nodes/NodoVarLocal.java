package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;

public class NodoVarLocal extends NodoSentencia{
    private NodoVariable var;
    private NodoExpresionCompuesta expresionCompuesta;

    public NodoVarLocal(NodoVariable var, NodoExpresionCompuesta expresionCompuesta){
        this.var = var;
        this.expresionCompuesta = expresionCompuesta;
    }

    public NodoVariable obtenerVar(){
        return var;
    }
    public NodoExpresionCompuesta obtenerExpresionCompuesta(){
        return expresionCompuesta;
    }

    public void chequear() throws SemanticException {

    }
}
