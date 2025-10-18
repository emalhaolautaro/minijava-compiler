package main.semantic.nodes;

public class NodoAsignacion {
    private NodoExpresionCompuesta izquierda;
    private NodoExpresion derecha;

    public NodoAsignacion(NodoExpresionCompuesta izquierda, NodoExpresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public void chequear() {
        // Implementar la lógica de chequeo para la asignación
        // Por ejemplo, verificar que los tipos de izquierda y derecha sean compatibles
    }

}
