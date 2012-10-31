package controler;

import java.rmi.*;

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