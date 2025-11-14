package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.*;
import main.utils.Instrucciones;
import main.utils.Token;

import java.util.ArrayList;
import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoEncadenado {

    private NodoExpresion izquierda;
    private Token id;
    private Tipo tipo;
    private NodoEncadenado encadenado;

    private Metodo metodoResuelto;
    private Atributo atributoResuelto;

    public NodoEncadenado(){}

    public NodoEncadenado(NodoExpresion izquierda, Token id) {
        this.izquierda = izquierda;
        this.id = id;
        this.tipo = null; // se asigna durante el chequeo
    }

    public NodoExpresion obtenerIzquierda() {
        return izquierda;
    }

    public Token obtenerId() {
        return id;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }

    public Metodo obtenerMetodoResuelto(){
        return metodoResuelto;
    }

    public Tipo obtenerTipo() {
        return tipo;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoEncadenado: " + id.obtenerLexema());
        if(encadenado != null){
            encadenado.imprimirAST(nivel + 1);
        }
    }

    public Tipo chequear(Tipo tipoIzquierdo) {
        Tipo tipoActual;

        if (!(tipoIzquierdo instanceof TipoClase)) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.TIPO_NO_ES_CLASE(id));
        }

        Clase claseIzq = tablaSimbolos.obtenerClasePorNombre(((TipoClase) tipoIzquierdo).obtenerNombreClase());

        if (izquierda instanceof NodoAccesoVar) {
            if (!claseIzq.existeAtributo(id.obtenerLexema())) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.VARIABLE_NO_DECLARADA(id));
            }

            atributoResuelto = claseIzq.obtenerAtributo(id.obtenerLexema());
            tipoActual = claseIzq.obtenerAtributo(id.obtenerLexema()).obtenerTipo();

        } else if (izquierda instanceof NodoLlamadaMetodo) {
            NodoLlamadaMetodo nodoMetodo = (NodoLlamadaMetodo) izquierda;

            if (!claseIzq.existeMetodo(id.obtenerLexema())) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.METODO_NO_DECLARADO(id));
            }

            Metodo metodo = claseIzq.obtenerMetodo(id.obtenerLexema());

            List<NodoExpresion> argsActuales = nodoMetodo.obtenerArgumentos();
            List<Tipo> parametrosFormales = new ArrayList<>();
            for (Parametro p : metodo.obtenerParametros()) {
                parametrosFormales.add(p.obtenerTipo());
            }

            if (argsActuales.size() != parametrosFormales.size()) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.PARAMETROS_INCORRECTOS(id));
            }

            for (int i = 0; i < argsActuales.size(); i++) {
                Tipo tipoArg = argsActuales.get(i).chequear();
                Tipo tipoFormal = parametrosFormales.get(i);

                if (!tipoArg.esCompatible(tipoFormal)) {
                    throw new SemanticException(
                            SemanticTwoErrorMessages.PARAMETROS_INCORRECTOS(id));
                }
            }

            metodoResuelto = metodo;
            tipoActual = metodo.obtenerTipoRetorno();

        } else {
            throw new SemanticException(SemanticTwoErrorMessages.ENCADENADO_NO_VALIDO(izquierda.obtenerValor()));
        }

        // Guardar el tipo actual
        this.tipo = tipoActual;

        // Propagar al siguiente encadenado
        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoActual);
        }

        return tipoActual;
    }

    public void generar(OutputManager output, Unidad unidadActual) {
        if (metodoResuelto != null) {
            if (!metodoResuelto.esStatic()) {
                output.generar(Instrucciones.DUP + " ; duplicar ref obj");
                output.generar(Instrucciones.LOADREF + " 0 ; cargar VT");
                output.generar(Instrucciones.LOADREF + " " + metodoResuelto.obtenerOffset() + " ; cargar dir metodo " + metodoResuelto.obtenerNombre().obtenerLexema());
                output.generar(Instrucciones.CALL.toString());
            } else {
                output.generar(Instrucciones.POP + " ; Sacar la referencia al objeto (no se usa)");
                output.generar(Instrucciones.PUSH + " lbl_" + metodoResuelto.obtenerNombre().obtenerLexema() + "@" + metodoResuelto.perteneceAClase().obtenerNombre().obtenerLexema());
                output.generar(Instrucciones.CALL.toString());
            }

        } else if (atributoResuelto != null) {
            output.generar(Instrucciones.LOADREF + " " + atributoResuelto.obtenerOffset() + " ; cargar atributo " + atributoResuelto.obtenerNombre().obtenerLexema());
        }

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            encadenado.generar(output, unidadActual);
        }
    }

    public void generarParaAlmacenar(OutputManager output, Unidad unidadActual) {
        if (!(tipo instanceof TipoClase)) {
            Clase claseIzq = unidadActual.perteneceAClase();
            Atributo atributo = claseIzq.obtenerAtributo(id.obtenerLexema());
            int offset = atributo.obtenerOffset();

            output.generar(Instrucciones.STOREREF + " " + offset + " ; almacenar atributo " + id.obtenerLexema());
            return;
        }

        if (encadenado != null) {
            encadenado.generarParaAlmacenar(output, unidadActual);
        }
    }
}
