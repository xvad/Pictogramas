package ucn.disc.cl.pictogramas;


/**
 * Created by Victor on 07-Mayo-2017.
 */
@com.raizlabs.android.dbflow.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database {

    /**
     * Key de la base de datos
     */
    public static final String NAME = "Database";

    /**
     * Version de la BD
     */
    public static final int VERSION = 2;

    /**
     * Tamanio del cache
     */
    public static final int CACHE_SIZE = 100;
}