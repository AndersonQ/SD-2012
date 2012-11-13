package client;

import java.rmi.RemoteException;
import server.InterfaceAcesso;
import controler.InterfaceControlador;
import controler.NenhumServidorDisponivelException;
import controler.ObjetoNaoEncontradoException;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */

public class Client implements InterfaceAcesso, InterfaceControlador
{

    public void armazena(String nome, Object obj) throws RemoteException,
    NenhumServidorDisponivelException
    {
        // TODO Auto-generated method stub
    }

    public String procura(String nome) throws RemoteException,
    NenhumServidorDisponivelException, ObjetoNaoEncontradoException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] lista() throws RemoteException,
    NenhumServidorDisponivelException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void apaga(String nome) throws RemoteException,
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
