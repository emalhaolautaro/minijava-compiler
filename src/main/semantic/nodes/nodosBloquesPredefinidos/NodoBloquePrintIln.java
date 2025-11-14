package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintIln extends NodoBloque {
    public NodoBloquePrintIln() {
        super();
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) +"NodoBloquePrintI");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        output.generar(Instrucciones.LOAD + " 3");
        output.generar(Instrucciones.IPRINT.toString());
        output.generar(Instrucciones.PRNLN.toString());
    }
}
