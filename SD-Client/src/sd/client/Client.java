package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceControlador;
import sd.interfaces.InterfaceControllerServer;
import sd.interfaces.InterfaceReplicacao;
import sd.types.Box;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */

public class Client
{
    public static void main(String[] trash)
    {
        InterfaceAcesso ia_cliente = null;
        InterfaceControlador ic_cliente= null;

        /* Connect to the Controller */
        try
        {
            ic_cliente = (InterfaceControlador) Naming.lookup("rmi://localhost/Controller");
        }
        catch (MalformedURLException e)
        {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            System.out.println("RemoteException");
            e.printStackTrace();
        }
        catch (NotBoundException e)
        {
            System.out.println("NotBoundException");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Other Exception");
            e.printStackTrace();
        }
        try{
        System.out.println(ic_cliente.Connection_cliente_OK());
        }
        catch (RemoteException e)
        {
            System.out.println("RemoteException");
            e.printStackTrace();
        }
        /*
         * Create objects, box them and
         * send to server store it.
         */
        try
        {
        	ic_cliente.armazena("Dez", new Box((Object) new String("Dez")));
        	ic_cliente.armazena("Vinte", new Box((Object) new String("Vinte")));
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NenhumServidorDisponivelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
