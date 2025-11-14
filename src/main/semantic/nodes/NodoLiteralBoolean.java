package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.Token;

public class NodoLiteralBoolean extends NodoExpresion{
    public NodoLiteralBoolean(Token valor) {
        super(valor);
    }

    public Tipo chequear() {
        // Tipo semántico: boolean true
        return new TipoBool(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Literal booleano: " + obtenerValor().obtenerLexema());
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {
        int valor = obtenerValor().obtenerLexema().equals("true") ? 1 : 0;
        output.generar(Instrucciones.PUSH + " " + valor);
    }
}
