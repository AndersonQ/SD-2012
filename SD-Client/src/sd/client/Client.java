package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
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
        InterfaceReplicacao ir = null;

        try
        {
            ir = (InterfaceReplicacao) Naming.lookup("rmi://localhost/Replica");
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
        System.out.println("Conected!");

        try
        {
            ir.replica(10, new Box((Object) new String("Dez")));
            ir.replica(20, new Box((Object) new String("Vinte")));
            ir.replica(30, new Box((Object) new String("Trinta")));

            ir.apaga(20);
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
        catch (ObjetoNaoEncontradoException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
