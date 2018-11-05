package Managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Layers.Layer;

public class ChildrenManagerTime extends Thread{
	

	private ServerSocket socket;
	private Layer layer;
	
	public ChildrenManagerTime(int socketNum, Layer layer){
		this.layer = layer;
		try {
			this.socket = new ServerSocket(socketNum+100);
			System.out.println("SocketServerCreat: " +  (socketNum+100));
		} catch (IOException e) {
			System.out.println("Error creating serverSocket " +  socketNum + e.getMessage());
		}
	}
	
	@Override
	public void run(){
		while (true){
			try {
				Socket socketCli = socket.accept();
				System.out.println("Nova connexio child!");
				managementChildren(socketCli);		
			} catch (IOException e) {
				System.out.println("IO Exception connectant socketClient");
			} 
		}
	}
	
	private void managementChildren(Socket socket){
		Thread t1 = new Thread(new Runnable() {           
			public void run() {
				try {
					
					Thread.sleep(100000);
					
					while (socket.isConnected()){
						layer.getLockVersion().acquire();
						int versio = layer.getVersio();
						int[] valors = layer.getValors();
						String content = "";
						for (int i=0; i<layer.getValors().length;i++){
							content = content + i + ":" + valors[i] + " ";
						}
						String message = Integer.toString(versio) + " " + content + "\n";
						System.out.println("Enviat: " +  message);
						socket.getOutputStream().write(message.getBytes());
						layer.getLockVersion().release();
						Thread.sleep(10000);
					}

				} catch (IOException e) {
					System.out.println("IOException with child");
				}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		});
		t1.start();
	}	
	
}
