package main.semantic.nodes;

import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Token;

public class NodoLiteralNull extends NodoExpresion{
    public NodoLiteralNull(Token valor) {
        super(valor);
    }

    public Tipo chequear() {
        // Tipo semántico: null
        return new TipoNull(super.obtenerValor());
    }

    @Override
    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Literal null");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        //TODO: consultar qué hace este literal
    }
}
