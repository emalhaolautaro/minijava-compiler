package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.errorhandling.messages.SemanticTwoErrorMessages;
import main.filemanager.OutputManager;
import main.semantic.nodes.*;
import main.utils.Token;
import main.utils.TokenImpl;

import java.util.*;

public class TablaSimbolos extends Elemento {
    private Map<Token, Clase> clases;
    private List<Clase> clasesOrdenadas;
    private Clase claseActual;
    private Unidad unidadActual;

    public TablaSimbolos() {
        resetearTabla();
    }

    private void resetearTabla() {
        clases = new HashMap<>();
        clasesOrdenadas = new ArrayList<>();
        unidadActual = null;
        clasesPredefinidas();
    }

    public void agregarClase(Token nombre, Clase nuevaClase) {
        if (existeClase(nombre.obtenerLexema())) {
            throw new SemanticException(SemanticErrorMessages.CLASE_REPETIDA(nombre, nombre.obtenerLexema()));
        } else {
            clases.put(nombre, nuevaClase);
            clasesOrdenadas.add(nuevaClase);
        }
    }

    public void setClaseActual(Clase nuevaClase) {
        claseActual = nuevaClase;
    }

    public void setUnidadActual(Unidad nuevaUnidad) {
        unidadActual = nuevaUnidad;
    }

    public Clase obtenerClaseActual() {
        return claseActual;
    }

    public Unidad obtenerUnidadActual() {
        return unidadActual;
    }

    public List<Clase> obtenerClasesOrdenadas() {
        return clasesOrdenadas;
    }

