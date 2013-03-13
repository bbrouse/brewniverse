import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class BrewniverseMainUI {
	private static JFrame frame;
	private JPanel leftMainPane, rightMainPane, mainPane, rightCardPanes, nameSearchPane, preferenceSearchPane, emptyListPanel;
	private BeerListPanel beerListPane;
	private BeerInfoPanel beerInfoPane;
	private JButton nameSeachShowButton, preferenceSearchShowButton, viewSavedButton;
	private JButton nameSearchCancelButton, nameSearchButton, preferenceSearchButton, preferenceSearchCancelButton;
	private JTextField nameSearchTextField;
	private List<Beer> beers = new ArrayList<Beer>();
	private List<Beer> savedBeers;
	private JScrollPane scrollPane;
	private JPanel beerInfoHolder;
	private CardLayout beerCardLayout;
	private SearchResult searchResults;
	private BrewApi brewApi;
	
	final static String BEERINFOPANEL = "Panel with Beer Info";
	final static String BEERLISTPANEL = "Panel with Beer List";
	final static String EMPTYLISTPANEL = "Panel with Empty List";
	
	final static Color COLORSCHEME1 = new Color(0x743311);
	final static Color COLORSCHEME2 = new Color(0xD9A184);
	final static Color COLORSCHEME3 = new Color(0xD98D65);
	final static Color COLORSCHEME4 = new Color(0xB25F33);
	final static Color COLORSCHEME5 = new Color(0x85573E);
	
	private final Insets WEST_INSETS = new Insets(10, 0, 10, 10);
	private final Insets EAST_INSETS = new Insets(10, 10, 10, 0);
	
	private JSlider bitternessOptionSlider, abvOptionSlider;
	private JComboBox styleOptionComboBox, srmOptionComboBox;
	private JCheckBox enableBitternessBox, enableAbvBox, enableStyleBox, enableSrmBox;
	
	public Component initComponents(){
		frame.addWindowListener(listener);
		brewApi = new BrewApi();
		BeerPreference pref = new BeerPreference("temp", 0, 33, 0, 20, null, null);
		searchResults = new SearchResult();
		savedBeers = brewApi.importSavedBeers();
		
		leftMainPane = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		leftMainPane.setLayout(new GridBagLayout());
		leftMainPane.setPreferredSize(new Dimension(175,600));
		leftMainPane.setMinimumSize(new Dimension(150,600));
		leftMainPane.setMaximumSize(new Dimension(150,800));
		leftMainPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
		leftMainPane.setBackground(COLORSCHEME1);
		
		nameSeachShowButton = new JButton("<html><body><center>Search Beer<br>by Name</center></body></html>");
		nameSeachShowButton.setActionCommand("name search");
		nameSeachShowButton.setToolTipText("");
		nameSeachShowButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(nameSearchPane.isVisible()){
					nameSearchPane.setVisible(false);
				}
				else{
					preferenceSearchPane.setVisible(false);
					nameSearchPane.setVisible(true);
				}
			}
		});
		
		preferenceSearchShowButton = new JButton("<html><body><center>Search Beer<br>by Preference</center></body></html>");
		preferenceSearchShowButton.setActionCommand("preference search");
		preferenceSearchShowButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(preferenceSearchPane.isVisible()){
					preferenceSearchPane.setVisible(false);
				}
				else{
					nameSearchPane.setVisible(false);
					preferenceSearchPane.setVisible(true);
				}
			}
		});
		preferenceSearchShowButton.setToolTipText("");
		
		viewSavedButton = new JButton("<html><body><center>View<br>Saved Beers</center></body></html>");
		viewSavedButton.setActionCommand("view saved");
		viewSavedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(savedBeers.size() == 0){
					nameSearchPane.setVisible(false);
					preferenceSearchPane.setVisible(false);
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
				}
				else{
					nameSearchPane.setVisible(false);
					preferenceSearchPane.setVisible(false);
					beerListPane.replaceBeers(savedBeers);
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			}
		});
		viewSavedButton.setToolTipText("");
		
		
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(0,0,10,0);
		leftMainPane.add(viewSavedButton, c);
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(0,0,0,0);
		leftMainPane.add(nameSeachShowButton,c);
		c.gridx = 0; c.gridy = 2; c.insets = new Insets(10,0,0,0);
		leftMainPane.add(preferenceSearchShowButton,c);
		//java.net.URL breweryDbIconURL = BrewniverseMainUI.class.getResource("images/Powered-By-BreweryDB.png");
		//leftMainPane.add(new JLabel(new ImageIcon(breweryDbIconURL)));
		
		emptyListPanel = new JPanel(new GridBagLayout());
		emptyListPanel.setBackground(COLORSCHEME4);
		c.gridx = 0; c.gridy = 0;
		emptyListPanel.add(new JLabel("No Beers to Display"), c);
		
		beerListPane = new BeerListPanel(new ArrayList<Beer>());
		beerInfoPane = new BeerInfoPanel(new Beer());
		rightCardPanes = new JPanel(new CardLayout());
		rightCardPanes.add(beerListPane, BEERLISTPANEL);
		rightCardPanes.add(beerInfoPane, BEERINFOPANEL);
		rightCardPanes.add(emptyListPanel, EMPTYLISTPANEL);
		beerCardLayout = (CardLayout)(rightCardPanes.getLayout());
	    beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
	    
	    nameSearchPane = new JPanel();
	    nameSearchPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    nameSearchPane.setVisible(false);
	    nameSearchPane.setBackground(COLORSCHEME5);
	    nameSearchTextField = new JTextField("",20);
	    nameSearchPane.add(nameSearchTextField);
	    nameSearchButton = new JButton("Search");
	    nameSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Searching for " + nameSearchTextField.getText());
				nameSearchPane.setVisible(false);
				searchResults = brewApi.searchBeersByName(nameSearchTextField.getText(), 1);
				if(searchResults.data.size() == 0){
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
				}
				else{
					beerListPane.replaceBeers(searchResults.data);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			}
		});
	    nameSearchPane.add(nameSearchButton);
	    nameSearchCancelButton = new JButton("Cancel");
	    nameSearchCancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				nameSearchPane.setVisible(false);
			}
		});
	    nameSearchPane.add(nameSearchCancelButton);
	    
	    preferenceSearchPane = new JPanel();
	    preferenceSearchPane.setLayout(new GridBagLayout());
	    preferenceSearchPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    preferenceSearchPane.setVisible(false);
	    preferenceSearchPane.setBackground(COLORSCHEME5);
	    
	    enableBitternessBox = new JCheckBox("Search by Bitterness");
	    enableBitternessBox.setActionCommand("bitterness");
	    enableBitternessBox.setSelected(true);
	    enableBitternessBox.addItemListener(new optionCheckBoxListener());
	    bitternessOptionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	    
	    GridBagConstraints c2 = createCorrectGBC(0,0);
	    preferenceSearchPane.add(enableBitternessBox,c2);
	    c2 = createCorrectGBC(1,0);
	    preferenceSearchPane.add(bitternessOptionSlider,c2);
	    
	    enableAbvBox = new JCheckBox("Search by ABV");
	    enableAbvBox.setActionCommand("abv");
	    enableAbvBox.setSelected(true);
	    enableAbvBox.addItemListener(new optionCheckBoxListener());
	    abvOptionSlider = new JSlider(JSlider.HORIZONTAL, 0, 25, 4);
	    
	    c2 = createCorrectGBC(0,1);
	    preferenceSearchPane.add(enableAbvBox,c2);
	    c2 = createCorrectGBC(1,1);
	    preferenceSearchPane.add(abvOptionSlider,c2);
	    
	    enableStyleBox = new JCheckBox("Search by Style");
	    enableStyleBox.setActionCommand("style");
	    enableStyleBox.setSelected(true);
	    enableStyleBox.addItemListener(new optionCheckBoxListener());
	    styleOptionComboBox = new JComboBox(brewApi.getAllStyles());
	    
	    c2 = createCorrectGBC(0,2);
	    preferenceSearchPane.add(enableStyleBox,c2);
	    c2 = createCorrectGBC(1,2);
	    preferenceSearchPane.add(styleOptionComboBox,c2);
	    
	    enableSrmBox = new JCheckBox("Search by Color");
	    enableSrmBox.setActionCommand("color");
	    enableSrmBox.setSelected(true);
	    enableSrmBox.addItemListener(new optionCheckBoxListener());
	    srmOptionComboBox = new JComboBox(brewApi.getAllSrmValues());
	    srmOptionComboBox.setRenderer(new SrmComboBoxRenderer());
	    srmOptionComboBox.setSelectedIndex(4);
    	srmOptionComboBox.setBackground(Color.decode("0x" + ((SrmValue)srmOptionComboBox.getSelectedItem()).hex));
    	srmOptionComboBox.setForeground(Color.decode("0x" + ((SrmValue)srmOptionComboBox.getSelectedItem()).hex));
	    srmOptionComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
	        	srmOptionComboBox.setBackground(Color.decode("0x" + ((SrmValue)srmOptionComboBox.getSelectedItem()).hex));
	        	srmOptionComboBox.setForeground(Color.decode("0x" + ((SrmValue)srmOptionComboBox.getSelectedItem()).hex));
			}
	    });
	    
	    c2 = createCorrectGBC(0,3);
	    preferenceSearchPane.add(enableSrmBox,c2);
	    c2 = createCorrectGBC(1,3);
	    preferenceSearchPane.add(srmOptionComboBox,c2);
	    
	    preferenceSearchButton = new JButton("Search");
	    preferenceSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				preferenceSearchPane.setVisible(false);
				BeerPreference beerPreference = getBeerPreferenceFromFields();
				searchResults = brewApi.searchBeersByPreference(beerPreference, 1);
				if(searchResults.data.size() == 0){
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
				}
				else{
					beerListPane.replaceBeers(searchResults.data);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			}
		});
	    
	    preferenceSearchCancelButton = new JButton("Cancel");
	    preferenceSearchCancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				preferenceSearchPane.setVisible(false);
			}
		});
	    
	    c2.gridx = 0; c2.gridy = 4; c2.gridheight = 0;
	    preferenceSearchPane.add(preferenceSearchButton,c2);
	    c2.gridx = 1; c2.gridy = 4; c2.gridheight = 0;
	    preferenceSearchPane.add(preferenceSearchCancelButton,c2);
		
	    rightMainPane = new JPanel();
	    rightMainPane.setLayout(new BoxLayout(rightMainPane, BoxLayout.Y_AXIS));
	    rightMainPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    rightMainPane.add(nameSearchPane);
	    rightMainPane.add(preferenceSearchPane);
		rightMainPane.add(rightCardPanes);
		
		mainPane = new JPanel();
		mainPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
		mainPane.setBackground(new Color(0xCC7D4C));
		mainPane.add(leftMainPane);
		mainPane.add(rightMainPane);
		return mainPane;
	}
	
	public BeerPreference getBeerPreferenceFromFields(){
		int ibuMin = -1;
		int ibuMax = -1;
		int abvMin = -1;
		int abvMax = -1;
		BeerStyle style = null;
		SrmValue srm = null;
		if(bitternessOptionSlider.isEnabled()){
			int ibuValue = bitternessOptionSlider.getValue();
			if(ibuValue > 5 && ibuValue < 95){
				ibuMin = ibuValue - 5;
				ibuMax = ibuValue + 5;
			}
			else if(ibuValue < 5){
				ibuMin = 0;
				ibuMax = ibuValue + 5;
			}
			else if(ibuValue > 95){
				ibuMin = ibuValue - 5;
				ibuMax = 100;
			}
		}
		
		if(abvOptionSlider.isEnabled()){
			int abvValue = abvOptionSlider.getValue();
			if(abvValue > 5 && abvValue < 95){
				abvMin = abvValue - 2;
				abvMax = abvValue + 2;
			}
			else if(abvValue < 5){
				abvMin = 0;
				abvMax = abvValue + 2;
			}
			else if(abvValue > 95){
				abvMin = abvValue - 2;
				abvMax = 100;
			}
		}
		
		if(styleOptionComboBox.isEnabled()){
			style = (BeerStyle) styleOptionComboBox.getSelectedItem();
		}
		
		if(srmOptionComboBox.isEnabled()){
			srm = (SrmValue) srmOptionComboBox.getSelectedItem();
		}
		return new BeerPreference("", ibuMin, ibuMax, abvMin, abvMax, srm, style);
	}
	
	public class SrmComboBoxRenderer extends DefaultListCellRenderer{
		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
			JLabel lbl = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			super.setBackground(Color.decode("0x" + ((SrmValue)value).hex));
			super.setForeground(Color.decode("0x" + ((SrmValue)value).hex)); 
			return lbl;  
		}
	}
	
	public class optionCheckBoxListener implements ItemListener{
		public void itemStateChanged(ItemEvent event) {
			JCheckBox item = (JCheckBox)event.getItem();
			if(item.getActionCommand().equals("bitterness")){
				bitternessOptionSlider.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
			}
			else if(item.getActionCommand().equals("abv")){
				abvOptionSlider.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
			}
			else if(item.getActionCommand().equals("style")){
				styleOptionComboBox.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
			}
			else if(item.getActionCommand().equals("color")){
				srmOptionComboBox.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
			}
		}
	}
	
	public class BeerListPanel extends JPanel{
		
		public BeerListPanel(List<Beer> inBeerList){
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setPreferredSize(new Dimension(650,400));
			beers = inBeerList;
			beerInfoHolder = new JPanel();
			beerInfoHolder.setBackground(COLORSCHEME4);
			beerInfoHolder.setLayout(new GridBagLayout());
			beerInfoHolder.setMaximumSize(new Dimension(999999,70));
			beerInfoHolder.setMinimumSize(new Dimension(100,70));
			refreshBeerPanels();

			scrollPane = new JScrollPane(beerInfoHolder);
			scrollPane.setViewportView(beerInfoHolder);
			scrollPane.setBackground(COLORSCHEME4);
			beerInfoHolder.setAutoscrolls(true);
			scrollPane.getVerticalScrollBar().setUnitIncrement(10);
			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
				public void adjustmentValueChanged(AdjustmentEvent event) {
					JScrollBar scrollBar = (JScrollBar) event.getAdjustable();
			        int extent = scrollBar.getModel().getExtent();
			        if((scrollBar.getValue() + extent) == (scrollBar.getMaximum())){
			        	if(searchResults.hasPagesLeft()){
			        		System.out.println("Searching now: " + searchResults.hasPagesLeft() + ", " +  searchResults.currentPage + ", " + searchResults.numberOfPages);
			        		searchResults = brewApi.getAdditionalResults(searchResults);
			        		addBeers(searchResults.data);
			        	}
			        }
				}
			});
			this.add(scrollPane);
		}
		
		public void addBeers(List<Beer> newBeers){
			beers.addAll(newBeers);
			refreshBeerPanels();
		}
		
		public void replaceBeers(List<Beer> newBeers){
			beers.clear();
			beers.addAll(newBeers);
			refreshBeerPanels();
		}
		
		private void refreshBeerPanels(){
			beerInfoHolder.removeAll();
			GridBagConstraints listConstraint = createCorrectGBC(0,0);
			GridBagConstraints panelConstraint = new GridBagConstraints();
			panelConstraint.fill = panelConstraint.BOTH;
			panelConstraint.anchor = panelConstraint.NORTH;
			panelConstraint.weightx = 1.0; panelConstraint.weighty = 1.0;
			for(Beer b: beers){
				JPanel beerPanel = new JPanel(new GridBagLayout());
				beerPanel.setMaximumSize(new Dimension(999999,70));
				beerPanel.setMinimumSize(new Dimension(100,70));
				beerPanel.setBackground(COLORSCHEME4);
				JButton moreInfoButton = new JButton("View Info");
				listConstraint = createCorrectGBC(0,0);
				listConstraint.insets = new Insets(5,10,5,5);
				if(b.labels == null){
					java.net.URL placeholderIcon = BrewniverseMainUI.class.getResource("images/placeholder_icon.png");
					beerPanel.add(new JLabel(b.name, new ImageIcon(placeholderIcon), JLabel.LEFT), listConstraint);
				}
				else{
					try {
						ImageIcon labelImage = new ImageIcon(Toolkit.getDefaultToolkit().getImage(new URL(b.labels.icon)));
						beerPanel.add(new JLabel(b.name, labelImage, JLabel.LEFT), listConstraint);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				listConstraint = createCorrectGBC(1,0);
				moreInfoButton.setPreferredSize(moreInfoButton.getPreferredSize());
				moreInfoButton.setActionCommand(b.id);
				moreInfoButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						beerInfoPane.setBeer(getBeerFromListById(e.getActionCommand()));
						nameSearchPane.setVisible(false);
						beerCardLayout.show(rightCardPanes, BEERINFOPANEL);
					}
				});
				beerPanel.add(moreInfoButton, listConstraint);
				beerPanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
				panelConstraint.gridy += 1;
				beerInfoHolder.add(beerPanel, panelConstraint);
			}
		}
	}
	
	public class BeerInfoPanel extends JPanel{
		private Beer beer;
		private JPanel beerInfoMainPane;
		private JScrollPane scrollPane;
		
		public BeerInfoPanel(Beer inBeer){
			super();
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(650,400));
			beer = inBeer;
			beerInfoMainPane = new JPanel();
			beerInfoMainPane.setLayout(new GridBagLayout());
			beerInfoMainPane.setBackground(COLORSCHEME4);
			setBeer(beer);
			scrollPane = new JScrollPane(beerInfoMainPane);
			scrollPane.setViewportView(beerInfoMainPane);
			scrollPane.setBackground(COLORSCHEME4);
			beerInfoMainPane.setAutoscrolls(true);
			scrollPane.getVerticalScrollBar().setUnitIncrement(10);
			this.add(scrollPane);
		}
		
		public void setBeer(Beer newBeer){
			GridBagConstraints c = new GridBagConstraints();
			beer = newBeer;
			beerInfoMainPane.removeAll();
			JLabel image = null;
			if(beer.labels == null){
				java.net.URL placeholderMedium = BrewniverseMainUI.class.getResource("images/placeholder_medium.png");
				image = new JLabel(new ImageIcon(placeholderMedium), JLabel.LEFT);
			}
			else{
				try {
					ImageIcon labelImage = new ImageIcon(Toolkit.getDefaultToolkit().getImage(new URL(beer.labels.medium)));
					image = new JLabel(labelImage, JLabel.LEFT);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			JLabel name = new JLabel(beer.name,JLabel.LEFT);
			name.setFont(new Font("Serif", Font.BOLD, 25));
			
			c.gridx = 0; c.gridy = 0; c.gridheight = 2; c.gridwidth = 1; c.anchor = c.NORTHWEST; c.ipadx = 10; c.ipady = 20;
			beerInfoMainPane.add(image,c);
			c.gridx = 1; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.anchor = c.NORTH; c.ipadx = 5; c.ipady = 10;
			beerInfoMainPane.add(name,c);
			
			c.gridx = 1; c.gridy = 1; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 5; c.ipadx = 10; c.anchor = c.WEST;
			String descriptionText = (beer.description == null) ? "No Description" : beer.description;
			JTextArea description = new JTextArea(descriptionText,15,25);
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			description.setEditable(false);
			description.setBackground(COLORSCHEME3);
			JScrollPane sp = new JScrollPane(description);
			beerInfoMainPane.add(sp, c);
			
			if(beer.ibu != 0){
				c.gridx = 0; c.gridy = 2; c.gridheight = 1; c.gridwidth = 2; c.ipady = 10; c.anchor = c.WEST;
				beerInfoMainPane.add(new JLabel("IBUs: " + Float.toString(beer.ibu) + " (" + beer.getIBUDescription() + ")"),c);
			}
			
			if(beer.abv != 0){
				c.gridx = 0; c.gridy = 3; c.gridheight = 1; c.gridwidth = 2; c.ipady = 10; c.anchor = c.WEST;
				beerInfoMainPane.add(new JLabel("ABV: " + Float.toString(beer.abv) + " (" + beer.getABVDescription() + ")"),c);
			}
		
			if(beer.style != null){
				c.gridx = 0; c.gridy = 4; c.gridheight = 1; c.gridwidth = 2; c.ipady = 10; c.anchor = c.WEST;
				beerInfoMainPane.add(new JLabel("Style: " + beer.style.name),c);
			}
			
			if(beer.available != null){
				c.gridx = 0; c.gridy = 5; c.gridheight = 1; c.gridwidth = 2; c.ipady = 10; c.anchor = c.WEST;
				beerInfoMainPane.add(new JLabel("Availability: " + beer.available.name + " (" + beer.available.description + ")"),c);
			}
			
			JButton beerInfoSaveButton = null;
			if(isBeerSaved(beer)){
				beerInfoSaveButton = new JButton("Remove from Saved Beers");
			}
			else{
				beerInfoSaveButton = new JButton("Save Beer");
			}
			beerInfoSaveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(isBeerSaved(beer)){
						removeSavedBeer(beer);
					}
					else{
						saveBeer(beer);
					}
					beerListPane.replaceBeers(savedBeers);
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			});
			c.gridx = 0; c.gridy = 6; c.gridheight = 2; c.gridwidth = 2; c.ipadx = 5; c.ipady = 15; c.anchor = c.SOUTHEAST;
			beerInfoMainPane.add(beerInfoSaveButton,c);
			JButton beerInfoBackButton = new JButton("Back");
			beerInfoBackButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			});
			c.gridx = 1; c.gridy = 6; c.gridheight = 2; c.gridwidth = 2; c.ipadx = 5; c.ipady = 15; c.anchor = c.SOUTHWEST;
			beerInfoMainPane.add(beerInfoBackButton,c);
		}
	}
	
	public Beer getBeerFromListById(String id){
		for(Beer b: beers){
			if(b.id.equals(id)){
				return b;
			}
		}
		return null;
	}
	
	public void removeSavedBeer(Beer beer){
		if(savedBeers.contains(beer)){
			savedBeers.remove(beer);
		}
	}
	
	public void saveBeer(Beer beer){
		savedBeers.add(beer);
	}
	
	public boolean isBeerSaved(Beer beer){
		if(beer.id != null){
			for(Beer b: savedBeers){
				if(beer.id.equals(b.id)){
					return true;
				}
			}
		}
		return false;
	}
	
	public GridBagConstraints createCorrectGBC(int x, int y) {
	      GridBagConstraints gbc = new GridBagConstraints();
	      gbc.gridx = x;
	      gbc.gridy = y;
	      gbc.gridwidth = 1;
	      gbc.gridheight = 1;

	      gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
	      //gbc.fill = (x == 0) ? GridBagConstraints.BOTH
	      //      : GridBagConstraints.HORIZONTAL;

	      gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	      gbc.weightx = (x == 0) ? 0.1 : 1.0;
	      gbc.weighty = 1.0;
	      return gbc;
	}
	
	private WindowAdapter listener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println("Exporting saved beers");
            brewApi.exportSavedBeers(savedBeers);
            System.exit(0);
        }
    };
	
	public static void main(String args[]){
		frame = new JFrame("Brewniverse");
		BrewniverseMainUI brewniverseMainUI = new BrewniverseMainUI();
		Component contents = brewniverseMainUI.initComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
