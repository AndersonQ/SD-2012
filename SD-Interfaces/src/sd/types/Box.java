package sd.types;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Box implements Serializable
{
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
