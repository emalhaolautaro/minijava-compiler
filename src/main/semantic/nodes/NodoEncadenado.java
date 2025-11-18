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

    private NodoExpresion expActual;
    private Token id;
    private Tipo tipo;
    private NodoEncadenado encadenado;

    private Metodo metodoResuelto;
    private Atributo atributoResuelto;

    private boolean retornoReservado = false;

    public NodoEncadenado(){}

    public NodoEncadenado(NodoExpresion izquierda, Token id) {
        this.expActual = izquierda;
        this.id = id;
        this.tipo = null; // se asigna durante el chequeo
    }

    public NodoExpresion obtenerIzquierda() {
        return expActual;
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

    public void setRetornoReservado(boolean retornoReservado) {
        this.retornoReservado = retornoReservado;
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

        if (expActual instanceof NodoAccesoVar) {
            if (!claseIzq.existeAtributo(id.obtenerLexema())) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.VARIABLE_NO_DECLARADA(id));
            }

            atributoResuelto = claseIzq.obtenerAtributo(id.obtenerLexema());
            tipoActual = claseIzq.obtenerAtributo(id.obtenerLexema()).obtenerTipo();

        } else if (expActual instanceof NodoLlamadaMetodo) {
            NodoLlamadaMetodo nodoMetodo = (NodoLlamadaMetodo) expActual;

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
            throw new SemanticException(SemanticTwoErrorMessages.ENCADENADO_NO_VALIDO(expActual.obtenerValor()));
        }

        this.tipo = tipoActual;

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoActual);
        }

        return tipoActual;
    }

    public void generar(OutputManager output, Unidad unidadActual) {
        if (expActual instanceof NodoLlamadaMetodo nodoLlamada) {

            List<NodoExpresion> argumentos = nodoLlamada.obtenerArgumentos();

            for (NodoExpresion argumento : argumentos) {
                argumento.generar(output, unidadActual);
                output.generar(Instrucciones.SWAP + " ; Poner argumento sobre 'this'");
            }

            Tipo tipoRet = metodoResuelto.obtenerTipoRetorno();
            if (!(tipoRet instanceof TipoVoid)) {
                output.generar(Instrucciones.RMEM + " 1 ; Reservar espacio para valor de retorno");
                output.generar(Instrucciones.SWAP + " ; Poner ret_val debajo de 'this'");
            }

            if (!metodoResuelto.esStatic()) {
                output.generar(Instrucciones.DUP + " ; duplicar ref obj (this)");
                output.generar(Instrucciones.LOADREF + " 0 ; cargar VT");
                output.generar(Instrucciones.LOADREF + " " + metodoResuelto.obtenerOffset() + " ; cargar dir metodo");
                output.generar(Instrucciones.CALL.toString());
            } else {
                int numArgs = argumentos.size();
                for (int i = 0; i < numArgs; i++) {
                    output.generar(Instrucciones.SWAP + " ; Mover 'this' hacia arriba");
                }
                output.generar(Instrucciones.POP + " ; Descartar 'this' de llamada estática");
                output.generar(Instrucciones.PUSH + " lbl_" + metodoResuelto.obtenerNombre().obtenerLexema() + "@" + metodoResuelto.perteneceAClase().obtenerNombre().obtenerLexema());
                output.generar(Instrucciones.CALL.toString());
            }

        } else if (expActual instanceof NodoAccesoVar) {
            output.generar(Instrucciones.LOADREF + " " + atributoResuelto.obtenerOffset() + " ; cargar atributo " + id.obtenerLexema());
        }

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            if (metodoResuelto != null && !(metodoResuelto.obtenerTipoRetorno() instanceof TipoVoid)) {
                encadenado.setRetornoReservado(true);
            }
            encadenado.generar(output, unidadActual);
        }
    }

    // En NodoEncadenado.java
    public void generarParaAlmacenar(OutputManager output, Unidad unidadActual) {

        if (encadenado == null || (encadenado instanceof NodoEncadenadoVacio)) {

            if (expActual instanceof NodoAccesoVar) {
                int offset = atributoResuelto.obtenerOffset();
                output.generar(Instrucciones.SWAP + " ; Invertir valor y objeto para STOREREF");
                output.generar(Instrucciones.STOREREF + " " + offset + " ; almacenar atributo " + id.obtenerLexema());
                return;

            } else {
                // Error: no se puede asignar a un método (ej. p.metodo() = n)
                // ('chequear' ya chequea este caso)
            }
        }

        // (El encadenado NO es nulo, obtener el siguiente puntero base)

        if (expActual instanceof NodoLlamadaMetodo nodoLlamada) {

            Metodo metodo = this.metodoResuelto;
            List<NodoExpresion> argumentos = nodoLlamada.obtenerArgumentos();

            for (NodoExpresion argumento : argumentos) {
                argumento.generar(output, unidadActual);
                output.generar(Instrucciones.SWAP + " ; Poner argumento sobre 'puntero_base'");
            }
            output.generar(Instrucciones.RMEM + " 1 ; Reservar espacio para valor de retorno");
            output.generar(Instrucciones.SWAP + " ; Poner ret_val debajo de 'puntero_base'");

            if (!metodo.esStatic()) {
                output.generar(Instrucciones.DUP + " ; duplicar ref obj (this)");
                output.generar(Instrucciones.LOADREF + " 0 ; cargar VT");
                output.generar(Instrucciones.LOADREF + " " + metodo.obtenerOffset() + " ; cargar dir metodo");
                output.generar(Instrucciones.CALL.toString());
            } else {
                String etiquetaMetodo = "lbl_" + metodo.obtenerNombre().obtenerLexema() + "@" + metodo.perteneceAClase().obtenerNombre().obtenerLexema();
                output.generar(Instrucciones.PUSH + " " + etiquetaMetodo);
                output.generar(Instrucciones.CALL.toString());
            }

        } else {
            int offset = atributoResuelto.obtenerOffset();
            output.generar(Instrucciones.LOADREF + " " + offset + " ; Cargar puntero intermedio " + id.obtenerLexema());
        }

        encadenado.generarParaAlmacenar(output, unidadActual);
    }
}
