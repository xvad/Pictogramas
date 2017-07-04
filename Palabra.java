package ucn.disc.cl.pictogramas;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by intermex on 9/05/2017.
 */
@Table(
        database = Database.class,
        cachingEnabled = false,
        orderedCursorLookUp = true, // https://github.com/Raizlabs/DBFlow/blob/develop/usage2/Retrieval.md#faster-retrieval
        cacheSize = Database.CACHE_SIZE
)
public class Palabra extends BaseModel {
    @Column
    private String nombre;

    @Column
    private String imagen;

    @Column
    @PrimaryKey(autoincrement = true)
    private  long id;

    @Column
    long oracionID ;

    public long getOracionID() {
        return oracionID;
    }

    public void setOracionID(long oracionID) {
        this.oracionID = oracionID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
