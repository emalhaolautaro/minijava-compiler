package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constructor extends Unidad {
    private Token visibilidad;

    public Constructor(Token nombre, Token visibilidad) {
        super(nombre);
        this.visibilidad = visibilidad;
    }

    public Token obtenerVisibilidad() {
        return visibilidad;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        // Buscar la clase por lexema
        Clase clase = ts.obtenerClasePorLexema(nombre.obtenerLexema());
        if (clase == null) {
            throw new SemanticException(
                    SemanticErrorMessages.CONSTRUCTOR_INEXISTENTE(nombre, nombre.obtenerLexema())
            );
        }

        // Verificar que el constructor existe en esa clase
        if (!clase.obtenerConstructor().containsKey(nombre.obtenerLexema())) {
            throw new SemanticException(
                    SemanticErrorMessages.CONSTRUCTOR_INEXISTENTE(nombre, nombre.obtenerLexema())
            );
        }

        // Declaración correcta de los parámetros
        for (Parametro p : obtenerParametros()) {
            p.declaracionCorrecta(ts);
        }

        Map<String, Parametro> param = new HashMap<>();
        for(Parametro p: obtenerParametros()){
            if(p.obtenerNombre().obtenerLexema() != null && param.containsKey(p.obtenerNombre().obtenerLexema())){
                throw new SemanticException(SemanticErrorMessages.PARAMETRO_EXISTENTE(p.obtenerNombre(), obtenerNombre().obtenerLexema(), this.nombre.obtenerLexema()));
            }
            param.put(p.obtenerNombre().obtenerLexema(), p);
        }
    }

    // Dentro de tu clase Constructor
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(this.obtenerNombre().obtenerLexema()).append("(");

        // Unimos los parámetros con comas
        List<String> paramsComoString = new ArrayList<>();
        for (Parametro p : obtenerParametros()) {
            paramsComoString.add(p.toString());
        }
        sb.append(String.join(", ", paramsComoString));

        sb.append(")");
        return sb.toString();
    }
}