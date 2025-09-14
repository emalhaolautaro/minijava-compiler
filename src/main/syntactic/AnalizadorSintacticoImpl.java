package main.syntactic;

import main.errorhandling.exceptions.SyntacticException;
import main.errorhandling.messages.SyntacticErrorMessages;
import main.filemanager.SourceManager;
import main.lexical.AnalizadorLexico;
import main.utils.Primeros;
import main.utils.PrimerosImpl;
import main.utils.Token;

public class AnalizadorSintacticoImpl implements AnalizadorSintactico{

    Token tokenActual;
    AnalizadorLexico aLex;
    SourceManager sourceManager;
    Primeros primeros;

    public AnalizadorSintacticoImpl(AnalizadorLexico aLex, SourceManager sourceManager){
        this.aLex = aLex;
        tokenActual = aLex.proximoToken();
        this.sourceManager = sourceManager;
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
        String tipo = tokenActual.obtenerTipo();

        modificadorOpcional();
        match("class");
        match("idClase");
        herenciaOpcional();
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
        if(tipo.equals("public")){
            match("public");
            match("idClase");
            argsFormales();
            bloque();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("public", tokenActual, sourceManager));
        }
    }

    private void metodoOAtributo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Tipo", tipo)){
            tipo();
            match("idMetVar");
            puntoYComaOAtributoFormal();
        }else if(primeros.incluidoEnPrimeros("Modificador", tipo)){
            modificador();
            tipoMetodo();
            match("idMetVar");
            argsFormales();
            bloqueOpcional();
        }else if(tipo.equals("void")){
            match("void");
            match("idMetVar");
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
            tipo();
            match("idMetVar");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo", tokenActual, sourceManager));
        }
    }

    private void tipoMetodo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("Tipo", tipo)){
            tipo();
        }else if(tipo.equals("void")){
            match("void");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo o 'void'", tokenActual, sourceManager));
        }
    }

    private void modificador() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("abstract")){
            match("abstract");
        } else if (tipo.equals("static")) {
            match("static");
        } else if (tipo.equals("final")) {
            match("final");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un modificador", tokenActual, sourceManager));
        }
    }

    private void puntoYComaOAtributoFormal() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("PuntoYComa")){
            match("PuntoYComa");
        }else if(tipo.equals("ParentesisIzq")){
            argsFormales();
            bloqueOpcional();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("';' o '('", tokenActual, sourceManager));
        }
    }

    private void tipo() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("TipoPrimitivo", tipo)){
            tipoPrimitivo();
        }else if(tipo.equals("idClase")){
            match("idClase");
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo primitivo o un id de clase", tokenActual, sourceManager));
        }
    }

    private void tipoPrimitivo() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("boolean")){
            match("boolean");
        } else if (tipo.equals("char")) {
            match("char");
        } else if (tipo.equals("int")) {
            match("int");
        } else {
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR("un tipo primitivo", tokenActual, sourceManager));
        }
    }

    private void herenciaOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(primeros.incluidoEnPrimeros("HerenciaOpcional", tipo)){
            match("extends");
            match("idClase");
        }else{
            //epsilon
        }
    }

    private void modificadorOpcional() {
        String tipo = tokenActual.obtenerTipo();
        if(tipo.equals("abstract")){
            match("abstract");
        } else if (tipo.equals("static")) {
            match("static");
        }else if (tipo.equals("final")){
            match("final");
        }else{
            //epsilon
        }
    }

    private void match(String nombreToken){
        if(nombreToken.equals(tokenActual.obtenerTipo())){
            tokenActual = aLex.proximoToken();
        }else{
            throw new SyntacticException(SyntacticErrorMessages.SYNTACTIC_ERR(nombreToken, tokenActual, sourceManager));
        }
    }
}