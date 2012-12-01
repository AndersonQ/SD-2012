package sd.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceControllerServer extends Remote {

	/**
	 * Ask to controller a ID to compose the service name, like it: serviceID
	 * @return the ID of this server or -1 if its fail
	 */
	int beforeBind()
			 throws RemoteException;
	
	/**
	 * Send to controller the server address and server id 
	 * @param addres the server address
	 * @param id the server id
	 * @return true if the controller added the server or false other wise
	 * @throws RemoteException
	 */
	boolean registryServer(String addres, int id)
			throws RemoteException;
}
