import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;


public class BrewniverseMainUI {
	private static JFrame frame;
	
	/*
	public Component initComponents(){
		
	}*/
	
	public static void main(String args[]){
		frame = new JFrame("Brewniverse");
		BrewniverseMainUI brewniverseMainUI = new BrewniverseMainUI();
		//Component contents = brewniverseMainUI.initComponents();
		//frame.getContentPane().add(contents, BorderLayout.CENTER);
		//frame.pack();
		//frame.setVisible(true);
		
		BrewApi brewApi = new BrewApi();
		brewApi.searchBeersByName("yuengling", 1);
	}

}
