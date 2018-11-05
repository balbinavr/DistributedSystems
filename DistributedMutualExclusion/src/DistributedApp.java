import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import communication.CommMatrix;
import process.ProcessA;
import process.ProcessB;
import protocols.Centralized;




public class DistributedApp {
	
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
                JFrame window = new JFrame();
                window.setTitle("Distributed App");
                window.setSize(400, 200);
                JTextArea textArea = new JTextArea(5, 20);              
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea); 
                window.setContentPane(scrollPane);
                window.setVisible(true);
                CommMatrix commMatrix = new CommMatrix(3);
                Centralized p0 = new Centralized(0, commMatrix, 450, 0);
                ProcessA pA = new ProcessA(1, commMatrix);
                ProcessB pB = new ProcessB(2, commMatrix);
                
                p0.start();
                pA.start();
                pB.start();
                
                textArea.setText("Inicialitzat");
                
                /*try {
					p0.join();
	                pA.join();
	                pB.join();              
				} catch (InterruptedException e) {
					System.out.println("Join principal processes error");
				} */
            }
        });
    }
    
}
