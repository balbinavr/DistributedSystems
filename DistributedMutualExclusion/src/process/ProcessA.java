package process;
import java.util.ArrayList;
import java.util.List;

import baseclasses.Message;
import baseclasses.Message.Type;
import communication.CommMatrix;
import protocols.Centralized;


public class ProcessA extends Centralized{

	public final int ProcessLWA_length = 3;
	private List<ProcessLWA> children;
	private CommMatrix commMatrixChildren;
	private boolean q[];
	
	public ProcessA(int myId, CommMatrix commMatrix){
		
		super(myId, commMatrix, 0, 250);
	
        this.commMatrixChildren = new CommMatrix(ProcessLWA_length+1); 
        this.children = new ArrayList<ProcessLWA>();
        this.children.add(new ProcessLWA(1, ProcessLWA_length+1,  this.commMatrixChildren, 900, 0));
        this.children.add(new ProcessLWA(2, ProcessLWA_length+1, this.commMatrixChildren, 900, 250));
        this.children.add(new ProcessLWA(3, ProcessLWA_length+1, this.commMatrixChildren, 900, 500));   
        this.q = new boolean[ProcessLWA_length];
        for (int i=0; i<ProcessLWA_length;i++){
        	this.q[i] = false;
        }
        
        for (ProcessLWA process: this.children){
			process.start();
		}        
              
	}
	
	public boolean childrenFinished(){
		int j=0;
		for (int i=0; i< ProcessLWA_length; i++){
			if ((!commMatrixChildren.getMatrix()[i+1][0].isEmpty())){
				Message message = (Message)commMatrixChildren.getMatrix()[i+1][0].pop();
				if (message.getType().equals(Type.release)){
					this.setContent(this.getContent() + "Tinc release: "+ (i+1) +"\n");
					this.getTextArea().setText(this.getContent());
					q[i] = true;
				}	
			}
			if (q[i]) j++;
		}
		if (j == ProcessLWA_length){
			return true;
		}
		return false;
	}
	
	@Override
	public void run(){
		while (true){
	        for (int i=0; i<ProcessLWA_length;i++){
	        	this.q[i] = false;
	        }
			this.setContent(this.getContent() + "Envia A \n");
	        this.getTextArea().setText(this.getContent());
			requestCS();
			while (!haveTocken()){}
			this.setContent(this.getContent() + "Tinc tocken: "+ this.getMyId() +"\n");
			this.getTextArea().setText(this.getContent());
			for (ProcessLWA process: this.children){
				this.setContent(this.getContent() + "Envia tocken als fills: "+ process.getMyId() + "\n");
		        this.getTextArea().setText(this.getContent());
				process.tocken(process.getMyId());
			}
			while (!childrenFinished()){}
			this.setContent(this.getContent() + "Children A acabat!! \n");
			this.getTextArea().setText(this.getContent());
			releaseCS();
			try {
			    Thread.sleep(1000);                
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			//while(true){}
		}
	}
}
