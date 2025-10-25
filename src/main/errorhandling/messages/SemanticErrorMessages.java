package main.errorhandling.messages;

import main.semantic.symboltable.Tipo;
import main.utils.Token;
import main.utils.TokenImpl;

public class SemanticErrorMessages {

    private static String format(String mensaje, Token token) {
        StringBuilder sb = new StringBuilder();

        sb.append("Error Semántico: ").append(mensaje);
        sb.append("\n");  // usar siempre \n en vez de System.lineSeparator()
        sb.append("[Error:").append(token.obtenerLexema())
                .append("|").append(token.obtenerLinea())
                .append("]");

        return sb.toString();
    }

    public static String METODO_REPETIDO(Token token, String metodo, String clase) {
        String mensaje = "El método " + metodo + " ya existe en la clase " + clase;
        return format(mensaje, token);
    }

    public static String ATRIBUTO_REPETIDO(Token token, String atributo, String clase) {
        String mensaje = "El atributo " + atributo + " ya existe en la clase " + clase;
        return format(mensaje, token);
    }

    public static String CONSTRUCTOR_REPETIDO(Token token, String constructor, String clase) {
        String mensaje = "El constructor " + constructor + " ya existe en la clase " + clase;
        return format(mensaje, token);
    }

    public static String PARAMETRO_EXISTENTE(Token token, String parametro, String unidad) {
        String mensaje = "El parámetro " + parametro + " ya existe en la unidad " + unidad;
        return format(mensaje, token);
    }

    public static String CLASE_REPETIDA(Token token, String clase) {
        String mensaje = "La clase " + clase + " ya fue declarada";
        return format(mensaje, token);
    }

    public static String CLASE_HEREDA_DE_SI_MISMA(Token token, String clase) {
        String mensaje = "La clase " + clase + " no puede heredar de sí misma";
        return format(mensaje, token);
    }

    public static String CLASE_FINAL_HEREDADA(Token token, String clase1, String clase2) {
        String mensaje = "La clase " + clase1 + " no puede heredar de la clase final " + clase2;
        return format(mensaje, token);
    }

    public static String CLASE_PADRE_NO_DECLARADA(Token token, String noclase, String clase) {
        String mensaje = "La clase padre " + noclase + " de la clase " + clase + " no fue declarada";
        return format(mensaje, token);
    }

    public static String CLASE_ABSTRACTA_HEREDA_DE_CLASE_NO_ABSTRACTA(Token token, String clase, String clase2) {
        String mensaje = "La clase abstracta " + clase + " no puede heredar de la clase no abstracta " + clase2;
        return format(mensaje, token);
    }

    public static String CLASE_PADRE_STATIC(Token token, String clase, String clase2) {
        String mensaje = "La clase " + clase + " no puede heredar de la clase static " + clase2;
        return format(mensaje, token);
    }

    public static String CLASE_ABSTRACTA_SIN_MODIFICADOR(Token token, String clase, String metodo) {
        String mensaje = "La clase " + clase + " no está declarada como abstracta y el método " + metodo + " es abstracto";
        return format(mensaje, token);
    }

    public static String CLASE_ABSTRACTA_CON_CONSTRUCTORES(Token token, String clase) {
        String mensaje = "La clase abstracta " + clase + " no puede tener constructores";
        return format(mensaje, token);
    }

    public static String PARAMETRO_TIPO_NO_DECLARADO(Token token, String parametro, String unidad) {
        String mensaje = "El tipo del parámetro " + parametro + " no fue declarado y se utiliza en la unidad " + unidad;
        return format(mensaje, token);
    }

    public static String TIPO_NO_DECLARADO(Token token) {
        String mensaje = "El tipo " + token.obtenerLexema() + " no fue declarado";
        return format(mensaje, token);
    }

    public static String CONSTRUCTOR_INEXISTENTE(Token token, String constructor) {
        String mensaje = "El constructor " + constructor + " no existe en la clase";
        return format(mensaje, token);
    }

    public static String ATRIBUTO_REDEFINIDO(Token token, String atributo, String clase) {
        String mensaje = "El atributo " + atributo + " ya fue declarado en la clase " + clase;
        return format(mensaje, token);
    }

    public static String METODO_FINAL_O_STATIC_REDEFINIDO(Token token, String metodo, String clase) {
        String mensaje = "El método " + metodo + " ya fue declarado en la clase " + clase;
        return format(mensaje, token);
    }

    public static String METODO_REDEFINIDO_CON_DIFERENCIAS(Token token, String metodo, String clase) {
        String mensaje = "El método " + metodo + " ya fue declarado en la clase " + clase + " con diferentes parámetros";
        return format(mensaje, token);
    }

    public static String METODO_ABSTRACTO_NO_IMPLEMENTADO(Token token, String metodo, String clase) {
        String mensaje = "El método abstracto " + metodo + " no fue implementado en la clase " + clase;
        return format(mensaje, token);
    }

    public static String METODO_REDEFINICION_FIRMA_INVALIDA(Token token, String metodo, String clasePadre) {
        String mensaje = "El método " + metodo + " de la clase hija redefine " + clasePadre + " de la clase padre pero no respeta los parámetros";
        return format(mensaje, token);
    }

    public static String HERENCIA_CIRCULAR(Token nombre, String clase) {
        String mensaje = "Herencia circular detectada. La clase '" + clase + "' forma parte de un ciclo de herencia";
        return format(mensaje, nombre);
    }

    public static String METODO_ABSTRACTO_CON_CUERPO(Token token, String metodo, String clase) {
        String mensaje = "El metodo abstracto "+ metodo + " de la clase " + clase + " tiene cuerpo";
        return format(mensaje, token);
    }

    public static String CONSTRUCTOR_INCOMPATIBLE(Token token, String constructor, String clase) {
        String mensaje = "El constructor " + constructor + " no es compatible con la clase " + clase;
        return format(mensaje, token);
    }

    public static String METODO_NO_ABSTRACTO_SIN_CUERPO(Token token, String s, String s1) {
        String mensaje = "El método " + s + " de la clase " + s1 + " no es abstracto y debe tener cuerpo";
        return format(mensaje, token);
    }


    public static String METODO_STATIC_REDEFINIDO_INCORRECTAMENTE(Token token, String nombreMetodo, String s) {
        String mensaje = "El método static " + nombreMetodo + " de la clase " + s + " no puede redefinir un método no static";
        return format(mensaje, token);
    }

    public static String METODO_MAIN_NO_DECLARADO() {
        String mensaje = "No se ha declarado el método main en ninguna clase que sea estático, no retorne nada y no tenga argumentos.";
        return format(mensaje, new TokenImpl("","",0));
    }

    public static String METODO_MAIN_REPETIDO(Token tokenMainEncontrado) {
        String mensaje = "El método main ya ha sido declarado anteriormente.";
        return format(mensaje, tokenMainEncontrado);
    }
}
