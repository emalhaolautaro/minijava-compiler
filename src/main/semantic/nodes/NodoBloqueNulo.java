package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;

public class NodoBloqueNulo extends NodoBloque{
    @Override
    public void chequear(){}

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel)+"Bloque Nulo:");
    }

    @Override
    public void generar(OutputManager output, Unidad unidad){}
}
