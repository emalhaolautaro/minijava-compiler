package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metodo extends Unidad {
    private Token modificador;
    private Tipo tipoRetorno;
    private boolean tieneCuerpo = false;
    private boolean esPredefinido = false;

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
    }

    public Token obtenerModificador() {
        return modificador;
    }

    public Tipo obtenerTipoRetorno() {
        return tipoRetorno;
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
}
