package communication;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import baseclasses.LamportMessage;
import baseclasses.Message;

public class CommSockets{

	//Matriu on la primera columna es de lectura i la segona d'escriptura
	private HashMap<Integer, Socket> sockets;
	private int socketsLength;
	
	public CommSockets(HashMap<Integer, Socket> sockets, int numProc){
		this.sockets = sockets;
		this.socketsLength = numProc;
	}
	
	public void sendMsg(Message msg) throws IOException{
		if (msg instanceof LamportMessage){
			msg.setMessage(msg.getType().toString() + " " + ((LamportMessage) msg).getLogicalClock()+"\n");
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Error en espera missatge enviat");
		}
		System.out.println("Envia: " + msg.getMessage() + "  " +msg.getTo()+ " "+ sockets.get(msg.getTo()));
		sockets.get(msg.getTo()).getOutputStream().write(msg.getMessage().getBytes());
	}
	
	public void broadcastAction(String action, int from) throws IOException{
		String message = "action "+ action + "\n";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Error en espera missatge enviat");
		}
		for (int i=1; i<=(socketsLength-1);i++){
			if ( i != from ){
				//System.out.println(i + " " + sockets.get(i).getPort());
				sockets.get(i).getOutputStream().write(message.getBytes());
			}
		}
	}

	public HashMap<Integer, Socket> getSockets() {
		return sockets;
	}

	public void setSockets(HashMap<Integer, Socket> sockets) {
		this.sockets = sockets;
	}

	public int getSocketsLength() {
		return socketsLength;
	}

	public void setSocketsLength(int socketsLength) {
		this.socketsLength = socketsLength;
	}
	
}
