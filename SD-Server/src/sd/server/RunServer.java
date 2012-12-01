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
    	//The server ID
    	int id;
    	//The server Address, it should be received as a parameter
    	String serverAddress  = new String("localhost");
    	//The server to run!
    	Server server;
    	
    	//It servers this
        InterfaceAcesso ia;
        InterfaceReplicacao ir;
        
        //It uses this to communicate with the Controller
        InterfaceControllerServer ics = null;
        
        //Me!!!!
        Server s;
        
        s = new Server();
        ia = s;
        ir = s;
        
        //DEBUG
        System.out.println("Server object created");
        
        try
        {
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
        //Getting Controller from binder
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
        //DEBUG
        System.out.println("Server Conected to Controller!");
        //Geting an ID
        id = ics.beforeBind();
        System.out.println("Asking a ID to server: " + id);
        
        server = new Server(serverAddress, id);
        
        //Registering service
        try
        {
        	Naming.rebind(String.format("%s%d", "rmi://localhost/Acesso", id), ia);
        	Naming.rebind(String.format("%s%d", "rmi://localhost/Replica", id), ir);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        ics.registryServer(serverAddress, id);
    }
}
