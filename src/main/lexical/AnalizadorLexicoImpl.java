package main.lexical;

import main.utils.Token;
import main.utils.TokenImpl;
import main.errorhandling.exceptions.LexicalException;
import main.errorhandling.messages.LexicalErrorMessages;
import main.filemanager.SourceManager;

import java.io.IOException;

import static java.lang.Character.*;
import static java.util.HexFormat.isHexDigit;
import static main.filemanager.SourceManager.END_OF_FILE;
import static main.utils.PalabraReservada.esPalabraReservada;
import static main.utils.PalabraReservada.obtenerTipo;

public class AnalizadorLexicoImpl implements AnalizadorLexico{
    String lexema = "";
    char caracterActual;
    SourceManager gestorFuente;

    private static final int MAX_LONG_INT = 9;

    public AnalizadorLexicoImpl(SourceManager gestorFuente) {
        this.gestorFuente = gestorFuente;
        actualizarCaracterActual();
    }

    @Override
    public Token proximoToken() {
        limpiarLexema();
        return e0();
    }

    private void actualizarCaracterActual() {
        try {
            caracterActual = gestorFuente.getNextChar();
        }catch (IOException e){
            throw new RuntimeException("Error al abrir el archivo fuente");
        }
    }

    private void actualizarLexema(){
        lexema += caracterActual;
    }

    private void limpiarLexema(){
        lexema = "";
    }

