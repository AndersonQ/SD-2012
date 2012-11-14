package sd.interfaces;

import java.rmi.*;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;

public interface InterfaceControlador extends Remote
{
  void armazena(String nome, Object obj) 
     throws RemoteException, NenhumServidorDisponivelException;
  String procura(String nome) 
     throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
  String[] lista() 
     throws RemoteException, NenhumServidorDisponivelException;
  void apaga(String nome) 
     throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
}