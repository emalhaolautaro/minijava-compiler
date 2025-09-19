package main.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrimerosImpl implements Primeros{

    static Map<String, Set<String>> primeros = new HashMap<>();

    static{
        // Reglas de alto nivel
        primeros.put("Inicial",  new HashSet<>(Arrays.asList("abstract", "static", "final", "class", "interface", "EOF")));
        primeros.put("ListaClases", new HashSet<>(Arrays.asList("abstract", "static", "final", "class", "interface"))); // Ahora es una lista de unidades
        primeros.put("Clase", new HashSet<>(Arrays.asList("abstract", "static", "final", "class")));

        // Modificadores y Herencia
        primeros.put("ModificadorOpcional", new HashSet<>(Arrays.asList("abstract", "static", "final")));
        primeros.put("Modificador", new HashSet<>(Arrays.asList("abstract", "static", "final")));
        primeros.put("HerenciaOpcional", new HashSet<>(Arrays.asList("extends")));
        primeros.put("InterfazOpcional", new HashSet<>(Arrays.asList("implements")));
        primeros.put("HerenciaOInterfazOpcional", new HashSet<>(Arrays.asList("extends", "implements")));

        // Miembros de clase
        primeros.put("Miembro", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase", "abstract", "static", "final", "void", "public")));
        primeros.put("MetodoOAtributo", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase", "abstract", "static", "final", "void")));
        primeros.put("Constructor", new HashSet<>(Arrays.asList("public")));

        // Tipos y Argumentos
        primeros.put("Tipo", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase")));
        primeros.put("TipoMetodo", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase", "void")));
        primeros.put("TipoPrimitivo", new HashSet<>(Arrays.asList("boolean", "char", "int")));
        primeros.put("ArgsFormales", new HashSet<>(Arrays.asList("ParentesisIzq")));
        primeros.put("ListaArgsFormalesOpcional", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase"))); // Usado en listaArgsFormalesOpcional
        primeros.put("ListaArgsFormales", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase")));
        primeros.put("ArgFormal", new HashSet<>(Arrays.asList("boolean", "char", "int", "idClase")));

        // Bloques y Sentencias
        primeros.put("Bloque", new HashSet<>(Arrays.asList("LlaveIzq")));
        primeros.put("Sentencia", new HashSet<>(Arrays.asList("PuntoYComa", "this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq", "Menos", "MenosMenos", "Not", "Mas", "MasMas", "true", "false", "intLiteral", "charLiteral", "null", "var", "return", "if", "while", "LlaveIzq")));
        primeros.put("VarLocal", new HashSet<>(Arrays.asList("var")));
        primeros.put("Return", new HashSet<>(Arrays.asList("return")));
        primeros.put("If", new HashSet<>(Arrays.asList("if")));
        primeros.put("While", new HashSet<>(Arrays.asList("while")));

        // Expresiones
        primeros.put("Expresion", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq", "Menos", "MenosMenos", "Not", "Mas", "MasMas", "true", "false", "intLiteral", "charLiteral", "null")));
        primeros.put("ExpresionCompuesta", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq", "Menos", "MenosMenos", "Not", "Mas", "MasMas", "true", "false", "intLiteral", "charLiteral", "null")));
        primeros.put("ExpresionBasica", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq", "Menos", "MenosMenos", "Not", "Mas", "MasMas", "true", "false", "intLiteral", "charLiteral", "null")));
        primeros.put("OperadorAsignacion", new HashSet<>(Arrays.asList("Igual")));
        primeros.put("OperadorBinario", new HashSet<>(Arrays.asList("OrCortocircuito", "AndCortocircuito", "IgualIgual", "NotIgual", "Menor", "Mayor", "MenorIgual", "MayorIgual", "Mas", "Menos", "Por", "Dividir", "Modulo")));
        primeros.put("OperadorUnario", new HashSet<>(Arrays.asList("Menos", "MenosMenos", "Not", "Mas", "MasMas")));
        primeros.put("Operando", new HashSet<>(Arrays.asList("true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq")));
        primeros.put("Primitivo", new HashSet<>(Arrays.asList("true", "false", "intLiteral", "charLiteral", "null")));
        primeros.put("Referencia", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq")));
        primeros.put("Primario", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq")));
        primeros.put("Encadenado", new HashSet<>(Arrays.asList("Punto")));
        primeros.put("ArgsActuales", new HashSet<>(Arrays.asList("ParentesisIzq")));
        primeros.put("LlamadaConstructor", new HashSet<>(Arrays.asList("new")));
        primeros.put("LlamadaMetodoEstatico", new HashSet<>(Arrays.asList("idClase")));
        primeros.put("ExpresionParentizada", new HashSet<>(Arrays.asList("ParentesisIzq")));
        primeros.put("ListaExps", new HashSet<>(Arrays.asList("this", "stringLiteral", "idMetVar", "new", "idClase", "ParentesisIzq", "Menos", "MenosMenos", "Not", "Mas", "MasMas", "true", "false", "intLiteral", "charLiteral", "null")));
    }

    @Override
    public Set<String> obtenerPrimeros(String noTerminal) {
        // Devuelve el conjunto o uno vacío si la clave no existe para evitar errores.
        return primeros.getOrDefault(noTerminal, new HashSet<>());
    }

    @Override
    public boolean incluidoEnPrimeros(String noTerminal, String tokenTipo) {
        return obtenerPrimeros(noTerminal).contains(tokenTipo);
    }
}
