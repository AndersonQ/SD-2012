package sd.types;

import java.io.Serializable;

/**
 * A box to store any type of object
 * @author Anderson Queiroz <contato(at)andersonq(dot)eti(dot)br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 * 
 */
@SuppressWarnings("serial")
public class Box implements Serializable
{
	/** The stored object */
    Object obj;

    /**
     * @param obj
     */
    public Box(Object obj)
    {
        this.obj = obj;
    }

    public Object getObj()
    {
        return obj;
    }

    public void setObj(Object obj)
    {
        this.obj = obj;
    }

}
