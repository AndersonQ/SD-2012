package sd.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.ObjetoNaoEncontradoException;

public interface InterfaceAcesso extends Remote
{
  Object recupera(String link)
     throws RemoteException, ObjetoNaoEncontradoException;
}