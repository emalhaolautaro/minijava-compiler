package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

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

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        int id = output.obtenerEIncrementarContIfsWhiles();

        String L_ELSE = "lbl_else" + id;
        String L_FIN = "lbl_finif" + id;

        // Evalua la condicion y la deja en el tope de la pila
        condicion.generar(output, unidadActual);

        // Evaluo si existe un else para ver como etiquetar
        if(!(sentenciaElse instanceof NodoSentenciaVacia)){
            output.generar(Instrucciones.BF + " " + L_ELSE);
            sentenciaThen.generar(output, unidadActual);
            output.generar(Instrucciones.JUMP + " " + L_FIN);
            output.generar(L_ELSE + ": " + Instrucciones.NOP);
            sentenciaElse.generar(output, unidadActual);
            output.generar(L_FIN + ": " + Instrucciones.NOP);
        }else{
            output.generar(Instrucciones.BF + " "+ L_FIN);
            sentenciaThen.generar(output, unidadActual);
            output.generar(L_FIN + ": " + Instrucciones.NOP);
        }
    }
}
