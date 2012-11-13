package server;

import java.rmi.RemoteException;

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
    public void replica(int id, Object obj) throws RemoteException,
    NenhumServidorDisponivelException
    {
        // TODO Auto-generated method stub
    }

    public void apaga(int id) throws RemoteException,
    NenhumServidorDisponivelException, ObjetoNaoEncontradoException
    {
        // TODO Auto-generated method stub
    }

    public Object recupera(String link) throws RemoteException,
    ObjetoNaoEncontradoException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
