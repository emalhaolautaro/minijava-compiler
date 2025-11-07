package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoAtributo extends NodoExpresion{
    Tipo tipo;
    NodoExpresion expresion;

    public NodoAtributo(Token nombre, Tipo tipo, NodoExpresion expresion){
        super(nombre);
        this.tipo = tipo;
        this.expresion = expresion;
    }

    public void setearExpresion(NodoExpresion exp){
        expresion = exp;
    }

    public NodoExpresion obtenerExpresion(){
        return expresion;
    }

    @Override
    public Tipo chequear() {
        if (expresion == null)
            return tipo;

        Tipo tipoExp = expresion.chequear();

        if (tipoExp instanceof TipoNull) {
            if (!(tipo instanceof TipoClase)) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_ATRIBUTO(
                                tipo.obtenerNombre(), tipoExp.obtenerNombre(), obtenerValor()
                        )
                );
            }
            return tipo;
        }

        if (tipoExp instanceof TipoUniversal || tipo.esCompatible(tipoExp))
            return tipo;

        throw new SemanticException(
                SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_ATRIBUTO(
                        tipo.obtenerNombre(), tipoExp.obtenerNombre(), obtenerValor()
                )
        );
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Atributo: " + tipo.obtenerNombre().obtenerLexema() + " " + obtenerValor().obtenerLexema() + ": ");
        if(expresion != null)
            expresion.imprimirAST(nivel + 1);
    }
}
