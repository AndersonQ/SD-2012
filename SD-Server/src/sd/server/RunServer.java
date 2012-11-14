package sd.server;

import java.rmi.Naming;

//import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceAcesso;
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
    public static void main(String[] args)
    {
        InterfaceAcesso ia;
        InterfaceReplicacao ir;
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
    }
}
