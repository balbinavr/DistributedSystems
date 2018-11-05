import java.io.*;

public class Client {

	private final static int numClient = 1;

	private BufferedReader bufferR;
	private FileReader fileR;

	public Client(int numClient) throws FileNotFoundException{

		File archivo = new File ("C:\\Users/usuario/workspace/ReplicacioEpidemicaClient/resources/Client"+numClient);
		fileR = new FileReader (archivo);
		bufferR = new BufferedReader(fileR);
		
	}

	public static void main(String[] args) {	
				try {					
					ActionManagement actionMan = new ActionManagement();
					//Obrir fitxer
					Client client = new Client(numClient);
					String action;
					//Mentres fitxer no buit llegir accio a realitzar
					while((action=client.getBufferR().readLine())!=null){
						System.out.println(action);
						String result = actionMan.start(action);
						//mostrar resultat per pantalla
						if ("Error".equals(result)){
							System.out.println("Malformat frame!");
						}else if (result != null){			
							System.out.println(result);
						}
					}
					if( null != client.getFileR() ){   
						client.getFileR().close();     
					}  		
				}
				catch(Exception e){
					e.printStackTrace();
				}
	}

	public BufferedReader getBufferR() {
		return bufferR;
	}

	public void setBufferR(BufferedReader bufferR) {
		this.bufferR = bufferR;
	}

	public FileReader getFileR() {
		return fileR;
	}

	public void setFileR(FileReader fileR) {
		this.fileR = fileR;
	}

}
