package main.semantic.nodes;

import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class TipoBool extends Tipo {

    public TipoBool(Token token) {
        super(token);
    }

    public boolean esCompatible(Tipo tipo) {
        return tipo instanceof TipoBool;
    }

}
