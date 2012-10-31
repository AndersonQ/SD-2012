package server;
import java.rmi.*;

import controler.ObjetoNaoEncontradoException;

public interface InterfaceAcesso extends Remote
{
  Object recupera(String link)
     throws RemoteException, ObjetoNaoEncontradoException;
}