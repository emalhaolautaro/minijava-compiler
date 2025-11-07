package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Parametro;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import java.util.ArrayList;
import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoEncadenado {

    private NodoExpresion izquierda;
    private Token id;
    private Tipo tipo;
    private NodoEncadenado encadenado;

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
}
