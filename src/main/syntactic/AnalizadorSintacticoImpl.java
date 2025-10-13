package main.syntactic;

import main.errorhandling.exceptions.SyntacticException;
import main.errorhandling.messages.SyntacticErrorMessages;
import main.filemanager.SourceManager;
import main.lexical.AnalizadorLexico;
import main.semantic.symboltable.*;
import main.utils.Primeros;
import main.utils.PrimerosImpl;
import main.utils.Token;
import main.utils.TokenImpl;

public class AnalizadorSintacticoImpl implements AnalizadorSintactico{

    private Token tokenActual;
    private AnalizadorLexico aLex;
    private SourceManager sourceManager;
    private Primeros primeros;
    private TablaSimbolos tablaSimbolos;

    public AnalizadorSintacticoImpl(AnalizadorLexico aLex, SourceManager sourceManager, TablaSimbolos ts){
        this.aLex = aLex;
        tokenActual = aLex.proximoToken();
        this.sourceManager = sourceManager;
        this.tablaSimbolos = ts;
        primeros = new PrimerosImpl();
    }

    @Override
     public void inicial() {
        listaClases();
        match("EOF");
    }

    private void listaClases() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Clase", tipo)){
            clase();
            listaClases();
        }else{
            //epsilon
        }
    }

    private void clase() {
        Token modificador, nombre, padre;

        modificador = modificadorOpcional();
        match("class");
        nombre = tokenActual;
        match("idClase");
        padre = herenciaOpcional();

        Clase nuevaClase = new Clase(modificador, nombre, padre);
        tablaSimbolos.agregarClase(nombre, nuevaClase);
        tablaSimbolos.setClaseActual(nuevaClase);


        match("LlaveIzq");
        listaMiembros();
        match("LlaveDer");
    }

    private void listaMiembros() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Miembro", tipo)){
            miembro();
            listaMiembros();
        }else{
            //epsilon
        }
    }

    private void miembro() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("MetodoOAtributo", tipo)){
            metodoOAtributo();
        }else if(primeros.incluidoEnPrimeros("Constructor", tipo)){
            constructor();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un miembro de clase (atributo, metodo o constructor)", tokenActual, sourceManager));
        }
    }

    private void constructor() {
        String tipo = tokenActual.obtenerTipo();
        Token visibilidad, nombre;

        if(tipo.equals("public")){
            visibilidad = tokenActual;
            match("public");
            nombre = tokenActual;
            match("idClase");

            Constructor cons =  new Constructor(nombre, visibilidad);
            tablaSimbolos.obtenerClaseActual().agregarConstructor(cons);
            tablaSimbolos.setUnidadActual(cons);

            argsFormales();
            bloque();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("public", tokenActual, sourceManager));
        }
    }

    private void metodoOAtributo() {
        String tipo = tokenActual.obtenerTipo();
        Tipo tipoAM;
        Token nombre, modificador;
        if(primeros.incluidoEnPrimeros("Tipo", tipo)){
            tipoAM = tipo();
            nombre = tokenActual;
            match("idMetVar");
            puntoYComaOAtributoFormal(tipoAM, nombre);
        }else if(primeros.incluidoEnPrimeros("Modificador", tipo)){
            modificador = modificador();
            tipoAM = tipoMetodo();
            nombre = tokenActual;
            match("idMetVar");

            Metodo nuevoMetodo = new Metodo(modificador, tipoAM, nombre);
            tablaSimbolos.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            tablaSimbolos.setUnidadActual(nuevoMetodo);

            argsFormales();
            bloqueOpcional();
        }else if(tipo.equals("void")){
            tipoAM = new Tipo(tokenActual);
            match("void");
            nombre = tokenActual;
            match("idMetVar");

            Metodo nuevoMetodo = new Metodo(null, tipoAM, nombre);
            tablaSimbolos.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            tablaSimbolos.setUnidadActual(nuevoMetodo);

            argsFormales();
            bloqueOpcional();
        } else{
          throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo, modificador o 'void'", tokenActual, sourceManager));
        }
    }

    private void bloqueOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("LlaveIzq")){
            bloque();
        }else if(tipo.equals("PuntoYComa")){
            match("PuntoYComa");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("'{' o ';'", tokenActual, sourceManager));
        }
    }

    private void bloque() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("LlaveIzq")){
            match("LlaveIzq");
            if(tablaSimbolos.obtenerUnidadActual() instanceof Metodo){
                ((Metodo) tablaSimbolos.obtenerUnidadActual()).setearCuerpo();
            }
            listaSentencias();
            match("LlaveDer");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("'{'", tokenActual, sourceManager));
        }
    }

    private void listaSentencias() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Sentencia", tipo)){
            sentencia();
            listaSentencias();
        }else {
            //epsilon
        }
    }

    private void sentencia() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("PuntoYComa")) { // Sentencia vacía
            match("PuntoYComa");
        } else if (primeros.incluidoEnPrimeros("Expresion", tipo)) { // Sentencia de expresión
            expresion();
            match("PuntoYComa");
        } else if (primeros.incluidoEnPrimeros("VarLocal", tipo)) { // Declaración de variable local
            varLocal();
            match("PuntoYComa");
        } else if (primeros.incluidoEnPrimeros("Return", tipo)) { // Sentencia return
            _return();
            match("PuntoYComa");
        } else if (primeros.incluidoEnPrimeros("If", tipo)) { // Sentencia if
            _if();
        } else if (primeros.incluidoEnPrimeros("While", tipo)) { // Sentencia while
            _while();
        } else if (primeros.incluidoEnPrimeros("Bloque", tipo)) { // Bloque de sentencias
            bloque();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una sentencia", tokenActual, sourceManager));
        }
    }

    private void _while() {
        match("while");
        match("ParentesisIzq");
        expresion();
        match("ParentesisDer");
        sentencia();
    }

    private void _if() {
        match("if");
        match("ParentesisIzq");
        expresion();
        match("ParentesisDer");
        sentencia();
        _else();
    }

    private void _else() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("else")){
            match("else");
            sentencia();
        }else{
            //epsilon
        }
    }

    private void _return() {
        match("return");
        expresionOpcional();
    }

    private void expresionOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Expresion", tipo)){
            expresion();
        }else{
            //epsilon
        }
    }

    private void varLocal() {
        match("var");
        match("idMetVar");
        match("Igual");
        expresionCompuesta();
    }

    private void expresion() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ExpresionCompuesta", tipo)){
            expresionCompuesta();
            expresionRecursivo();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una expresion", tokenActual, sourceManager));
        }
    }

    private void expresionRecursivo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorAsignacion", tipo)){
            operadorAsignacion();
            expresionCompuesta();
        }else{
            //epsilon
        }
    }

    private void operadorAsignacion() {
        match("Igual");
    }

    private void operadorBinario() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("OrCortocircuito")) { // ||
            match("OrCortocircuito");
        } else if (tipo.equals("AndCortocircuito")) { // &&
            match("AndCortocircuito");
        } else if (tipo.equals("IgualIgual")) { // ==
            match("IgualIgual");
        } else if (tipo.equals("NotIgual")) { // !=
            match("NotIgual");
        } else if (tipo.equals("Menor")) { // <
            match("Menor");
        } else if (tipo.equals("Mayor")) { // >
            match("Mayor");
        } else if (tipo.equals("MenorIgual")) { // <=
            match("MenorIgual");
        } else if (tipo.equals("MayorIgual")) { // >=
            match("MayorIgual");
        } else if (tipo.equals("Mas")) { // +
            match("Mas");
        } else if (tipo.equals("Menos")) { // -
            match("Menos");
        } else if (tipo.equals("Por")) { // *
            match("Por");
        } else if (tipo.equals("Dividir")) { // /
            match("Dividir");
        } else if (tipo.equals("Modulo")) { // %
            match("Modulo");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un operador binario", tokenActual, sourceManager));
        }
    }

    private void expresionCompuesta() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ExpresionBasica", tipo)){
            expresionBasica();
            expresionCompuestaRecursivo();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una expresion", tokenActual, sourceManager));
        }
    }

    private void expresionCompuestaRecursivo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorBinario", tipo)){
            operadorBinario();
            expresionBasica();
            expresionCompuestaRecursivo();
        }else{
            //epsilon
        }
    }

    private void expresionBasica() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorUnario", tipo)) {
            operadorUnario();
            operando();
        } else if (primeros.incluidoEnPrimeros("Operando", tipo)) {
            operando();
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un operando o un operador unario", tokenActual, sourceManager));
        }
    }

    private void operando() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Primitivo", tipo)){
            primitivo();
        }else if(primeros.incluidoEnPrimeros("Referencia", tipo)){
            referencia();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un literal o una referencia", tokenActual, sourceManager));
        }
    }

    private void referencia() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Primario", tipo)){
            primario();
            referenciaRecursivo();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un primario", tokenActual, sourceManager));
        }
    }

    private void referenciaRecursivo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Encadenado", tipo)){
            encadenado();
            referenciaRecursivo();
        }else{
            //epsilon
        }
    }

    private void encadenado() {
        match("Punto");
        match("idMetVar");
        encadenadoConOSinArgs();
    }

    private void encadenadoConOSinArgs() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ArgsActuales", tipo)){
            argsActuales();
        }else{
            //epsilon
        }
    }

    private void primario() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("this")){
            match("this");
        }else if(tipo.equals("stringLiteral")){
            match("stringLiteral");
        }else if(tipo.equals("idMetVar")){
            match("idMetVar");
            primarioSufijo();
        }else if(primeros.incluidoEnPrimeros("LlamadaConstructor", tipo)){
            llamadaConstructor();
        }else if(primeros.incluidoEnPrimeros("LlamadaMetodoEstatico", tipo)){
            llamadaMetodoEstatico();
        }else if(primeros.incluidoEnPrimeros("ExpresionParentizada", tipo)){
            expresionParentizada();
        }
    }

    private void expresionParentizada() {
        match("ParentesisIzq");
        expresion();
        match("ParentesisDer");
    }

    private void llamadaMetodoEstatico() {
        match("idClase");
        match("Punto");
        match("idMetVar");
        argsActuales();
    }

    private void llamadaConstructor() {
        match("new");
        match("idClase");
        argsActuales();
    }

    private void primarioSufijo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ArgsActuales", tipo)){
            argsActuales();
        }else{
            //epsilon
        }
    }

    private void argsActuales() {
        match("ParentesisIzq");
        listaExpsOpcional();
        match("ParentesisDer");
    }

    private void listaExpsOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if (primeros.incluidoEnPrimeros("ListaExps", tipo)){
            listaExps();
        }else{
            //epsilon
        }
    }

    private void listaExps() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Expresion", tipo)) {
            expresion();
            listaExpsRecursivo();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("expresion", tokenActual, sourceManager));
        }
    }

    private void listaExpsRecursivo() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("Coma")){
            match("Coma");
            expresion();
            listaExpsRecursivo();
        }else{
            //epsilon
        }
    }

    private void primitivo() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("true")){
            match("true");
        } else if (tipo.equals("false")) {
            match("false");
        } else if (tipo.equals("intLiteral")) {
            match("intLiteral");
        } else if (tipo.equals("charLiteral")) {
            match("charLiteral");
        } else if (tipo.equals("null")) {
            match("null");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un literal primitivo", tokenActual, sourceManager));
        }
    }

    private void operadorUnario() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("Menos")){
            match("Menos");
        } else if (tipo.equals("MenosMenos")) {
            match("MenosMenos");
        } else if(tipo.equals("Not")){
            match("Not");
        } else if (tipo.equals("Mas")) {
            match("Mas");
        } else if (tipo.equals("MasMas")) {
            match("MasMas");
        } else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un operador unario", tokenActual, sourceManager));
        }
    }

    private void argsFormales() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("ParentesisIzq")){
            match("ParentesisIzq");
            listaArgsFormalesOpcional();
            match("ParentesisDer");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("'('", tokenActual, sourceManager));
        }
    }

    private void listaArgsFormalesOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ListaArgsFormalesOpcional", tipo)){
            listaArgsFormales();
        }else{
            //epsilon
        }
    }

    private void listaArgsFormales() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ArgFormal", tipo)){
            argFormal();
            listaArgumentosF();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo de argumento", tokenActual, sourceManager));
        }
    }

    private void listaArgumentosF() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("Coma")){
            match("Coma");
            argFormal();
            listaArgumentosF();
        }else{
            //epsilon
        }
    }

    private void argFormal() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Tipo", tipo)){
            Tipo type = tipo();
            Token nombre = tokenActual;
            match("idMetVar");

            Parametro p = new Parametro(type, nombre);
            tablaSimbolos.obtenerUnidadActual().agregarParametro(p);
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo", tokenActual, sourceManager));
        }
    }

    private Tipo tipoMetodo() {
        String tipo = tokenActual.obtenerTipo();
        Tipo type;

        if(primeros.incluidoEnPrimeros("Tipo", tipo)){
            type = tipo();
        }else if(tipo.equals("void")){
            type = new Tipo(tokenActual);
            match("void");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo o 'void'", tokenActual, sourceManager));
        }
        return type;
    }

    private Token modificador() {
        String tipo = tokenActual.obtenerTipo();
        Token modificador;
        if(tipo.equals("abstract")){
            modificador = tokenActual;
            match("abstract");
        } else if (tipo.equals("static")) {
            modificador = tokenActual;
            match("static");
        } else if (tipo.equals("final")) {
            modificador = tokenActual;
            match("final");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un modificador", tokenActual, sourceManager));
        }
        return modificador;
    }

    private void puntoYComaOAtributoFormal(Tipo tipoAM, Token nombre) {
        String tipo = tokenActual.obtenerTipo();
        /* Ahora hay 3 posibilidades después de un 'idMetVar':
        1. Un '=' para inicializar un atributo.
        2. Un ';' para declarar un atributo sin inicializar.
        3. Un '(' para declarar un método.*/
        if (tipo.equals("Igual")) { // Atributo con inicialización
            Atributo nuevoAtributo = new Atributo(nombre, tipoAM, null);
            tablaSimbolos.obtenerClaseActual().agregarAtributo(nuevoAtributo, tablaSimbolos);

            match("Igual");
            expresion();
            match("PuntoYComa");
        } else if(tipo.equals("PuntoYComa")){ // Atributo sin inicialización
            Atributo nuevoAtributo = new Atributo(nombre, tipoAM, null);
            tablaSimbolos.obtenerClaseActual().agregarAtributo(nuevoAtributo, tablaSimbolos);

            match("PuntoYComa");
        } else if(tipo.equals("ParentesisIzq")){ // Es un método
            Metodo nuevoMetodo = new Metodo(null, tipoAM, nombre);
            tablaSimbolos.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            tablaSimbolos.setUnidadActual(nuevoMetodo);

            argsFormales();
            bloqueOpcional();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("';', '=' o '('", tokenActual, sourceManager));
        }
    }

    private Tipo tipo() {
        String tipo = tokenActual.obtenerTipo();
        Tipo type;
        if(primeros.incluidoEnPrimeros("TipoPrimitivo", tipo)){
            type = tipoPrimitivo();
        }else if(tipo.equals("idClase")){
            type = new Tipo(tokenActual);
            match("idClase");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo primitivo o un id de clase", tokenActual, sourceManager));
        }
        return type;
    }

    private Tipo tipoPrimitivo() {
        String tipo = tokenActual.obtenerTipo();
        Tipo type;
        if(tipo.equals("boolean")){
            type = new Tipo(tokenActual);
            match("boolean");
        } else if (tipo.equals("char")) {
            type = new Tipo(tokenActual);
            match("char");
        } else if (tipo.equals("int")) {
            type = new Tipo(tokenActual);
            match("int");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo primitivo", tokenActual, sourceManager));
        }
        return type;
    }

    private Token herenciaOpcional() {
        String tipo = tokenActual.obtenerTipo();
        Token retorno;
        if(primeros.incluidoEnPrimeros("HerenciaOpcional", tipo)){
            match("extends");
            retorno = tokenActual;
            match("idClase");
        }else{
            //epsilon
            retorno = new TokenImpl(null, "Object", -1);
        }

        return retorno;
    }

    private Token modificadorOpcional() {
        String tipo = tokenActual.obtenerTipo();
        Token retorno;

        if(tipo.equals("abstract")){
            retorno = tokenActual;
            match("abstract");
        } else if (tipo.equals("static")) {
            retorno = tokenActual;
            match("static");
        }else if (tipo.equals("final")){
            retorno = tokenActual;
            match("final");
        }else{
            //epsilon
            retorno = null;
        }

        return retorno;
    }

    private void match(String nombreToken){
        if(nombreToken.equals(tokenActual.obtenerTipo())){
            tokenActual = aLex.proximoToken();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR(nombreToken, tokenActual, sourceManager));
        }
    }
}