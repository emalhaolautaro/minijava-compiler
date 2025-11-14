package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.Token;

public class NodoLiteralInt extends NodoExpresion{
    public NodoLiteralInt(Token valor) {
        super(valor);
    }

    public Tipo chequear() {
        // Tipo semántico: int
        return new TipoInt(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Literal entero: " + obtenerValor().obtenerLexema());
    }

    public void generar(OutputManager output, Unidad unidadActual){
        output.generar(Instrucciones.PUSH + " " + obtenerValor().obtenerLexema());
    }
}
