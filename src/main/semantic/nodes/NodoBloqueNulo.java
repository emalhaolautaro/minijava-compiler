package main.semantic.nodes;

public class NodoBloqueNulo extends NodoBloque{
    @Override
    public void chequear(){}

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel)+"Bloque Nulo:");
    }
}
