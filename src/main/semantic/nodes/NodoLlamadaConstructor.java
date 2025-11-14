package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Constructor;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
import main.utils.Token;

import java.util.List;
import java.util.Map;

import static main.Main.tablaSimbolos;

public class NodoLlamadaConstructor extends NodoExpresion{
    private List<NodoExpresion> argumentos;
    private NodoEncadenado encadenado;
    private Clase claseResuelta;
    private Constructor constructorResuelto;
    private boolean esLadoDerechoAsignacion = false;

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

    public void setEsLadoDerechoAsignacion(boolean esLadoDerechoAsignacion){
        this.esLadoDerechoAsignacion = esLadoDerechoAsignacion;
    }

    public boolean esLadoDerechoAsignacion(){
        return esLadoDerechoAsignacion;
    }

    @Override
    public Tipo chequear() {
        Clase clase = tablaSimbolos.obtenerClasePorNombre(this.obtenerValor().obtenerLexema());
        claseResuelta = clase;

        if (!tablaSimbolos.existeClase(this.obtenerValor().obtenerLexema())) {
            throw new SemanticException(
                    SemanticTwoErrorMessages.CLASE_NO_DECLARADA(obtenerValor()));
        }

        boolean encontrado = false;
        Map<String, Constructor> constructor = clase.obtenerConstructor();
        for (Constructor c : constructor.values()) {
            if (c.coincideParametros(argumentos)) {
                encontrado = true;
                constructorResuelto = c;
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

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        int k_size = 1 + claseResuelta.obtenerAtributos().size();

        String l_malloc = "simple_malloc";
        String l_VT = "VT@" + claseResuelta.obtenerNombre().obtenerLexema();
        String l_constr = "lbl_builder@" + claseResuelta.obtenerNombre().obtenerLexema();

        output.generar(Instrucciones.RMEM + " 1" + " ; Reservar espacio para el puntero 'new'");

        output.generar(Instrucciones.PUSH + " " + k_size + " ; Apilar tamaño del CIR (k)");
        output.generar(Instrucciones.PUSH + " " + l_malloc);
        output.generar(Instrucciones.CALL.toString());

        output.generar(Instrucciones.DUP.toString());

        output.generar(Instrucciones.PUSH + " " + l_VT);
        output.generar(Instrucciones.STOREREF + " 0" + " ; Guardar puntero a la VT en offset 0");

        output.generar(Instrucciones.DUP.toString());

        for(NodoExpresion arg : argumentos) {
            arg.generar(output, unidadActual);
            output.generar(Instrucciones.SWAP + " ; Mover 'this' debajo del argumento");
        }

        output.generar(Instrucciones.PUSH + " " + l_constr);
        output.generar(Instrucciones.CALL.toString());
    }
}
