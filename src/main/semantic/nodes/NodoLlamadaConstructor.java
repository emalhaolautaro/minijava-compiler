package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Constructor;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import java.util.List;
import java.util.Map;

import static main.Main.tablaSimbolos;

public class NodoLlamadaConstructor extends NodoExpresion{
    private List<NodoExpresion> argumentos;
    private NodoEncadenado encadenado;

    public NodoLlamadaConstructor(Token nombreClase, List<NodoExpresion> argumentos) {
        super(nombreClase);
        this.argumentos = argumentos;
    }

    public List<NodoExpresion> obtenerArgumentos() {
        return argumentos;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "Llamada Constructor: " + this.obtenerValor().obtenerLexema());
        for(NodoExpresion arg : argumentos) {
            arg.imprimirAST(nivel + 1);
        }
        if (encadenado != null) {
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        Clase clase = tablaSimbolos.obtenerClasePorNombre(this.obtenerValor().obtenerLexema());

        if (!tablaSimbolos.existeClase(this.obtenerValor().obtenerLexema())) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.CLASE_NO_DECLARADA(obtenerValor()));
        }

        boolean encontrado = false;
        Map<String, Constructor> constructor = clase.obtenerConstructor();
        for (Constructor c : constructor.values()) {
            if (c.coincideParametros(argumentos)) {
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.CONSTRUCTOR_NO_ENCONTRADO(obtenerValor())
            );
        }

        Tipo tipoBase = new TipoClase(clase.obtenerNombre());

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoBase);
        }

        return tipoBase;
    }

    public void setEncadenado(NodoEncadenado e) {
        this.encadenado = e;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }

    @Override
    public Token obtenerValor() {
        return super.obtenerValor();
    }
}
