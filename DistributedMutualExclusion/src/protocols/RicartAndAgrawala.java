package protocols;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import baseclasses.LamportClock;
import baseclasses.LamportMessage;
import baseclasses.Message;
import baseclasses.Message.Type;
import communication.CommMatrix;

public class RicartAndAgrawala extends Thread{
	private JTextArea textArea, textArea2;	
	private String content="", content2="";
	private int myts, numProc, myId;
	private LamportClock c;
	LinkedList<Integer> pendingQ = new LinkedList<Integer>();
	private Integer numOkay = 0;
	private CommMatrix commMatrix;
	private boolean tocken = false;
	
	public RicartAndAgrawala(int myId, int numProc, CommMatrix commMatrix, int pos1, int pos2){
		this.myId = myId;
		this.numProc = numProc;
		this.commMatrix = commMatrix;
		this.c = new LamportClock();
		this.pendingQ = new LinkedList<Integer>();
		this.myts = Integer.MAX_VALUE;
		this.numOkay = 0;
		
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
        window.setVisible(true);
        this.textArea = textArea1;
        window1.setContentPane(scrollPane1);
        window1.setVisible(true);
        this.textArea2 = textArea3;
        
        content = content + "Inicialitzat \n";
        textArea.setText(content);
        
	}
	
	public synchronized void requestCS(){
		c.tick();
		myts = c.getValue();
		for (int j=1; j<this.numProc; j++){
			if (j != this.myId){
				content = content + "Request enviat a: " + j + "de " + myId + "\n";
		        textArea.setText(content);
				commMatrix.sendMsg(new LamportMessage(myId, j, Type.request, myts));
			}
		}
	}
	
	public synchronized void releaseCS(){
		myts = Integer.MAX_VALUE;
		while (!pendingQ.isEmpty()){
			int pid = pendingQ.removeFirst();
			content = content + "Release enviat a: " + pid + "de " + myId + "\n";
	        textArea.setText(content);
			commMatrix.sendMsg(new LamportMessage(myId, pid, Type.release, myts));
		}
		content = content + "Release enviat a: " + 0 + "de " + myId + "\n";
        textArea.setText(content);
		commMatrix.sendMsg(new LamportMessage(myId, 0, Type.release, c.getValue()));
	}
	
	public synchronized void handleMsg(Message m){
		int timeStamp = ((LamportMessage)m).getLogicalClock();
		this.c.receiveAction(m.getFrom(), timeStamp);
		if (m.getType().equals(Type.request)){
			content2 = content2 + myId + " " + myts + " " +timeStamp + "\n";
	        textArea2.setText(content2);
			if ((myts == Integer.MAX_VALUE) || (timeStamp < myts) || (timeStamp == myts) && (m.getFrom() < myId)){
				content2 = content2 + "Es compleix!! Release enviat de: " + m.getFrom() + "a " + myId + "\n";
		        textArea2.setText(content2);
		        commMatrix.sendMsg(new LamportMessage(myId, m.getFrom(), Type.release, c.getValue()));
			}else{
				content2 = content2 + m.getFrom() +" Afegit al pending"+ "\n";
		        textArea2.setText(content2);
				pendingQ.add(m.getFrom());
			}
		}else if (m.getType().equals(Type.release)){
			content2 = content2 + "Release enviat de: " + m.getFrom() + "a " + myId + "\n";
	        textArea2.setText(content2);
			this.numOkay = this.numOkay+1;
		}
	}
	
	public void tocken(int to){
		commMatrix.sendMsg(new Message(0, myId, Type.tocken));				
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
	
	private boolean okayCS(){
		int numFinal = this.numProc-2;
		if (numOkay.equals(numFinal)){
			return true;
		}else{
			return false;
		}
	}
	
	private void showPrint(){
		for (int i=0; i<10; i++){
			System.out.println(i + " - Soc el proces lightweight B"+ this.myId);
	        try {
			    Thread.sleep(10);                
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}		
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
	
	private void RicartAndAgrawalaFlow(){
	    Thread t = new Thread(new Runnable() {           
	        public void run() { 
	        	content = content + "LamportFlow Inicialitzat" + "\n";
		        textArea.setText(content);
				while (true){
					myts = Integer.MAX_VALUE;
					c.reset();
					while (!haveTocken()){}
					content = content + "LWB tinc tocken \n";
			        textArea.setText(content);
					requestCS();
					numOkay = 0;
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
		RicartAndAgrawalaFlow();
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

	public int getMyts() {
		return myts;
	}

	public void setMyts(int myts) {
		this.myts = myts;
	}

	public int getNumProc() {
		return numProc;
	}

	public void setNumProc(int numProc) {
		this.numProc = numProc;
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public LamportClock getC() {
		return c;
	}

	public void setC(LamportClock c) {
		this.c = c;
	}

	public LinkedList<Integer> getPendingQ() {
		return pendingQ;
	}

	public void setPendingQ(LinkedList<Integer> pendingQ) {
		this.pendingQ = pendingQ;
	}

	public Integer getNumOkay() {
		return numOkay;
	}

	public void setNumOkay(Integer numOkay) {
		this.numOkay = numOkay;
	}

	public CommMatrix getCommMatrix() {
		return commMatrix;
	}

	public void setCommMatrix(CommMatrix commMatrix) {
		this.commMatrix = commMatrix;
	}

	public boolean isTocken() {
		return tocken;
	}

	public void setTocken(boolean tocken) {
		this.tocken = tocken;
	}
}