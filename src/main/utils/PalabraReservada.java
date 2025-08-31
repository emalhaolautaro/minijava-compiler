package main.utils;

import java.util.HashMap;
import java.util.Map;

public class PalabraReservada {
    private static Map<String, String> palabrasReservadas = new HashMap<>();

    static{
        palabrasReservadas.put("class", "CLASS");
        palabrasReservadas.put("extends", "EXTENDS");
        palabrasReservadas.put("public", "PUBLIC");
        palabrasReservadas.put("static", "STATIC");
        palabrasReservadas.put("void", "VOID");

        palabrasReservadas.put("boolean", "BOOLEAN");
        palabrasReservadas.put("char", "CHAR");
        palabrasReservadas.put("int", "INT");
        palabrasReservadas.put("abstract", "ABSTRACT");
        palabrasReservadas.put("final", "FINAL");

        palabrasReservadas.put("if", "IF");
        palabrasReservadas.put("else", "ELSE");
        palabrasReservadas.put("while", "WHILE");
        palabrasReservadas.put("return", "RETURN");
        palabrasReservadas.put("var", "VAR");

        palabrasReservadas.put("this", "THIS");
        palabrasReservadas.put("new", "NEW");
        palabrasReservadas.put("null", "NULL");
        palabrasReservadas.put("true", "TRUE");
        palabrasReservadas.put("false", "FALSE");
    }

    public static boolean esPalabraReservada(String lexema){
        return palabrasReservadas.containsKey(lexema);
    }

    public static String obtenerTipo(String lexema){
        return palabrasReservadas.get(lexema);
    }
}
