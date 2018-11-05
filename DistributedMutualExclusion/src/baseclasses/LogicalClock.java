package baseclasses;

public class LogicalClock {
	public int[] clock;
	int myId;
	int numProc;
	
	public LogicalClock(int numProc, int id){
		this.myId = id;
		this.numProc = numProc;
		clock = new int [numProc];
		for (int i = 0; i <numProc; i++){
			clock[i] = 0; 
		}
		clock [myId] = 1;		
		
	}
	
	public void reset(){
		for (int i = 0; i <numProc; i++){
			clock[i] = 0; 
		}
		clock [myId] = 1;
	}
	
	public int getValue(int i){
		return clock [i]; 
	}
	
	public void tick () {
		clock [myId]++;
	}

	public void receiveAction (int sender , int sentvalue) { 
		clock [sender] = Math.max( clock [sender], sentvalue );
		clock [myId] = Math.max(clock [myId], sentvalue) + 1;
	}
	
}
