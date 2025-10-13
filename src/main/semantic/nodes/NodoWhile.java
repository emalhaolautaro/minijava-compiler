package main.semantic.nodes;

public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;

    public NodoWhile(NodoExpresion condicion, NodoSentencia sentencia) {
        this.condicion = condicion;
        this.sentencia = sentencia;
    }

    public NodoExpresion obtenerCondicion(){
        return condicion;
    }

    public NodoSentencia obtenerSentencia(){
        return sentencia;
    }

    @Override
    public void chequear() {

    }
}
