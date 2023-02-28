package pasarFichero;

import java.io.*;
import java.net.*;

/**
 *
 * @author gandalfvaro
 */
public class ClienteFichero {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 6000;
        String path = "prueba.txt";

        int numeroBytes = 0;
        try {
            Socket socketCliente = new Socket(host, port);
            
            ObjectOutputStream out = new ObjectOutputStream(socketCliente.getOutputStream());
            MensajeDameFichero mensaje = new MensajeDameFichero();
            mensaje.nombreFichero = path;
            
            System.out.println("Fichero solicitado por el cliente " + mensaje.nombreFichero);
            System.out.println("********************************************************");
            
            out.writeObject(mensaje);
            
            FileOutputStream copia = new FileOutputStream(mensaje.nombreFichero + "_copia");
            ObjectInputStream in = new ObjectInputStream(socketCliente.getInputStream());
            
            MensajeTomaFichero mensajeRecibido;
            Object mensajeAux;
            
            do {
                mensajeAux = in.readObject();
                if (mensajeAux instanceof MensajeTomaFichero) {
                    
                    mensajeRecibido = (MensajeTomaFichero) mensajeAux;
                    System.out.print(new String(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos));
                    copia.write(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos);
                    numeroBytes = numeroBytes + mensajeRecibido.bytesValidos;
                } else {
                    System.err.println("Mensaje no esperado "+ mensajeAux.getClass().getName());
                    break;
                }
            } while (!mensajeRecibido.ultimoMensaje);
            
            System.out.println("\n\nFichero copiado en " + mensaje.nombreFichero + "_copia" + " Se han copiado un total de " + numeroBytes + " Bytes ");

            copia.close();
            in.close();
            socketCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
