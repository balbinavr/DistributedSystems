package Layers;

import Managers.ChildrenManagerTime;
import Managers.ParentManager;

public class Layer1 extends Layer {
	
	private ParentManager parentManager;
	private ChildrenManagerTime childrenManager;
	
	public Layer1(int myId, int socketNum, int numProc){
		
		super(myId, socketNum, numProc, 1);
		parentManager = new ParentManager(socketNum, this);
		childrenManager = new ChildrenManagerTime(socketNum, this);
	}
	
	@Override
	public void run(){
		
		parentManager.start();
		clientsManager.start();
		childrenManager.start();
	}

	

}
