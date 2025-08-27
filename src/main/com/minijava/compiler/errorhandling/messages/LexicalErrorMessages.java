package main.com.minijava.compiler.errorhandling.messages;

import main.com.minijava.compiler.filemanager.SourceManager;

public class LexicalErrorMessages {
    public static String ERR_UNEXPECTED_CHAR (String unexpectedChar, SourceManager sourceManager) {
        String mensaje = errorGenerico(sourceManager) + " " + unexpectedChar + " no es un símbolo valido." + '\n';
        mensaje = mensaje + reporteDeErrorElegante(unexpectedChar, sourceManager);
        return mensaje;
    }

    public static String ERR_LONG_INT(String lexema, SourceManager gestorFuente) {
        String mensaje = errorGenerico(gestorFuente) + " El literal entero " + lexema + " excede la longitud máxima permitida de 9 dígitos." + '\n';
        mensaje = mensaje + reporteDeErrorElegante(lexema, gestorFuente);
        return mensaje;
    }

    public static String ERR_CLOSED_OR_TOO_LONG_CHAR(String lexema, SourceManager gestorFuente) {
        return errorGenerico(gestorFuente) + " El literal char " + lexema + " no está correctamente cerrado o es muy largo." + '\n' +
                reporteDeErrorElegante(lexema, gestorFuente);
    }

    public static String ERR_RESERVED_WORD(String lexema, SourceManager gestorFuente) {
        return errorGenerico(gestorFuente) + " La palabra " + lexema + " es una palabra reservada y no puede ser utilizada como identificador." + '\n' +
                reporteDeErrorElegante(lexema, gestorFuente);
    }

    private static String errorGenerico(SourceManager sourceManager) {
        return "Error Léxico en linea " + sourceManager.getLineNumber() + ":";
    }

    private static String reporteDeErrorElegante(String lexemaErroneo, SourceManager sourceManager) {
        StringBuilder reporte = new StringBuilder("Detalle: ");
        int fila = sourceManager.getLineNumber();
        int columna = sourceManager.getColumnNumber();

        reporte.append(sourceManager.getCurrentLine()).append('\n');
        reporte.append(" ".repeat(columna + "Detalle: ".length() - 1)).append("^\n");
        reporte.append("[Error:").append(lexemaErroneo).append("|").append(fila).append("]\n");

        return reporte.toString();
    }

    public static String ERR_UNEXPECTED_EOF(String lexema, SourceManager gestorFuente) {
        return errorGenerico(gestorFuente) + " Se encontró el fin del archivo inesperadamente. Posible causa: " + lexema + " no está cerrado correctamente." + '\n' +
                reporteDeErrorElegante(lexema, gestorFuente);
    }

    public static String ERR_UNEXPECTED_ENTER(String lexema, SourceManager gestorFuente) {
        return errorGenerico(gestorFuente) + " Se encontró un salto de línea inesperado. Posible causa: " + lexema + " no está cerrado correctamente." + '\n' +
                reporteDeErrorElegante(lexema, gestorFuente);
    }

    public static String ERR_EMPTY_CHAR(String lexema, SourceManager gestorFuente) {
        return errorGenerico(gestorFuente) + " El literal char " + lexema + " está vacío." + '\n' +
                reporteDeErrorElegante(lexema, gestorFuente);
    }
}
