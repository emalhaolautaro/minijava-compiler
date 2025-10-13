package main.semantic.nodes;

import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.Tipo;
import main.utils.Token;
import main.errorhandling.exceptions.SemanticException;

public abstract class NodoEncadenado {

    protected NodoExpresion izquierda;
    protected Token id;
    protected Tipo tipo;

    public NodoEncadenado(NodoExpresion izquierda, Token id) {
        this.izquierda = izquierda;
        this.id = id;
        this.tipo = null; // se asigna durante el chequeo
    }

    public NodoExpresion getIzquierda() {
        return izquierda;
    }

    public Token getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public abstract Tipo chequear(TablaSimbolos tabla) throws SemanticException;
}
