package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Token;
import main.utils.TokenImpl;

public class NodoEncadenadoVacio extends NodoEncadenado{
    public NodoEncadenadoVacio() {}

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoEncadenadoVacio");
    }

    public Tipo chequear(Tipo izq) {
        return new TipoNull(new TokenImpl("null", "null", -1));
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){}
}
