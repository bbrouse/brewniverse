import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

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
		BeerPreference pref = new BeerPreference("temp", 0, 33, 0, 20, null, null);
		brewApi.searchBeersByPreference(pref, 1);
		List<Style> data = brewApi.getAllStyles();
		System.out.println(data.get(0).name);
	}

}
