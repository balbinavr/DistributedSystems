package baseClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {

	protected int id;
	protected int value;
	protected final Lock lockValue = new ReentrantLock();

	
	public Server (int id){
		this.id = id;
		this.value = 0;
	}
	
	
	public int getCurrentValue(){
		lockValue.lock();
		int valor =  this.value;
		lockValue.unlock();
		return valor;
	}
	
	public int setValue(int valor){
		lockValue.lock();
		this.value =  valor;
		System.out.println("    VALUE MODIFIED: "+ this.value);
		lockValue.unlock();
		return valor;
	}
	
	
	public int updateCurrentValue(int valor){
		lockValue.lock();
		this.value = valor;
		lockValue.unlock();
		return valor;
	}


	public Lock getLockValue() {
		return lockValue;
	}
	
	
	
	
	
}
