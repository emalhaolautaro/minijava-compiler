package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoNull extends Tipo {
    public TipoNull(Token token) {
        super(token);
    }

    public boolean esCompatible(Tipo t){
        return true;
    }
}
