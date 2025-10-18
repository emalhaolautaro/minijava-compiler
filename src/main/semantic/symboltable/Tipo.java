package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.semantic.nodes.TipoBool;
import main.utils.Token;

public class Tipo extends TipoAbstracto{

    public Tipo(Token nombre){
        super(nombre);
    }

    public void declaracionCorrecta(TablaSimbolos ts) {
        if ("idClase".equals(nombre.obtenerTipo())) {
            Clase claseTipo = ts.obtenerClasePorNombre(nombre.obtenerLexema());
            if (claseTipo == null) {
                throw new SemanticException(
                        SemanticErrorMessages.TIPO_NO_DECLARADO(nombre)
                );
            }
        }
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tipo)) return false;
        Tipo otro = (Tipo) obj;
        // comparar por lexema en vez de por objeto
        return this.nombre.obtenerLexema().equals(otro.nombre.obtenerLexema());
    }

    public boolean esCompatible(Tipo tipo){
        return true;
    }
}
