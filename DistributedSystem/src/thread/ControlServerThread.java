package thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import baseClasses.ControlServer;
import baseClasses.Server;
import utils.FrameUtils;

public class ControlServerThread extends Thread{

	private ControlServer cs;
	private Socket socket;
	private int id;
	private BufferedWriter out;
	private BufferedReader in;
	
	public ControlServerThread(ControlServer cs,Socket socket, int id){
		this.cs = cs;
		this.socket = socket;
		this.id = id;
	}
	
	@Override
	public void run(){

		try {
	        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
	        String line = null;
	        while ((line = this.in.readLine()) != null) {
	            String type = FrameUtils.getTypeFrame(line);            
	            switch(type){
		            case "get":
		            	cs.getLockValue().lock();
			            out.write(FrameUtils.createGetFrame(this.id, this.cs.getCurrentValue()));
			            cs.getLockValue().unlock();
			            out.newLine();
			            out.flush();
			            
		            	break;
		            case "set":
		            	int valor = FrameUtils.getValueFrame(line);
		            	//System.out.println("thread main : dins "+valor);
		            	cs.getLockValue().lock();
		            	cs.setValue(valor);
		            	cs.getLockValue().unlock();
		            	//ENVIAR NOU VALOR A TOOTS ELS SERVIDORS
		            	cs.getLockSockets().lock();
		            	LinkedList<BufferedWriter> sockets = new LinkedList<BufferedWriter>(this.cs.getList());
		            	for (BufferedWriter buff : sockets){
		            		buff.write(FrameUtils.createUpdateFrame(this.id, valor));
		            		buff.newLine();
		            		buff.flush();
		            	}
		            	cs.getLockSockets().unlock();
		            		
		            	break;
		            default:
		            	System.out.println(id + " Type no conegut: " + line);
		            	break;
	            }
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
