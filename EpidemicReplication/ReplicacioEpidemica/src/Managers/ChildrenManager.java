package Managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChildrenManager extends Thread{
	
	private List<Socket> sockets;
	private ServerSocket socket;
	
	public ChildrenManager(int socketNum){
		sockets = new ArrayList<Socket>();
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
				sockets.add(socketCli);			
			} catch (IOException e) {
				System.out.println("IO Exception connectant socketClient");
			} 
		}
	}
	
	public void notifyChildren(int version, String content){
		for (Socket socket:sockets){
			if (socket.isConnected()){
				System.out.println("Notifica child connectat: " +  socket.getPort());
				managementChildren(socket, version, content);
			}
		}
		
	}
	
	private void managementChildren(Socket socket, int version, String content){
		Thread t1 = new Thread(new Runnable() {           
			public void run() {
				try {
					String message = version + " " + content + "\n";
					System.out.println("Enviat: " +  message);
					socket.getOutputStream().write(message.getBytes());
				} catch (IOException e) {
					System.out.println("IOException with child");
				}
			} 
		});
		t1.start();
	}	
	
}
