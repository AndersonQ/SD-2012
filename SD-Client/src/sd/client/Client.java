package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;


import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoNaoEncontradoException;
import sd.interfaces.InterfaceAcesso;
import sd.interfaces.InterfaceControlador;
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
		
		/** This variable will later bind to a server in the case 2 below */
		InterfaceAcesso ia_cliente = null;
		/** This one will bind to the Controller, most of the functions comes from it */
        InterfaceControlador ic_cliente= null;
        /** This array will receive a copy of the list of objects on the Controller */
        ArrayList<String> ALS = null;
        /** Those strings are used through the code to get what the user types and keep some important information */
        String busca = null, nome = null, tmp = null;
        /** This array is necessary so we can split the tuple {object_id, server, service} */
        String[] busca_split;
        /** The simple and easy to use scanner function from java.util library */
        Scanner sc = new Scanner(System.in);
        /** opt and sub opt are used to get the options through the interface, 
         * ID and ADDR are actually constants, to access the busca_split String indexes, 
         * if necessary later I may declare it as final const */
        int opt=1, subopt=1, ID=0, ADDR=1;
        /** Generic object used on some functions */
        Box obj = null;
        
        /*End of variable declaration*/
        
        /* Connect to the Controller */
        try
        {
            ic_cliente = (InterfaceControlador) Naming.lookup("rmi://localhost/Controller"); /*Try to bind the variable with the Controller object*/
        }
        /*A Lot of catches so you know what's wrong, if something go wrong*/
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
        
        try
        {
        	/*This function is a Debug Method to make sure everything is running smoothly*/
        	System.out.println(ic_cliente.Connection_cliente_OK()+"\n");
        }
        catch (RemoteException e)
        {   
        	System.out.println("RemoteException");
        	e.printStackTrace();
        }	
        /*At this point, the controller and the Client are connected, we might as well starting to making queries and inputs*/
        
      //Loop infinito enquanto o programa atende o usuário.
        while (opt > 0)
        {

        	/*Dialogo com o usuário*/
        	System.out.println("O que deseja fazer?\n" +
        			"1 - Criar e armazenar objeto\n" +
        			"2 - Recuperar e Editar um objeto\n" +
        			"3 - Apagar um objeto\n" +
        			"4 - Listar objetos disponíveis\n" +
        			"0 - Finalizar o Cliente");
        	opt=sc.nextInt();
        	/*After this, the real thing starts to happen*/
        	switch (opt) 
        	{
	        	/*Armazenamento de objetos*/
	        	case 1:
	        		System.out.println("Digite o nome do objeto:");
	        		nome=sc.next();
	        		System.out.println("Armazenando objeto...");
	        		try
	        		{
	        			/*This sends the object to the Controller, so he makes replicas on the servers*/
	        			ic_cliente.armazena(nome, new Box((Object) nome));
	        		}
	        		catch (RemoteException e)
	        		{
	        			System.out.println("RemoteException");
	        			e.printStackTrace();
	        			break;
	        		}
	        		catch (NenhumServidorDisponivelException e)
	        		{
	        			System.out.println("NenhumServidorDisponivelException");
	        			e.printStackTrace();
	        			break;
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
	        		try
	        		{
	        			busca=ic_cliente.procura(nome);
	        		}
	        		catch (RemoteException e2)
	        		{
	        			System.out.println("RemoteException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (NenhumServidorDisponivelException e2)
	        		{
	        			System.out.println("NenhumServidorDisponivelException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (ObjetoNaoEncontradoException e2)
	        		{
	        			System.out.println("ObjetoNaoEncontradoException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		busca_split=busca.split("@");
	        		System.out.println("Objeto encontrado com ID: "+busca_split[ID]);
	        		System.out.println("No Endereço: "+busca_split[ADDR]);
	        		System.out.println("Conectando ao servidor...");
	        		try
	        		{
	        			ia_cliente= (InterfaceAcesso) Naming.lookup(busca_split[ADDR]);
	        		}
	        		catch (MalformedURLException e2)
	        		{
	        			System.out.println("MalformedURLException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (RemoteException e2)
	        		{
	        			System.out.println("RemoteException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (NotBoundException e2)
	        		{
	        			System.out.println("NotBoundException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		System.out.println("Conectado!");
	        		try
	        		{
	        			obj=ia_cliente.recupera(Integer.parseInt(busca_split[ID]));
	        		}
	        		catch (NumberFormatException e2)
	        		{
	        			System.out.println("NumberFormatException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (RemoteException e2)
	        		{
	        			System.out.println("RemoteException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		catch (ObjetoNaoEncontradoException e2)
	        		{
	        			System.out.println("ObjetoNaoEncontradoException");
	        			e2.printStackTrace();
	        			break;
	        		}
	        		System.out.println("Deseja alterar o objeto?\n1 - SIM\n2 - NAO");
	        		subopt=sc.nextInt();
	        		tmp=nome;
	        		if(subopt==1)
	        		{
	        			while(tmp.equals(nome))
	        			{
	        				System.out.println("Digite um novo nome para o objeto.");
	        				nome=sc.next();
	        				if(nome.equals(tmp))
	        				{
	        					System.out.println("Objeto com nome igual.");
	        				}
	        			}
	        			
	        			System.out.println("Deseja apagar o objeto original?\n1 - SIM\n2 - NAO");
	        			subopt=sc.nextInt();
	        			if(subopt==1)
	        			{
	        				System.out.println("Apagando...");
	        				try
	        				{
	        					ic_cliente.intControladorApaga(nome);
	        				}
	        				catch (RemoteException e)
	        				{
	        					System.out.println("RemoteException");
	        					e.printStackTrace();
	        					break;
	        				}
	        				catch (NenhumServidorDisponivelException e)
	        				{
	        					System.out.println("NenhumServidorDisponivelException");
	        					e.printStackTrace();
	        					break;
	        				}
	        				catch (ObjetoNaoEncontradoException e)
	        				{
	        					System.out.println("Objeto Não encontrado\n");
	        					break;
	        				}
	        				System.out.println("Objeto removido com sucesso\n");
	        			}
	        			
	        			System.out.println("Armazenando novo objeto...");
	        			try
	        			{
	        				/*This sends the new object to the Controller, so he makes replicas on the servers*/
	        				ic_cliente.armazena(nome, new Box((Object) nome));
	        			}
	        			catch (RemoteException e)
	        			{
	        				System.out.println("RemoteException");
	        				e.printStackTrace();
	        				break;
	        			}
	        			catch (NenhumServidorDisponivelException e)
	        			{
	        				System.out.println("NenhumServidorDisponivelException");
	        				e.printStackTrace();
	        				break;
	        			}
	        			System.out.println("Novo Objeto armazenado com sucesso\n");
	        		}
	        		break;
	        		/*Final de recuperar e Editar um objeto*/
	        		/*Apagar um objeto*/
	        		/**
	        		 * TODO I've been thinking on making part of this a fully implemented function, at some point we should call something like "RemoveObject(String nome)
	        		 * the reason is that the case 2 could use it on editting an Object
	        		 * UPDATE - Changed my mind, it's too small to be usefull, but I will keep it noted
	        		 */
	        	case 3:
	        		System.out.println("\nDigite o nome do objeto a ser apagado");
	        		nome=sc.next();
	        		try
	        		{
	        			System.out.println("\nProcurando...\n");
	        			ic_cliente.intControladorApaga(nome);
	        			System.out.println("Objeto removido com sucesso\n");
	        		}
	        		catch (RemoteException e)
	        		{
	        			System.out.println("RemoteException");
	        			e.printStackTrace();
	        			break;
	        		}
	        		catch (NenhumServidorDisponivelException e)
	        		{
	        			System.out.println("NenhumServidorDisponivelException");
	        			e.printStackTrace();
	        			break;
	        		}
	        		catch (ObjetoNaoEncontradoException e)
	        		{
	        			System.out.println("Objeto Não encontrado\n");
	        			break;
	        		}
	        		break;
	        		/*Final de apagar um objeto*/
	        		/*Listar objetos disponíveis*/
	        		/**
	        		 * This simply loads the object list from the Controller in a local 
	        		 */
	        	case 4:
	        		try
	        		{
	        			ALS = ic_cliente.lista();
	        		}
	        		catch (RemoteException e1)
	        		{
	        			System.out.println("RemoteException");
	        			e1.printStackTrace();
	        			break;
	        		}
	        		catch (NenhumServidorDisponivelException e1)
	        		{
	        			System.out.println("NenhumServidorDisponivelException");
	        			e1.printStackTrace();
	        			break;
	        		}
	        		System.out.println("\nListando nomes dos objetos armazenados");
	        		for (String nome_objetos: ALS)
	        		{
	        			System.out.println(nome_objetos);
	        		}
	        		System.out.println();
	        		break;
	        		/*FIM Listar objetos disponíveis*/
	        	default:
	        		opt=0;
	        		break;
	        }
        }
    }
}
