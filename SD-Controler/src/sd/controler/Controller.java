package sd.controler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.interfaces.InterfaceControlador;
import sd.interfaces.InterfaceReplicacao;
import sd.types.Box;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */

@SuppressWarnings("serial")
public class Controller extends UnicastRemoteObject implements InterfaceControlador
{
	/** The servers */
	private Vector<InterfaceReplicacao> servers;
	/** A 'list' of all stored objects */
	private Hashtable<String, Integer> objects;
	/** The next server to be used */
	int nextserver;
	/** The last object ID */
	private static int ID;

    protected Controller() throws RemoteException
    {
    	//Calls super class
        super();
        
        //Initialising variables
        this.servers = new Vector<InterfaceReplicacao>();
        this.objects = new Hashtable<String, Integer>();
        this.nextserver = 0;
        Controller.ID = 0;
    }

    //===================Begin interface Controller===================
	@Override
	/**
	 * Stores a object in all servers
	 * @param nome The name of the object
	 * @param obj the object to be stored
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 */
	public void armazena(String nome, Box obj) throws RemoteException,
			NenhumServidorDisponivelException 
	{
		Integer id = new Integer(ID++);
		//Put the object in the list
		objects.put(nome, new Integer(id));
		
		//Store object in all servers
		//TODO store the object in the servers
		/*for(InterfaceReplicacao ir: servers)
		{
			ir.replica(id, new Box(obj));
			//DEBUG
			System.out.println("Stored " + obj + ", id " + id + " in server " + ir);
		}*/
	}

	@Override
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
	public String procura(String nome) throws RemoteException,
			NenhumServidorDisponivelException, ObjetoNaoEncontradoException 
	{
		//Looking for the object
		Integer id = objects.get(nome);
		InterfaceReplicacao s = servers.get(id);
		
		//If there is not a object named nome, it throws exception
		if(id == null)
		{
			throw new ObjetoNaoEncontradoException(nome);
		}
		
		return String.format("%d@rmi://%s/%s", id, nextServer(), nome);
	}

	@Override
	/**
	 * List all stored objects
	 * TODO define syntax to string
	 * @return a string with all stored objects
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 */
	public ArrayList<String> lista() throws RemoteException,
			NenhumServidorDisponivelException
	{
		ArrayList<String> list = new ArrayList<String>();
		
		Set<String> objs = objects.keySet();
		
		//Add all object names into 'list'
		for(String s: objs)
		{
			list.add(s);
		}

		return list;
	}

	@Override
	/**
	 * Delete an object
	 * @param nome the name of the object to be deleted
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 * @throws ObjetoNaoEncontradoException Object not found
	 */
	public void apaga(String nome) throws RemoteException,
			NenhumServidorDisponivelException, ObjetoNaoEncontradoException
	{
		//Delete object to the object list
		Integer id = objects.remove(nome);
		
		//If there is not a object named nome, it throws exception
		if(id == null)
		{
			throw new ObjetoNaoEncontradoException(nome);
		}
		
		//Delete the object from each server
		for(InterfaceReplicacao s: servers)
		{
			s.apaga(id);
		}
	}
	//===================End interface Controller=====================
	
	/**
	 * Gets the server to be informed to the client
	 * @return
	 */
	private String nextServer()
	{
		return servers.get(nextserver % servers.size()).getName();
	}
}
