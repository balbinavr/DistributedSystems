package utils;

public class FrameUtils {

	public static String createUpdateFrame(int id, int value){
		return new String(id+":update:"+value);
	}
	
	public static String createGetFrame(int id, int value){
		return new String(id+":get:"+value);
	}
	
	public static String createGetFrame(int id){
		return new String(id+":get");
	}
	
	public static String createSetFrame(int id, int value){
		return new String(id+":set:"+value);		
	}
	
	public static int getIdFrame(String frame){
		String[] parts = frame.split(":", -1);
		return Integer.parseInt(parts[0]);
	}

	public static int getValueFrame(String frame){
		String[] parts = frame.split(":", -1);
		return Integer.parseInt(parts[2]);
	}
	
	public static String getTypeFrame(String frame){
		String[] parts = frame.split(":", -1);
		return parts[1];
	}

}
