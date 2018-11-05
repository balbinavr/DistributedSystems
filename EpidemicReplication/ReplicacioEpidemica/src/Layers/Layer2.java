package Layers;

import Managers.ParentManager;

public class Layer2 extends Layer{
	
	private ParentManager parentManager;
	
	public Layer2(int myId, int socket, int numProc){
		super(myId, socket, numProc, 2);
		parentManager = new ParentManager(60002, this);
		
	}
	
	@Override
	public void run(){
		
		parentManager.start();
		clientsManager.start();
	}

}
