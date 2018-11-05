package process;

import communication.CommMatrix;

import protocols.RicartAndAgrawala;

public class ProcessLWB extends RicartAndAgrawala{
	public ProcessLWB(int myId, int numProc, CommMatrix commMatrix, int pos1, int pos2){
		super(myId, numProc, commMatrix, pos1, pos2);	
	}
}
