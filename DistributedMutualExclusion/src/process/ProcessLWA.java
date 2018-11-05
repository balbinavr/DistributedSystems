package process;

import communication.CommMatrix;
import protocols.Lamport;

public class ProcessLWA extends Lamport{
	
	public ProcessLWA(int myId, int numProc, CommMatrix commMatrix, int pos1, int pos2){
		
		super(myId, numProc, commMatrix, pos1, pos2);	
		
	}

}
