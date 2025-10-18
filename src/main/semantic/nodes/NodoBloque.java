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
        for(NodoSentencia s: sentencias){
            s.chequear();
        }
    }

    public void agregarSentencia(NodoSentencia sent) {
        sentencias.add(sent);
    }

    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel)+"Bloque:");
        for(NodoSentencia s: sentencias){
            s.imprimirAST(nivel + 1);
        }
    }
}
