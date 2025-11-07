package main.syntactic;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.exceptions.SyntacticException;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.errorhandling.messages.SyntacticErrorMessages;
import main.filemanager.SourceManager;
import main.lexical.AnalizadorLexico;
import main.semantic.symboltable.TablaSimbolos;
import main.semantic.symboltable.*;
import main.utils.Primeros;
import main.utils.PrimerosImpl;
import main.utils.Token;
import main.utils.TokenImpl;
import main.semantic.nodes.*;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorSintacticoImpl implements AnalizadorSintactico{

    private Token tokenActual;
    private AnalizadorLexico aLex;
    private SourceManager sourceManager;
    private Primeros primeros;
    private TablaSimbolos ts;

    public AnalizadorSintacticoImpl(AnalizadorLexico aLex, SourceManager sourceManager, TablaSimbolos ts){
        this.aLex = aLex;
        tokenActual = aLex.proximoToken();
        this.sourceManager = sourceManager;
        this.ts = ts;
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
        ts.agregarClase(nombre, nuevaClase);
        ts.setClaseActual(nuevaClase);


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
            ts.obtenerClaseActual().agregarConstructor(cons);
            ts.setUnidadActual(cons);

            argsFormales();

            //agregar bloque al constructor
            NodoBloque b = bloque();
            cons.agregarBloque(b);

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
            ts.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            ts.setUnidadActual(nuevoMetodo);
            nuevoMetodo.setearClase(ts.obtenerClaseActual());

            argsFormales();

            //agregar bloque
            NodoBloque b = bloqueOpcional();
            nuevoMetodo.agregarBloque(b);
        }else if(tipo.equals("void")){
            tipoAM = new TipoVoid(tokenActual);
            match("void");
            nombre = tokenActual;
            match("idMetVar");

            Metodo nuevoMetodo = new Metodo(null, tipoAM, nombre);
            ts.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            ts.setUnidadActual(nuevoMetodo);
            nuevoMetodo.setearClase(ts.obtenerClaseActual());

            argsFormales();

            //agregar bloque
            NodoBloque b = bloqueOpcional();
            nuevoMetodo.agregarBloque(b);
        } else{
          throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo, modificador o 'void'", tokenActual, sourceManager));
        }
    }

    private NodoBloque bloqueOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("LlaveIzq")){
            return bloque();
        }else if(tipo.equals("PuntoYComa")){
            match("PuntoYComa");
            return new NodoBloqueNulo();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("'{' o ';'", tokenActual, sourceManager));
        }
    }

    private NodoBloque bloque() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("LlaveIzq")){
            NodoBloque bloque = new NodoBloque();
            match("LlaveIzq");
            if(ts.obtenerUnidadActual() instanceof Metodo){
                ((Metodo) ts.obtenerUnidadActual()).setearCuerpo();
            }
            listaSentencias(bloque);
            match("LlaveDer");
            return bloque;
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("'{'", tokenActual, sourceManager));
        }
    }

    private void listaSentencias(NodoBloque bloque) {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Sentencia", tipo)){
            NodoSentencia sent = sentencia();
            bloque.agregarSentencia(sent);
            listaSentencias(bloque);
        }else {
            //epsilon
        }
    }

    private NodoSentencia sentencia() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("PuntoYComa")) { // Sentencia vacía
            match("PuntoYComa");
            return new NodoSentenciaVacia();
        } else if (primeros.incluidoEnPrimeros("Expresion", tipo)) { // Sentencia de expresión
            NodoExpresion exp = expresion();
            match("PuntoYComa");
            return new NodoSentenciaExpresion(exp);
        } else if (primeros.incluidoEnPrimeros("VarLocal", tipo)) { // Declaración de variable local
            NodoSentencia varLocal = varLocal();
            match("PuntoYComa");
            return varLocal;
        } else if (primeros.incluidoEnPrimeros("Return", tipo)) { // Sentencia return
            NodoSentencia ret = _return();
            match("PuntoYComa");
            return ret;
        } else if (primeros.incluidoEnPrimeros("If", tipo)) { // Sentencia if
            return _if();
        } else if (primeros.incluidoEnPrimeros("While", tipo)) { // Sentencia while
            return _while();
        } else if (primeros.incluidoEnPrimeros("Bloque", tipo)) { // Bloque de sentencias
            return bloque();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una sentencia", tokenActual, sourceManager));
        }
    }

    private NodoSentencia _while() {
        match("while");
        match("ParentesisIzq");
        NodoExpresion cond = expresion();
        match("ParentesisDer");
        NodoSentencia sent = sentencia();
        return new NodoWhile(cond, sent);
    }

    private NodoSentencia _if() {
        match("if");
        match("ParentesisIzq");
        NodoExpresion e = expresion();
        match("ParentesisDer");
        NodoSentencia sentenciaThen = sentencia();
        NodoSentencia sentenciaElse = _else();
        return new NodoIf(e, sentenciaThen, sentenciaElse);
    }

    private NodoSentencia _else() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("else")){
            match("else");
            return sentencia();
        }else{
            //epsilon
        }
        return new NodoSentenciaVacia();
    }

    private NodoSentencia _return() {
        if(ts.obtenerUnidadActual() instanceof Constructor)
            throw new SemanticException(SemanticTwoErrorMessages.CONSTRUCTOR_RETURN_ERR(tokenActual, sourceManager));
        Token r = tokenActual;
        match("return");
        NodoExpresion exp = expresionOpcional();
        return new NodoReturn(exp, ts.obtenerUnidadActual().obtenerTipoRetorno(), r);
    }

    private NodoExpresion expresionOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Expresion", tipo)){
            return expresion();
        }else{
            return new NodoExpresionVacia();
        }
    }

    private NodoSentencia varLocal() {
        match("var");
        Token nombre = tokenActual;
        match("idMetVar");

        if (ts.obtenerUnidadActual().existeVariableLocal(nombre.obtenerLexema()) ||
                ts.obtenerUnidadActual().existeParametro(nombre.obtenerLexema())) {
            throw new SemanticException(SemanticTwoErrorMessages.VAR_LOCAL_EXISTENTE_ERR(nombre));
        }

        match("Igual");
        NodoExpresion expresion = expresionCompuesta();

        NodoVarLocal variable = new NodoVarLocal(nombre, expresion);
        //ts.obtenerUnidadActual().agregarVariableLocal(variable);

        return variable;
    }

    private NodoExpresion expresion() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ExpresionCompuesta", tipo)){
            NodoExpresion e = expresionCompuesta();
            return expresionRecursivo(e);
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una expresion", tokenActual, sourceManager));
        }
    }

    private NodoExpresion expresionRecursivo(NodoExpresion e) {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorAsignacion", tipo)){
            Token operadorAsign = tokenActual;;
            operadorAsignacion();
            NodoExpresion exp = expresionCompuesta();
            return new NodoAsignacion(e, exp, operadorAsign);
        }else{
            //epsilon
        }
        return e;
    }

    private void operadorAsignacion() {
        match("Igual");
    }

    private Token operadorBinario() {
        String tipo = tokenActual.obtenerTipo();
        if (tipo.equals("OrCortocircuito")) {
            Token or = tokenActual;
            match("OrCortocircuito");
            return or;
        } else if (tipo.equals("AndCortocircuito")) { // &&
            Token and = tokenActual;
            match("AndCortocircuito");
            return and;
        } else if (tipo.equals("IgualIgual")) { // ==
            Token igualigual = tokenActual;
            match("IgualIgual");
            return igualigual;
        } else if (tipo.equals("NotIgual")) { // !=
            Token notIgual = tokenActual;
            match("NotIgual");
            return notIgual;
        } else if (tipo.equals("Menor")) { // <
            Token menor = tokenActual;
            match("Menor");
            return menor;
        } else if (tipo.equals("Mayor")) { // >
            Token mayor = tokenActual;
            match("Mayor");
            return mayor;
        } else if (tipo.equals("MenorIgual")) { // <=
            Token menorIgual = tokenActual;
            match("MenorIgual");
            return menorIgual;
        } else if (tipo.equals("MayorIgual")) { // >=
            Token mayorIgual = tokenActual;
            match("MayorIgual");
            return mayorIgual;
        } else if (tipo.equals("Mas")) { // +
            Token mas = tokenActual;
            match("Mas");
            return mas;
        } else if (tipo.equals("Menos")) { // -
            Token menos = tokenActual;
            match("Menos");
            return menos;
        } else if (tipo.equals("Por")) { // *
            Token por = tokenActual;
            match("Por");
            return por;
        } else if (tipo.equals("Dividir")) { // /
            Token div = tokenActual;
            match("Dividir");
            return div;
        } else if (tipo.equals("Modulo")) { // %
            Token modulo = tokenActual;
            match("Modulo");
            return modulo;
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un operador binario", tokenActual, sourceManager));
        }
    }

    private NodoExpresion expresionCompuesta() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ExpresionBasica", tipo)){
            NodoExpresion expBas = expresionBasica();
            return expresionCompuestaRecursivo(expBas);
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("una expresion", tokenActual, sourceManager));
        }
    }

    private NodoExpresion expresionCompuestaRecursivo(NodoExpresion expresionIzq) {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorBinario", tipo)){
            Token opBin = operadorBinario();
            NodoExpresion expDer = expresionBasica();
            NodoExpresionBinaria expresionCompuesta = new NodoExpresionBinaria(opBin, expresionIzq, expDer);
            return expresionCompuestaRecursivo(expresionCompuesta);
        }else{
            //epsilon
        }
        return expresionIzq;
    }

    private NodoExpresion expresionBasica() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("OperadorUnario", tipo)) {
            Token opUnario = operadorUnario();
            NodoExpresion opNode = operando();
            return new NodoExpresionUnaria(opNode, opUnario);
        } else if (primeros.incluidoEnPrimeros("Operando", tipo)) {
            return operando();
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un operando o un operador unario", tokenActual, sourceManager));
        }
        //return new NodoExpresionVacia();
    }

    private NodoExpresion operando() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Primitivo", tipo)){
            return primitivo();
        }else if(primeros.incluidoEnPrimeros("Referencia", tipo)){
            return referencia();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un literal o una referencia", tokenActual, sourceManager));
        }
    }

    private NodoExpresion referencia() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Primario", tipo)){
            NodoExpresion exp = primario();
            NodoEncadenado e = referenciaRecursivo();

            if(exp instanceof NodoAccesoVar) {
                ((NodoAccesoVar) exp).setEncadenado(e);
            }else if(exp instanceof NodoThis){
                ((NodoThis) exp).setEncadenado(e);
            }else if(exp instanceof NodoLlamadaMetodo){
                ((NodoLlamadaMetodo) exp).setEncadenado(e);
            }else if(exp instanceof NodoLlamadaConstructor){
                ((NodoLlamadaConstructor) exp).setEncadenado(e);
            }else if(exp instanceof NodoLlamadaMetodoEstatico){
                ((NodoLlamadaMetodoEstatico) exp).setEncadenado(e);
            }else if(exp instanceof NodoExpresionParentizada){
                ((NodoExpresionParentizada) exp).setEncadenado(e);
            }
            return exp;
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un primario", tokenActual, sourceManager));
        }
        //return new NodoExpresionVacia();
    }

    private NodoEncadenado referenciaRecursivo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Encadenado", tipo)){
            NodoEncadenado enc = encadenado();
            NodoEncadenado sigEnc = referenciaRecursivo();
            enc.setEncadenado(sigEnc);
            return enc;
        }else{
            //epsilon
            return new NodoEncadenadoVacio();
        }
    }

    private NodoEncadenado encadenado() {
        match("Punto");
        Token id = tokenActual;
        match("idMetVar");
        return encadenadoConOSinArgs(id);
    }

    private NodoEncadenado encadenadoConOSinArgs(Token id) {
        String tipo = tokenActual.obtenerTipo();

        if (primeros.incluidoEnPrimeros("ArgsActuales", tipo)) {
            List<NodoExpresion> args = argsActuales();
            NodoLlamadaMetodo nodoIzquierda = new NodoLlamadaMetodo(id, args);
            return new NodoEncadenado(nodoIzquierda, id);

        } else {
            NodoAccesoVar nodoIzquierda = new NodoAccesoVar(id);
            return new NodoEncadenado(nodoIzquierda, id);
        }
    }

    private NodoExpresion primario() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("this")){
            NodoThis th = new NodoThis(tokenActual);
            match("this");
            return th;
        }else if(tipo.equals("stringLiteral")){
            NodoLiteralString literal = new NodoLiteralString(tokenActual);
            match("stringLiteral");
            return literal;
        }else if(tipo.equals("idMetVar")){
            Token id = tokenActual;
            match("idMetVar");
            return primarioSufijo(id);
        }else if(primeros.incluidoEnPrimeros("LlamadaConstructor", tipo)){
            return llamadaConstructor();
        }else if(primeros.incluidoEnPrimeros("LlamadaMetodoEstatico", tipo)){
            return llamadaMetodoEstatico();
        }else if(primeros.incluidoEnPrimeros("ExpresionParentizada", tipo)){
            return expresionParentizada();
        }
        return new NodoExpresionVacia();
    }

    private NodoExpresion expresionParentizada() {
        match("ParentesisIzq");
        NodoExpresion expPar = expresion();
        match("ParentesisDer");
        return new NodoExpresionParentizada(expPar);
    }

    private NodoExpresion llamadaMetodoEstatico() {
        Token id = tokenActual;
        match("idClase");
        match("Punto");
        Token metodo = tokenActual;
        match("idMetVar");
        List<NodoExpresion> args = argsActuales();
        return new NodoLlamadaMetodoEstatico(id, metodo, args);
    }

    private NodoExpresion llamadaConstructor() {
        match("new");
        Token actual = tokenActual;
        match("idClase");

        //argumentos actuales
        List<NodoExpresion> argumentos;
        argumentos = argsActuales();

        return new NodoLlamadaConstructor(actual, argumentos);
    }

    private NodoExpresion primarioSufijo(Token id) {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("ArgsActuales", tipo)){
            List<NodoExpresion> args = argsActuales();
            return new NodoLlamadaMetodo(id, args);
        }else{
            //epsilon
            return new NodoAccesoVar(id);
        }
    }

    private List<NodoExpresion> argsActuales() {
        match("ParentesisIzq");
        List<NodoExpresion> args = listaExpsOpcional();
        match("ParentesisDer");
        return args;
    }

    private List<NodoExpresion> listaExpsOpcional() {
        String tipo = tokenActual.obtenerTipo();
        List<NodoExpresion> listaExps = new ArrayList<>();
        if (primeros.incluidoEnPrimeros("ListaExps", tipo)){
            listaExps(listaExps);
            return listaExps;
        }else{
            //epsilon
            return listaExps;
        }
    }

    private void listaExps(List<NodoExpresion> listaExps) {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Expresion", tipo)) {
            NodoExpresion exp = expresion();
            listaExps.add(exp);
            listaExpsRecursivo(listaExps);
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("expresion", tokenActual, sourceManager));
        }
    }

    private void listaExpsRecursivo(List<NodoExpresion> listaExps) {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("Coma")){
            match("Coma");
            NodoExpresion exp = expresion();
            listaExps.add(exp);
            listaExpsRecursivo(listaExps);
        }else{
            //epsilon
        }
    }

    private NodoExpresion primitivo() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("true")){
            NodoExpresion bool = new NodoLiteralBoolean(tokenActual);
            match("true");
            return bool;
        } else if (tipo.equals("false")) {
            NodoExpresion bool = new NodoLiteralBoolean(tokenActual);
            match("false");
            return bool;
        } else if (tipo.equals("intLiteral")) {
            NodoExpresion intLit = new NodoLiteralInt(tokenActual);
            match("intLiteral");
            return intLit;
        } else if (tipo.equals("charLiteral")) {
            NodoExpresion charLit = new NodoLiteralChar(tokenActual);
            match("charLiteral");
            return charLit;
        } else if (tipo.equals("null")) {
            NodoExpresion nullLit = new NodoLiteralNull(tokenActual);
            match("null");
            return nullLit;
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un literal primitivo", tokenActual, sourceManager));
        }
    }

    private Token operadorUnario() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("Menos")){
            Token menos = tokenActual;
            match("Menos");
            return menos;
        } else if (tipo.equals("MenosMenos")) {
            Token menosMenos = tokenActual;
            match("MenosMenos");
            return menosMenos;
        } else if(tipo.equals("Not")){
            Token not = tokenActual;
            match("Not");
            return not;
        } else if (tipo.equals("Mas")) {
            Token mas = tokenActual;
            match("Mas");
            return mas;
        } else if (tipo.equals("MasMas")) {
            Token masMas = tokenActual;
            match("MasMas");
            return masMas;
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
            ts.obtenerUnidadActual().agregarParametro(p);
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
            type = new TipoVoid(tokenActual);
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


            match("Igual");
            NodoExpresion expAtributo = expresion();
            match("PuntoYComa");

            NodoAtributo atributo = new NodoAtributo(nombre, tipoAM, expAtributo);
            ts.obtenerClaseActual().agregarAtributo(nuevoAtributo, atributo);

        } else if(tipo.equals("PuntoYComa")){ // Atributo sin inicialización
            Atributo nuevoAtributo = new Atributo(nombre, tipoAM, null);
            NodoAtributo atributo = new NodoAtributo(nombre, tipoAM, new NodoExpresionVacia());
            ts.obtenerClaseActual().agregarAtributo(nuevoAtributo, atributo);


            match("PuntoYComa");
        } else if(tipo.equals("ParentesisIzq")){ // Es un método
            Metodo nuevoMetodo = new Metodo(null, tipoAM, nombre);
            ts.obtenerClaseActual().agregarMetodo(nuevoMetodo);
            ts.setUnidadActual(nuevoMetodo);
            nuevoMetodo.setearClase(ts.obtenerClaseActual());

            argsFormales();
            NodoBloque bloque  = bloqueOpcional();
            nuevoMetodo.agregarBloque(bloque);
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
            type = new TipoClase(tokenActual);
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
            type = new TipoBool(tokenActual);
            match("boolean");
        } else if (tipo.equals("char")) {
            type = new TipoChar(tokenActual);
            match("char");
        } else if (tipo.equals("int")) {
            type = new TipoInt(tokenActual);
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
            retorno = new TokenImpl("idClase", "Object", -1);
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