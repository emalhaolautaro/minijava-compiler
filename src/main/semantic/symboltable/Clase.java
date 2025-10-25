package main.semantic.symboltable;

import main.errorhandling.exceptions.SemanticException;
import main.errorhandling.messages.SemanticErrorMessages;
import main.semantic.nodes.NodoBloqueNulo;
import main.utils.Token;
import main.utils.TokenImpl;

import java.util.*;

import static main.Main.tablaSimbolos;

public class Clase extends Elemento {
    private Token modificador;
    private Token padre;
    private Map<String, Atributo> atributos;
    private Map<String, Metodo> metodos;
    private Map<String, Constructor> constructores;
    private boolean consolidada = false;
    private boolean esPredefinida = false;

    public Clase(Token modificador, Token nombre, Token padre) {
        super(nombre);
        this.modificador = modificador;
        this.padre = padre;
        this.constructores = new HashMap<>();
        this.atributos = new HashMap<>();
        this.metodos = new HashMap<>();
    }

    public void agregarAtributo(Atributo atributo, TablaSimbolos ts) {
        if (atributos.containsKey(atributo.obtenerNombre().obtenerLexema())) {
            throw new SemanticException(SemanticErrorMessages.ATRIBUTO_REPETIDO(
                    atributo.obtenerNombre(),
                    atributo.obtenerNombre().obtenerLexema(),
                    this.nombre.obtenerLexema()
            ));
        }
        if (padre != null && !padre.obtenerLexema().equals("Object")) {
            Clase clasePadre = ts.obtenerClasePorNombre(padre.obtenerLexema());
            if (clasePadre != null && clasePadre.obtenerAtributos().containsKey(atributo.obtenerNombre().obtenerLexema())) {
                throw new SemanticException(SemanticErrorMessages.ATRIBUTO_REDEFINIDO(
                        atributo.obtenerNombre(),
                        atributo.obtenerNombre().obtenerLexema(),
                        this.nombre.obtenerLexema()
                ));
            }
        }

        atributos.put(atributo.obtenerNombre().obtenerLexema(), atributo);
    }

    public void agregarMetodo(Metodo metodo) {
        if (metodos.containsKey(metodo.obtenerNombre().obtenerLexema())) {
            throw new SemanticException(SemanticErrorMessages.METODO_REPETIDO(
                    metodo.obtenerNombre(),
                    metodo.obtenerNombre().obtenerLexema(),
                    this.nombre.obtenerLexema()
            ));
        }
        metodos.put(metodo.obtenerNombre().obtenerLexema(), metodo);
    }

    public void agregarConstructor(Constructor constructor) {
        if (constructores.containsKey(constructor.obtenerNombre().obtenerLexema())) {
            throw new SemanticException(SemanticErrorMessages.CONSTRUCTOR_REPETIDO(
                    constructor.obtenerNombre(),
                    constructor.obtenerNombre().obtenerLexema(),
                    this.nombre.obtenerLexema()
            ));
        }
        if(!constructor.obtenerNombre().obtenerLexema().equals(this.nombre.obtenerLexema())){
            throw new SemanticException(SemanticErrorMessages.CONSTRUCTOR_INCOMPATIBLE(constructor.obtenerNombre(),
                    constructor.obtenerNombre().obtenerLexema(), this.nombre.obtenerLexema()));
        }
        constructores.put(constructor.obtenerNombre().obtenerLexema(), constructor);
    }

    public Token obtenerModificador() {
        return modificador;
    }

    public Token obtenerPadre() {
        return padre;
    }

    public Map<String, Atributo> obtenerAtributos() {
        return atributos;
    }

    public Atributo obtenerAtributo(String nombre) {
        return atributos.get(nombre);
    }

    public boolean existeAtributo(String nombre) {
        return atributos.containsKey(nombre);
    }

    public Map<String, Metodo> obtenerMetodos() {
        return metodos;
    }

    public Metodo obtenerMetodo(String nombre) {
        return metodos.get(nombre);
    }

    public Map<String, Constructor> obtenerConstructor() {
        return constructores;
    }

