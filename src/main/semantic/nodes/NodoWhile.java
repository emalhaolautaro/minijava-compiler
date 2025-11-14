package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

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
        sentencia.chequear();
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "While:");
        condicion.imprimirAST(nivel + 1);
        sentencia.imprimirAST(nivel + 1);
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        int id = output.obtenerEIncrementarContIfsWhiles();

        String LOOP_INICIO = "lbl_loop_ini" + id;
        String LOOP_FIN = "lbl_loop_fin" + id;

        output.generar(LOOP_INICIO + ": " + Instrucciones.NOP);
        condicion.generar(output, unidadActual);
        output.generar(Instrucciones.BF + " " + LOOP_FIN);
        sentencia.generar(output, unidadActual);
        output.generar(Instrucciones.JUMP + " " + LOOP_INICIO);
        output.generar(LOOP_FIN + ": " + Instrucciones.NOP);
    }
}
