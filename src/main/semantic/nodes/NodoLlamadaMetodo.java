package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Token;

import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoLlamadaMetodo extends NodoExpresion{
    private Token nombre;
    private List<NodoExpresion> argumentos;
    private NodoEncadenado encadenado;

    public NodoLlamadaMetodo(Token nombre, List<NodoExpresion> argumentos) {
        this.nombre = nombre;
        this.argumentos = argumentos;
    }

    public Token obtenerNombre() {
        return nombre;
    }

    public List<NodoExpresion> obtenerArgumentos() {
        return argumentos;
    }

    public Token obtenerValor(){ return nombre; }

    public void imprimirAST(int nivel){
        System.out.println("  ".repeat(nivel) + "LlamadaMetodo: " + nombre.obtenerLexema());
        for (NodoExpresion argumento : argumentos) {
            argumento.imprimirAST(nivel + 1);
        }
        if (encadenado != null) {
            encadenado.imprimirAST(nivel + 1);
        }
    }

    @Override
    public Tipo chequear() {
        // Obtener la clase actual donde se está haciendo la llamada
        Clase claseActual = tablaSimbolos.obtenerClaseActual();
        if (claseActual == null) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.METODO_EN_CLASE_INEXISTENTE(nombre)
            );
        }

        Metodo m = claseActual.obtenerMetodo(nombre.obtenerLexema());
        if (m == null) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.METODO_INEXISTENTE(nombre)
            );
        }

        Unidad unidadActual = tablaSimbolos.obtenerUnidadActual();

        if (unidadActual instanceof Metodo && ((Metodo) unidadActual).esStatic()) {

            if (!m.esStatic()) {
                throw new SemanticException(SemanticTwoErrorMessages.METODO_DINAMICO_EN_METODO_ESTATICO(nombre));
            }
        }

        if (m.esStatic()) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.METODO_ESTATICO_INEXISTENTE(nombre)
            );
        }

        if (argumentos.size() != m.obtenerParametros().size()) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.CANTIDAD_ARGUMENTOS_INCORRECTA(
                            nombre, m.obtenerParametros().size(), argumentos.size()
                    )
            );
        }

        for (int i = 0; i < argumentos.size(); i++) {
            Tipo tipoArg = argumentos.get(i).chequear();
            Tipo tipoParam = m.obtenerParametros().get(i).obtenerTipo();

            if (!tipoArg.esCompatible(tipoParam)) {
                throw new SemanticException(
                        SemanticTwoErrorMessages.TIPO_ARGUMENTO_INCORRECTO(
                                nombre, i + 1, tipoParam, tipoArg
                        )
                );
            }
        }

        Tipo tipoBase = m.obtenerTipoRetorno();

        if (encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)) {
            return encadenado.chequear(tipoBase);
        }

        return tipoBase;
    }

    public void setEncadenado(NodoEncadenado e) {
        encadenado = e;
    }

    public NodoEncadenado obtenerEncadenado() {
        return encadenado;
    }
}
