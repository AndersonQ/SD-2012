package sd.types;

import java.io.Serializable;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

@SuppressWarnings("serial")
public class Box implements Serializable
{
	/** The stored object */
    Object obj;

    /**
     * Creates a box
     * @param obj the object to be stored
     */
    public Box(Object obj)
    {
        this.obj = obj;
    }

    /**
     * Gets the object stored
     * @return the object stored
     */
    public Object getObj()
    {
        return obj;
    }

    /**
     * Set the object stored
     * @param obj a new object to be stored
     */
    public void setObj(Object obj)
    {
        this.obj = obj;
    }

}
