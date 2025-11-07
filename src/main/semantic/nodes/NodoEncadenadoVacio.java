package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
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
}
