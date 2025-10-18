package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoUniversal extends Tipo {
    public TipoUniversal(Token token) {
        super(token);
    }

    public boolean esCompatible(Tipo t){
        return true;
    }
}
