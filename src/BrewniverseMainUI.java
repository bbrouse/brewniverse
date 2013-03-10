import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class BrewniverseMainUI {
	private static JFrame frame;
	private JPanel leftMainPane, rightMainPane, mainPane;
	private JButton nameSeachButton, preferenceSearchButton;
	
	public Component initComponents(){
		leftMainPane = new JPanel();
		leftMainPane.setMaximumSize(new Dimension(200,600));
		leftMainPane.setPreferredSize(new Dimension(200,600));
		leftMainPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
		
		nameSeachButton = new JButton("<html><body><center>Search Beer<br>by Name</center></body></html>");
		nameSeachButton.setActionCommand("name search");
		//nameSeachButton.addActionListener(new MainPanelButtonListener());
		nameSeachButton.setToolTipText("");
		
		preferenceSearchButton = new JButton("<html><body><center>Search Beer<br>by Preference</center></body></html>");
		preferenceSearchButton.setActionCommand("preference search");
		//nameSeachButton.addActionListener(new MainPanelButtonListener());
		preferenceSearchButton.setToolTipText("");
		
		leftMainPane.add(nameSeachButton);
		leftMainPane.add(preferenceSearchButton);
		
		BrewApi brewApi = new BrewApi();
		BeerPreference pref = new BeerPreference("temp", 0, 33, 0, 20, null, null);
		SearchResult results = brewApi.searchBeersByPreference(pref, 1);
		
		BeerListPanel beerListPanel = new BeerListPanel(results.data);
		rightMainPane = beerListPanel;
		
		mainPane = new JPanel();
		mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
		mainPane.add(leftMainPane);
		mainPane.add(rightMainPane);
		return mainPane;
	}
	
	public static void main(String args[]){
		frame = new JFrame("Brewniverse");
		BrewniverseMainUI brewniverseMainUI = new BrewniverseMainUI();
		Component contents = brewniverseMainUI.initComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
