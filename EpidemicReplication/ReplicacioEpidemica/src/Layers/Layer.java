package Layers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Managers.ClientsManager;

public class Layer extends Thread{
	
	protected int myId;
	protected int socketNum;
	protected int numProc;
	protected ClientsManager clientsManager;
	protected ServerSocket socket;
	protected int[] valors;
	protected ReadWriteLock lock;
	protected Semaphore lockVersion;
	protected int versio;
	protected String actualitzacions;
	protected File file;
	
	public Layer(int myId, int socketNum, int numProc, int numLayer){
		
		this.myId = myId;
		this.socketNum = socketNum;
		this.numProc = numProc;
		this.valors = new int[100];
		this.lockVersion = new Semaphore(1);
		this.actualitzacions = "";	
		this.lock = new ReentrantReadWriteLock();
		for (int i=0; i<100; i++){
			valors[i] = i;
		}
		
		if ( numLayer != 2){
			try {
				this.socket = new ServerSocket(socketNum);
				System.out.println("SocketServerCreat: " +  socketNum);
			} catch (IOException e) {
				System.out.println("Error creating serverSocket " +  socketNum + e.getMessage());
			}
		}
		this.clientsManager = new ClientsManager(socketNum, numLayer, this.lock, this);
		versio = 0;
		file = new File("./L_"+numLayer+"_"+myId);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.versio+"\n");
			bw.close();
		} catch (IOException e) {
			System.out.println("IOException creating file!");
		}
	
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public int getSocket() {
		return socketNum;
	}

	public void setSocket(int socket) {
		this.socketNum = socket;
	}

	public int getNumProc() {
		return numProc;
	}

	public void setNumProc(int numProc) {
		this.numProc = numProc;
	}

	public int[] getValors() {
		return valors;
	}

	public void setValors(int[] valors) {
		this.valors = valors;
	}

	public int getVersio() {
		return versio;
	}

	public void setVersio(int versio) {
		this.versio = versio;
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
	}

	public ClientsManager getClientsManager() {
		return clientsManager;
	}

	public void setClientsManager(ClientsManager clientsManager) {
		this.clientsManager = clientsManager;
	}

	public Semaphore getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(Semaphore lockVersion) {
		this.lockVersion = lockVersion;
	}

	public String getActualitzacions() {
		return actualitzacions;
	}

	public void setActualitzacions(String actualitzacions) {
		this.actualitzacions = actualitzacions;
	}
	

}
