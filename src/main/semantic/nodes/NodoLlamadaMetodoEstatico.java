package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import java.util.ArrayList;
import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoLlamadaMetodoEstatico extends NodoExpresion{
    private Token clase;
    private Token metodo;
    private List<NodoExpresion> argumentos = new ArrayList<>();
    private NodoEncadenado encadenado;

    public NodoLlamadaMetodoEstatico(Token clase, Token metodo, List<NodoExpresion> parametros) {
        this.clase = clase;
        this.metodo = metodo;
        this.argumentos = parametros;
    }

    public void establecerClase(Token clase) {
        this.clase = clase;
    }

    public void establecerMetodo(Token metodo) {
        this.metodo = metodo;
    }

    public void agregarParametro(NodoExpresion parametro) {
        this.argumentos.add(parametro);
    }

    public Token obtenerClase() {
        return clase;
    }

    public Token obtenerMetodo() {
        return metodo;
    }

    public List<NodoExpresion> obtenerParametros() {
        return argumentos;
    }

    public void imprimirAST(int nivel){
        System.out.println("  ".repeat(nivel) + "LlamadaMetodoEstatico: " + clase.obtenerLexema() + "." + metodo.obtenerLexema());
        for (NodoExpresion parametro : argumentos) {
            parametro.imprimirAST(nivel + 1);
        }
        if (encadenado != null) {
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        Clase c = tablaSimbolos.obtenerClasePorNombre(clase.obtenerLexema());

        if (c == null)
            throw new SemanticException(SemanticTwoErrorMessages.CLASE_NO_DECLARADA(clase));

        Metodo m = c.obtenerMetodo(metodo.obtenerLexema());

        if (m == null)
            throw new SemanticException(SemanticTwoErrorMessages.METODO_ESTATICO_INEXISTENTE(metodo));

        if (!m.esStatic()) {
            throw new SemanticException(SemanticTwoErrorMessages.METODO_NO_ESTATICO(metodo, clase));
        }

        if (argumentos.size() != m.obtenerParametros().size()) {
            throw new SemanticException(SemanticTwoErrorMessages.CANTIDAD_ARGUMENTOS_INCORRECTA(
                    metodo,
                    m.obtenerParametros().size(),
                    argumentos.size()
            ));
        }

        for (int i = 0; i < argumentos.size(); i++) {
            Tipo tipoArgumento = argumentos.get(i).chequear();
            Tipo tipoParametro = m.obtenerParametros().get(i).obtenerTipo();
            if (!tipoArgumento.esCompatible(tipoParametro)) {
                throw new SemanticException(SemanticTwoErrorMessages.TIPO_ARGUMENTO_INCORRECTO(
                        metodo,
                        i + 1,
                        tipoParametro,
                        tipoArgumento
                ));
            }
        }

        Tipo tipoBase = m.obtenerTipoRetorno();

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoBase);
        }

        return tipoBase;
    }

    public void setEncadenado(NodoEncadenado e) {
        this.encadenado = e;
    }

    public NodoEncadenado getEncadenado() {
        return encadenado;
    }

    public Token obtenerValor(){ return metodo; }
}
