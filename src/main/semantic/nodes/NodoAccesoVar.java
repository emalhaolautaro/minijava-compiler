package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.*;
import main.utils.Instrucciones;
import main.utils.Token;

import static main.Main.tablaSimbolos;

public class NodoAccesoVar extends NodoExpresion{
    private NodoEncadenado encadenado;

    private NodoVarLocal varLocal;
    private Parametro parametro;
    private Atributo atributo;

    public NodoAccesoVar(Token id){
        super(id);
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoAccesoVar: " + obtenerValor().obtenerLexema());
        if(encadenado != null){
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();
        Clase claseActual = tablaSimbolos.obtenerClaseActual();
        String nombreVar = obtenerValor().obtenerLexema();

        Tipo tipoBase;

        if (unidadActual.existeVariableLocal(nombreVar)) {
            varLocal = unidadActual.obtenerVariableLocal(nombreVar);
            tipoBase = varLocal.obtenerTipo();
        } else if (unidadActual.existeParametro(nombreVar)) {
            parametro = unidadActual.obtenerParametro(nombreVar);
            tipoBase = parametro.obtenerTipo();
        } else if (claseActual.existeAtributo(nombreVar)) {
            if (unidadActual instanceof Metodo && ((Metodo) unidadActual).esStatic()) {
                throw new SemanticException(SemanticTwoErrorMessages.ACCESO_ATRIBUTO_NO_STATIC_DESDE_METODO_STATIC(obtenerValor()));
            }
            atributo = claseActual.obtenerAtributo(nombreVar);
            tipoBase = atributo.obtenerTipo();
        } else {
            throw new SemanticException(SemanticTwoErrorMessages.VARIABLE_NO_DECLARADA(obtenerValor()));
        }

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoBase);
        }

        return tipoBase;
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        String nombreVar = obtenerValor().obtenerLexema();
        System.out.println("Intentando acceder a la variable: " + nombreVar);
        // Caso 1: variable local
        if (varLocal != null) {
            int offset = varLocal.obtenerOffset();
            System.out.println("Accedi a la variable: " + nombreVar);
            output.generar("LOAD " + offset + " ; Cargar variable local " + nombreVar);
        }

        // Caso 2: parámetro
        if (parametro != null) {
            int offset = parametro.obtenerOffset();
            System.out.println("Accedi al parametro: " + nombreVar);
            output.generar("LOAD " + offset + " ; Cargar parámetro " + nombreVar);
        }

        // Caso 3: atributo (requiere this)
        if (atributo != null) {
            int offset = atributo.obtenerOffset();
            System.out.println("Accedi al atributo: " + nombreVar);
            output.generar("LOAD 3 ; Cargar this");
            output.generar("LOADREF " + offset + " ; Cargar atributo " + nombreVar);
        }

        if(encadenado != null && !(encadenado instanceof NodoEncadenadoVacio))
            encadenado.generar(output, unidadActual);
    }

    public void generarParaAlmacenar(OutputManager output){
        int offset;

        // Caso 1: Variable local
        if (varLocal != null) {
            offset = varLocal.obtenerOffset();
            output.generar(Instrucciones.STORE + " " + offset);
        }

        // Caso 2: Parámetro
        else if (parametro != null) {
            offset = parametro.obtenerOffset();
            output.generar(Instrucciones.STORE + " " + offset);
        }

        // Caso 3: Atributo de instancia
        else {
            if (atributo != null) {
                offset = atributo.obtenerOffset();
                output.generar(Instrucciones.LOAD + " 3");
                output.generar(Instrucciones.SWAP.toString());
                output.generar(Instrucciones.STOREREF + " " + offset);
            }
        }
    }
}
