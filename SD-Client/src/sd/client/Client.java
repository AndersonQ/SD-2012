package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.xml.internal.ws.api.pipe.NextAction;

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
    	/*Begin variable declaration*/
        InterfaceAcesso ia_cliente = null;			/*This variable will later bind to the server in the case 2 below*/
        InterfaceControlador ic_cliente= null;		/*This one will bind to the Controller, most of the functions comes from it*/
        ArrayList<String> ALS = null;				/*This array will receive a copy of the list of objects on the Controller*/
        String busca="", nome="", conteudo="";		/*Those strings are used through the code to get what the user types and some important information*/
        String[] busca_split;						/*This array is necessary so we can split the tuple {object_id, server, service}*/
        Scanner sc = new Scanner(System.in);		/*The simple and easy to use scanner function from java.util library*/
        int opt=1, ID=0, ADDR=1;					/*opt is used to get the options through the interface, ID and ADDR are actually constants, so access the busca_split String, if necessary later I may declare it as final const */
        Box obj = null;								/*Generic object used on some functions*/
        /*End of variable declaration*/
        
        
        /* Connect to the Controller */
        try
        {
            ic_cliente = (InterfaceControlador) Naming.lookup("rmi://localhost/Controller"); /*Try to bind the variable with the Controller object*/
        }
        catch (MalformedURLException e)
        {   System.out.println("MalformedURLException");
            e.printStackTrace();}
        catch (RemoteException e)
        {   System.out.println("RemoteException");
            e.printStackTrace();}
        catch (NotBoundException e)
        {   System.out.println("NotBoundException");
            e.printStackTrace();}
        catch (Exception e)													/*A Lot of catches so you know what's wrong*/
        {   System.out.println("Other Exception");
            e.printStackTrace();}
        try{
        System.out.println(ic_cliente.Connection_cliente_OK()+"\n");		/*This function is a Debug Method to make sure everything is running smootly*/
        }
        catch (RemoteException e)
        {   System.out.println("RemoteException");
            e.printStackTrace();}	
        /*At this point, the controller and the Client are connected, we might as well starting to making queries and inputs*/
        while (opt > 0)	{	//Loop infinito enquanto o programa atende o usuário.
        	
        	/*Dialogo com o usuário*/
        	System.out.println("O que deseja fazer?\n" +
        			"1 - Criar e armazenar objeto\n" +
        			"2 - Recuperar e Editar um objeto\n" +
        			"3 - Apagar um objeto\n" +
        			"4 - Listar objetos disponíveis");
        			opt=sc.nextInt();
        	/*After this, the real thing starts to happen*/
        	switch (opt) {
        	/*Armazenamento de objetos*/
			case 1:
	        	System.out.println("Digite o nome do objeto:");
	        	nome=sc.next();
	        	System.out.println("Digite o seu conteúdo:");
	        	conteudo=sc.next();
	        	System.out.println("Armazenando objeto...");
	        	try{
	        	ic_cliente.armazena(nome, new Box((Object) conteudo));
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
	            System.out.println("Objeto armazenado com sucesso\n");
				break;
				/*Fim de armazenamento de objetos*/
				/*Recuperar e Editar um objeto*/
				/**
				 * TODO This is not complete yet, we should be able to edit and re-insert the object
				 */
			case 2:
				System.out.println("Digite o nome do objeto que deseja recuperar:");
	        	nome=sc.next();
	        	try {
					busca=ic_cliente.procura(nome);
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (NenhumServidorDisponivelException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ObjetoNaoEncontradoException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				busca_split=busca.split("@");
				System.out.println("Objeto encontrado com ID: "+busca_split[ID]);
				System.out.println("No Endereço: "+busca_split[ADDR]);
				System.out.println("Conectando ao servidor...");
				try {
					ia_cliente= (InterfaceAcesso) Naming.lookup(busca_split[ADDR]);
				} catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (NotBoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("Conectado!");
				try {
					obj=ia_cliente.recupera(Integer.parseInt(busca_split[ID]));
				} catch (NumberFormatException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ObjetoNaoEncontradoException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println(obj.toString());
				break;
				/*Final de recuperar e Editar um objeto*/
				/*Apagar um objeto*/
				/**
				 * TODO I've been thinking on making part of this a fully implemented function, at some point we should call something like "RemoveObject(String nome)
				 * the reason is that the case 2 could use it on editting an Object
				 */
			case 3:
				System.out.println("\nDigite o nome do objeto a ser apagado");
				nome=sc.next();
				try {
					System.out.println("\nProcurando...\n");
					ic_cliente.intControladorApaga(nome);
					System.out.println("Objeto removido com sucesso\n");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NenhumServidorDisponivelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjetoNaoEncontradoException e) {
					// TODO Auto-generated catch block
					System.out.println("Objeto Não encontrado\n");
				}
				
				break;
				/*Final de apagar um objeto*/
				/*Listar objetos disponíveis*/
			case 4:
				try {
					ALS = ic_cliente.lista();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NenhumServidorDisponivelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	System.out.println("\nListando nomes dos objetos armazenados");
	        	for (String nome_objetos: ALS)
	        		System.out.println(nome_objetos);
	        		System.out.println();
				break;
				/*FIM Listar objetos disponíveis*/
			default:
				opt=0;
				break;
			}
    }}
}
