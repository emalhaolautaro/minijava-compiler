package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.utils.Token;

public class Parametro extends Elemento{
    private Tipo tipo;

    public Parametro(Tipo tipo, Token nombre) {
        super(nombre);
        this.tipo = tipo;
    }

    public Tipo obtenerTipo() {
        return tipo;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        // Solo si es un tipo de clase
        if ("idClase".equals(tipo.obtenerNombre().obtenerTipo())) {
            Clase claseTipo = ts.obtenerClasePorLexema(tipo.obtenerNombre().obtenerLexema());
            if (claseTipo == null) {
                throw new SemanticException(
                        SemanticErrorMessages.PARAMETRO_TIPO_NO_DECLARADO(
                                tipo.obtenerNombre(),
                                nombre.obtenerLexema(),
                                ts.obtenerUnidadActual().obtenerNombre().obtenerLexema()
                        )
                );
            }
        }

        // Delegar a Tipo para que haga sus propios chequeos semánticos si tiene
        tipo.declaracionCorrecta(ts);
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Parametro)) return false;
        Parametro otro = (Parametro) obj;
        return this.nombre.obtenerLexema().equals(otro.nombre.obtenerLexema()) &&
                this.tipo.nombre.obtenerLexema().equals(otro.tipo.nombre.obtenerLexema());
    }

    // Dentro de tu clase Parametro
    @Override
    public String toString() {
        // Retorna algo como "int cantidad" o "MiClase referencia"
        return this.tipo.obtenerNombre().obtenerLexema() + " " + this.obtenerNombre().obtenerLexema();
    }
}
