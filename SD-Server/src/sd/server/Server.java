package sd.server;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.TreeSet;

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
