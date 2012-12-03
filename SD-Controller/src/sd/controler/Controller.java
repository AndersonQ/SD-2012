package sd.controler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoJaExisteException;
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
	/** Keep the pair service-server_position_in_vector_server.
	 *  It will be used to remove inactive servers
	 */
	private Hashtable<String, Integer> services;
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
        this.services = new Hashtable<String, Integer>();
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
			NenhumServidorDisponivelException, ObjetoJaExisteException
	{
		Integer id;
		id = objects.get(nome);
		if(id != null)
		    throw new ObjetoJaExisteException("\"" + nome + "\""+ " already stored!");
		id = new Integer(ID++);
		//Put the object in the list
		objects.put(nome, new Integer(id));

		//Store object in all servers
		//TODO TEST IT!!!!
		for(InterfaceReplicacao ir: servers)
		{
		    boolean alive = false;
		    alive = ir.areYouAlive();
		    if(!alive)
		        removeDeadServer(ir);
			ir.replica(id, obj);
			//DEBUG
			System.out.println("Stored object " + obj + ", id " + id + " in server " + ir.getId());
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
			throw new ObjetoNaoEncontradoException(nome);

		//Delete the object from each server
		for(InterfaceReplicacao s: servers)
		{
			s.intReplicacaoApaga(id);
		}
	}
	
	public void reportFail(String service) throws RemoteException
	{
		int pos = services.get(service);

		try
		{
			System.out.println("Client " + RemoteServer.getClientHost() + " reported down of service " + 
								service + " removing it from servers list");
		}
		catch (ServerNotActiveException e)
		{
			System.err.println("Controller.reportFail catched ServerNotActiveException");
			e.printStackTrace();
		}
		
		//Remove the inactive server from servers list
		servers.remove(pos);
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
		int nid = nextServerID();
		try
		{
			System.out.println("Server " + RemoteServer.getClientHost() + " asked a ID, sending " + nid);
		}
		catch (ServerNotActiveException e)
		{
			System.err.println("Controller.beforeBind() catch ServerNotActiveException");
			e.printStackTrace();
		}
		
		return nid;
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
		/* Was the server registered?
		 * If something fails it will be set to 'false'
		 */
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
		
		/*
		 * Get a server to provide all objects to the new server
		 * If there is not any server, this new server is the first
		 */
		InterfaceReplicacao nserver = null;
		try 
		{
			//Get a server
			nserver = nextServer();
			
			String server =  String.format("rmi://%s/%s", nserver.getAddress(), String.format("Acesso%d", nserver.getId()));
			
			//Command the new server copy all objects from another server
			registered = newserver.replicaAll(objects, server);
		} 
		catch (NenhumServidorDisponivelException e) 
		{
			System.out.println("First server connecting...");
		}

		//Putting the new server with all servers
		registered = servers.add(newserver);
		
		//Keeping the pair service-server_position_in_vector_server. it will be used to remove inactive servers
		services.put(String.format("Acesso%d", id), servers.indexOf(newserver));
		
		System.out.println("New server connected addres: " + 
							address + ", id: " + id);
		return registered;
	}
	
	//===================End interface ControllerServer=====================

	/**
	 * Gets the server to be informed to the client
	 * @return
	 * @throws RemoteException 
	 * @throws NenhumServidorDisponivelException 
	 */
	private InterfaceReplicacao nextServer() throws RemoteException, NenhumServidorDisponivelException
	{
	    boolean alive = false;
	    ArrayList<Integer> To_remove = new ArrayList<Integer>();
		if(servers.size() == 0)
		{
			throw new NenhumServidorDisponivelException();
		}
		/**
		 * TODO Just to add a note, things went wrong after this "for", when using Procura. 
		 * Changed some things so it can be used for both registry and procura. But still needs reviews.
		 * 
		 * Let me explain what I tested. If there were 2 servers, with ID 0 and 1, and the server 0 is down it will 
		 * catch an exception at i==0 and nextserver==0 (on a first time run, for example), and it will do "nextserver++". 
		 * On the second step of the "for" it will simply skip the "for" as i==1 and servers.size()==1 now, 
		 * thus not checking the other server. 
		 * There are other problems, even if the "for" was not skiped, on the next "try" it would do the line as 
		 * "servers.get(1+1%servers.size)", it seens that it would skip a server on the list, if the list were bigger.
		 * 
		 * 
		 * This comment makes more sense if viewed in gitg than in eclipse, since you can see both versions of the code there, 
		 * not only the most recent version
		 */
		for(int i = 0; i < servers.size(); i++)
		{
		    try
		    {
		    	/*This part seens ok, it will eventually check every server on the list so, no change */
		        alive = servers.get((nextserver + i) % servers.size()).areYouAlive();
		        /*If you found an alive server, get it's index and break the search*/
		        if (alive){
		        nextserver=(nextserver + i) % servers.size();
		        break;}
		        
		    }
		    catch (RemoteException e)
		    {
		    	/*Add the index of the server that is down for later removal*/
		    	To_remove.add((Integer)(nextserver + i) % servers.size());
		    }
		}
		/**
		 * TODO I believe this part needs some refine, as there is a very specific case it won't work, 
		 * if 2 servers are down, the last and the first for example, it may remove a server with a 
		 * low index first and not find the other server at all.
		 * Also, is there anything else that use server indexes? They may be compromised after the removal. 
		 */
		/*If there is someone to remove, try to safely remove servers with highest index first, so it won't screw indexes*/
		if(To_remove.size()>0)
			for(int i=(To_remove.size()-1);i>-1;i--)
				removeDeadServer(To_remove.get(i).intValue()); 
		/*If no one is alive, we got some serious problem*/
		if(!alive)
		    throw new NenhumServidorDisponivelException();
		/*This makes sure that on next search it will start at the next server*/
		nextserver++;
		/*Finally, it returns the server it found*/
		return servers.get(nextserver-1);
	}
	
	/**
	 * Returns the next server ID
	 * @return the next server ID
	 */
	private int nextServerID()
	{
		//TODO verify if 'return Controller.serverID' works
		int id = Controller.serverID;
		Controller.serverID = Controller.serverID + 1;

		return id;
	}

	private void removeDeadServer(int server)
	{
		
	    services.remove(servers.get(server));
	    servers.remove(server);
	    System.err.printf("Server %d removed!\n", nextserver% servers.size());
	}

	private void removeDeadServer(InterfaceReplicacao server)
	{
	    services.remove(server);
	    servers.remove(server);
	    System.err.printf("Server %d removed!\n", nextserver% servers.size());
	}
}
