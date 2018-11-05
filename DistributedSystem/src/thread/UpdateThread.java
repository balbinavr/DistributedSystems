package thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import utils.FrameUtils;
import baseClasses.ControlServer;
import baseClasses.Server;

public class UpdateThread extends Thread {
	
	private Server ss;
	private Socket socket;
	private int id;
	private BufferedReader in;
	
	public UpdateThread(Server ss,Socket socket, int id, BufferedReader in){
		this.ss = ss;
		this.socket = socket;
		this.id = id;
		this.in = in;
	}
	
	public void run(){
		//System.out.println("lectura id: "+this.id);
		while (true){
			String line = null;
			try {
				
				while ((line = this.in.readLine()) != null) {
					//tracta arribada
					//System.out.println("thread id: "+this.id+line);
					//si la trama que arriba es de set --> actualitza valor de la variable
					if (FrameUtils.getTypeFrame(line).equals("update")){
						int valor = this.ss.updateCurrentValue(FrameUtils.getValueFrame(line));
						System.out.println(this.id+ " - Valor rebut modificat: " + valor);
					}    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
