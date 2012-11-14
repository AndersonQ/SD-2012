package sd.interfaces;

import java.rmi.*;
import java.util.ArrayList;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.types.Box;

public interface InterfaceControlador extends Remote
{
    void armazena(String nome, Box obj)
            throws RemoteException, NenhumServidorDisponivelException;
    String procura(String nome)
            throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
    ArrayList<String> lista()
            throws RemoteException, NenhumServidorDisponivelException;
    void apaga(String nome)
            throws RemoteException, NenhumServidorDisponivelException, ObjetoNaoEncontradoException;
}
