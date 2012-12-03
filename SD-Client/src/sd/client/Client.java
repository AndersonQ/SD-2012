package sd.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;


import sd.exceptions.NenhumServidorDisponivelException;
import sd.exceptions.ObjetoJaExisteException;
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
		
		/** This one will bind to the Controller, most of the functions comes from it */
        InterfaceControlador ic_cliente= null;
        /** String used to keep useful information */
        String nome = null;
        /** The simple and easy to use scanner function from java.util library */
        Scanner sc = new Scanner(System.in);
        /** opt is used to get the options through the interface,*/         
        int opt=1;
        /** Generic object used on some functions */
        Box obj = null;
        
        /*End of variable declaration*/
        
        /* Connect to the Controller */
        try
        {
        	/*Try to bind the variable with the Controller object*/
            ic_cliente = (InterfaceControlador) Naming.lookup("rmi://localhost/Controller");
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
	        		Cliente_Armazena(nome, ic_cliente);
	        		break;
	        	/*Fim de armazenamento de objetos*/
	        		
	        	/*Recuperar e Editar um objeto*/
	        	case 2:
	        		Cliente_recupera_e_edita(nome,ic_cliente,obj);
	        		break;
	        	/*Final de recuperar e Editar um objeto*/
	        		
	        	/*Apagar um objeto*/
	        	case 3:
	        		Cliente_Apaga(nome, ic_cliente);
	        		break;
	        	/*Final de apagar um objeto*/
	        		
	        	/*Listar objetos disponíveis*/
	        	case 4:
	        		Cliente_Lista(nome, ic_cliente);
	        		break;
	        		/*FIM Listar objetos disponíveis*/
	        	default:
	        		opt = 0;
	        		break;
	        }
        }
    }
    /**
     * Cria um objeto e solicita que o controlador o armazene
     * @param nome O nome do objeto, embora o método pede para o usuário escrever esse nome
     * @param ic_cliente O objeto que liga o cliente ao controlador
     */
    public static void Cliente_Armazena(String nome, InterfaceControlador ic_cliente){
        Scanner sc = new Scanner(System.in);
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
			return;
		}
		catch (NenhumServidorDisponivelException e)
		{
			System.out.println("NenhumServidorDisponivelException");
			e.printStackTrace();
			return;
		} catch (ObjetoJaExisteException e)
		{
		    System.out.println("Objeto Já Existe!");
            e.printStackTrace();
        }
		System.out.println("Objeto armazenado com sucesso\n");
    	
    }
    /**
     * Solicita ao controlador conexão com um servidor e em seguida recupera um objeto para edição
     * Também permite apagar um objeto após a sua edição, mas pode manter uma cópia nos servidores
     * TODO Implementar método reportFail(String service) da interface controlador
     * @param nome Nome do objeto que se deseja recuperar, o método solicita seu nome
     * @param ic_cliente O objeto que conecta o cliente ao controlador
     * @param obj Objeto da classe Box, simplesmente um objeto que herda serializable
     */
    public static void Cliente_recupera_e_edita(String nome, InterfaceControlador ic_cliente, Box obj){
    	/** ID and ADDR are actually constants, to access the busca_split String indexes, if necessary later I may declare it as final const
    	 * subopt is just for collecting options data from the user */
    	int  subopt=1, ID=0, ADDR=1;
    	/** This variable will later bind to a server*/
		InterfaceAcesso ia_cliente = null;
		/**Just a Scanner*/
    	Scanner sc = new Scanner(System.in);
    	/**Strings to keep important information and used for data manipulations*/
    	String busca, tmp;
        /** This array is necessary so we can split the tuple {object_id, server, service} */
        String[] busca_split;
        
        /*Start*/
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
			return;
		}
		catch (NenhumServidorDisponivelException e2)
		{
			System.out.println("NenhumServidorDisponivelException");
			e2.printStackTrace();
			return;
		}
		catch (ObjetoNaoEncontradoException e2)
		{
			System.out.println("ObjetoNaoEncontradoException");
			e2.printStackTrace();
			return;
		}
		busca_split=busca.split("@");
		System.out.println("Objeto encontrado com ID: " + busca_split[ID]);
		System.out.println("No Endereço: " + busca_split[ADDR]);
		System.out.println("Conectando ao servidor...");
		try
		{
			ia_cliente = (InterfaceAcesso) Naming.lookup(busca_split[ADDR]);
		}
		catch (MalformedURLException e2)
		{
			System.out.println("MalformedURLException");
			e2.printStackTrace();
			return;
		}
		catch (RemoteException e2)
		{
			System.out.println("RemoteException");
			e2.printStackTrace();
			return;
		}
		catch (NotBoundException e2)
		{
			System.out.println("NotBoundException");
			e2.printStackTrace();
			return;
		}
		System.out.println("Conectado!");
		
		try
		{
			obj = ia_cliente.recupera(Integer.parseInt(busca_split[ID]));
		}
		catch (NumberFormatException e2)
		{
			System.out.println("NumberFormatException");
			e2.printStackTrace();
			return;
		}
		catch (RemoteException e2)
		{
			System.out.println("RemoteException");
			e2.printStackTrace();
			return;
		}
		catch (ObjetoNaoEncontradoException e2)
		{
			System.out.println("ObjetoNaoEncontradoException");
			e2.printStackTrace();
			return;
		}
		System.out.println("Deseja alterar o objeto?\n1 - SIM\n2 - NAO");
		subopt = sc.nextInt();
		tmp = nome;
		
		if(subopt == 1)
		{
			while(tmp.equals(nome))
			{
				System.out.println("Digite um novo nome para o objeto.");
				nome = sc.next();
				
				if(nome.equals(tmp))
				{
					System.out.println("Objeto com nome igual.");
				}
			}
			
			System.out.println("Deseja apagar o objeto original?\n1 - SIM\n2 - NAO");
			subopt = sc.nextInt();
			
			if(subopt == 1)
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
					return;
				}
				catch (NenhumServidorDisponivelException e)
				{
					System.out.println("NenhumServidorDisponivelException");
					e.printStackTrace();
					return;
				}
				catch (ObjetoNaoEncontradoException e)
				{
					System.out.println("Objeto Não encontrado\n");
					return;
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
				return;
			}
			catch (NenhumServidorDisponivelException e)
			{
				System.out.println("NenhumServidorDisponivelException");
				e.printStackTrace();
				return;
			} catch (ObjetoJaExisteException e)
			{
			    System.out.println("Objeto Já Existe!");
			    e.printStackTrace();
			    return;
            }
			System.out.println("Novo Objeto armazenado com sucesso\n");
		}
    	
    }
    
    public static void Cliente_Apaga(String nome, InterfaceControlador ic_cliente){
    	/**Just a Scanner*/
    	Scanner sc = new Scanner(System.in);
    	System.out.println("\nDigite o nome do objeto a ser apagado");
		nome = sc.next();		
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
			return;
		}
		catch (NenhumServidorDisponivelException e)
		{
			System.out.println("NenhumServidorDisponivelException");
			e.printStackTrace();
			return;
		}
		catch (ObjetoNaoEncontradoException e)
		{
			System.out.println("Objeto Não encontrado\n");
			return;
		}
    	
    }
    
    public static void Cliente_Lista(String nome, InterfaceControlador ic_cliente){
    	/** This array will receive a copy of the list of objects from the Controller */
        ArrayList<String> ALS = null;
        try
        {
        	ALS = ic_cliente.lista();
        }
        catch (RemoteException e1)
        {
        	System.out.println("RemoteException");
        	e1.printStackTrace();
        	return;
        }
        catch (NenhumServidorDisponivelException e1)
        {
        	System.out.println("NenhumServidorDisponivelException");
        	e1.printStackTrace();
        	return;
        }
        System.out.println("\nListando nomes dos objetos armazenados");
	
        for (String nome_objetos: ALS)
        {
        	System.out.println(nome_objetos);
        }
        System.out.println();
		}
    }
