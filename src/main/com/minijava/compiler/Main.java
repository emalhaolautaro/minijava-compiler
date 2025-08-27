package main.com.minijava.compiler;

import main.com.minijava.compiler.errorhandling.exceptions.LexicalException;
import main.com.minijava.compiler.errorhandling.messages.ConsoleMessages;
import main.com.minijava.compiler.filemanager.SourceManager;
import main.com.minijava.compiler.filemanager.SourceManagerImpl;
import main.com.minijava.compiler.lexical.AnalizadorLexico;
import main.com.minijava.compiler.lexical.AnalizadorLexicoImpl;
import main.com.minijava.compiler.utils.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static SourceManager sourceManager;
    private static AnalizadorLexico analizadorLexico;

    public static void main(String[] args) {
        if(args.length == 1){
            String filePath = args[0];
            abrirArchivo(filePath);
            ejecutarAnalizadorLexico();
            cerrarArchivo();
        } else {
            System.out.println("Usage: java -jar MiniJavaCompiler.jar <source-file>");
        }
    }

    private static void cerrarArchivo() {
        try{
            sourceManager.close();
        } catch (IOException exception){
            System.out.println("Error al cerrar el archivo: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static void ejecutarAnalizadorLexico() {
        Token token = null;
        ConsoleMessages consoleMessage = new ConsoleMessages();
        try {
            do {
                token = analizadorLexico.proximoToken();
                consoleMessage.setSuccessMessage(token.toString());
            } while (token != null && !token.obtenerTipo().equals("EOF"));
            System.out.println(consoleMessage.getSuccessMessage());
        } catch (LexicalException e) {
            System.out.println(e.getMessage());
        }
    }

    /*private static void ejecutarAnalizadorLexico() {
        Token token = null;
        ConsoleMessages consoleMessage = new ConsoleMessages();
        do{
            try {
                token = analizadorLexico.proximoToken();
                consoleMessage.setSuccessMessage(token.toString());
                consoleMessage.setErrorMessage(e.getMessage());java
                private static void ejecutarAnalizadorLexico() {
                    Token token = null;
                    ConsoleMessages consoleMessage = new ConsoleMessages();
                    try {
                        do {
                            token = analizadorLexico.proximoToken();
                            consoleMessage.setSuccessMessage(token.toString());
                        } while (token != null && !token.obtenerTipo().equals("EOF"));
                        System.out.println(consoleMessage.getSuccessMessage());
                    } catch (LexicalException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }while (token != null && !token.obtenerTipo().equals("EOF"));
        System.out.println(consoleMessage.getSuccessMessage());
        System.out.println(consoleMessage.getErrorMessage());
    }
    */

    private static void abrirArchivo(String filePath) {
        try{
            sourceManager = new SourceManagerImpl(); // Create SourceManager first
            sourceManager.open(filePath); // Then, open the file and initialize the reader
            analizadorLexico = new AnalizadorLexicoImpl(sourceManager); // Now, create the analyzer
        } catch (FileNotFoundException exception){
            System.out.println("Error al abrir el archivo: " + exception.getMessage());
            System.exit(1);
        }
    }

    public static void initialize(){
        sourceManager = new SourceManagerImpl();
        analizadorLexico = new AnalizadorLexicoImpl(sourceManager);
    }
}