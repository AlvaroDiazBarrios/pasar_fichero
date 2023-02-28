package pasarFichero;

import java.io.Serializable;

/**
 *
 * @author gandalfvaro
 */
public class MensajeTomaFichero implements Serializable {
    public String nombreFichero;
    public boolean ultimoMensaje = true;
    public int bytesValidos = 0;
    public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    public final static int LONGITUD_MAXIMA = 1024;
}
