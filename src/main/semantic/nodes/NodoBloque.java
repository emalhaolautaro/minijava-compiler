package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

import java.util.ArrayList;
import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoBloque extends NodoSentencia{
    List<NodoSentencia> sentencias;
    private List<NodoVarLocal> variablesLocales;

    public NodoBloque(){
        sentencias = new ArrayList<>();
        variablesLocales = new ArrayList<>();
    }

    public List<NodoSentencia> obtenerSentencias(){
        return sentencias;
    }

    @Override
    public void chequear() {
        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();
        unidadActual.abrirAmbito();
        for(NodoSentencia s: sentencias){
            s.chequear();
        }
        unidadActual.cerrarAmbito();
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

    public void generar(OutputManager output, Unidad unidadActual) {
        for(NodoSentencia s: sentencias){
            s.generar(output, unidadActual);
        }
    }
}
