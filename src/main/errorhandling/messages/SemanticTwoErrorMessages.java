package main.errorhandling.messages;

import main.semantic.nodes.NodoExpresion;
import main.utils.Token;

public class SemanticTwoErrorMessages {
    private static String format(String mensaje, Token token) {
        StringBuilder sb = new StringBuilder();

        sb.append("Error Semántico: ").append(mensaje);
        sb.append("\n");  // usar siempre \n en vez de System.lineSeparator()
        sb.append("[Error:").append(token.obtenerLexema())
                .append("|").append(token.obtenerLinea())
                .append("]");

        return sb.toString();
    }


    public static String IF_COND_NO_BOOL(NodoExpresion condicion) {
        String mensaje = "La condicion del if no es booleana: " + condicion.obtenerValor().obtenerLexema();
        return format(mensaje, condicion.obtenerValor());
    }

    public static String WHILE_COND_NO_BOOL(NodoExpresion condicion) {
        String mensaje = "La condicion del while no es booleana: " + condicion.obtenerValor().obtenerLexema();
        return format(mensaje, condicion.obtenerValor());
    }
}
