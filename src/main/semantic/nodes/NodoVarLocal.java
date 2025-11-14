package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.PalabraReservada;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoVarLocal extends NodoSentencia{
    private Token var;
    private NodoExpresion expresionAsignada;
    private Tipo tipoVar;
    int Offset;

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
        System.out.println("- ".repeat(nivel + 2) + "Offset: " + Offset);
        expresionAsignada.imprimirAST(nivel + 2);
    }

    public int obtenerOffset() {
        return Offset;
    }

    public void setOffset(int i) {
        Offset = i;
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        expresionAsignada.generar(output, unidadActual);
        output.generar("STORE " + (Offset) + " ; Guardar variable local " + var.obtenerLexema());
        if (expresionAsignada instanceof NodoLlamadaConstructor) {
            output.generar(Instrucciones.FMEM + " 1" + " ; Liberar slot temporal del 'new'");
        }
    }
}
