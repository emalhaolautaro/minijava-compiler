package main.utils;

import java.util.HashMap;
import java.util.Map;

public class PalabraReservada {
    private static Map<String, String> palabrasReservadas = new HashMap<>();

    static{
        palabrasReservadas.put("class", "class");
        palabrasReservadas.put("extends", "extends");
        palabrasReservadas.put("interface", "interface");
        palabrasReservadas.put("implements", "implements");
        palabrasReservadas.put("public", "public");
        palabrasReservadas.put("static", "static");
        palabrasReservadas.put("void", "void");

        palabrasReservadas.put("boolean", "boolean");
        palabrasReservadas.put("char", "char");
        palabrasReservadas.put("int", "int");
        palabrasReservadas.put("abstract", "abstract");
        palabrasReservadas.put("final", "final");

        palabrasReservadas.put("if", "if");
        palabrasReservadas.put("else", "else");
        palabrasReservadas.put("while", "while");
        palabrasReservadas.put("return", "return");
        palabrasReservadas.put("var", "var");

        palabrasReservadas.put("this", "this");
        palabrasReservadas.put("new", "new");
        palabrasReservadas.put("null", "null");
        palabrasReservadas.put("true", "true");
        palabrasReservadas.put("false", "false");
    }

    public static boolean esPalabraReservada(String lexema){
        return palabrasReservadas.containsKey(lexema);
    }

    public static String obtenerTipo(String lexema){
        return palabrasReservadas.get(lexema);
    }
}
