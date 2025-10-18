package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.utils.Token;
import main.utils.TokenImpl;

import java.util.*;

public class TablaSimbolos extends Elemento{
    private Map<Token, Clase> clases;
    private List<Clase> clasesOrdenadas;
    private Clase claseActual;
    private Unidad unidadActual;
    private int cantClasesMain = 0;
    
    public TablaSimbolos(){
        resetearTabla();
    }

    private void resetearTabla() {
        clases = new HashMap<>();
        clasesOrdenadas = new ArrayList<>();
        unidadActual = null;
        clasesPredefinidas();
    }

    public void agregarClase(Token nombre, Clase nuevaClase) {
        if(existeClase(nombre.obtenerLexema())){
            throw new SemanticException(SemanticErrorMessages.CLASE_REPETIDA(nombre, nombre.obtenerLexema()));
        }else{
            if(nombre.obtenerLexema().equals("Main")) cantClasesMain++;
            clases.put(nombre, nuevaClase);
            clasesOrdenadas.add(nuevaClase);
        }
    }

    public void setClaseActual(Clase nuevaClase) {
        claseActual = nuevaClase;
    }

    public void setUnidadActual(Unidad nuevaUnidad){
        unidadActual = nuevaUnidad;
    }

    public Clase obtenerClaseActual(){
        return claseActual;
    }

    public Unidad obtenerUnidadActual(){
        return unidadActual;
    }

    public List<Clase> obtenerClasesOrdenadas(){
        return clasesOrdenadas;
    }

    public Map<Token, Clase> obtenerTablaSimbolos(){
        return clases;
    }

    private void clasesPredefinidas() {
        crearObject();
        crearString();
        crearSystem();
    }

