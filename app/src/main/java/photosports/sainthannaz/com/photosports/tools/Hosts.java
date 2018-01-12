package photosports.sainthannaz.com.photosports.tools;

/**
 * Created by Gabriel on 03/01/2018.
 */

public class Hosts {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;
    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "";
    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "laboratorioslcc.com";
    //private static final String IP =  "192.168.10.227";
    /**
     * URLs del Web Service
     */
    public static final String GET = "http://" + IP + PUERTO_HOST + "/photosport/obtener_metas.php";
    public static final String INSERT = "http://" + IP + PUERTO_HOST + "/photosport/insertar_metas.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}
