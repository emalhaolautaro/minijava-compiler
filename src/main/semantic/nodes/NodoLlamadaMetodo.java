package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Metodo;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.Token;

import java.util.List;

import static main.Main.tablaSimbolos;

public class NodoLlamadaMetodo extends NodoExpresion{
    private Token nombre;
    private List<NodoExpresion> argumentos;
    private NodoEncadenado encadenado;
    private boolean esEstatico = false;
    private boolean esEncadenado = false;

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

    public void setEsEstatico(boolean esEstatico){
        this.esEstatico = esEstatico;
    }

    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "LlamadaMetodo: " + nombre.obtenerLexema());
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

        if(m.esStatic()) {
            esEstatico = true;
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

    public void setearEsEncadenado(boolean esEncadenado){
        this.esEncadenado = esEncadenado;
    }

    public boolean obtenerEsEncadenado(){
        return esEncadenado;
    }

    public void generar(OutputManager output, Unidad unidadActual) {
        Clase clase = unidadActual.perteneceAClase();
        Metodo metodo = clase.obtenerMetodo(nombre.obtenerLexema());
        Clase clasePertenece = metodo.perteneceAClase();
        System.out.println("esEstatico = " + esEstatico + " ; método = " + nombre.obtenerLexema());

        Tipo tipoRetorno = metodo.obtenerTipoRetorno();
        if (!(tipoRetorno instanceof TipoVoid)) {
            output.generar(Instrucciones.RMEM + " 1" + " ; Reservar espacio para el valor de retorno");
        }

        System.out.println(argumentos.size());
        for(NodoExpresion arg: argumentos){
            System.out.println("→ Generando argumento: " + arg.getClass().getSimpleName());
            arg.generar(output, unidadActual);
        }

        if(esEstatico){
            System.out.println(unidadActual.perteneceAClase().obtenerNombre().obtenerLexema());
            output.generar(Instrucciones.PUSH + " lbl_" + nombre.obtenerLexema() + "@" + clasePertenece.obtenerNombre().obtenerLexema());
            output.generar(Instrucciones.CALL.toString());
        }else{
            output.generar(Instrucciones.LOAD + " 3");
            for (int i = 0; i < argumentos.size(); i++) {
                output.generar(Instrucciones.SWAP.toString());
            }
            output.generar(Instrucciones.DUP.toString());
            output.generar(Instrucciones.LOADREF + " 0" + " ; Cargar puntero a la VT");
            int offsetMetodo = metodo.obtenerOffset(); // (Offset calculado en Clase.calcularOffsetMetodos)
            output.generar(Instrucciones.LOADREF + " " + offsetMetodo + " ; Cargar dirección del método (offset " + offsetMetodo + ")");
            output.generar(Instrucciones.CALL.toString());
        }

        if(encadenado != null && !(encadenado instanceof NodoEncadenadoVacio)){
            encadenado.generar(output, unidadActual);
        }
    }
}
