package ui;
import javax.swing.*;        

public class Window {
	
	private String title;
	private int width;
	private int height;
	private String textLabel;
	
	public Window(String title, int width, int height, String text){
		this.title = title;
		this.width = width;
		this.height = height;
		this.textLabel = text;
	}
	
	public void show() {
        JFrame frame = new JFrame(this.title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(10, 10, this.width, this.height);
        JLabel label = new JLabel(this.textLabel);
        label.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(label);
        frame.setVisible(true);
    }

}