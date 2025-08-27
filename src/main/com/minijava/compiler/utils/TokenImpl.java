package main.com.minijava.compiler.utils;

public class TokenImpl implements Token {
    private String tipo;
    private String lexema;
    private int linea;

    public TokenImpl(String tipo, String lexema, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
    }

    @Override
    public String obtenerTipo() {
        return tipo;
    }

    @Override
    public String obtenerLexema() {
        return lexema;
    }

    @Override
    public int obtenerLinea() {
        return linea;
    }

    public String toString() {
        return "Token{" +
                "tipo='" + tipo + '\'' +
                ", lexema='" + lexema + '\'' +
                ", linea=" + linea +
                '}';
    }
}