    @Override
    public void declaracionCorrecta(TablaSimbolos ts) {
        if (padre != null && !padre.obtenerLexema().equals("Object")) {

            Clase clasePadre = ts.obtenerClasePorNombre(padre.obtenerLexema());
            if (padre.obtenerLexema().equals(nombre.obtenerLexema())) {
                throw new SemanticException(SemanticErrorMessages.CLASE_HEREDA_DE_SI_MISMA(
                        nombre, nombre.obtenerLexema()
                ));
            }
            if (clasePadre == null) {
                throw new SemanticException(SemanticErrorMessages.CLASE_PADRE_NO_DECLARADA(
                        padre, padre.obtenerLexema(), nombre.obtenerLexema()
                ));
            }

            chequeoHerenciaCircular(ts);

            if (clasePadre.esFinal()) {
                throw new SemanticException(SemanticErrorMessages.CLASE_FINAL_HEREDADA(
                        padre, nombre.obtenerLexema(), padre.obtenerLexema()
                ));
            }
            if (esAbstracta() && !clasePadre.esAbstracta()) {
                throw new SemanticException(SemanticErrorMessages.CLASE_ABSTRACTA_HEREDA_DE_CLASE_NO_ABSTRACTA(
                        nombre, nombre.obtenerLexema(), padre.obtenerLexema()
                ));
            }
            if (clasePadre.esStatic()) {
                throw new SemanticException(SemanticErrorMessages.CLASE_PADRE_STATIC(
                        padre, nombre.obtenerLexema(), padre.obtenerLexema()
                ));
            }
        }

        // Chequeos de atributos
        for (Atributo a : atributos.values()) {
            a.declaracionCorrecta(ts);
        }

        // Chequeos de métodos
        for (Metodo m : metodos.values()) {
            m.declaracionCorrecta(ts);

            if (m.obtenerModificador() != null &&
                    "abstract".equals(m.obtenerModificador().obtenerLexema()) &&
                    !esAbstracta()) {
                throw new SemanticException(SemanticErrorMessages.CLASE_ABSTRACTA_SIN_MODIFICADOR(
                        m.obtenerNombre(), nombre.obtenerLexema(), m.obtenerNombre().obtenerLexema()
                ));
            }
            if(m.esAbstracto() && m.tieneCuerpo()){
                throw new SemanticException(SemanticErrorMessages.METODO_ABSTRACTO_CON_CUERPO(
                        m.obtenerNombre(),
                        m.obtenerNombre().obtenerLexema(),
                        nombre.obtenerLexema()
                ));
            }
            if(!m.esAbstracto() && !m.tieneCuerpo() && !m.esPredefinido()){
                throw new SemanticException(SemanticErrorMessages.METODO_NO_ABSTRACTO_SIN_CUERPO(
                        m.obtenerNombre(),
                        m.obtenerNombre().obtenerLexema(),
                        nombre.obtenerLexema()
                ));
            }
        }

        // Chequeo de constructores
        if (esAbstracta() && !constructores.isEmpty()) {
            throw new SemanticException(SemanticErrorMessages.CLASE_ABSTRACTA_CON_CONSTRUCTORES(
                    constructores.values().iterator().next().obtenerNombre(), nombre.obtenerLexema()
            ));
        } else {
            for (Constructor c : constructores.values()) {
                c.declaracionCorrecta(ts);
            }
        }

        aniadirConstructorPredefinido();
    }

    private void aniadirConstructorPredefinido() {
        if (constructores.isEmpty() && !esAbstracta()) {
            Token nombrePredefinido = new TokenImpl("idClase", nombre.obtenerLexema(), -1);
            Token modificadorPredefinido = new TokenImpl("public", "public", -1);
            Constructor predefinido = new Constructor(nombrePredefinido, modificadorPredefinido);
            predefinido.agregarBloque(new NodoBloqueNulo());
            constructores.put(nombrePredefinido.obtenerLexema(), predefinido);
        }
    }

