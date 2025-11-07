package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.utils.ElementoConOffset;
import main.utils.Token;

public class Atributo extends Elemento implements ElementoConOffset {
    private Tipo tipo;
    private Token modificador;
    private int Offset;

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

    public void setOffset(int i) {
        Offset = i;
    }

    public int obtenerOffset() {
        return Offset;
    }

    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Atributo: " + tipo.obtenerNombre().obtenerLexema() + " " + this.obtenerNombre().obtenerLexema());
        System.out.println("- ".repeat(i + 1) + "Offset: " + Offset);
    }

    public boolean esStatic() {
        return modificador != null && "static".equals(modificador.obtenerLexema());
    }
}
