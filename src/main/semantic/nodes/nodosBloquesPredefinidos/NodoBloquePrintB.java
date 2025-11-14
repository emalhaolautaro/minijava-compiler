package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloquePrintB extends NodoBloque {
    public NodoBloquePrintB() {
        super();
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Bloque PrintB");
    }

    @Override
    public String toString() {
        return "Bloque PrintB";
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        output.generar(Instrucciones.LOAD + " 3");
        output.generar(Instrucciones.BPRINT.toString());
    }
}