    private void chequeoHerenciaCircular(TablaSimbolos ts) {
        // Map para guardar el estado de cada clase
        Map<Clase, Estado> estados = new HashMap<>();

        // Inicializamos todos los estados como NO_VISITADO
        for (Clase c : ts.obtenerClasesOrdenadas()) {
            estados.put(c, Estado.NO_VISITADO);
        }

        // Recorremos todas las clases en orden de declaración
        for (Clase c : ts.obtenerClasesOrdenadas()) {
            if (estados.get(c) == Estado.NO_VISITADO) {
                dfsChequeo(c, ts, estados, new ArrayList<>());
            }
        }
    }

    // DFS recursivo
    private void dfsChequeo(Clase clase, TablaSimbolos ts, Map<Clase, Estado> estados, List<Token> camino) {
        Estado estadoActual = estados.get(clase);
        if (estadoActual == Estado.VISITANDO) {
            // Ciclo detectado, reportamos el primer token repetido
            Token primerTokenCiclo = camino.stream()
                    .filter(t -> t.obtenerLexema().equals(clase.obtenerNombre().obtenerLexema()))
                    .findFirst()
                    .get();
            throw new SemanticException(SemanticErrorMessages.HERENCIA_CIRCULAR(
                    primerTokenCiclo, // primer nodo del ciclo
                    clase.obtenerNombre().obtenerLexema() // nodo actual (cierra el ciclo)
            ));
        }

        if (estadoActual == Estado.VISITADO) return;

        // Marcamos como visitando y agregamos al camino
        estados.put(clase, Estado.VISITANDO);
        camino.add(clase.obtenerNombre());

        // DFS hacia el padre si existe
        Token padreToken = clase.obtenerPadre();
        if (padreToken != null && !"Object".equals(padreToken.obtenerLexema())) {
            Clase padre = ts.obtenerClasePorNombre(padreToken.obtenerLexema());
            if (padre != null) {
                dfsChequeo(padre, ts, estados, camino);
            }
        }

        // Terminamos la visita
        estados.put(clase, Estado.VISITADO);
        camino.remove(camino.size() - 1); // sacamos del camino al volver
    }

    public void setearPadre(Token object) {
        this.padre = object;
    }

    public void imprimirAST() {
        System.out.println("Clase: " + nombre.obtenerLexema());
        for(Constructor c: constructores.values()){
            c.imprimirAST(1);
        }

        for(Metodo m: metodos.values()){
            m.imprimirAST(1);
        }
    }

    // Estado para DFS
    private enum Estado { NO_VISITADO, VISITANDO, VISITADO }

    public void chequearSentencias() {
        for(Constructor c : constructores.values()) {
            tablaSimbolos.setUnidadActual(c);
            c.chequearSentencias();
        }

        for (Metodo m : metodos.values()) {
            if (m.tieneCuerpo() && !m.esPredefinido()) {
                tablaSimbolos.setUnidadActual(m);
                m.chequearSentencias();
            }
        }
    }

