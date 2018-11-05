package baseClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import thread.ControlServerThread;
import thread.UpdateThread;
import utils.FrameUtils;

public class ReadOnlyServer extends Server{
	
		
	private BufferedWriter out;
	private BufferedReader in;
	private Socket socket;
	
	public ReadOnlyServer (int id){
		super(id);
	}

	@Override
	public void run(){
		try {
            this.socket = new Socket("localhost", 60010);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.value = getCurrentValueStart();
            new UpdateThread(this, this.socket, this.id, this.in).start(); 
            System.out.println(this.id +"comenca - "+ this.value);
            for (int i=0; i<10 ; i++){
            	int valor = getCurrentValue();
            	System.out.println(this.id+ " - "+ i +" Valor: "+ valor);
            	sleep(1000);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	protected int getCurrentValueStart(){
		try {
			System.out.println(this.id+ " - Valor demanat");
			out.write(FrameUtils.createGetFrame(this.id));
		    out.newLine();
		    out.flush();
	        String line = null;
	        while ((line = in.readLine()) == null) {
	        }
	        System.out.println(this.id+ " - Valor rebut");
	        int valor = FrameUtils.getValueFrame(line);
	        return valor;	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return -1;
	}
	
	
	
	
}
