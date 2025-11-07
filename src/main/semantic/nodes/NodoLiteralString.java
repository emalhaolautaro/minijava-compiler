package main.semantic.nodes;

import main.Main;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoLiteralString extends NodoExpresion{
    public NodoLiteralString(Token valor){
        super(valor);
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Literal String: " + obtenerValor().obtenerLexema());
    }

    @Override
    public Tipo chequear() {
        Clase string = tablaSimbolos.obtenerClasePorNombre("String");
        return new TipoClase(string.obtenerNombre());
    }
}