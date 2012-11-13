package server;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.TreeSet;

import controler.NenhumServidorDisponivelException;
import controler.ObjetoNaoEncontradoException;

/**
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */
public class Server implements InterfaceAcesso, InterfaceReplicacao
{
    Hashtable<Integer, Object> h;
    TreeSet<Integer> bst;

    public Server()
    {
        h = new Hashtable<Integer, Object>();
        bst = new TreeSet<Integer>();
    }

    public void replica(int id, Object obj) throws RemoteException,
    NenhumServidorDisponivelException
    {
        Integer i = new Integer(id);

        h.put(i, obj);
        bst.add(i);
    }

    public void apaga(int id) throws RemoteException,
    NenhumServidorDisponivelException, ObjetoNaoEncontradoException
    {
        Integer i = new Integer(id);

        if(! bst.contains(i))
            throw new ObjetoNaoEncontradoException(i.toString());
        else
        {
            bst.remove(i);
            h.remove(i);
        }
    }

    public Object recupera(String link) throws RemoteException,
    ObjetoNaoEncontradoException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
