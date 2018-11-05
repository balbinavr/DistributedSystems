import java.util.LinkedList;
import java.util.Scanner;

import baseClasses.ControlServer;
import baseClasses.ReadOnlyServer;
import baseClasses.ReadWriteServer;
import baseClasses.Server;



public class Main {
	
	private static final int MAX = 5;
	
	public static final String INICIALITZATION_COMPLETED="Inicialitzacio completada satisfactoriament!";
	public static final String EXECUTION_START="Premeu 'e' per comencar l'execucio:";
	public static final String NEXT_LINE="\n";
	
    public static void main(String[] args){
    	
    	//Inicialitzar cada Servidor
    	LinkedList<Server> servers = inicialitzaServers();
    	System.out.println(NEXT_LINE + INICIALITZATION_COMPLETED);
    	System.out.println(NEXT_LINE + EXECUTION_START);
    	Scanner scanIn = new Scanner(System.in);
    	String teclat = scanIn.nextLine();
    	    	    
    	if (teclat.equals("e")){
    		//Executar tots els processos preparats
    	    System.out.println("\nExecutar");
    	    executaServers(servers);    	
    	}
    	
    	finalitzaServers(servers);    
    	scanIn.close(); 
    	System.out.println("\nAcabat");
  		
    		
    }
    
    private static LinkedList<Server> inicialitzaServers(){
    	LinkedList<Server> servers = new LinkedList<Server>();
    	int j;
    	
		for (int i=0; i<MAX;i++){
			if ( i == 0){
				//Inicialitzar servior central (controlarà a tots els altres)
				Server server = new ControlServer(i);
				servers.add(i, server);
				System.out.println("\nServidor central [ " + i + " ]");
			}else{
				j = i%2;
				switch (j){
				case 0:
					//Inicialitzar servidor d'escriptura
					Server server2 = new ReadWriteServer(i);
					servers.add(i, server2);
					System.out.println("\nServidor d'escriptura [ " + i + " ]");
					break;
				case 1:
					//Inicialitzar servidor de lectura
					Server server1 = new ReadOnlyServer(i);
					servers.add(i, server1);
					System.out.println("\nServidor de lectura [ " + i + " ]");
					break;
				default:
					break;
				}
			}
		}
		return servers;
    }
    
    private static void executaServers(LinkedList<Server> servers){
    	for (Server server: servers){
    		server.start();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
    
    private static void finalitzaServers(LinkedList<Server> servers){
		try {
	    	for (Server server: servers){
	    		server.join();
	    		
	    	}
		} catch (InterruptedException e) {}
    }
}
