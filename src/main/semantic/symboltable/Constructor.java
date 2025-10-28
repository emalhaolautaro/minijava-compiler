package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.semantic.nodes.NodoBloque;
import main.semantic.nodes.NodoExpresion;
import main.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constructor extends Unidad {
    private Token visibilidad;
    private NodoBloque bloque;

    public Constructor(Token nombre, Token visibilidad) {
        super(nombre);
        this.visibilidad = visibilidad;
    }

    public Token obtenerVisibilidad() {
        return visibilidad;
    }

    public void agregarBloque(NodoBloque b){
        this.bloque = b;
    }

    public NodoBloque obtenerBloque(){
        return bloque;
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

    public void chequearSentencias() {
        this.abrirAmbito();
        bloque.chequear();
        this.cerrarAmbito();
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Constructor: " + nombre.obtenerLexema());
        if (!obtenerParametros().isEmpty()) {
            System.out.println("- ".repeat(nivel + 1) + "Parametros:");
            for (Parametro p : obtenerParametros()) {
                // Asumo que Parametro tiene un imprimirAST o un toString()
                p.imprimirAST(nivel + 2);
            }
        }
        if (bloque != null) {
            bloque.imprimirAST(nivel + 1);
        }
    }

    public boolean coincideParametros(List<NodoExpresion> argumentos) {
        List<Parametro> parametros = this.obtenerParametros();
        if (parametros.size() != argumentos.size()) {
            return false;
        }

        for (int i = 0; i < parametros.size(); i++) {
            Tipo tipoParametro = parametros.get(i).obtenerTipo();
            Tipo tipoArgumento = argumentos.get(i).chequear();

            if (!tipoParametro.esCompatible(tipoArgumento)) {
                return false;
            }
        }

        return true;
    }
}