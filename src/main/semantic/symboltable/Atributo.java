package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.utils.Token;

public class Atributo extends Elemento {
    private Tipo tipo;
    private Token modificador;

    public Atributo(Token nombre, Tipo tipo, Token modificador) {
        super(nombre);
        this.tipo = tipo;
        this.modificador = modificador;
    }

    public Tipo obtenerTipo() {
        return tipo;
    }

    public Token obtenerModificador() {
        return modificador;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        // Chequeos semánticos del atributo
        tipo.declaracionCorrecta(ts);
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {

    }

    // Dentro de tu clase Atributo
    @Override
    public String toString() {
        // Retorna algo como "int contador;"
        return this.tipo.obtenerNombre().obtenerLexema() + " " + this.obtenerNombre().obtenerLexema() + ";";
    }
}
