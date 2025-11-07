package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.nodes.NodoVarLocal;
import main.utils.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class Unidad extends Elemento{
    private List<Parametro> parametros;
    private Stack<Map<String, NodoVarLocal>> ambitosLocales;
    private List<NodoVarLocal> variablesLocales;


    public Unidad(Token nombre){
        super(nombre);
        parametros = new ArrayList<>();
        ambitosLocales = new Stack<>();
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

    public void abrirAmbito() {
        ambitosLocales.push(new HashMap<>());
    }

    public void cerrarAmbito() {
        if (!ambitosLocales.isEmpty()) {
            ambitosLocales.pop();
        }
    }

    public Tipo obtenerTipoRetorno(){
        return null;
    }

    public List<NodoVarLocal> obtenerVariablesLocales(){ return variablesLocales; }

    public NodoVarLocal obtenerVariableLocal(String nombre) {
        for (int i = ambitosLocales.size() - 1; i >= 0; i--) {
            if (ambitosLocales.get(i).containsKey(nombre)) {
                return ambitosLocales.get(i).get(nombre);
            }
        }
        return null;
    }

    @Override
    public abstract void declaracionCorrecta(TablaSimbolos ts);

    public void consolidar(TablaSimbolos ts) throws SemanticException{}

    public boolean existeVariableLocal(String nombre) {
        for (int i = ambitosLocales.size() - 1; i >= 0; i--) {
            if (ambitosLocales.get(i).containsKey(nombre)) {
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
        String nombreVar = varLocal.obtenerVar().obtenerLexema();

        if (ambitosLocales.peek().containsKey(nombreVar)) {
            throw new SemanticException(SemanticTwoErrorMessages.VAR_LOCAL_EXISTENTE_ERR(varLocal.obtenerVar()));
        }

        if (this.existeVariableLocal(nombreVar) || this.existeParametro(nombreVar)) {
            throw new SemanticException(SemanticTwoErrorMessages.VAR_LOCAL_EXISTENTE_ERR(varLocal.obtenerVar()));
        }

        ambitosLocales.peek().put(nombreVar, varLocal);
    }

    public abstract void generar(OutputManager output, String nombreClase);
}
