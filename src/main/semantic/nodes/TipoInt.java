package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoInt extends Tipo {
    public TipoInt(Token valor) {
        super(valor);
    }

    public boolean esCompatible(Tipo t){
        return t instanceof TipoInt;
    }
}
