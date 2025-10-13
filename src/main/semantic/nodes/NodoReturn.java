package main.semantic.nodes;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion retorno;

    public NodoReturn(NodoExpresion expresion) {
        retorno = expresion;
    }

    public NodoExpresion obtenerRetorno(){
        return retorno;
    }

    @Override
    public void chequear() {
        // Implementar la lógica de chequeo para el retorno
        // Por ejemplo, verificar que el tipo de la expresión coincida con el tipo de retorno del método
    }

}
