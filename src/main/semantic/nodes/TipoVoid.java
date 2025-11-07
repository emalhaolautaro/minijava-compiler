package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoVoid extends Tipo {
    public TipoVoid(Token nombre) {
        super(nombre);
    }

    public boolean esCompatible(Tipo t){
        return t instanceof TipoVoid;
    }

    public String obtenerTipo(){
        return "void";
    }
}
