package sd.interfaces;

import java.rmi.*;
import java.util.ArrayList;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

public interface InterfaceControlador extends Remote
{
	/**
	 * Stores a object in all servers
	 * @param nome The name of the object
	 * @param obj the object to be stored
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 */
	void armazena(String nome, Box obj)
			throws RemoteException, NenhumServidorDisponivelException;
	
	/**
	 * Look for a object
	 * @param nome name of the object
	 * @return A string with the tuple {object_id, server, service}, the syntax is:
	 *  ID@rmi://SERVER/SERVICE, example:
	 *  13@rmi://computer1/ServerMirror
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 * @throws ObjetoNaoEncontradoException Object not found
	 */
	String procura(String nome)
			throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
	
	/**
	 * List all stored objects
	 * TODO define syntax to string
	 * @return a string with all stored objects
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 */
	ArrayList<String> lista()
			throws RemoteException, NenhumServidorDisponivelException;
	
	/**
	 * Delete an object
	 * @param nome the name of the object to be deleted
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 * @throws ObjetoNaoEncontradoException Object not found
	 */
	void intControladorApaga(String nome)
			throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
}
