package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloqueDebugPrint extends NodoBloque {
    public NodoBloqueDebugPrint() {
        super();
    }

    @Override
    public String toString() {
        return "NodoBloqueDebugPrint";
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel)+"BloqueDebugPrint");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        output.generar(Instrucciones.LOAD + " 3");
        output.generar(Instrucciones.IPRINT.toString());
    }
}
