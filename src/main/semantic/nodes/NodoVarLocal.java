package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.PalabraReservada;
import main.utils.Token;

import static main.Main.tablaSimbolos;

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

        if(tipoVar instanceof TipoVoid){
            throw new SemanticException(SemanticTwoErrorMessages.VAR_TIPO_VOID(var));
        }

        if(tipoVar instanceof TipoNull){
            throw new SemanticException(SemanticTwoErrorMessages.VAR_TIPO_NULL(var));
        }

        if(PalabraReservada.esPalabraReservada(var.obtenerLexema())){
            throw new SemanticException(SemanticTwoErrorMessages.VAR_LOCAL_NOMBRE_INVALIDO(var));
        }

        tablaSimbolos.obtenerUnidadActual().agregarVariableLocal(this);
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "VarLocal:");
        System.out.println("- ".repeat(nivel + 1) + "Variable: " + var.obtenerLexema());
        expresionAsignada.imprimirAST(nivel + 2);
    }
}
