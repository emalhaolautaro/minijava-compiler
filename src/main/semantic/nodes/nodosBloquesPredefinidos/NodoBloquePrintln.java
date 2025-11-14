package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintln extends NodoBloque {
    public NodoBloquePrintln() {
        super();
    }

    @Override
    public String toString() {
        return "NodoBloquePrintln";
    }

    @Override
    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + toString());
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        output.generar(Instrucciones.PRNLN.toString());
    }
}
