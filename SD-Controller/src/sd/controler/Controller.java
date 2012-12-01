package sd.controler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.interfaces.InterfaceControlador;
import sd.interfaces.InterfaceControllerServer;
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
public class Controller extends UnicastRemoteObject 
						implements InterfaceControlador, InterfaceControllerServer
{
	/** The servers */
	private Vector<InterfaceReplicacao> servers;
	/** A 'list' of all stored objects */
	private Hashtable<String, Integer> objects;
	/** The next server to be used by clients */
	int nextserver;
	/** The next object ID */
	private static int ID;
	/** The next server ID */
	private static int serverID;

    protected Controller() throws RemoteException
    {
    	//Calls super class
        super();

        //Initialising variables
        this.servers = new Vector<InterfaceReplicacao>();
        this.objects = new Hashtable<String, Integer>();
        this.nextserver = 0;
        Controller.ID = 0;
        Controller.serverID = 0;
    }

    //===================Begin interface Controller===================

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
		//TODO see if this object already exist
		Integer id = new Integer(ID++);
		//Put the object in the list
		objects.put(nome, new Integer(id));

		//Store object in all servers
		//TODO TEST IT!!!!
		for(InterfaceReplicacao ir: servers)
		{
			ir.replica(id, obj);
			//DEBUG
			System.out.println("Stored " + obj + ", id " + id + " in server " + ir);
		}
	}

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
		//InterfaceReplicacao s = servers.get(id);

		//If there is not a object named nome, it throws exception
		if(id == null)
		{
			throw new ObjetoNaoEncontradoException(nome);
		}
		
		InterfaceReplicacao nserver = nextServer();
		return String.format("%d@rmi://%s/%s", id.intValue(), nserver.getAddress(), String.format("Acesso%d", nserver.getId()));
	}

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
	
	/**
	 * Debug for client-controller connection
	 * @return uma string com a mesma mensagem que foi impressa no controlador
	 * @throws RemoteException
	 */
	public String Connection_cliente_OK() throws RemoteException
	{
		String s ="Conexão Cliente-Controlador OK";
		System.out.println(s);
		return s;
	}
	

	/**
	 * Delete an object
	 * @param nome the name of the object to be deleted
	 * @throws RemoteException
	 * @throws NenhumServidorDisponivelException There are NOT any available server
	 * @throws ObjetoNaoEncontradoException Object not found
	 */
	public void intControladorApaga(String nome) throws RemoteException,
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
			s.intReplicacaoApaga(id);
		}
	}
	//===================End interface Controller=====================


	//===================Begin interface ControllerServer===================

	/**
	 * Ask to controller a ID to compose the service name, like it: serviceID
	 * @return the ID of this server or -1 if its fail
	 */
	public int beforeBind() throws RemoteException
	{
		/*
		 * The ID to this server, it will compose the serviceName, like this:
		 * ServiceID
		 */
		return nextServerID();
	}

	/**
	 * Send to controller the server address and service name
	 * @param addres the server address
	 * @param serviceAcesso name registered to service Acesso
	 * @param serviceReplicacao name registered to service Replicacao
	 * @return true if the controller added the server or false other wise
	 * @throws RemoteException
	 */
	public boolean registryServer(String address, int id) throws RemoteException
	{
		//Was the server registered?
		boolean registered = true;
		//New server
		InterfaceReplicacao newserver = null;
		
		//Looking for the new server
		try
		{
			newserver = (InterfaceReplicacao) Naming.lookup(String.format("rmi://%s/Replica%d", address, id));
		}
		catch (MalformedURLException e)
		{
			registered = false;
			System.err.println("MalformedURLException");
			e.printStackTrace();
		}
		catch(NotBoundException e)
		{
			registered = false;
			System.err.println("NotBoundException");
			e.printStackTrace();
		}
		
		//Putting the new server with all servers
		registered = servers.add(newserver);
		
		System.out.println("New server connected addres: " + 
							address + ", id: " + id);
		return registered;
	}
	//===================End interface ControllerServer=====================

	/**
	 * Gets the server to be informed to the client
	 * @return
	 * @throws RemoteException 
	 */
	private InterfaceReplicacao nextServer() throws RemoteException
	{
		return servers.get(nextserver % servers.size());
	}
	
	/**
	 * 
	 * @return
	 */
	private int nextServerID()
	{
		//TODO verify if 'return Controller.serverID' works
		int id = Controller.serverID;
		Controller.serverID = Controller.serverID + 1;

		return id;
	}
}
