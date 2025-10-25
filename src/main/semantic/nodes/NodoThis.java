package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoThis extends NodoExpresion{
    public NodoThis(Token valor){
        super(valor);
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "This");
    }

    @Override
    public Tipo chequear() {
        // El tipo de 'this' es la clase actual
        Clase claseActual = tablaSimbolos.obtenerClaseActual();

        if (claseActual == null) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_FUERA_DE_CLASE(obtenerValor()));
        }

        // Obtener el método actual desde la tabla de símbolos
        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();

        if (unidadActual == null) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_FUERA_DE_METODO(obtenerValor()));
        }

        if (unidadActual instanceof Metodo && ((Metodo) unidadActual).esStatic()) {
            throw new SemanticException(SemanticTwoErrorMessages.THIS_EN_METODO_STATIC(obtenerValor()));
        }

        return new TipoClase(claseActual.obtenerNombre());
    }
}