    private Token e0(){
        if(isWhitespace(caracterActual)){
            actualizarCaracterActual();
            return e0();
        }
        else if(isDigit(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return intLiteral();
        }
        else if(isLetter(caracterActual)){
            if(isUpperCase(caracterActual)){
                actualizarLexema();
                actualizarCaracterActual();
                return idClase();
            }
            else{
                actualizarLexema();
                actualizarCaracterActual();
                return idMetVar();
            }
        }
        else if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return charLiteral();
        }
        else if (caracterActual == '"') {
            actualizarLexema();
            actualizarCaracterActual();
            return stringLiteral();
        }
        else if (caracterActual == '(' ||
                    caracterActual == ')' ||
                    caracterActual == '{' ||
                    caracterActual == '}' ||
                    caracterActual == ';' ||
                    caracterActual == ',' ||
                    caracterActual == ':' ||
                    caracterActual == '.'
        ){
            actualizarLexema();
            return puntuacion();
        }
        else if (caracterActual == '>' ||
                    caracterActual == '<' ||
                    caracterActual == '!' ||
                    caracterActual == '='
        ) {
            String tipo = null;
            if (caracterActual == '>') {
                tipo = "Mayor";
            } else if (caracterActual == '<') {
                tipo = "Menor";
            } else if (caracterActual == '!') {
                tipo = "Not";
            } else {
                tipo = "Igual";
            }

            actualizarLexema();
            actualizarCaracterActual();

            if(caracterActual == '='){
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl(tipo+"Igual", lexema, gestorFuente.getLineNumber());
            }else{
                return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
            }
        }
        else if (caracterActual == '+') {
            String tipo = "Mas";
            actualizarLexema();
            actualizarCaracterActual();
            if (caracterActual == '+') {
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl(tipo+"Mas", lexema, gestorFuente.getLineNumber());
            } else {
                return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
            }
        }
        else if(caracterActual == '-'){
            String tipo = "Menos";
            actualizarLexema();
            actualizarCaracterActual();
            if(caracterActual == '-'){
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl(tipo+"Menos", lexema, gestorFuente.getLineNumber());
            }else{
                return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
            }
        }
        else if (caracterActual == '&') {
            String tipo = "And";
            actualizarLexema();
            actualizarCaracterActual();
            if (caracterActual == '&') {
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl(tipo+"Cortocircuito", lexema, gestorFuente.getLineNumber());
            } else {
                throw new LexicalException(LexicalErrorMessages.ERR_INCORRECT_OPERATOR(lexema, gestorFuente));
            }
        }
        else if (caracterActual == '|') {
            String tipo = "Or";
            actualizarLexema();
            actualizarCaracterActual();
            if (caracterActual == '|') {
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl(tipo + "Cortocircuito", lexema, gestorFuente.getLineNumber());
            }  else {
                throw new LexicalException(LexicalErrorMessages.ERR_INCORRECT_OPERATOR(lexema, gestorFuente));
            }
        }
        else if (caracterActual == '%' || caracterActual == '*') {
            if (caracterActual == '%') {
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl("Modulo", lexema, gestorFuente.getLineNumber());
            } else {
                actualizarLexema();
                actualizarCaracterActual();
                return new TokenImpl("Por", lexema, gestorFuente.getLineNumber());
            }
        }
        else if (caracterActual == '/'){
            actualizarLexema();
            actualizarCaracterActual();
            if(caracterActual == '/'){
                actualizarLexema();
                actualizarCaracterActual();
                return comentarioLinea();
            } else if(caracterActual == '*'){
                actualizarLexema();
                actualizarCaracterActual();
                return comentarioBloque();
            } else{
                return new TokenImpl("Dividir", lexema, gestorFuente.getLineNumber());
            }
        }
        else if(caracterActual == END_OF_FILE){
                return new TokenImpl("EOF", "EOF", gestorFuente.getLineNumber());
        }
        else {
            actualizarLexema();
            int columnaError = gestorFuente.getColumnNumber();
            actualizarCaracterActual();
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_CHAR(lexema, gestorFuente, columnaError));
        }
    }

    private Token puntuacion() {
        String tipo;
        switch (lexema) {
            case "(": tipo = "ParentesisIzq"; break;
            case ")": tipo = "ParentesisDer"; break;
            case "{": tipo = "LlaveIzq"; break;
            case "}": tipo = "LlaveDer"; break;
            case ";": tipo = "PuntoYComa"; break;
            case ",": tipo = "Coma"; break;
            case ":": tipo = "DosPuntos"; break;
            case ".": tipo = "Punto"; break;
            default:
                throw new IllegalStateException("Error interno del compilador: Puntuacion desconocida '" + lexema + "'");
        }

        actualizarCaracterActual();
        return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
    }

    private Token comentarioBloque() {
        if(caracterActual == '*') {
            actualizarCaracterActual();
            return posibleCierreComentarioBloque();
        }else{
            if(caracterActual != END_OF_FILE){
                actualizarCaracterActual();
                return comentarioBloque();
            }else{
                throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_EOF(lexema, gestorFuente));
            }
        }
    }

    private Token posibleCierreComentarioBloque() {
        if(caracterActual == '/'){
            limpiarLexema();
            actualizarCaracterActual();
            return e0();
        }else if(caracterActual == END_OF_FILE){
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_EOF(lexema, gestorFuente));
        }else{
            actualizarCaracterActual();
            return comentarioBloque();
        }
    }

    private Token comentarioLinea() {
        if(caracterActual != '\n' && caracterActual != END_OF_FILE){
            actualizarCaracterActual();
            return comentarioLinea();
        }else{
            limpiarLexema();
            actualizarCaracterActual();
            return e0();
        }
    }

    private Token stringLiteral() {
        if (caracterActual == '\\'){
            actualizarLexema();
            actualizarCaracterActual();
            return barraInvertidaString();
        }else if(caracterActual == '"'){
            actualizarLexema();
            actualizarCaracterActual();
            return stringCierre();
        }else if(caracterActual == END_OF_FILE){
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_EOF(lexema, gestorFuente));
        }else if(caracterActual == '\n'){
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_ENTER(lexema, gestorFuente));
        } else {
            actualizarLexema();
            actualizarCaracterActual();
            return stringLiteral();
        }
    }

    private Token stringCierre() {
        return new TokenImpl("stringLiteral", lexema, gestorFuente.getLineNumber());
    }

    private Token barraInvertidaString() {
        if(caracterActual != END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return stringLiteral();
        }else{
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_EOF(lexema, gestorFuente));
        }
    }

    private Token charLiteral() {
        if(caracterActual == '\\'){
            actualizarLexema();
            actualizarCaracterActual();
            return barraInvertidaChar();
        } else if (caracterActual == '\'') {
            actualizarLexema();
            throw new LexicalException(LexicalErrorMessages.ERR_EMPTY_CHAR(lexema, gestorFuente));
        } else{
            actualizarLexema();
            actualizarCaracterActual();
            return espacioLetraDigitoSimboloChar();
        }
    }

    private Token espacioLetraDigitoSimboloChar() {
        if(caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return charCierre();
        }else{
            actualizarLexema();
            //actualizarCaracterActual();
            throw new LexicalException(LexicalErrorMessages.ERR_CLOSED_OR_TOO_LONG_CHAR(lexema, gestorFuente));
        }
    }

    private Token barraInvertidaChar() {
        if(caracterActual == END_OF_FILE){
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_EOF(lexema, gestorFuente));
        }else if(caracterActual == 'u'){
            actualizarLexema();
            actualizarCaracterActual();
            return unicodeChar();
        }
        else {
            actualizarLexema();
            actualizarCaracterActual();
            return espacioLetraDigitoSimboloChar();
        }
    }

    private Token unicodeChar() {
        if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return charCierre();
        }
        for (int i = 0; i < 4; i++) {
            if (isHexDigit(caracterActual)) {
                actualizarLexema();
                actualizarCaracterActual();
            } else {
                actualizarLexema();
                throw new LexicalException(LexicalErrorMessages.ERR_MALFORMED_UNICODE_ESCAPE(lexema, gestorFuente));
            }
        }
        if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return charCierre();
        } else {
            actualizarLexema();
            throw new LexicalException(LexicalErrorMessages.ERR_MALFORMED_UNICODE_ESCAPE(lexema, gestorFuente));
        }
    }

    private Token charCierre() {
        return new TokenImpl("charLiteral", lexema, gestorFuente.getLineNumber());
    }

    private Token idMetVar() {
        if (isLetterOrDigit(caracterActual) || caracterActual == '_') {
            actualizarLexema();
            actualizarCaracterActual();
            return idMetVar();
        } else if(esPalabraReservada(lexema)){
            return new TokenImpl(obtenerTipo(lexema), lexema, gestorFuente.getLineNumber());
        } else {
            return new TokenImpl("idMetVar", lexema, gestorFuente.getLineNumber());
        }
    }

    private Token idClase() {
        if (isLetterOrDigit(caracterActual) || caracterActual == '_') {
            actualizarLexema();
            actualizarCaracterActual();
            return idClase();
        } else {
            return new TokenImpl("idClase", lexema, gestorFuente.getLineNumber());
        }
    }

    private Token intLiteral() {
        if(isDigit(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return intLiteral();
        }
        if (lexema.length() > MAX_LONG_INT) {
            throw new LexicalException(LexicalErrorMessages.ERR_LONG_INT(lexema, gestorFuente));
        }
        return new TokenImpl("intLiteral", lexema, gestorFuente.getLineNumber());
    }
}
