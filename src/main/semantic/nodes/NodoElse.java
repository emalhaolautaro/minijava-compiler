package main.semantic.nodes;

public class NodoElse extends NodoSentencia{
    private NodoSentencia sentenciaElse;

    public NodoElse(NodoSentencia sentenciaElse) {
        this.sentenciaElse = sentenciaElse;
    }

    public NodoSentencia obtenerSentenciaElse(){
        return sentenciaElse;
    }

    @Override
    public void chequear() {

    }

}
