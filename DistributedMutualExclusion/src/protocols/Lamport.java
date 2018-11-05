package protocols;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import communication.CommMatrix;
import baseclasses.LamportMessage;
import baseclasses.LogicalClock;
import baseclasses.Message;
import baseclasses.Message.Type;

public class Lamport extends Thread{
	private JTextArea textArea, textArea2;	
	private String content="", content2="";
	private int myId, numProc;
	private CommMatrix commMatrix;
	private LogicalClock v;
	private int[] q;
	
	public Lamport(int myId, int numProc, CommMatrix commMatrix, int pos1, int pos2){
		this.myId = myId;
		this.numProc = numProc;
		this.commMatrix = commMatrix;
		this.v = new LogicalClock(this.numProc, this.myId);
		this.q = new int[this.numProc];
		for(int j=0; j<this.numProc; j++){
			this.q[j] = Integer.MAX_VALUE;
		}	
        JFrame window = new JFrame();
        window.setTitle(this.getClass().toString());
        window.setSize(200, 200);
        window.setLocation(pos1, pos2);
        
        JFrame window1 = new JFrame();
        window1.setTitle(this.getClass().toString());
        window1.setSize(200, 200);
        window1.setLocation(pos1+202, pos2);
        
        
        JTextArea textArea1 = new JTextArea(200, 300);
        textArea1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea1); 
        
        JTextArea textArea3 = new JTextArea(200, 300); 
        textArea3.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(textArea3); 
        
        window.setContentPane(scrollPane);
        window1.setContentPane(scrollPane1);
        window.setVisible(true);
        window1.setVisible(true);
        
    
        this.textArea = textArea1;
        this.textArea2 = textArea3;
        
        content = content + "Inicialitzat \n";
        textArea.setText(content);
        
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public synchronized void requestCS(){
		v.tick();
		q[myId] = v.getValue(myId);
		for (int j=1; j<this.numProc; j++){
			if (j != this.myId){
				content = content + "Request enviat a: " + j + "de " + myId + "\n";
		        textArea.setText(content);
				commMatrix.sendMsg(new LamportMessage(myId, j, Type.request, this.v.getValue(myId)));
			}
		}
	}
	
	public synchronized void releaseCS(){
		this.q[this.myId] = -1;
		for (int j=0; j<this.numProc; j++){
			if (j != this.myId){
				content = content + "Release enviat a: " + j + "de " + myId + "\n";
		        textArea.setText(content);
				commMatrix.sendMsg(new LamportMessage(myId, j, Type.release, this.v.getValue(myId)));
			}
		}
	}
	
	private boolean okayCS(){
		for (int j=1; j<this.numProc; j++){
			content = content + this.q[this.myId] +" "+ this.myId +" "+ this.q[j]+ " "+ j +" "+ this.v.getValue(j)+ "\n";
	        textArea.setText(content);
			if (isGreater(this.q[this.myId], this.myId, this.q[j], j)){
				content = content + "false \n";
		        textArea.setText(content);
				return false;
			}
			if (isGreater(this.q[myId], this.myId, this.v.getValue(j), j)){
				content = content + "false \n";
		        textArea.setText(content);
				return false;
			}
		}
		content = content + "true \n";
        textArea.setText(content);
		return true;
	}
	
	public void tocken(int to){
		commMatrix.sendMsg(new Message(0, myId, Type.tocken));				
	}
	
	private boolean isGreater(int entry1, int pid1, int entry2, int pid2){
		if (entry2 == Integer.MAX_VALUE){
			return false;
		}	
		return ((entry1 > entry2)) || ((entry1 == entry2) && (pid1 > pid2 ));		
	}
	
	public synchronized void handleMsg(Message m){
		int timeStamp = ((LamportMessage)m).getLogicalClock();
		this.v.receiveAction(m.getFrom(), timeStamp);
		if (m.getType().equals(Type.request)){
			content2 = content2 + "Request enviat de: " + m.getFrom() + "a " + myId + "\n";
	        textArea2.setText(content2);
			this.q[m.getFrom()] = timeStamp;
			commMatrix.sendMsg(new LamportMessage(myId, m.getFrom(), Type.ack, this.v.getValue(myId)));
		}else if (m.getType().equals(Type.release)){
			content2 = content2 + "Release enviat de: " + m.getFrom() + "a " + myId + "\n";
	        textArea2.setText(content2);
			this.q[m.getFrom()] = Integer.MAX_VALUE;
		}
	}
	
	private void showPrint(){
		for (int i=0; i<10; i++){
	        System.out.println(i + " - Soc el proces lightweight A"+ this.myId);
	        try {
			    Thread.sleep(10);                
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}		
	}
	
	protected Boolean haveTocken(){
		if ((!this.getCommMatrix().getMatrix()[0][this.getMyId()].isEmpty())){
			Message msg = (Message)this.getCommMatrix().getMatrix()[0][this.getMyId()].pop();
			if (msg.getType().equals(Type.tocken)){
				return Boolean.TRUE;
			}			
		}
		return Boolean.FALSE;

	}
	
	private void MessagesManagement(){
	    Thread t1 = new Thread(new Runnable() {           
	        public void run() {
	        	content2 = content2 + "LamportManagement Inicialitzat" + "\n";
		        textArea2.setText(content2);
	        	while (true){
	        		for (int i=1; i<commMatrix.getMatrixLength(); i++){
	        			if (i!=myId && (!commMatrix.getMatrix()[i][myId].isEmpty())){
	    			        Message message = (LamportMessage)commMatrix.getMatrix()[i][myId].pop();
	    			        content2 = content2 + "Llegeixo "+ message.getType().toString() + " a "+ myId+ " de " + i + "\n";
	    			        textArea2.setText(content2);
	    			        handleMsg(message);
	        			}
	        		}
	        	}
	        } 
	    });
	    t1.start();
	}
	
	private void LamportFlow(){
	    Thread t = new Thread(new Runnable() {           
	        public void run() { 
	        	content = content + "LamportFlow Inicialitzat" + "\n";
		        textArea.setText(content);
	    		while (true){
	    			for(int j=0; j<numProc; j++){
	    				q[j] = Integer.MAX_VALUE;
	    			}	
	    			v.reset();
	    			while (!haveTocken()){}
	    			content = content + "LWA tinc tocken \n";
			        textArea.setText(content);
	    			requestCS();
	    			while(!okayCS()){}
	    			showPrint();
	    			releaseCS();
	    			//Esperar 1 segon
	    			//while(true){}
	    		}
	        } 
	    });
	    t.start();
		
	}
		
	@Override
	public void run(){
		MessagesManagement();
		LamportFlow();
	}
	
	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public int getNumProc() {
		return numProc;
	}

	public void setNumProc(int numProc) {
		this.numProc = numProc;
	}

	public CommMatrix getCommMatrix() {
		return commMatrix;
	}

	public void setCommMatrix(CommMatrix commMatrix) {
		this.commMatrix = commMatrix;
	}

	public LogicalClock getV() {
		return v;
	}

	public void setV(LogicalClock v) {
		this.v = v;
	}

	public int[] getQ() {
		return q;
	}

	public void setQ(int[] q) {
		this.q = q;
	}

}
