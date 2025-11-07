package main;

import main.errorhandling.exceptions.LexicalException;
import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.exceptions.SyntacticException;
import main.errorhandling.messages.ConsoleMessages;
import main.filemanager.OutputManager;
import main.filemanager.OutputManagerImpl;
import main.filemanager.SourceManager;
import main.filemanager.SourceManagerImpl;
import main.lexical.AnalizadorLexico;
import main.lexical.AnalizadorLexicoImpl;
import main.semantic.symboltable.TablaSimbolos;
import main.syntactic.AnalizadorSintactico;
import main.syntactic.AnalizadorSintacticoImpl;
import main.utils.Token;

import java.io.FileNotFoundException;
import java.io.IOException;

import static jdk.jfr.consumer.EventStream.openFile;

public class Main {
    private static SourceManager sourceManager;
    private static AnalizadorLexico analizadorLexico;
    private static AnalizadorSintactico analizadorSintactico;
    public static TablaSimbolos tablaSimbolos;

    public static void main(String[] args) {
        if(args.length == 1){
            String filePath = args[0];
            abrirArchivo(filePath);
            try{
                analizadorSintactico.inicial();
                // Primera pasada: chequeo de declaraciones
                tablaSimbolos.declaracionCorrecta(tablaSimbolos);
                // Segunda pasada: consolidación (herencia, atributos y métodos heredados)
                tablaSimbolos.consolidar(tablaSimbolos);
                // Chequeo de Sentencias
                tablaSimbolos.chequearSentencias();
                // Calculo offsets
                tablaSimbolos.calcularOffset();

                // Imprimir AST
                tablaSimbolos.imprimirAST();
                System.out.println("Chequeo semántico completado correctamente.");
                //System.out.println(tablaSimbolos.toString());
                System.out.println("[SinErrores]");
            }catch (SyntacticException | SemanticException e){
                System.out.println(e.getMessage());
            }
            //ejecutarAnalizadorLexico();
            //ejecutarAnalizadorSintactico();
            //ejecutarAnalizadorSemantico(tablaSimbolos);
            cerrarArchivo();
        } else if(args.length == 2){
            try {
                String nombreArchivo = args[0];
                String nombreArchivoSalida = args[1];
                abrirArchivo(nombreArchivo);
                analizadorSintactico.inicial();
                cerrarArchivo();
                tablaSimbolos.declaracionCorrecta(tablaSimbolos);
                tablaSimbolos.consolidar(tablaSimbolos);
                tablaSimbolos.chequearSentencias();
                tablaSimbolos.calcularOffset();
                generarCodigo(nombreArchivoSalida);
                System.out.println("Compilacion exitosa.");
                System.out.println("[SinErrores]");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Usage: java -jar MiniJavaCompiler.jar <source-file>");
        }
    }

    private static void generarCodigo(String nombreArchivoSalida) {
        OutputManager outp = new OutputManagerImpl(nombreArchivoSalida);
        tablaSimbolos.generar(outp);
        outp.cerrar();
    }

    private static void ejecutarAnalizadorSemantico(TablaSimbolos ts) {
        // Ejecutar analizador semántico
        try {
            // Primera pasada: chequeo de declaraciones
            ts.declaracionCorrecta(ts);

            // Segunda pasada: consolidación (herencia, atributos y métodos heredados)
            ts.consolidar(ts);

            System.out.println("Chequeo semántico completado correctamente.");
            System.out.println("[SinErrores]");
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ejecutarAnalizadorSintactico() {
        try{
            analizadorSintactico.inicial();
        } catch (SyntacticException e){
            System.out.println(e.getMessage());
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
            tablaSimbolos = new TablaSimbolos();
            analizadorLexico = new AnalizadorLexicoImpl(sourceManager);
            analizadorSintactico = new AnalizadorSintacticoImpl(analizadorLexico, sourceManager, tablaSimbolos);

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