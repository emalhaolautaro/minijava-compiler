package main.semantic.nodes;

import main.filemanager.OutputManager;

public abstract class NodoSentencia{
    public abstract void chequear();

    public abstract void imprimirAST(int nivel);

    public void generar(OutputManager output, String nombreClase, String nombreMetodo) {}
}