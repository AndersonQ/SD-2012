package sd.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

public interface InterfaceReplicacao extends Remote
{

	/** 
	 * Creates a replica of object in each on-line server 
	 * @param id identifier to object
	 * @param obj object to be replicated
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 */
	void replica(Integer id, Box obj)
			throws RemoteException, NenhumServidorDisponivelException;
	
	/**
	 * Delete an object from all servers
	 * @param id Identifier of the object to be deleted
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 * @throws ObjetoNaoEncontradoException Object not found
	 */
	void intReplicacaoApaga(Integer id)
			throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
	
	/**
	 * Get the name of this server
	 * @return the name of the server
	 */
	String getName() throws RemoteException;
}
