///20&30&40&50&60&70&80&exitosamente

class Nodo {
    int valor;
    Nodo izq;
    Nodo der;

    public Nodo(int v) {
        valor = v;
        izq = null; // Inicialización explícita (CRÍTICO para tu malloc)
        der = null;
    }
}

class Arbol {
    Nodo raiz;

    public Arbol() {
        raiz = null;
    }

    // Método público para insertar
    void insertar(int v) {
        if (raiz == null) {
            raiz = new Nodo(v);
        } else {
            insertarRec(raiz, v);
        }
    }

    // Método recursivo de inserción
    // Prueba: Recursión, paso de parámetros, acceso a atributos (LOADREF/STOREREF)
    void insertarRec(Nodo actual, int v) {
        if (v < actual.valor) {
            if (actual.izq == null) {
                actual.izq = new Nodo(v); // Asignación a atributo de tipo clase
            } else {
                insertarRec(actual.izq, v); // Llamada recursiva
            }
        } else {
            if (actual.der == null) {
                actual.der = new Nodo(v);
            } else {
                insertarRec(actual.der, v);
            }
        }
    }

    // Recorrido In-Order (Izquierda -> Raíz -> Derecha)
    // Esto debe imprimir los números ordenados de menor a mayor.
    void imprimir() {
        imprimirRec(raiz);
    }

    void imprimirRec(Nodo actual) {
        if (actual != null) {
            imprimirRec(actual.izq);
            System.printIln(actual.valor); // Imprimir valor del nodo
            imprimirRec(actual.der);
        }
    }
}

class Main {
    static void main() {
        var arbol = new Arbol();

        // Insertamos datos desordenados
        // Estructura esperada:
        //      50
        //     /  \
        //   30    70
        //  /  \  /  \
        // 20 40 60  80

        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(20);
        arbol.insertar(40);
        arbol.insertar(70);
        arbol.insertar(60);
        arbol.insertar(80);

        // Si el árbol funciona bien, esto imprimirá:
        // 20, 30, 40, 50, 60, 70, 80
        arbol.imprimir();
    }
}