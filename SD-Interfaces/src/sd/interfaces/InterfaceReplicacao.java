package sd.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

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
	 * 
	 * Copy all objects from a server
	 * @param objects a hashtable with object's name and ID
	 * @param server a RMI string to server to get the objects
	 * @return true if all objects were copied, or false other wise
	 * @throws RemoteException
	 */
	boolean replicaAll(Hashtable<String, Integer> objects, String server) throws RemoteException;
	
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
	 * Get the address of this server
	 * @return address of this server
	 * @throws RemoteException
	 */
	String getAddress() throws RemoteException;
	
	/**
	 * Get the ID of this server
	 * @return the ID of this server
	 * @throws RemoteException
	 */
	int getId() throws RemoteException;
	
	/**
	 * Checks if the server is alive
	 * @return true if the server is alive
	 * @throws RemoteException
	 */
	boolean areYouAlive() throws RemoteException;
}
