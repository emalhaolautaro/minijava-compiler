package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintBln extends NodoBloque {
    public NodoBloquePrintBln() {
        super();
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Bloque PrintBln");
    }

    @Override
    public String toString() {
        return "Bloque PrintBln";
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        output.generar(Instrucciones.LOAD + " 3");
        output.generar(Instrucciones.BPRINT.toString());
        output.generar(Instrucciones.PRNLN.toString());
    }
}
