package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Unidad extends Elemento{
    private List<Parametro> parametros;

    public Unidad(Token nombre){
        super(nombre);
        parametros = new ArrayList<>();
    }

    public void agregarParametro(Parametro parametro) {
        if(parametros.contains(parametro)){
            // Error: parámetro ya declarado
            throw new SemanticException(SemanticErrorMessages.PARAMETRO_EXISTENTE(parametro.obtenerNombre(), parametro.obtenerNombre().obtenerLexema(), this.nombre.obtenerLexema()));
        }
        parametros.add(parametro);
    }

    public List<Parametro> obtenerParametros() {
        return parametros;
    }

    @Override
    public abstract void declaracionCorrecta(TablaSimbolos ts);

    public void consolidar(TablaSimbolos ts) throws SemanticException{}
}
