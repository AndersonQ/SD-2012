package sd.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;

public interface InterfaceReplicacao extends Remote
{
  void replica(int id, Object obj) 
     throws RemoteException, NenhumServidorDisponivelException;
  void apaga(int id) 
     throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
  
}