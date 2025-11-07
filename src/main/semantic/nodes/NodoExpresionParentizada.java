package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoExpresionParentizada extends NodoExpresion{
    private NodoExpresion expresion;
    private NodoEncadenado encadenado;

    public NodoExpresionParentizada(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public Token obtenerValor(){
        return expresion.obtenerValor();
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoExpresionParentizada: ");
        expresion.imprimirAST(nivel + 1);
        if (encadenado != null) {
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear(){
        Tipo tipoBase = expresion.chequear();

        if(encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)){
            return encadenado.chequear(tipoBase);
        }
        return tipoBase;
    }

    public void setEncadenado(NodoEncadenado e) {
        encadenado = e;
    }

    public NodoEncadenado obtenerEncadenado(){
        return encadenado;
    }
}
