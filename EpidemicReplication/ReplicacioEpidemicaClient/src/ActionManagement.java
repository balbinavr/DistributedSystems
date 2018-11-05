import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ActionManagement {
	
	private long[] sockets;
	
	public ActionManagement(){
		
		//crea l'array amb els threads amb els que es podria connectar
		sockets = new long[7];
		sockets[0] = 40101;
		sockets[1] = 40102;
		sockets[2] = 40103;
		sockets[3] = 50101;
		sockets[4] = 50102;
		sockets[5] = 60101;
		sockets[6] = 60102;	
		
	}
	
	public String start(String action){
		
		//mirar node al que cal enviar
		String[] actions = action.split(",");
		if (actions.length > 1 && (actions[0].length()==1 || actions[0].length()==4) && "c".equals(actions[actions.length-1])){
			int layer = -1;
			if ("b".equals(actions[0])){
				layer = 0;
			}else{
				layer = Integer.parseInt(actions[0].substring(2, 3));
			}
			//System.out.println("Layer Number "+layer);
			int port = randomNode(layer);
			//int port = 40201;
			System.out.println("Port "+port);
			
			if (port != -1){
				try {
					//Conexio amb el node
					Socket socket = new Socket("localhost", port);
					//Envia la trama
					action =  action +"\n";
					socket.getOutputStream().write(action.getBytes());
					//System.out.println("trama enviada: " + action);
					//Espera fins a rebre resposta
					while (socket.getInputStream().available() < 0){}
					//Llegir resposta
					//System.out.println("resposta");
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = in.readLine();
					//System.out.println("missatge" + message);
					//Tanca el socket
					socket.close();
					//retorna la resposta de l'accio
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return message;
				} catch (UnknownHostException e) {
					System.out.println("Unknown host creating client socket");
					return null;
				} catch (IOException e) {
					System.out.println("IOException client socket");
					return null;
				}
			}else{
				System.out.println("Invalid layer Number");
			}
		}
		//Si no ha anat be, retornara null
		return "Error";
	}
	
	private int randomNode(int layer){
		Random rand = new Random();
		int port = 0;
		switch (layer){
		case 0:
			port = rand.nextInt(3) + 1;
			port = port + 40200;
			break;
		case 1:
			port = rand.nextInt(2) + 1;
			port = port + 50200;
			break;
		case 2:
			port = rand.nextInt(2) + 1;
			port = port + 60200;
			break;
		default:
			port = -1;
			break;
		}
		return port;
	}

}
