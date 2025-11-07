package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.nodes.NodoBloqueNulo;
import main.utils.ElementoConOffset;
import main.utils.Instrucciones;
import main.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metodo extends Unidad implements ElementoConOffset {
    private Token modificador;
    private Tipo tipoRetorno;
    private boolean tieneCuerpo = false;
    private boolean esPredefinido = false;
    private Clase pertenece;
    private NodoBloque bloque;
    private int Offset;

    public boolean esPredefinido() {
        return esPredefinido;
    }

    public void marcarComoPredefinido() {
        this.esPredefinido = true;
    }

    public Metodo(Token modificador, Tipo tipoRetorno, Token nombre) {
        super(nombre);
        this.modificador = modificador;
        this.tipoRetorno = tipoRetorno;
        bloque = new NodoBloqueNulo();
    }

    public Token obtenerModificador() {
        return modificador;
    }

    @Override
    public Tipo obtenerTipoRetorno() {
        return tipoRetorno;
    }

    public void setearClase(Clase c){
        pertenece = c;
    }

    public Clase perteneceAClase(){
        return pertenece;
    }

    public void agregarBloque(NodoBloque b){
        bloque = b;
    }

    public NodoBloque obtenerBloque(){
        return bloque;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        tipoRetorno.declaracionCorrecta(ts);
        for (Parametro p : obtenerParametros()) {
            p.declaracionCorrecta(ts);
        }

        Map<String, Parametro> param = new HashMap<>();
        for(Parametro p: obtenerParametros()){
            if(p.obtenerNombre().obtenerLexema() != null && param.containsKey(p.obtenerNombre().obtenerLexema())){
                throw new SemanticException(SemanticErrorMessages.PARAMETRO_EXISTENTE(p.obtenerNombre(), obtenerNombre().obtenerLexema(), this.nombre.obtenerLexema()));
            }
            param.put(p.obtenerNombre().obtenerLexema(), p);
        }
    }

    public boolean mismaFirma(Metodo otro) {
        List<Parametro> params1 = this.obtenerParametros();
        List<Parametro> params2 = otro.obtenerParametros();

        if (params1.size() != params2.size()) return false;

        for (int i = 0; i < params1.size(); i++) {
            if (!params1.get(i).obtenerTipo().equals(params2.get(i).obtenerTipo())) {
                return false;
            }
        }
        return true;
    }

    public boolean esFinal() {
        return modificador != null && "final".equals(modificador.obtenerLexema());
    }

    public boolean esStatic() {
        return modificador != null && "static".equals(modificador.obtenerLexema());
    }

    public boolean esAbstracto() {
        return modificador != null && "abstract".equals(modificador.obtenerLexema());
    }

    public boolean tieneCuerpo(){
        return tieneCuerpo;
    }

    public void setearCuerpo(){
        tieneCuerpo = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Metodo)) return false;
        Metodo otro = (Metodo) obj;

        // Comparar nombre y misma firma
        return this.nombre.obtenerLexema().equals(otro.nombre.obtenerLexema()) &&
                this.mismaFirma(otro);
    }

    // Dentro de tu clase Metodo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Añadir modificador si existe
        if (this.modificador != null) {
            sb.append(this.modificador.obtenerLexema()).append(" ");
        }

        // Añadir tipo de retorno y nombre
        sb.append(this.tipoRetorno.obtenerNombre().obtenerLexema()).append(" ").append(this.obtenerNombre().obtenerLexema()).append("(");

        // Unir los parámetros con comas
        List<String> paramsComoString = new ArrayList<>();
        for (Parametro p : obtenerParametros()) {
            paramsComoString.add(p.toString());
        }
        sb.append(String.join(", ", paramsComoString));

        sb.append(")");
        return sb.toString();
    }

    public void chequearSentencias() {
        this.abrirAmbito();
        bloque.chequear();
        this.cerrarAmbito();
    }

    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Metodo: " + nombre.obtenerLexema());
        System.out.println("- ".repeat(nivel + nivel) + "Offset: " + Offset);
        if (!obtenerParametros().isEmpty()) {
            System.out.println("- ".repeat(nivel + 1) + "Parametros:");
            for (Parametro p : obtenerParametros()) {
                p.imprimirAST(nivel + 2);
            }
        }
        if (bloque != null) {
            bloque.imprimirAST(nivel + 1);
        }
    }

    public void setOffset(int i){
        Offset = i;
    }

    public int obtenerOffset() {
        return Offset;
    }

    @Override
    public void generar(OutputManager output, String nombreClase) {
        String nombreMetodo;
        nombreMetodo = nombre.obtenerLexema() + "_" + nombreClase;
        output.generar("lbl_" + nombreMetodo + ": " + Instrucciones.NOP);
        output.generar("");
    }
}
