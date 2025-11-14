package main.filemanager;

import java.io.FileWriter;
import java.io.IOException;

public class OutputManagerImpl implements OutputManager{
    private FileWriter filewriter;
    int contador = 0;
    int contadorStrings = 0;

    public OutputManagerImpl(String file_name) {
        try {
            filewriter = new FileWriter(file_name);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void generar(String linea){
        try {
            filewriter.write(linea);
            filewriter.write("\n");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void cerrar(){
        try {
            filewriter.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int obtenerEIncrementarContIfsWhiles(){
        contador++;
        return contador;
    }

    public int obtenerEIncrementarContsStrings(){
        contadorStrings++;
        return contadorStrings;
    }
}
