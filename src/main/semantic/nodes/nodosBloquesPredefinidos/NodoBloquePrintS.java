package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintS extends NodoBloque {
    public NodoBloquePrintS() {
        super();
    }

    @Override
    public String toString() {
        return "NodoBloquePrintS";
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Bloque PrintS");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        output.generar(Instrucciones.LOAD + " 3" + " ; Cargar parámetro 's' (puntero al CIR)");

        output.generar(Instrucciones.PUSH + " 1" + " ; Apilar offset de los datos del string");
        output.generar(Instrucciones.ADD + " ; Calcular Puntero_CIR + 1");

        output.generar(Instrucciones.SPRINT + " ; Imprimir el string");
        //TODO: Corregir manejo de strings
    }

}
