package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoChar extends Tipo {
    public TipoChar(Token valor) {
        super(valor);
    }

    public boolean esCompatible(Tipo t){
        return t instanceof TipoChar;
    }

    public String obtenerTipo(){
        return "char";
    }
}
