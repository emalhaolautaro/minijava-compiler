package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintSln extends NodoBloque {
    public NodoBloquePrintSln() {
        super();
    }

    @Override
    public String toString() {
        return "NodoBloquePrintSln";
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Bloque PrintSln");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        output.generar(Instrucciones.LOAD + " 3" + " ; Cargar parámetro 's' (puntero al CIR)");
        output.generar(Instrucciones.SPRINT + " ; Imprimir el string");
        output.generar(Instrucciones.PRNLN.toString());
        //TODO: Corregir manejo de strings
    }
}
