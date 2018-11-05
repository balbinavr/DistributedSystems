package baseclasses;


public class LamportMessage extends Message{
	
	private int logicalClock;
	private String action;
	
	public LamportMessage(){
		
	}
	
	public LamportMessage(int from, int to, Type type, int logicalClock){
		super(from, to, type, "");
		this.setLogicalClock(logicalClock);
	}

	public int getLogicalClock() {
		return logicalClock;
	}

	public void setLogicalClock(int logicalClock) {
		this.logicalClock = logicalClock;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
