package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.nodes.NodoVarLocal;
import main.utils.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class Unidad extends Elemento{
    private List<Parametro> parametros;
    private List<NodoVarLocal> variablesLocales;


    public Unidad(Token nombre){
        super(nombre);
        parametros = new ArrayList<>();
        variablesLocales = new ArrayList<>();
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

    public Parametro obtenerParametro(String nombre) {
        for(Parametro parametro : parametros){
            if(parametro.obtenerNombre().obtenerLexema().equals(nombre)){
                return parametro;
            }
        }
        return null;
    }

    public Tipo obtenerTipoRetorno(){
        return null;
    }

    public List<NodoVarLocal> obtenerVariablesLocales(){ return variablesLocales; }

    public NodoVarLocal obtenerVariableLocal(String nombre) {
        for(NodoVarLocal varLocal : variablesLocales){
            if(varLocal.obtenerVar().obtenerLexema().equals(nombre)){
                return varLocal;
            }
        }
        return null;
    }

    @Override
    public abstract void declaracionCorrecta(TablaSimbolos ts);

    public void consolidar(TablaSimbolos ts) throws SemanticException{}

    public boolean existeVariableLocal(String nombre) {
        for(NodoVarLocal varLocal : variablesLocales){
            if(varLocal.obtenerVar().obtenerLexema().equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public boolean existeParametro(String nombre) {
        for(Parametro parametro : parametros){
            if(parametro.obtenerNombre().obtenerLexema().equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public void agregarVariableLocal(NodoVarLocal varLocal) {
        if(existeVariableLocal(varLocal.obtenerVar().obtenerLexema()) || existeParametro(varLocal.obtenerVar().obtenerLexema())){
            // Error: variable local ya declarada
            throw new SemanticException(SemanticTwoErrorMessages.VAR_LOCAL_EXISTENTE_ERR(nombre));
        }
        variablesLocales.add(varLocal);
    }
}
