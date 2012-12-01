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
    /** Search tree to look for a element */
    private TreeSet<Integer> bst;
    /** The server address */
    private String address;
    /** The server ID */
    private int ID;

    public Server() throws RemoteException
    {
        h = new Hashtable<Integer, Box>();
        bst = new TreeSet<Integer>();

        //Default address
        this.address = "localhost";
    }
    
    public Server(String address, int id) throws RemoteException
    {
        h = new Hashtable<Integer, Box>();
        bst = new TreeSet<Integer>();
    	
    	this.address = address;
    	this.ID = id;
    }

    public void replica(Integer id, Box obj) throws RemoteException,
    NenhumServidorDisponivelException
    {
        h.put(id, obj);
        bst.add(id);

        try
        {
            System.out.printf("Received %s from %s\n", obj.toString(), RemoteServer.getClientHost());
        }
        catch (ServerNotActiveException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void intReplicacaoApaga(Integer id) throws RemoteException,
    NenhumServidorDisponivelException, ObjetoNaoEncontradoException
    {
        try
        {
            System.out.printf("%s asked to delete %s\n", RemoteServer.getClientHost(), id);
        }
        catch (ServerNotActiveException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(! bst.contains(id))
            throw new ObjetoNaoEncontradoException(id.toString());
        else
        {
            bst.remove(id);
            h.remove(id);
            System.out.printf("%s deleted!\n", id);
        }
    }

    public Box recupera(Integer id) throws RemoteException,
    ObjetoNaoEncontradoException
    {
        if(bst.contains(id))
            return (Box) h.get(id);
        else
            throw new ObjetoNaoEncontradoException(id.toString());
    }

	@Override
	public String getAddress() throws RemoteException {
		
		return this.address;
	}

	@Override
	public int getId() throws RemoteException {
		
		return this.ID;
	}
}
