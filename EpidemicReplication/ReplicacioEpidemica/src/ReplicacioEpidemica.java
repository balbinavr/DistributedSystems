import Layers.CoreLayer;
import Layers.Layer;
import Layers.Layer1;
import Layers.Layer2;


public class ReplicacioEpidemica {
	
	private final static int socket = 40001;
	private final static int numNodes = 3;
	private final static int myId = 1;
	private final static int layer = 0;
	
	public static void main(String[] args) {
		Layer server = null;
		switch(layer){
		case 0:
			server = new CoreLayer(myId, socket, numNodes);
			break;
		case 1:
			server = new Layer1(myId, socket, numNodes);
			break;
		case 2:
			server = new Layer2(myId, socket, numNodes);
			break;
		default:
			System.out.println("Incorrect layer value");
			break;
		}
		if (server != null){
			server.start();
		}

	}
	 
	 

}
