package sd.exceptions;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

@SuppressWarnings("serial")
public class ObjetoJaExisteException extends Exception
{

    /**
     * Creates an exception with the message
     * passed as argument
     * @param message
     */
    public ObjetoJaExisteException(String message)
    {
        super(message);
    }

}