    @Override
    public void consolidar(TablaSimbolos ts) throws SemanticException {
        if(consolidada){
            return;
        }else{
            consolidada = true;
        }

        if (padre != null) {
            Clase clasePadre = ts.obtenerClasePorNombre(padre.obtenerLexema());

            clasePadre.consolidar(ts);

            // Heredar atributos
            for (Atributo a : clasePadre.obtenerAtributos().values()) {
                String nombreAtrib = a.obtenerNombre().obtenerLexema();
                if (!atributos.containsKey(nombreAtrib)) {
                    atributos.put(nombreAtrib, a); // heredamos
                    System.out.println(">> " + this.nombre.obtenerLexema() + " hereda atributo " + nombreAtrib);
                }
                // NO lanzamos error si ya existe, eso se chequea al agregar el atributo explícitamente
            }

            // Heredar métodos
            for (Metodo metodoPadre : clasePadre.obtenerMetodos().values()) {
                String nombreMetodo = metodoPadre.obtenerNombre().obtenerLexema();
                if (metodos.containsKey(nombreMetodo)) {
                    Metodo metodoHijo = metodos.get(nombreMetodo);

                    if (metodoPadre.esFinal() || metodoPadre.esStatic()) {
                        throw new SemanticException(SemanticErrorMessages.METODO_FINAL_O_STATIC_REDEFINIDO(
                                metodoHijo.obtenerNombre(), nombreMetodo, nombre.obtenerLexema()
                        ));
                    }

                    if (!metodoPadre.obtenerTipoRetorno().equals(metodoHijo.obtenerTipoRetorno())
                            || !metodoPadre.mismaFirma(metodoHijo)) {
                        throw new SemanticException(SemanticErrorMessages.METODO_REDEFINIDO_CON_DIFERENCIAS(
                                metodoHijo.obtenerNombre(), nombreMetodo, nombre.obtenerLexema()
                        ));
                    }

                    if (!metodoPadre.mismaFirma(metodoHijo)) {
                        throw new SemanticException(SemanticErrorMessages.METODO_REDEFINICION_FIRMA_INVALIDA(
                                nombre, metodoHijo.obtenerNombre().obtenerLexema(), clasePadre.obtenerNombre().obtenerLexema()
                        ));
                    }

                    if (metodoPadre.esStatic() != metodoHijo.esStatic()) {
                        throw new SemanticException(
                                SemanticErrorMessages.METODO_STATIC_REDEFINIDO_INCORRECTAMENTE(
                                        metodoHijo.obtenerNombre(),
                                        nombreMetodo,
                                        nombre.obtenerLexema()
                                )
                        );
                    }
                } else {
                    metodos.put(nombreMetodo, metodoPadre);
                }
            }

            // Después de heredar, verificamos si quedaron métodos abstractos sin implementar en una clase concreta.
            if (!esAbstracta()) {
                for (Metodo metodoHeredado : metodos.values()) {
                    if (metodoHeredado.esAbstracto()) {
                        throw new SemanticException(SemanticErrorMessages.METODO_ABSTRACTO_NO_IMPLEMENTADO(
                                nombre, metodoHeredado.obtenerNombre().obtenerLexema(), nombre.obtenerLexema()
                        ));
                    }
                }
            }
        }
    }

    public boolean esAbstracta() {
        return modificador != null && "abstract".equals(modificador.obtenerLexema());
    }

    public boolean esFinal() {
        return modificador != null && "final".equals(modificador.obtenerLexema());
    }

    public boolean esStatic() {
        return modificador != null && "static".equals(modificador.obtenerLexema());
    }

    public boolean tieneAncestro() {
        return padre != null;
    }

    public boolean esPredefinida() {
        return esPredefinida;
    }

    public void marcarComoPredefinida() {
        this.esPredefinida = true;
    }

    // Dentro de tu clase Clase
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // --- Cabecera de la Clase ---
        sb.append("class ").append(this.obtenerNombre().obtenerLexema());
        // Añadir herencia solo si no es 'Object'
        if (this.padre != null && !this.padre.obtenerLexema().equals("Object")) {
            sb.append(" extends ").append(this.padre.obtenerLexema());
        }
        sb.append(" {\n");

        // --- Atributos ---
        sb.append("\tAtributos:\n");
        if (atributos.isEmpty()) {
            sb.append("\t\t(ninguno)\n");
        } else {
            for (Atributo attr : atributos.values()) {
                // Delega la impresión al toString() del atributo
                sb.append("\t\t- ").append(attr.toString()).append("\n");
            }
        }

        // --- Constructores ---
        sb.append("\n\tConstructores:\n");
        if (constructores.isEmpty()) {
            sb.append("\t\t(ninguno)\n");
        } else {
            for (Constructor c : constructores.values()) {
                // Delega la impresión al toString() del constructor
                sb.append("\t\t- ").append(c.toString()).append("\n");
            }
        }

        // --- Métodos ---
        sb.append("\n\tMetodos:\n");
        if (metodos.isEmpty()) {
            sb.append("\t\t(ninguno)\n");
        } else {
            for (Metodo m : metodos.values()) {
                // Delega la impresión al toString() del método
                sb.append("\t\t- ").append(m.toString()).append("\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }
}
