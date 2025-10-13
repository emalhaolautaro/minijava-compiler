package main.semantic.nodes;

public class NodoIf extends NodoSentencia{
    private NodoExpresion condición;
    private NodoSentencia sentenciaThen;
    private NodoSentencia sentenciaElse;

    public NodoIf(NodoExpresion condición, NodoSentencia sentencia, NodoSentencia sentenciaElse) {
        this.condición = condición;
        this.sentenciaThen = sentencia;
        this.sentenciaElse = sentenciaElse;
    }

    public NodoExpresion obtenerCondicion(){
        return condición;
    }

    public NodoSentencia obtenerSentencia(){
        return sentenciaThen;
    }

    public NodoSentencia obtenerSentenciaElse(){
        return sentenciaElse;
    }

    @Override
    public void chequear() {

    }
}
