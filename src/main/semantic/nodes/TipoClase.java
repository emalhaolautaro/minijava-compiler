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
        if (!(otroTipo instanceof TipoClase)) {
            return false;
        }

        TipoClase otroTipoClase = (TipoClase) otroTipo;
        String esteNombre = this.obtenerNombreClase();
        String otroNombre = otroTipoClase.obtenerNombreClase();

        if (esteNombre.equals(otroNombre)) {
            return true;
        }

        if (this.heredaDe(otroNombre)) {
            return true;
        }

        return false;
    }

    public boolean heredaDe(String s) {
        if(tablaSimbolos.existeClase(this.obtenerNombreClase())) {
            Clase claseActual = tablaSimbolos.obtenerClasePorNombre(this.obtenerNombreClase());
            Token tokenPadre = claseActual.obtenerPadre();

            while (tokenPadre != null) {
                String clasePadre = tokenPadre.obtenerLexema();

                if (clasePadre.equals(s)) {
                    return true;
                }

                if (clasePadre.equals("Object")) {
                    break;
                }

                if (tablaSimbolos.existeClase(clasePadre)) {
                    claseActual = tablaSimbolos.obtenerClasePorNombre(clasePadre);
                    tokenPadre = claseActual.obtenerPadre();
                } else {
                    tokenPadre = null;
                }
            }
        }
        return false;
    }

    public String obtenerTipo(){
        return obtenerNombre().obtenerLexema();
    }
}
