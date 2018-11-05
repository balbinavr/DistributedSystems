package Layers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Managers.ChildrenManager;
import Managers.ClientsManager;
import communication.CommSockets;
import protocols.RicartAndAgrawala;

public class CoreLayer extends Layer{
	
	private RicartAndAgrawala rAM;
	private HashMap<Integer,Socket> sockets;
	private ChildrenManager childrenManager;
	
	
	public CoreLayer(int myId, int socketNum, int numProc){
		super(myId, socketNum, numProc, 0);
		
		
		sockets = new HashMap<Integer, Socket>();
		for (int i = 1; i<myId; i++){
			try {
				sockets.put(i,(new Socket("localhost", (40000+i))));
				System.out.println("Connectat del " + myId + " al:"+ i);
			} catch (UnknownHostException e) {
				System.out.println("Unknown Host Exception del " + myId + " al:"+ i);
			} catch (IOException e) {
				System.out.println("IO Exception del " + myId + " al:"+ i);
			}
		}
		for (int i =(myId+1); i<=numProc; i++){
			try {
				sockets.put(i, socket.accept());
				System.out.println("Connectat del " + myId + " al:"+ i);
			} catch (IOException e) {
				System.out.println("IO Exception del " + myId + " al:"+ i);
			} 
		}
		for (int i=1; i<=numProc;i++){
			if ( i != myId ){
				System.out.println(i + " " + sockets.get(i).getPort());
			}
		}
		this.lock = new ReentrantReadWriteLock();
		this.childrenManager = new ChildrenManager(socketNum);
		this.rAM = new RicartAndAgrawala(myId, numProc+1, new CommSockets(this.sockets, numProc+1), 10, 10, this);
		System.out.println("Fora");
		
	}
	
	@Override
	public void run(){
	
		rAM.start();
		clientsManager.start();
		childrenManager.start();
		while (true){
		}
	}

	public RicartAndAgrawala getrAM() {
		return rAM;
	}

	public void setrAM(RicartAndAgrawala rAM) {
		this.rAM = rAM;
	}

	public ClientsManager getClientsManager() {
		return clientsManager;
	}

	public void setClientsManager(ClientsManager clientsManager) {
		this.clientsManager = clientsManager;
	}

	public HashMap<Integer, Socket> getSockets() {
		return sockets;
	}

	public void setSockets(HashMap<Integer, Socket> sockets) {
		this.sockets = sockets;
	}
	
	public ChildrenManager getChildrenManager() {
		return childrenManager;
	}

	public void setChildrenManager(ChildrenManager childrenManager) {
		this.childrenManager = childrenManager;
	}
	
	public synchronized void incrementaVersio(String accio){
		this.versio = this.versio + 1;
		this.actualitzacions = actualitzacions + accio;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.versio+"\n");
			bw.close();
		} catch (IOException e) {
			System.out.println("IOException creating file!");
		}
		if ((versio%10) == 0 && socketNum < 50000){
			System.out.println("NOTIFICA VERSIO");
			this.childrenManager.notifyChildren(versio, new String(actualitzacions));
			actualitzacions = "";
		}
	}

}
