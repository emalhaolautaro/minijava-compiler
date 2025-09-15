package main.utils;

import java.util.Set;

public interface Primeros {
    public Set<String> obtenerPrimeros(String noterminal);
    public boolean incluidoEnPrimeros(String noTerminal, String tokenTipo);
}
