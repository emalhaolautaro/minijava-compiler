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
    private NodoEncadenado encadenado;

    public NodoAccesoVar(Token id){
        super(id);
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoAccesoVar: " + obtenerValor().obtenerLexema());
        if(encadenado != null){
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();
        Clase claseActual = tablaSimbolos.obtenerClaseActual();
        String nombreVar = obtenerValor().obtenerLexema();

        Tipo tipoBase;

        if (unidadActual.existeVariableLocal(nombreVar)) {
            tipoBase = unidadActual.obtenerVariableLocal(nombreVar).obtenerTipo();
        } else if (unidadActual.existeParametro(nombreVar)) {
            tipoBase = unidadActual.obtenerParametro(nombreVar).obtenerTipo();
        } else if (claseActual.existeAtributo(nombreVar)) {
            if (unidadActual instanceof Metodo && ((Metodo) unidadActual).esStatic()) {
                throw new SemanticException(SemanticTwoErrorMessages.ACCESO_ATRIBUTO_NO_STATIC_DESDE_METODO_STATIC(obtenerValor()));
            }
            tipoBase = claseActual.obtenerAtributo(nombreVar).obtenerTipo();
        } else {
            throw new SemanticException(SemanticTwoErrorMessages.VARIABLE_NO_DECLARADA(obtenerValor()));
        }

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoBase);
        }

        return tipoBase;
    }
}
