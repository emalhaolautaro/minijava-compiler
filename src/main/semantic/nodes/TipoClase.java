package main.semantic.nodes;

import main.semantic.symboltable.Clase;
import main.semantic.symboltable.Tipo;
import main.utils.Token;
import static main.Main.tablaSimbolos;

public class TipoClase extends Tipo {

    public TipoClase(Token nombreClase) {
        super(nombreClase);
    }

    public String obtenerNombreClase() {
        return obtenerNombre().obtenerLexema();
    }

    public boolean esCompatible(Tipo otroTipo) {
        // dos tipos de clase son compatibles si tienen el mismo nombre.
        if (otroTipo instanceof TipoClase) {
            TipoClase otroTipoClase = (TipoClase) otroTipo;
            if(this.obtenerNombreClase().equals(otroTipoClase.obtenerNombreClase()) ||
            otroTipoClase.heredaDe(this.obtenerNombreClase()))
                return true;
        }
        return false;
    }

    private boolean heredaDe(String s) {
        // Verificar si esta clase hereda de la clase con nombre s
        // Aquí se debería implementar la lógica para verificar la herencia en la tabla de símbolos
        if(tablaSimbolos.existeClase(this.obtenerNombreClase())) {
            Clase claseActual = tablaSimbolos.obtenerClasePorNombre(this.obtenerNombreClase());
            String clasePadre = claseActual.obtenerPadre().obtenerLexema();
            while (clasePadre != null) {
                if (clasePadre.equals(s)) {
                    return true;
                }
                if(tablaSimbolos.existeClase(clasePadre)) {
                    claseActual = tablaSimbolos.obtenerClasePorNombre(clasePadre);
                    clasePadre = claseActual.obtenerPadre().obtenerLexema();
                } else {
                    break;
                }
            }
        }
        return false;
    }

    public String obtenerTipo(){
        return obtenerNombre().obtenerLexema();
    }
}
