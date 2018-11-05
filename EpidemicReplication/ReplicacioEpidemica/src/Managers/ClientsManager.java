package Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

import Layers.CoreLayer;
import Layers.Layer;
import baseclasses.Action;

public class ClientsManager extends Thread{

	private List<Socket> sockets;
	private ServerSocket socket;
	private int numLayer;
	private ReadWriteLock lock;
	private Layer layer;
	
	public ClientsManager(int socketNum, int numLayer, ReadWriteLock lock, Layer layer){
		this.numLayer = numLayer;
		this.lock = lock;
		this.layer = layer;
		sockets = new ArrayList<Socket>();
		try {
			this.socket = new ServerSocket(socketNum+200);
			System.out.println("SocketServerCreat: " +  (socketNum+200));
		} catch (IOException e) {
			System.out.println("Error creating serverSocket " +  socketNum + e.getMessage());
		}
	}
	
	@Override
	public void run(){
		while (true){
			try {
				Socket socketCli = socket.accept();
				System.out.println("Nova connexio client!");
				sockets.add(socketCli);
				//Establir nova escolta pel client
				MessagesManagement(socketCli);			
			} catch (IOException e) {
				System.out.println("IO Exception connectant socketClient");
			} 
		}
	}
	
	private void MessagesManagement(Socket socket){
		Thread t1 = new Thread(new Runnable() {           
			public void run() {
				try {
					//mentre no estigui desconnectat el socket
					while (socket.isConnected()){
						//llegim si hi ha nova informacio disponible
						if (socket.getInputStream().available() > 0){
							//llegeix nova accio
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String message = in.readLine();
							System.out.println("Missatge rebut: " + message);
							//Fracciona les accions
							String[] actions = message.split(",");
							//Comprova que la accio sigui pel node
							if (actions.length > 0){
								if ("b".equals(actions[0]) || Integer.toString(numLayer).equals(actions[0].substring(2, 3))){
									int i = 1;
									String response = "";
									//realitza les accions demanades, una per una
									while(!"c".equals(actions[i].substring(0,1))){
										//Comprova si la accio es de lectura o d'escriptura
										if ("r".equals(actions[i].substring(0, 1)))	{
											//mode de lectura
											String num = actions[i].substring(2, actions[i].length()-1);
											System.out.println("Posicio a llegir:" + num);
											lock.readLock().lock();
											int value = layer.getValors()[Integer.parseInt(num)];
											System.out.println("Valor llegit:" + value);
											lock.readLock().unlock();
											response = response + num + ":" + value + " ";
										}else if ("w".equals(actions[i].substring(0, 1))){
											//mode de escriptura, espera el valor de retorn
											String[] num = actions[i].split(":");
											lock.writeLock().lock();
											System.out.println("Escriptura: " + actions[i].substring(2, actions[i].length()-1));
											Action action = new Action(actions[i].substring(2, actions[i].length()-1), this);
											((CoreLayer)layer).getrAM().setAction(action);
											try {
												synchronized(this){
													wait();
												}
												int value = layer.getValors()[Integer.parseInt(num[0].substring(2))];
												response = response + num[0].substring(2) + ":" + value + " ";
											} catch (InterruptedException e) {
												System.out.println("Error writting wait");
											}finally{
												lock.writeLock().unlock();
											}
										}else{
											//retorna missatge d'error, espera el valor de retorn
										}
										i++;
									}
									//retorna la trama creada amb totes les accions
									response = "r "+ response + "c";
									response = response.replace(' ' , ',');
									response = response + "\n";
									System.out.println("Resposta " + response );
									socket.getOutputStream().write(response.getBytes());
									
								}else{
									//retorna missatge d'error!
								}
							}
						}
					}
				} catch (IOException e) {
					System.out.println("IOException with client");
				}
			} 
		});
		t1.start();
	}
}
