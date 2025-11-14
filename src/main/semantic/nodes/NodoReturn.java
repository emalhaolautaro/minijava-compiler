package main.semantic.nodes;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.symboltable.Tipo;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;
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

        if (metodoEsVoid) {
            if (retorno != null && !(retorno instanceof NodoExpresionVacia)) {
                // Error: método void con retorno con expresión
                Token tokError = (retorno.obtenerValor() != null) ? retorno.obtenerValor() : returnTok;
                throw new SemanticException(SemanticTwoErrorMessages.RETURN_VOID_CON_EXPRESION(tokError));
            }
            return;
        }

        if (retorno == null || retorno instanceof NodoExpresionVacia) {
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
    public void generar(OutputManager output, Unidad unidadActual){
        boolean metodoVoid = tipoMetodo instanceof TipoVoid;

        if(!metodoVoid){
            retorno.generar(output, unidadActual);
            int m_size = unidadActual.obtenerParametros().size();
            int offsetRetorno = m_size + 3;
            output.generar(Instrucciones.STORE + " " + offsetRetorno + " ; Guardar valor de retorno en M[fp+" + offsetRetorno + "]");
        }

        String nombreMetodo = unidadActual.obtenerNombre().obtenerLexema();
        // Obtener el nombre de la clase (ej: "TestNew")
        String nombreClase = unidadActual.perteneceAClase().obtenerNombre().obtenerLexema();

        // Construir la etiqueta única del epílogo (ej: "lbl_end_doble@TestNew")
        String etiquetaFin = "lbl_end_" + nombreMetodo + "@" + nombreClase;

        output.generar(Instrucciones.JUMP + " " + etiquetaFin +
                " ; salto al final del método tras return");
    }

    @Override
    public void imprimirAST(int nivel) {
        System.out.println("- ".repeat(nivel) + "Return:");
        retorno.imprimirAST(nivel + 1);
    }
}
