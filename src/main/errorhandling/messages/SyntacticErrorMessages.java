package main.errorhandling.messages;

import main.filemanager.SourceManager;
import main.utils.Token;

public class SyntacticErrorMessages {

    public static String SYNTACTIC_ERR(String esperado, Token encontrado, SourceManager sourceManager){
        StringBuilder error = new StringBuilder();
        int linea = encontrado.obtenerLinea();

        error.append("Error Sintactico en linea ").append(linea).append(": se esperaba '").append(esperado).append("' pero se encontro '").append(encontrado.obtenerLexema()).append("'.\n");
        error.append("[Error:").append(encontrado.obtenerLexema()).append("|").append(linea).append("]\n");
        return error.toString();
    }
}
