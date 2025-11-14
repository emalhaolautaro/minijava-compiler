package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Unidad;

public abstract class NodoSentencia{
    public abstract void chequear();

    public abstract void imprimirAST(int nivel);

    public void generar(OutputManager output, Unidad unidadActual) {}
}