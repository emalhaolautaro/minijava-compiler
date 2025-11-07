package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoExpresionUnaria extends NodoExpresion{
    private NodoExpresion operando;
    private Token operador;

    public NodoExpresionUnaria(NodoExpresion operando, Token operador){
        this.operando = operando;
        this.operador = operador;
    }

    public NodoExpresion obtenerOperando(){
        return operando;
    }

    public Token obtenerOperador(){
        return operador;
    }

    public void imprimirAST(int i) {
        System.out.println("- ".repeat(i) + "Expresion Unaria: " + operador.obtenerLexema());
        operando.imprimirAST(i + 1);
    }

    public Tipo chequear() {
        /*
        Los operadores unarios +, -, ++ y -- trabajan solo con subexpresiones int y siempre retornan un
        resultado de tipo int. En caso de tener una subexpresion de otro tipo se producira un error de tipos.
        Por otra parte, el operador unario ! trabaja solo con subexpresiones boolean y retorna resultados
        boolean. Al igual que con los demas operadores unarios, si la subexpresion es de otro tipo se debera
        reportar un error de tipos
        */
        Tipo tipoOperando = operando.chequear();
        String op = operador.obtenerTipo();

        if(op.equals("MenosMenos") || op.equals("MasMas") || op.equals("Mas") || op.equals("Menos")){
            if(tipoOperando instanceof TipoInt)
                return new TipoInt(operador);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_UNARIA(tipoOperando.obtenerNombre().obtenerLexema(), operador));
        }
        else if(op.equals("Not")){
            if(tipoOperando instanceof TipoBool)
                return new TipoBool(operador);
            else
                throw new SemanticException(SemanticTwoErrorMessages.TIPOS_INCOMPATIBLES_UNARIA(tipoOperando.obtenerNombre().obtenerLexema(), operador));
        }
        return new TipoNull(operador);
    }
}
