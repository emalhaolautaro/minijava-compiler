package main.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class LambdaOrdenamiento {

    /**
     * Ordena una lista de elementos que implementan ElementoConOffset (o un objeto
     * que tenga un método getOffset() o similar) en función de su valor de offset,
     * de forma ascendente (menor a mayor offset).
     *
     * @param <T> El tipo de los elementos en la lista, que deben implementar ElementoConOffset.
     * @param listaLa lista a ordenar (se ordena in-place).
     */
    public static <T extends ElementoConOffset> void ordenarPorOffset(List<T> lista) {

        // Usamos Collections.sort() con un Comparator definido por una expresión Lambda.
        // El orden es ascendente: a.obtenerOffset() - b.obtenerOffset()
        Collections.sort(lista, (a, b) -> Integer.compare(a.obtenerOffset(), b.obtenerOffset()));
    }
}