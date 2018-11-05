package baseClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import thread.ControlServerThread;
import utils.FrameUtils;

public class ControlServer extends Server {

	private final Lock lockSockets = new ReentrantLock();
	private LinkedList<BufferedWriter> sockets = new LinkedList<BufferedWriter>();
	
	
	public ControlServer (int id){
		super(id);
	}
	
	@Override
	public void run(){
		ServerSocket ss;
        try {
        	//Configura el serverSocket per rebre peticions
            ss = new ServerSocket(60010);
            //Espera infinitament a rebre peticions de sockets
            while (true) {
                try {
                	Socket s = ss.accept();
                	//Afegim el nou socket connectat a la llista
                	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                	addList(out);
                	//Crea un nou thread per cada socket que ha demanat connexio
                    new ControlServerThread(this, s, this.id).start();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }             
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public synchronized LinkedList<BufferedWriter> getList(){
		lockSockets.lock();
		LinkedList<BufferedWriter> list = this.sockets;
		lockSockets.unlock();
		return list;
	}
	
	public synchronized void setList(LinkedList<BufferedWriter> list){
		lockSockets.lock();
		this.sockets = list;
		lockSockets.unlock();
	}
	
	private synchronized void addList(BufferedWriter socket){
		lockSockets.lock();
		this.sockets.add(socket);
		lockSockets.unlock();
	}

	public Lock getLockSockets() {
		return lockSockets;
	}
	
}
