
package main;

import main.errorhandling.exceptions.LexicalException;
import main.errorhandling.messages.ConsoleMessages;
import main.filemanager.SourceManager;
import main.filemanager.SourceManagerImpl;
import main.lexical.AnalizadorLexico;
import main.lexical.AnalizadorLexicoImpl;
import main.utils.Token;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainLexico {
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
        while (true) {
            try {
                token = analizadorLexico.proximoToken();
                consoleMessage.setSuccessMessage(token.toString());

                if (token.obtenerTipo().equals("EOF")) {
                    break;
                }
            } catch (LexicalException e) {
                consoleMessage.setErrorMessage(e.getMessage());
            }
        }
        System.out.println(consoleMessage.getSuccessMessage());
        System.out.println(consoleMessage.getErrorMessage());
    }

    private static void abrirArchivo(String filePath) {
        try{
            sourceManager = new SourceManagerImpl();
            sourceManager.open(filePath);
            analizadorLexico = new AnalizadorLexicoImpl(sourceManager);
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