    public Map<Token, Clase> obtenerTablaSimbolos() {
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
        cons.agregarBloque(new NodoBloqueNulo());
        try {
            system.agregarConstructor(cons);
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear token modificador comun
        Token modificadorStatic = new TokenImpl("static", "static", -1);

        //Crear metodo read()
        Token metodoRead = new TokenImpl(null, "read", -1);
        Tipo tipoRetornoRead = new TipoInt(new TokenImpl(null, "int", -1));
        Metodo read = new Metodo(modificadorStatic, tipoRetornoRead, metodoRead);
        read.marcarComoPredefinido();
        read.setOffset(-1);
        read.setearClase(system);

        //Crear metodo printB(boolean b)
        Token metodoPrintB = new TokenImpl(null, "printB", -1);
        Tipo tipoRetornoPrintB = new TipoVoid(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintB = new TipoBool(new TokenImpl(null, "boolean", -1));
        Metodo printB = new Metodo(modificadorStatic, tipoRetornoPrintB, metodoPrintB);
        printB.marcarComoPredefinido();
        printB.setOffset(-1);
        printB.setearClase(system);
        try {
            printB.agregarParametro(new Parametro(tipoParametroPrintB, new TokenImpl(null, "b", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printC(char c)
        Token metodoPrintC = new TokenImpl(null, "printC", -1);
        Tipo tipoRetornoPrintC = new TipoVoid(new TokenImpl(null, "void", -1));
        Tipo tipoParametroPrintC = new TipoChar(new TokenImpl(null, "char", -1));
        Metodo printC = new Metodo(modificadorStatic, tipoRetornoPrintC, metodoPrintC);
        printC.marcarComoPredefinido();
        printC.setOffset(-1);
        printC.setearClase(system);
        try {
            printC.agregarParametro(new Parametro(tipoParametroPrintC, new TokenImpl(null, "c", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printI(int i)
        Token metodoPrintI = new TokenImpl(null, "printI", -1);
        Tipo tipoRetornoPrintI = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintI = new TipoInt(new TokenImpl("int", "int", -1));
        Metodo printI = new Metodo(modificadorStatic, tipoRetornoPrintI, metodoPrintI);
        printI.marcarComoPredefinido();
        printI.setOffset(-1);
        printI.setearClase(system);
        try {
            printI.agregarParametro(new Parametro(tipoParametroPrintI, new TokenImpl(null, "i", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printS(String s)
        Token metodoPrintS = new TokenImpl(null, "printS", -1);
        Tipo tipoRetornoPrintS = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintS = new TipoClase(new TokenImpl("String", "String", -1));
        Metodo printS = new Metodo(modificadorStatic, tipoRetornoPrintS, metodoPrintS);
        printS.marcarComoPredefinido();
        printS.setOffset(-1);
        printS.setearClase(system);
        try {
            printS.agregarParametro(new Parametro(tipoParametroPrintS, new TokenImpl(null, "s", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo println()
        Token metodoPrintln = new TokenImpl(null, "println", -1);
        Tipo tipoRetornoPrintln = new TipoVoid(new TokenImpl("void", "void", -1));
        Metodo println = new Metodo(modificadorStatic, tipoRetornoPrintln, metodoPrintln);
        println.marcarComoPredefinido();
        println.setOffset(-1);
        println.setearClase(system);

        //Crear metodo printBln(boolean b)
        Token metodoPrintBln = new TokenImpl(null, "printBln", -1);
        Tipo tipoRetornoPrintBln = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintBln = new TipoBool(new TokenImpl("boolean", "boolean", -1));
        Metodo printBln = new Metodo(modificadorStatic, tipoRetornoPrintBln, metodoPrintBln);
        printBln.marcarComoPredefinido();
        printBln.setOffset(-1);
        printBln.setearClase(system);
        try {
            printBln.agregarParametro(new Parametro(tipoParametroPrintBln, new TokenImpl(null, "b", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printCln(char c)
        Token metodoPrintCln = new TokenImpl(null, "printCln", -1);
        Tipo tipoRetornoPrintCln = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintCln = new TipoChar(new TokenImpl("char", "char", -1));
        Metodo printCln = new Metodo(modificadorStatic, tipoRetornoPrintCln, metodoPrintCln);
        printCln.marcarComoPredefinido();
        printCln.setOffset(-1);
        printCln.setearClase(system);
        try {
            printCln.agregarParametro(new Parametro(tipoParametroPrintCln, new TokenImpl(null, "c", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printIln(int i)
        Token metodoPrintIln = new TokenImpl(null, "printIln", -1);
        Tipo tipoRetornoPrintIln = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintIln = new TipoInt(new TokenImpl("int", "int", -1));
        Metodo printIln = new Metodo(modificadorStatic, tipoRetornoPrintIln, metodoPrintIln);
        printIln.marcarComoPredefinido();
        printIln.setOffset(-1);
        printIln.setearClase(system);
        try {
            printIln.agregarParametro(new Parametro(tipoParametroPrintIln, new TokenImpl(null, "i", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Crear metodo printSln(String s)
        Token metodoPrintSln = new TokenImpl(null, "printSln", -1);
        Tipo tipoRetornoPrintSln = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametroPrintSln = new TipoClase(new TokenImpl("String", "String", -1));
        Metodo printSln = new Metodo(modificadorStatic, tipoRetornoPrintSln, metodoPrintSln);
        printSln.marcarComoPredefinido();
        printSln.setOffset(-1);
        printSln.setearClase(system);
        try {
            printSln.agregarParametro(new Parametro(tipoParametroPrintSln, new TokenImpl(null, "s", -1)));
        } catch (SemanticException e) {
            // No debería llegar acá
            e.printStackTrace();
        }

        //Agregar métodos a la clase System
        try {
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
        } catch (SemanticException e) {
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
        cons.agregarBloque(new NodoBloqueNulo());
        try {
            string.agregarConstructor(cons);
        } catch (SemanticException e) {
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
        cons.agregarBloque(new NodoBloqueNulo());
        try {
            object.agregarConstructor(cons);
        } catch (SemanticException e) {
            // No deberia llegar aca
            e.printStackTrace();
        }

        //Crear método static void debugPrint(int i)
        Token metodoNombre = new TokenImpl(null, "debugPrint", -1);
        Token modificador = new TokenImpl("static", "static", -1);
        Tipo tipoRetorno = new TipoVoid(new TokenImpl("void", "void", -1));
        Tipo tipoParametro = new TipoInt(new TokenImpl("int", "int", -1));
        Metodo metodo = new Metodo(modificador, tipoRetorno, metodoNombre);
        metodo.marcarComoPredefinido();
        metodo.setOffset(-1); // No se usa en la generación de código
        metodo.setearClase(object);
        try {
            metodo.agregarParametro(new Parametro(tipoParametro, new TokenImpl(null, "i", -1)));
            object.agregarMetodo(metodo);
        } catch (SemanticException e) {
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
        for (Clase c : clases.values()) {
            c.declaracionCorrecta(this);
        }
        int cantMain = 0;
        Token tokenMainEncontrado = null;
        for (Clase c : clases.values()) {
            for (Metodo m : c.obtenerMetodos().values()) {
                boolean esMain =
                        m.obtenerNombre().obtenerLexema().equals("main") &&
                                m.esStatic() &&
                                m.obtenerTipoRetorno() instanceof TipoVoid &&
                                m.obtenerParametros().isEmpty();

                if (esMain) {
                    cantMain++;
                    tokenMainEncontrado = m.obtenerNombre();
                }
            }
        }

        if (cantMain == 0) {
            throw new SemanticException(
                    SemanticErrorMessages.METODO_MAIN_NO_DECLARADO()
            );
        }
        if (cantMain > 1) {
            throw new SemanticException(
                    SemanticErrorMessages.METODO_MAIN_REPETIDO(tokenMainEncontrado)
            );
        }
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {
        for (Clase c : clases.values()) {
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
        for (Clase c : clases.values()) {
            claseActual = c;
            c.chequearSentencias();
        }
    }

    public void imprimirAST() {
        for (Clase c : clases.values()) {
            c.imprimirAST();
        }
    }

    public void calcularOffset() {
        for (Clase c : clases.values()) {
            c.calcularOffset();
        }
    }

    public void generar(OutputManager output) {
        Metodo metMain = buscarMetodoMain();

        output.generar(".CODE");


        for (Clase c : clases.values()) {
            c.generar(output);
        }
    }

    public Metodo buscarMetodoMain() {
        for (Clase c : clases.values()) {
            Metodo m = c.obtenerMetodo("main");
            return m;
        }
        throw new SemanticException(SemanticErrorMessages.METODO_MAIN_NO_DECLARADO());
    }
}
