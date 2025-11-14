package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoThis extends NodoExpresion{
    private NodoEncadenado encadenado;

    public NodoThis(Token valor){
        super(valor);
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "This");
        if (encadenado != null) {
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        Clase claseActual = tablaSimbolos.obtenerClaseActual();

        if (claseActual == null) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_FUERA_DE_CLASE(obtenerValor()));
        }

        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();

        if (unidadActual == null) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_FUERA_DE_METODO(obtenerValor()));
        }

        if (unidadActual instanceof Metodo && ((Metodo) unidadActual).esStatic()) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_EN_METODO_STATIC(obtenerValor()));
        }

        TipoClase tipoThis = new TipoClase(claseActual.obtenerNombre());

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoThis);
        }

        return tipoThis;
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual) {

    }

    public void generarParaAlmacenar(OutputManager output) {

    }
}
