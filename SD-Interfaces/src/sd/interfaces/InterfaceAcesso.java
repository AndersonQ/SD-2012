package sd.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

public interface InterfaceAcesso extends Remote
{
    Box recupera(Integer id)
            throws RemoteException, ObjetoNaoEncontradoException;
}
