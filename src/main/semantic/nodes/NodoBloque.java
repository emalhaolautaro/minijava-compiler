package main.semantic.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodoBloque extends NodoSentencia{
    List<NodoSentencia> sentencias;

    public NodoBloque(){
        sentencias = new ArrayList<>();
    }

    public List<NodoSentencia> obtenerSentencias(){
        return sentencias;
    }

    @Override
    public void chequear() {

    }
}
