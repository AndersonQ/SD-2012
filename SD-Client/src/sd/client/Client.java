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
        InterfaceAcesso ia_cliente = null;
        InterfaceControlador ic_cliente= null;
        ArrayList<String> ALS = null;
        String busca="", nome="", conteudo="";
        String[] busca_split;
        Scanner sc = new Scanner(System.in);
        int opt=1, ID=0, ADDR=1;
        Box obj = null;
        /* Connect to the Controller */
        try
        {
            ic_cliente = (InterfaceControlador) Naming.lookup("rmi://localhost/Controller");
        }
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
        try{
        System.out.println(ic_cliente.Connection_cliente_OK()+"\n");
        }
        catch (RemoteException e)
        {
            System.out.println("RemoteException");
            e.printStackTrace();
        }	while (opt > 0)	{			
        	//Loop infinito enquanto o programa atende o usuário.
        	/*Dialogo com o usuário*/
        	System.out.println("O que deseja fazer?\n1 - Criar e armazenar objeto\n2 - Recuperar e Editar um objeto\n3 - Apagar um objeto\n4 - Listar objetos disponíveis");
        	opt=sc.nextInt();
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
	
			default:
			
				break;
			}
    }}
}
