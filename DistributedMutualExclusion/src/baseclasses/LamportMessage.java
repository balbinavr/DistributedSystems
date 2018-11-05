package baseclasses;


public class LamportMessage extends Message{
	
	private int logicalClock;
	
	public LamportMessage(int from, int to, Type type, int logicalClock){
		super(from, to, type);
		this.setLogicalClock(logicalClock);
	}

	public int getLogicalClock() {
		return logicalClock;
	}

	public void setLogicalClock(int logicalClock) {
		this.logicalClock = logicalClock;
	}

}
