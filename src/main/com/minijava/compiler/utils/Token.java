package main.com.minijava.compiler.utils;

public interface Token {
    public String obtenerTipo();
    public String obtenerLexema();
    public int obtenerLinea();
}
