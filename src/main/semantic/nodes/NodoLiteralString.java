package main.semantic.nodes;

import main.Main;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
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

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        output.generar(Instrucciones.PUSH + " " +obtenerValor().obtenerLexema());
    }
}