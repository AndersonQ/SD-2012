package sd.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Set;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceReplicacao;
import sd.types.Box;

/**
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */
@SuppressWarnings("serial")
public class Server extends UnicastRemoteObject implements InterfaceAcesso, InterfaceReplicacao
{
	/** Storage to objects */
    private Hashtable<Integer, Box> h;
    /** The server address */
    private String address;
    /** The server ID */
    private int ID;

    public Server() throws RemoteException
    {
        h = new Hashtable<Integer, Box>();

        //Default address
        this.address = "localhost";
    }
    
    public Server(String address, int id) throws RemoteException
    {
        h = new Hashtable<Integer, Box>();
    	
    	this.address = address;
    	this.ID = id;
    }

    //===================Begin interface Acesso===================
    
    @Override
    public Box recupera(Integer id) throws RemoteException,
    ObjetoNaoEncontradoException
    {
    	Box box = h.get(id);
    	
    	//If there is a object in box, return it, other wise throws a exception
        if(box != null)
        {
            return box;
        }
        else //throws a exception
        {
            throw new ObjetoNaoEncontradoException(id.toString());
        }
    }

    //===================End interface Acesso=====================
 
    //===================Begin interface Replicacao===================
    
    @Override
    public void replica(Integer id, Box obj) throws RemoteException, NenhumServidorDisponivelException
    {
        h.put(id, obj);

        try
        {
            System.out.printf("Received object %s from %s\n", obj.toString(), RemoteServer.getClientHost());
        }
        catch (ServerNotActiveException e)
        {
            e.printStackTrace();
        }
    }
    
	@Override
	public boolean replicaAll(Hashtable<String, Integer> objects, String server) throws RemoteException
	{
		//Interface to access other server to get all objects
		InterfaceAcesso ia = null;
		//Was replicaAll a success?
		boolean success = true; 
		
		//Looking for the server
		try
		{
			ia = (InterfaceAcesso) Naming.lookup(server);
		}
		catch(MalformedURLException e)
		{
			System.err.println("Server.replicaAll catch MalformedURLException");
			e.printStackTrace();
			success = false;
		}
		catch(NotBoundException e)
		{
			System.err.println("Server.replicaAll catch NotBoundException");
			e.printStackTrace();
			success = false;
		}
		
		Set<String> objs = objects.keySet();

		System.out.println("Coping objects from " + ia);
		//Get all objects
		for(String s: objs)
		{
			Integer id = objects.get(s);
			try
			{
				Box obj = ia.recupera(id);
				h.put(id, obj);
			} 
			catch (ObjetoNaoEncontradoException e)
			{
				System.err.println("Server.replicaAll catch ObjetoNaoEncontradoException to ID " + id.toString());
				e.printStackTrace();
				success = false;
			}
		}
		
		System.out.println("All objects copied from " + ia);
		
		return success;
	}

    @Override
    public void intReplicacaoApaga(Integer id) throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException
    {
        try
        {
            System.out.printf("Client %s asked to delete %s\n", RemoteServer.getClientHost(), id);
        }
        catch (ServerNotActiveException e)
        {
            e.printStackTrace();
        }
        if(h.get(id) == null)
        {
            throw new ObjetoNaoEncontradoException(id.toString());
        }
        else
        {
            h.remove(id);
            System.out.printf("Object %s deleted!\n", id);
        }
    }
    
	@Override
	public String getAddress() throws RemoteException
	{	
		return this.address;
	}

	@Override
	public int getId() throws RemoteException
	{	
		return this.ID;
	}

	//===================End interface Replicacao=====================
}
