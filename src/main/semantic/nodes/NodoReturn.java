package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion retorno;
    private Tipo tipoMetodo;
    private Token returnTok;

    public NodoReturn(NodoExpresion expresion, Tipo tipoRet, Token retornoT) {
        retorno = expresion;
        tipoMetodo = tipoRet;
        returnTok = retornoT;
    }

    public NodoExpresion obtenerRetorno(){
        return retorno;
    }

    public Tipo obtenerTipoMetodo(){
        return tipoMetodo;
    }

    public void setearTipoMetodo(Tipo t){
        tipoMetodo = t;
    }

    @Override
    public void chequear() {
        boolean metodoEsVoid = tipoMetodo instanceof TipoVoid;

        // 🔹 Caso 1: método void → no puede retornar expresión
        if (metodoEsVoid) {
            if (retorno != null && !(retorno instanceof NodoExpresionVacia)) {
                // Error: método void con retorno con expresión
                Token tokError = (retorno.obtenerValor() != null) ? retorno.obtenerValor() : returnTok;
                throw new SemanticException(SemanticTwoErrorMessages.RETURN_VOID_CON_EXPRESION(tokError));
            }
            return; // ✅ correcto: return; vacío en método void
        }

        // Regla: return no puede ser vacío (return e;).
        if (retorno == null || retorno instanceof NodoExpresionVacia) {
            // Error: El método espera un retorno, pero se encontró un return vacío.
            throw new SemanticException(SemanticTwoErrorMessages.RETURN_NO_VOID_VACIO(returnTok));
        }

        // Si hay expresión, chequear la conformidad de tipos.
        // Regla: El tipo de e (expresión) debe conformar con el tipo de retorno del método.
        Tipo tipoRetornoExpresion = retorno.chequear();

        if (!tipoRetornoExpresion.esCompatible(tipoMetodo)) {
            throw new SemanticException(SemanticTwoErrorMessages.RETORNO_NO_COMPATIBLE(retorno, tipoMetodo, tipoRetornoExpresion));
        }
        // Si es compatible, es válido.
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Return:");
        retorno.imprimirAST(nivel + 1);
    }
}
