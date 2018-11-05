package Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import Layers.Layer;

public class ParentManager extends Thread{

	private Socket parentSocket;
	private Layer layer;
	
	public ParentManager(int socketNum, Layer layer) {

		this.layer = layer;
		try {
			parentSocket = new Socket("localhost", (socketNum-10000+100));
			System.out.println("Connectat al:"+ (socketNum-10000+100));
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host Exception");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}	
		
	}
	
	@Override
	public void run(){
		while (parentSocket.isConnected()){
			try {
				if (parentSocket.getInputStream().available() > 0){
					layer.getLockVersion().acquire();
					BufferedReader in = new BufferedReader(new InputStreamReader(parentSocket.getInputStream()));
					String message = in.readLine();
					System.out.println("Missatge rebut:" + message);
					String[] actions = message.split(" ");
					if (layer.getVersio() != Integer.parseInt(actions[0])){				
						layer.setVersio(Integer.parseInt(actions[0]));
						String actualitzacions = layer.getActualitzacions();
						for (int i=1; i<actions.length; i++){
							String[] frames = actions[i].split(":");
							System.out.println("Actualitza:" + frames[0] + " " + frames[1]);
							layer.getValors()[Integer.parseInt(frames[0])] = Integer.parseInt(frames[1]); 
							actualitzacions = actualitzacions + actions[1] + " ";
						}
						layer.setActualitzacions(actualitzacions);
						
					}else{
						System.out.println("IGNORE MESSAGE OF ACTUALITZATION ALREADY DONE");
					}
					layer.getLockVersion().release();
				}
			} catch (IOException e) {
				System.out.println("IOException thread");
			} catch (InterruptedException e) {
				System.out.println("InterruptedException thread");
			}

		}
	}
}