    private void crearSystem() {
        //Crear clase System
        Token nombreClase = new TokenImpl(null, "System", -1);
        Token padre = obtenerClasePorNombre("Object").obtenerNombre();
        Clase system = new Clase(null, nombreClase, padre);
        system.marcarComoPredefinida();
        clases.put(nombreClase, system);

        //Crear constructor
        Constructor cons = new Constructor(nombreClase, new TokenImpl("public", "public", -1));
        try{
            system.agregarConstructor(cons);
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear token modificador comun
        Token modificadorStatic = new TokenImpl(null, "static", -1);

        //Crear metodo read()
        Token metodoRead = new TokenImpl(null, "read", -1);
        Tipo tipoRetornoRead = new Tipo(new TokenImpl(null, "int", -1));
        Metodo read = new Metodo(modificadorStatic, tipoRetornoRead, metodoRead);
        read.marcarComoPredefinido();
        read.setearClase(system);

        //Crear metodo printB(boolean b)
        Token metodoPrintB = new TokenImpl(null, "printB", -1);
        Tipo tipoRetornoPrintB = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintB = new Tipo(new TokenImpl(null, "boolean", -1));
        Metodo printB = new Metodo(modificadorStatic, tipoRetornoPrintB, metodoPrintB);
        printB.marcarComoPredefinido();
        printB.setearClase(system);
        try{
            printB.agregarParametro(new Parametro(tipoParametroPrintB, new TokenImpl(null, "b", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printC(char c)
        Token metodoPrintC = new TokenImpl(null, "printC", -1);
        Tipo tipoRetornoPrintC = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintC = new Tipo(new TokenImpl(null, "char", -1));
        Metodo printC = new Metodo(modificadorStatic, tipoRetornoPrintC, metodoPrintC);
        printC.marcarComoPredefinido();
        printC.setearClase(system);
        try{
            printC.agregarParametro(new Parametro(tipoParametroPrintC, new TokenImpl(null, "c", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printI(int i)
        Token metodoPrintI = new TokenImpl(null, "printI", -1);
        Tipo tipoRetornoPrintI = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintI = new Tipo(new TokenImpl(null, "int", -1));
        Metodo printI = new Metodo(modificadorStatic, tipoRetornoPrintI, metodoPrintI);
        printI.marcarComoPredefinido();
        printI.setearClase(system);
        try{
            printI.agregarParametro(new Parametro(tipoParametroPrintI, new TokenImpl(null, "i", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printS(String s)
        Token metodoPrintS = new TokenImpl(null, "printS", -1);
        Tipo tipoRetornoPrintS = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintS = new Tipo(new TokenImpl(null, "String", -1));
        Metodo printS = new Metodo(modificadorStatic, tipoRetornoPrintS, metodoPrintS);
        printS.marcarComoPredefinido();
        printS.setearClase(system);
        try{
            printS.agregarParametro(new Parametro(tipoParametroPrintS, new TokenImpl(null, "s", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo println()
        Token metodoPrintln = new TokenImpl(null, "println", -1);
        Tipo tipoRetornoPrintln = new Tipo(new TokenImpl(null, "void", -1));
        Metodo println = new Metodo(modificadorStatic, tipoRetornoPrintln, metodoPrintln);
        println.marcarComoPredefinido();
        println.setearClase(system);

        //Crear metodo printBln(boolean b)
        Token metodoPrintBln = new TokenImpl(null, "printBln", -1);
        Tipo tipoRetornoPrintBln = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintBln = new Tipo(new TokenImpl(null, "boolean", -1));
        Metodo printBln = new Metodo(modificadorStatic, tipoRetornoPrintBln, metodoPrintBln);
        printBln.marcarComoPredefinido();
        printBln.setearClase(system);
        try{
            printBln.agregarParametro(new Parametro(tipoParametroPrintBln, new TokenImpl(null, "b", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printCln(char c)
        Token metodoPrintCln = new TokenImpl(null, "printCln", -1);
        Tipo tipoRetornoPrintCln = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintCln = new Tipo(new TokenImpl(null, "char", -1));
        Metodo printCln = new Metodo(modificadorStatic, tipoRetornoPrintCln, metodoPrintCln);
        printCln.marcarComoPredefinido();
        printCln.setearClase(system);
        try{
            printCln.agregarParametro(new Parametro(tipoParametroPrintCln, new TokenImpl(null, "c", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printIln(int i)
        Token metodoPrintIln = new TokenImpl(null, "printIln", -1);
        Tipo tipoRetornoPrintIln = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintIln = new Tipo(new TokenImpl(null, "int", -1));
        Metodo printIln = new Metodo(modificadorStatic, tipoRetornoPrintIln, metodoPrintIln);
        printIln.marcarComoPredefinido();
        printIln.setearClase(system);
        try{
            printIln.agregarParametro(new Parametro(tipoParametroPrintIln, new TokenImpl(null, "i", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printSln(String s)
        Token metodoPrintSln = new TokenImpl(null, "printSln", -1);
        Tipo tipoRetornoPrintSln = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintSln = new Tipo(new TokenImpl(null, "String", -1));
        Metodo printSln = new Metodo(modificadorStatic, tipoRetornoPrintSln, metodoPrintSln);
        printSln.marcarComoPredefinido();
        printSln.setearClase(system);
        try{
            printSln.agregarParametro(new Parametro(tipoParametroPrintSln, new TokenImpl(null, "s", -1)));
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Agregar métodos a la clase System
        try{
            system.agregarMetodo(read);
            system.agregarMetodo(printB);
            system.agregarMetodo(printC);
            system.agregarMetodo(printI);
            system.agregarMetodo(printS);
            system.agregarMetodo(println);
            system.agregarMetodo(printBln);
            system.agregarMetodo(printCln);
            system.agregarMetodo(printIln);
            system.agregarMetodo(printSln);
        }catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

    }

    private void crearString() {
        //Crear clase String
        Token nombreClase = new TokenImpl(null, "String", -1);
        Token padre = obtenerClasePorNombre("Object").obtenerNombre();
        Clase string = new Clase(null, nombreClase, clases.get(padre).obtenerNombre());
        string.marcarComoPredefinida();
        clases.put(nombreClase, string);

        //Crear constructor
        Constructor cons = new Constructor(nombreClase, new TokenImpl("public", "public", -1));
        try{
            string.agregarConstructor(cons);
        }catch (SemanticException e){
            // No debería llegar acá
            e.printStackTrace();
        }
    }

    private void crearObject() {
        //Crear clase Object
        Token nombreClase = new TokenImpl(null, "Object", -1);
        Clase object = new Clase(null, nombreClase, null);
        object.marcarComoPredefinida();
        clases.put(nombreClase, object);

        //Crear constructor
        Constructor cons = new Constructor(nombreClase, new TokenImpl("public", "public", -1));
        try{
            object.agregarConstructor(cons);
        }catch (SemanticException e){
            // No deberia llegar aca
            e.printStackTrace();
        }

        //Crear método static void debugPrint(int i)
        Token metodoNombre = new TokenImpl(null, "debugPrint", -1);
        Token modificador = new TokenImpl(null, "static", -1);
        Tipo tipoRetorno = new Tipo(new TokenImpl(null, "void", -1));
        Tipo tipoParametro = new Tipo(new TokenImpl(null, "int", -1));
        Metodo metodo = new Metodo(modificador, tipoRetorno, metodoNombre);
        metodo.marcarComoPredefinido();
        metodo.setearClase(object);
        try{
            metodo.agregarParametro(new Parametro(tipoParametro, new TokenImpl(null, "i", -1)));
            object.agregarMetodo(metodo);
        }catch (SemanticException e){
            // No debería llegar acá
            e.printStackTrace();
        }

    }

    public Clase obtenerClasePorNombre(String nombre) {
        for (Map.Entry<Token, Clase> entry : clases.entrySet()) {
            if (entry.getKey().obtenerLexema().equals(nombre)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        for (Clase c: clases.values()) {
            c.declaracionCorrecta(this);
        }
        if(cantClasesMain == 0){
            throw new SemanticException(SemanticErrorMessages.CLASE_NO_DECLARADA("Main"));
        }
        if(cantClasesMain > 1){
            throw new SemanticException(SemanticErrorMessages.CLASE_REPETIDA(obtenerClasePorNombre("Main").obtenerNombre()
                    , "Main"));
        }
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {
        for (Clase c: clases.values()) {
            c.consolidar(this);
        }
    }

    public Clase obtenerClasePorLexema(String unidadLexema) {
        for (Clase c : clases.values()) {
            if (c.obtenerNombre().obtenerLexema().equals(unidadLexema)) {
                return c;
            }
        }
        return null;
    }

    public boolean existeClase(String lexema) {
        for (Token t : clases.keySet()) {
            if (t.obtenerLexema().equals(lexema)) return true;
        }
        return false;
    }

    // Dentro de tu clase TablaSimbolos
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- INICIO DE LA TABLA DE SÍMBOLOS ---\n\n");

        for (Clase c : clases.values()) {
            // Delega la impresión a cada clase
            sb.append(c.toString());
            sb.append("----------------------------------------\n");
        }

        sb.append("--- FIN DE LA TABLA DE SÍMBOLOS ---\n");
        return sb.toString();
    }

    public void chequearSentencias() {
        for (Clase c: clases.values()) {
            c.chequearSentencias();
        }
    }

    public void imprimirAST(){
        for(Clase c: clases.values()){
            c.imprimirAST();
        }
    }
}
