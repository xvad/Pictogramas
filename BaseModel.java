package ucn.disc.cl.pictogramas;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Entidad base
 *
 * @author Victor Araya Delgado
 */

public abstract class BaseModel extends com.raizlabs.android.dbflow.structure.BaseModel {

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

    }

}
