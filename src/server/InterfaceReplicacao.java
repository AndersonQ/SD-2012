package server;

import java.rmi.*;

import controler.NenhumServidorDisponivelException;
import controler.ObjetoNaoEncontradoException;

public interface InterfaceReplicacao extends Remote
{
  void replica(int id, Object obj) 
     throws RemoteException, NenhumServidorDisponivelException;
  void apaga(int id) 
     throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
  
}