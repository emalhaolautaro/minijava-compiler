package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoAccesoVar extends NodoExpresion{


    public NodoAccesoVar(Token id){
        super(id);
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoAccesoVar: " + obtenerValor().obtenerLexema());
    }

    @Override
    public Tipo chequear() {
        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();
        Clase claseActual = tablaSimbolos.obtenerClaseActual();
        String nombreVar = obtenerValor().obtenerLexema();

        System.out.println("[DEBUG] AccesoVar '" + nombreVar + "' en clase: "
                + claseActual.obtenerNombre().obtenerLexema());

        if (unidadActual.existeVariableLocal(nombreVar)) {
            return unidadActual.obtenerVariableLocal(nombreVar).obtenerTipo();
        }
        if (unidadActual.existeParametro(nombreVar)) {
            return unidadActual.obtenerParametro(nombreVar).obtenerTipo();
        }
        if (claseActual.existeAtributo(nombreVar)) {
            if (unidadActual instanceof Metodo) {
                Metodo metodoActual = (Metodo) unidadActual;
                if (metodoActual.esStatic()) {
                    throw new SemanticException(SemanticTwoErrorMessages.ACCESO_ATRIBUTO_NO_STATIC_DESDE_METODO_STATIC(obtenerValor()));
                }
            }
            return claseActual.obtenerAtributo(nombreVar).obtenerTipo();
        }

        // Error: variable no declarada
        throw new SemanticException(SemanticTwoErrorMessages.VARIABLE_NO_DECLARADA(obtenerValor()));
    }
}
