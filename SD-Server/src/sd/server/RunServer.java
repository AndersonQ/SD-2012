package sd.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

//import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceControllerServer;
import sd.interfaces.InterfaceReplicacao;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br
 *
 */

public class RunServer
{
    public static void main(String[] args) throws RemoteException
    {
    	//It servers this
        InterfaceAcesso ia;
        InterfaceReplicacao ir;
        
        //It uses this
        InterfaceControllerServer ics = null;
        
        //Me!!!!
        Server s;

        System.out.println("Server object created");
        try
        {
            s = new Server();
            ia = s;
            ir = s;

            System.out.println("Binding " + s);
            Naming.rebind("rmi://localhost/Acesso", ia);
            Naming.rebind("rmi://localhost/Replica", ir);
            System.out.println("Binded!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Running!");
        
        System.out.println("Trying conect to Controller:");
        try
        {
        	ics = (InterfaceControllerServer) Naming.lookup("rmi://localhost/Controller");
        }
        catch (MalformedURLException e)
        {
        	System.err.println("MalformedURLException");
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
        	System.err.println("RemoteException");
            e.printStackTrace();
        }
        catch (NotBoundException e)
        {
        	System.err.println("NotBoundException");
            e.printStackTrace();
        }
        catch (Exception e)
        {
        	System.err.println("Other Exception");
            e.printStackTrace();
        }
        System.out.println("Server Conected to Controller!");
        int id = ics.beforeBind();
        System.out.println("Asking a ID to server: " + id);
        System.out.println("conect: " + ics.conect("localhost", String.format("service%d", id)));
    }
}
