package main.semantic.nodes.nodosBloquesPredefinidos;

import main.filemanager.OutputManager;
import main.semantic.nodes.NodoBloque;
import main.semantic.symboltable.Unidad;
import main.utils.Instrucciones;

public class NodoBloqueRead extends NodoBloque {
    public NodoBloqueRead() {
        super();
    }

    @Override
    public void imprimirAST(int nivel){
        System.out.println("- ".repeat(nivel) + "NodoBloqueRead");
    }

    @Override
    public void generar(OutputManager output, Unidad unidadActual){
        output.generar(Instrucciones.READ.toString());
        output.generar(Instrucciones.PUSH + " 48");
        output.generar(Instrucciones.SUB.toString());
        output.generar(Instrucciones.STORE + " 3");
        ///TODO: Consultar si es correcto o si debería recorrer todo el buffer
    }
}
