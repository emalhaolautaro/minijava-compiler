package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoLlamadaMetodo extends NodoExpresion{
    private Token nombre;
    private List<NodoExpresion> argumentos;
    private NodoExpresion encadenado;

    public NodoLlamadaMetodo(Token nombre, List<NodoExpresion> argumentos) {
        this.nombre = nombre;
        this.argumentos = argumentos;
    }

    public NodoLlamadaMetodo(Token nombre, List<NodoExpresion> argumentos, NodoExpresion base) {
        this.nombre = nombre;
        this.argumentos = argumentos;
        this.encadenado = base;
    }

    public Token obtenerNombre() {
        return nombre;
    }

    public List<NodoExpresion> obtenerArgumentos() {
        return argumentos;
    }

    public void imprimirAST(int nivel){
        System.out.println("  ".repeat(nivel) + "LlamadaMetodo: " + nombre.obtenerLexema());
        for (NodoExpresion argumento : argumentos) {
            argumento.imprimirAST(nivel + 1);
        }
    }

    public NodoExpresion obtenerEncadenado(){
        return encadenado;
    }

    public void agregarEncadenado(NodoExpresion encadenado){
        this.encadenado = encadenado;
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

        // Buscar el método en la clase actual o en sus ancestros
        Metodo m = claseActual.obtenerMetodo(nombre.obtenerLexema());
        if (m == null) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.METODO_INEXISTENTE(nombre)
            );
        }

        // Verificar que no sea estático
        if (m.esStatic()) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.METODO_ESTATICO_INEXISTENTE(nombre)
            );
        }

        // Verificar cantidad de argumentos
        if (argumentos.size() != m.obtenerParametros().size()) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.CANTIDAD_ARGUMENTOS_INCORRECTA(
                            nombre, m.obtenerParametros().size(), argumentos.size()
                    )
            );
        }

        // Chequear tipos de cada argumento
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

        // Retornar tipo de retorno del método
        return m.obtenerTipoRetorno();
    }
}
