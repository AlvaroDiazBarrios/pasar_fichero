package pasarFichero;

import java.io.*;
import java.net.*;

/**
 *
 * @author gandalfvaro
 */
public class ServidorFichero {

    public static void main(String[] args) {
        int port = 6000; //puerto de escucha del servidor
        
        try {
            ServerSocket socketServidor = new ServerSocket(port); //Creamos el serverSocket 
            System.out.println("Esperando Cliente"); //Informamos de que esperamos al cliente
            
            Socket cliente = socketServidor.accept(); //Aceptamos al cliente
            System.out.println("Aceptado cliente");
            //cliente.setSoLinger(true, 10);
            
            ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
            Object mensaje = flujoEntrada.readObject();
            
            if (mensaje instanceof MensajeDameFichero) {
                System.out.println("El servidor muestra el fichero solicitado por el cliente y lo envia al cliente");

                System.out.println("Me piden: " + ((MensajeDameFichero) mensaje).nombreFichero);
                enviaFichero(((MensajeDameFichero) mensaje).nombreFichero, new ObjectOutputStream(cliente.getOutputStream()));
            } else {
                System.err.println("Mensaje no esperado" + mensaje.getClass().getName());
            }
            
            cliente.close();
            socketServidor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviaFichero(String fichero, ObjectOutputStream oos) {
        try {
            boolean enviadoUltimo = false;
            FileInputStream fis = new FileInputStream(fichero);
            MensajeTomaFichero mensaje = new MensajeTomaFichero();
            mensaje.nombreFichero = fichero;
            int leidos = fis.read(mensaje.contenidoFichero);
            while (leidos > -1) {
                mensaje.bytesValidos = leidos;
                if (leidos < MensajeTomaFichero.LONGITUD_MAXIMA) {
                    mensaje.ultimoMensaje = true;
                    enviadoUltimo = true;
                } else {
                    mensaje.ultimoMensaje = false;
                }
                oos.writeObject(mensaje);

                if (mensaje.ultimoMensaje) {
                    break;
                }
                mensaje = new MensajeTomaFichero();
                mensaje.nombreFichero = fichero;
                leidos = fis.read(mensaje.contenidoFichero);
            }
            if (enviadoUltimo == false) {
                mensaje.ultimoMensaje = true;
                mensaje.bytesValidos = 0;
                oos.writeObject(mensaje);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
