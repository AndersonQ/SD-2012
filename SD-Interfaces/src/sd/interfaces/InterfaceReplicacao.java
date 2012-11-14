package sd.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

public interface InterfaceReplicacao extends Remote
{
  void replica(Integer id, Box obj)
     throws RemoteException, NenhumServidorDisponivelException;
  void apaga(Integer id)
     throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
}
