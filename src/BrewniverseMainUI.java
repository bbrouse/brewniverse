import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;


public class BrewniverseMainUI {
	private static JFrame frame;
	
	public static void main(String args[]){
		frame = new JFrame("Brewniverse");
		BrewniverseMainUI mainUI = new BrewniverseMainUI();
		//Component contents = mainUI.initComponents();
		//frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
