package sd.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

public interface InterfaceAcesso extends Remote
{
	/**
	 * Gets an object from a server
	 * @param id Identifier of the object
	 * @return the stored object
	 * @throws RemoteException
	 * @throws ObjetoNaoEncontradoException
	 */
    Box recupera(Integer id)
            throws RemoteException, ObjetoNaoEncontradoException;
}
