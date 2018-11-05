package communication;

import java.util.Stack;

import baseclasses.Message;

public class CommMatrix{

	//Matriu on la primera columna es de lectura i la segona d'escriptura
	private Stack<Message>[][] matrix;
	private int matrixLength;
	
	@SuppressWarnings("unchecked")
	public CommMatrix(int num){
		this.matrix = new Stack[num][num];
		this.matrixLength = num;
		for (int i=0; i<num;i++){
			for (int j=0; j<matrixLength;j++){
				this.matrix[i][j] = new Stack<Message>();
			}
		}
	}
	
	public void sendMsg(Message msg){
		matrix[msg.getFrom()][msg.getTo()].push(msg);
	}

	public int getMatrixLength() {
		return matrixLength;
	}

	public void setMatrixLength(int matrixLength) {
		this.matrixLength = matrixLength;
	}

	public Stack<Message>[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(Stack<Message>[][] matrix) {
		this.matrix = matrix;
	}

	
	
}
