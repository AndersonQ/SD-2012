package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import sd.exceptions.NenhumServidorDisponivelException;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NotBoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Conected!");

        try
        {
            ir.replica(10, new Box((Object) new String("Dez")));
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
