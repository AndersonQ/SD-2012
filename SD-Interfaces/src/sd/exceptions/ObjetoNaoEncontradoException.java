package sd.exceptions;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

@SuppressWarnings("serial")
public class ObjetoNaoEncontradoException extends Exception {
  
	public ObjetoNaoEncontradoException(String objName)
	{
		super(objName);
	}
}
