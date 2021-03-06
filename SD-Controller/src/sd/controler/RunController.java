package sd.controler;

import java.rmi.Naming;

import sd.interfaces.InterfaceControlador;

/**
 * 
 * @author Anderson de França Queiroz <contato (at) andersonq (dot) eti (dot) br>
 * @author André Xavier Martinez
 * @author Tiago de França Queiroz <contato (at) tiago (dot) eti (dot) br>
 *
 */

public class RunController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        InterfaceControlador ic;
        
        System.out.println("Controller object created");
        try
        {
        	ic = new Controller();
            System.out.println("Binding controller " + ic);
            Naming.rebind("rmi://localhost/Controller", ic);
            System.out.println("Controller Binded!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Controller Running!\n\n");
    }
}
