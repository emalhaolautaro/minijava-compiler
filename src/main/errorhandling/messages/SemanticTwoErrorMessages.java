package main.errorhandling.messages;

import main.filemanager.SourceManager;
import main.semantic.nodes.NodoExpresion;
import main.semantic.symboltable.Tipo;
import main.utils.Token;

public class SemanticTwoErrorMessages {
    private static String format(String mensaje, Token token) {
        StringBuilder sb = new StringBuilder();

        sb.append("Error Semántico: ").append(mensaje);
        sb.append("\n");  // usar siempre \n en vez de System.lineSeparator()
        sb.append("[Error:").append(token.obtenerLexema())
                .append("|").append(token.obtenerLinea())
                .append("]");

        return sb.toString();
    }


    public static String IF_COND_NO_BOOL(NodoExpresion condicion) {
        String mensaje = "La condicion del if no es booleana: " + condicion.obtenerValor().obtenerLexema();
        return format(mensaje, condicion.obtenerValor());
    }

    public static String WHILE_COND_NO_BOOL(NodoExpresion condicion) {
        String mensaje = "La condicion del while no es booleana: " + condicion.obtenerValor().obtenerLexema();
        return format(mensaje, condicion.obtenerValor());
    }

    public static String CONSTRUCTOR_RETURN_ERR(Token tokenActual, SourceManager sourceManager) {
        String mensaje = "El constructor no debe retornar un valor.";
        return format(mensaje, tokenActual);
    }

    public static String VAR_LOCAL_EXISTENTE_ERR(Token nombre) {
        String mensaje = "La variable local '" + nombre.obtenerLexema() + "' ya ha sido declarada en este ámbito.";
        return format(mensaje, nombre);
    }

    public static String RETORNO_NO_COMPATIBLE(NodoExpresion retorno, Tipo tipoMetodo, Tipo tipoRetorno) {
        String mensaje = "El valor de retorno '" + tipoRetorno.obtenerNombre().obtenerLexema() + "' no es compatible con el tipo de retorno '" + tipoMetodo.obtenerNombre().obtenerLexema() + "'.";
        return format(mensaje, retorno.obtenerValor());
    }

    public static String TIPOS_INCOMPATIBLES_ASIGNACION(String tipoIzquierda, Token tipoDerecha, Token op) {
        String mensaje = "Tipos incompatibles en asignación: no se puede asignar un valor de tipo '" + tipoDerecha.obtenerLexema() + "' a una variable de tipo '" + tipoIzquierda+ "'.";
        return format(mensaje, op);
    }

    public static String TIPOS_INCOMPATIBLES_EXPRESION(String s, String s1, String s2, Token derecha) {
        String mensaje = "Los tipos " + s + " y " + s1 + " son incompatibles para la operacion " + s2;
        return format(mensaje, derecha);
    }

    public static String TIPOS_INCOMPATIBLES_UNARIA(String s, Token op) {
        String mensaje = "El tipo " + s + " es incompatible con el operador " + op.obtenerLexema();
        return format(mensaje, op);
    }

    public static String SENTENCIA_EXPRESION_NO_VALIDA(Token token) {
        String mensaje = "La sentencia de expresión no es válida. Se esperaba una asignación o una llamada, pero se encontró: " + token.obtenerLexema();
        return format(mensaje, token);
    }

    public static String THIS_FUERA_DE_CLASE(Token token) {
        String mensaje = "El uso de 'this' fuera de una clase no es permitido.";
        return format(mensaje, token);
    }

    public static String THIS_FUERA_DE_METODO(Token token) {
        String mensaje = "El uso de 'this' fuera de un método no es permitido.";
        return format(mensaje, token);
    }

    public static String THIS_EN_METODO_STATIC(Token token) {
        String mensaje = "El uso de 'this' en un método estático no es permitido.";
        return format(mensaje, token);
    }

    public static String VARIABLE_NO_DECLARADA(Token token) {
        String mensaje = "La variable '" + token.obtenerLexema() + "' no ha sido declarada en este ámbito.";
        return format(mensaje, token);
    }

    public static String ACCESO_ATRIBUTO_NO_STATIC_DESDE_METODO_STATIC(Token nombreVar) {
        String mensaje = "No se puede acceder al atributo '" + nombreVar.obtenerLexema() + "' desde un método estático.";
        return format(mensaje, nombreVar);
    }

    public static String CLASE_NO_DECLARADA(Token token) {
        String mensaje = "La clase '" + token.obtenerLexema() + "' no ha sido declarada.";
        return format(mensaje, token);
    }

    public static String CONSTRUCTOR_NO_ENCONTRADO(Token token) {
        String mensaje = "No se encontró un constructor en la clase '" + token.obtenerLexema() + "' que acepte la cantidad de parametros o tipos pasados";
        return format(mensaje, token);
    }

