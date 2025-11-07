// Elemento.java
package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.utils.Primeros;
import main.utils.PrimerosImpl;
import main.utils.Token;

public abstract class Elemento {
    protected Token nombre;
    protected Primeros primeros = new PrimerosImpl();
    // Otros campos comunes

    public Elemento(){}

    public Elemento(Token nombre){
        this.nombre = nombre;
    }

    public abstract void declaracionCorrecta(TablaSimbolos ts);

    public Token obtenerNombre() {
        return nombre;
    }

    public abstract void consolidar(TablaSimbolos ts) throws SemanticException;
}

