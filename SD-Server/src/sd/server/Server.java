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
    /** To name servers */
    private static int nextname = 0;
    /** The server name */
    private String name;

    public Server() throws RemoteException
    {
        h = new Hashtable<Integer, Box>();
        bst = new TreeSet<Integer>();
        //TODO make it works
        //this.name = new String(String.format("%d", nextname++));
        this.name = "localhost";
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

    public void apaga(Integer id) throws RemoteException,
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

    public Box recupera(String link) throws RemoteException,
    ObjetoNaoEncontradoException
    {
        String id;
        Integer i;

        /*
         * It splits the string link and get the part before "#"
         * in other words, it gets the object ID.
         */
        id = link.split("#")[0];

        i = new Integer(id);

        if(bst.contains(i))
            return (Box) h.get(i);
        else
            throw new ObjetoNaoEncontradoException(id);
    }

	@Override
	public String getName() {
		return name;
	}
}