    public static String METODO_ESTATICO_INEXISTENTE(Token metodo) {
        String mensaje = "El método estático '" + metodo.obtenerLexema() + "' no existe en la clase especificada.";
        return format(mensaje, metodo);
    }

    public static String METODO_NO_ESTATICO(Token metodo, Token clase) {
        String mensaje = "El método '" + metodo.obtenerLexema() + "' de la clase '" + clase.obtenerLexema() + "' no es estático.";
        return format(mensaje, metodo);
    }

    public static String TIPO_ARGUMENTO_INCORRECTO(Token metodo, int i, Tipo tipoParametro, Tipo tipoArgumento) {
        String mensaje = "El tipo del argumento " + i + " en la llamada al método " + metodo.obtenerLexema() +
                " es incorrecto. Se esperaba un tipo " + tipoParametro.obtenerNombre().obtenerLexema() +
                " pero se recibió un tipo " + tipoArgumento.obtenerNombre().obtenerLexema() + ".";
        return format(mensaje, metodo);
    }

    public static String CANTIDAD_ARGUMENTOS_INCORRECTA(Token metodo, int size, int size1) {
        String mensaje = "La cantidad de argumentos en la llamada al método " + metodo.obtenerLexema() +
                " es incorrecta. Se esperaban " + size + " pero se recibieron " + size1 + ".";
        return format(mensaje, metodo);
    }

    public static String METODO_EN_CLASE_INEXISTENTE(Token nombre) {
        String mensaje = "No se puede llamar al método '" + nombre.obtenerLexema() + "' porque no se está dentro de una clase.";
        return format(mensaje, nombre);
    }

    public static String METODO_INEXISTENTE(Token nombre) {
        String mensaje = "El método '" + nombre.obtenerLexema() + "' no existe en la clase actual o en sus ancestros.";
        return format(mensaje, nombre);
    }

    public static String METODO_NO_DECLARADO(Token token) {
        String mensaje = "El método '" + token.obtenerLexema() + "' no ha sido declarado en esta clase.";
        return format(mensaje, token);
    }

    public static String PARAMETROS_INCORRECTOS(Token token) {
        String mensaje = "Los parámetros proporcionados en la llamada al método '" + token.obtenerLexema() + "' no son compatibles con su definición.";
        return format(mensaje, token);
    }

    public static String VAR_TIPO_VOID(Token var) {
        String mensaje = "La variable local '" + var.obtenerLexema() + "' no puede ser de tipo void.";
        return format(mensaje, var);
    }

    public static String TIPO_NO_ES_CLASE(Token id) {
        String mensaje = "No es posible encadenar '" + id.obtenerLexema() + "' porque el tipo del lado izquierdo no es una clase.";
        return format(mensaje, id);
    }

    public static String VAR_LOCAL_NOMBRE_INVALIDO(Token var) {
        String mensaje = "El nombre de la variable local '" + var.obtenerLexema() + "' es inválido.";
        return format(mensaje, var);
    }

    public static String IZQUIERDA_ASIGNACION_NO_VALIDA(Token token, Token op) {
        String mensaje = "El lado izquierdo de una asignación no es válido. Se esperaba una variable o un atributo, pero se encontró: " + token.obtenerLexema();
        return format(mensaje, op);
    }

    public static String METODO_DINAMICO_EN_METODO_ESTATICO(Token nombre) {
        String mensaje = "No se puede llamar al método dinámico '" + nombre.obtenerLexema() + "' desde un método estático.";
        return format(mensaje, nombre);
    }

    public static String VAR_TIPO_NULL(Token var) {
        String mensaje = "La variable local '" + var.obtenerLexema() + "' no puede ser de tipo null.";
        return format(mensaje, var);
    }

    public static String ENCADENADO_NO_VALIDO(Token token) {
        String mensaje = "El tipo de expresión en el encadenado " + token +" no es válido. Se esperaba acceso a atributo o llamada a método.";
        return format(mensaje, token);
    }

    public static String TIPOS_INCOMPATIBLES_ATRIBUTO(Token at, Token exp, Token nombre) {
        String mensaje = "El tipo de atributo '" + at.obtenerLexema() + "' no es compatible con el valor que se paso en asignacion: " + exp.obtenerLexema();
        return format(mensaje, nombre);
    }

    public static String RETURN_NO_VOID_VACIO(Token returnToken) {
        String mensaje = "El retorno de un metodo que no es void no puede ser vacio";
        return format(mensaje, returnToken);
    }

    public static String RETURN_VOID_CON_EXPRESION(Token tokError) {
        String mensaje = "El metodo void retorna una expresion";
        return format(mensaje, tokError);
    }
}
