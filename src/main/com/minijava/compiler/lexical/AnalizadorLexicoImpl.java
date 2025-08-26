package main.com.minijava.compiler.lexical;

import main.com.minijava.compiler.Token;
import main.com.minijava.compiler.TokenImpl;
import main.com.minijava.compiler.errorhandling.exceptions.LexicalException;
import main.com.minijava.compiler.errorhandling.messages.LexicalErrorMessages;
import main.com.minijava.compiler.filemanager.SourceManager;

import java.io.IOException;

import static java.lang.Character.*;
import static main.com.minijava.compiler.filemanager.SourceManager.END_OF_FILE;

public class AnalizadorLexicoImpl implements AnalizadorLexico{
    String lexema = "";
    char caracterActual;
    SourceManager gestorFuente;

    private static final int MAX_LONG_INT = 9;

    public AnalizadorLexicoImpl(SourceManager gestorFuente) throws IOException {
        this.gestorFuente = gestorFuente;
        actualizarCaracterActual();
    }

    @Override
    public Token proximoToken() {
        return e0();
    }

    private void actualizarCaracterActual() {
        try {
            caracterActual = gestorFuente.getNextChar();
        }catch (IOException e){
            System.out.println(e.getMessage());
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
            actualizarCaracterActual();
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
                return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
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
            }else {
                return new TokenImpl(tipo, lexema, gestorFuente.getLineNumber());
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
            }else if(caracterActual == '*'){
                actualizarLexema();
                actualizarCaracterActual();
                return comentarioBloque();
            }else{
                return new TokenImpl("Dividir", lexema, gestorFuente.getLineNumber());
            }
        }
        else if(caracterActual == END_OF_FILE){
                return new TokenImpl("EOF", "EOF", gestorFuente.getLineNumber());
        }
        else {
            actualizarLexema();
            throw new LexicalException(LexicalErrorMessages.ERR_UNEXPECTED_CHAR(lexema, gestorFuente));
        }
    }

    private Token intLiteral() {
        if (isDigit(caracterActual)) {
            if (lexema.length() < MAX_LONG_INT) {
                actualizarLexema();
                actualizarCaracterActual();
                return intLiteral();
            } else {
                throw new LexicalException(LexicalErrorMessages.ERR_LONG_INT(lexema, gestorFuente));
            }
        }else{
            return new TokenImpl("intLiteral", lexema, gestorFuente.getLineNumber());
        }
    }
}
