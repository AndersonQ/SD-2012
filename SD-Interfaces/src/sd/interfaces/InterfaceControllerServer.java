package sd.interfaces;

import java.rmi.Remote;

public interface InterfaceControllerServer extends Remote {

	/**
	 * Ask to connect to controller sending ServerName 
	 * to build this tuple {object_id, server, service} with this syntax: 
	 * ID@rmi://SERVER_NAME/SERVICE_NAME
	 * the service name is SEVERID, and ID will be returned
	 * @param name the name of the server 
	 * @param service name of the service
	 * @return the ID of this server or -1 if its fail
	 */
	int beforeConect(String name);
}
