/*
 * Ben Brouse
 * bjb85@drexel.edu
 * CS338:GUI, Final Project (Brewniverse)
 */

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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class BrewniverseMainUI {
	//All main panes used in the gui
	private static JFrame frame;
	private JPanel leftMainPane, rightMainPane, mainPane, rightCardPanes;
	private JPanel nameSearchPane, preferenceSearchPane, emptyListPanel, communicationPane;
	private JPanel beerInfoHolder;
	private JScrollPane scrollPane;
	private CardLayout beerCardLayout;
	
	//Ass swing elements used for I/O between gui and user
	private JButton nameSeachShowButton, preferenceSearchShowButton, viewSavedButton;
	private JButton nameSearchCancelButton, nameSearchButton, preferenceSearchButton, preferenceSearchCancelButton;
	private JTextField ibuCounter, abvCounter, nameSearchTextField;
	private JSlider bitternessOptionSlider, abvOptionSlider;
	private JComboBox styleOptionComboBox, srmOptionComboBox;
	private JCheckBox enableBitternessBox, enableAbvBox, enableStyleBox, enableSrmBox;
	
	private JLabel communicationLabel;
	
	//Custom panes used to show beer list as well as beer description
	private BeerListPanel beerListPane;
	private BeerInfoPanel beerInfoPane;
	
	//Beers currently shown in the UI
	private List<Beer> beers = new ArrayList<Beer>();
	private List<Beer> savedBeers;
	
	//Utility constructs to get and hold data from the BreweryDB api
	private SearchResult searchResults;
	private BrewApi brewApi;
	
	//Constant strings used in switching the card pane to different panes
	final static String BEERINFOPANEL = "Panel with Beer Info";
	final static String BEERLISTPANEL = "Panel with Beer List";
	final static String EMPTYLISTPANEL = "Panel with Empty List";
	
	//In an attempt to stick with a color pallette, kept colors constant
	//Pallette was generated at http://colorschemedesigner.com/
	final static Color COLORSCHEME1 = new Color(0x743311);
	final static Color COLORSCHEME2 = new Color(0xD9A184);
	final static Color COLORSCHEME3 = new Color(0xD98D65);
	final static Color COLORSCHEME4 = new Color(0xB25F33);
	final static Color COLORSCHEME5 = new Color(0x85573E);
	
	//Constant insets used in arranging items in the Beer list pane
	private final Insets WEST_INSETS = new Insets(10, 0, 10, 10);
	private final Insets EAST_INSETS = new Insets(10, 10, 10, 0);
	
	//Here's where all the magic happens (where all the main components are generated)
	public Component initComponents(){
		frame.addWindowListener(listener);
		brewApi = new BrewApi();
		searchResults = new SearchResult();
		savedBeers = brewApi.importSavedBeers(); //get all saved beers from file, if there is one
		
		//The left pane is the "control bar" containing the logo and three buttons (search options and view saved)
		leftMainPane = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		leftMainPane.setLayout(new GridBagLayout());
		leftMainPane.setPreferredSize(new Dimension(175,600));
		leftMainPane.setMinimumSize(new Dimension(150,600));
		leftMainPane.setMaximumSize(new Dimension(150,800));
		leftMainPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
		leftMainPane.setBackground(COLORSCHEME1);
		
		c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.NORTHWEST; c.insets = new Insets(10,5,10,5);
		java.net.URL brewniverseIcon = BrewniverseMainUI.class.getResource("images/brewniverse_logo.png");
		leftMainPane.add(new JLabel(new ImageIcon(brewniverseIcon)),c);
		
		//Create the button that shows the name search bar
		nameSeachShowButton = new JButton("<html><body><center>Search Beer<br>by Name</center></body></html>");
		nameSeachShowButton.setActionCommand("name search");
		nameSeachShowButton.setToolTipText("Search for a beer by name");
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
		
		//Create the button that shows the preference search bar
		preferenceSearchShowButton = new JButton("<html><body><center>Search Beer<br>by Preference</center></body></html>");
		preferenceSearchShowButton.setActionCommand("preference search");
		preferenceSearchShowButton.setToolTipText("Search for a beer based on personal preferences");
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
		
		//Create the button that brings up the list of saved beers
		viewSavedButton = new JButton("<html><body><center>View<br>Saved Beers</center></body></html>");
		viewSavedButton.setActionCommand("view saved");
		viewSavedButton.setToolTipText("View all of your saved beers");
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
					statusMessage("Currently Viewing: Saved Beers");
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			}
		});
		
		//Arrange the three main buttons into the left main pane
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(0,0,10,0); c.anchor = GridBagConstraints.SOUTH;
		leftMainPane.add(viewSavedButton, c);
		c.gridx = 0; c.gridy = 2; c.insets = new Insets(0,0,0,0);
		leftMainPane.add(nameSeachShowButton,c);
		c.gridx = 0; c.gridy = 3; c.insets = new Insets(10,0,0,0);
		leftMainPane.add(preferenceSearchShowButton,c);
		
		//Create the pane used when there are no beer results to display
		emptyListPanel = new JPanel(new GridBagLayout());
		emptyListPanel.setBackground(COLORSCHEME4);
		c.gridx = 0; c.gridy = 0;
		emptyListPanel.add(new JLabel("No Beers to Display"), c);
		c.gridx = 0; c.gridy = 1;
		java.net.URL bottlecapIcon = BrewniverseMainUI.class.getResource("images/bottle_caps.png");
		emptyListPanel.add(new JLabel(new ImageIcon(bottlecapIcon)), c);
		
		//Initialize the two main screens- beer list and beer info panes
		beerListPane = new BeerListPanel(new ArrayList<Beer>());
		beerInfoPane = new BeerInfoPanel(new Beer());
		
		//Card panes hold the three main views that take up the changing main pane (empty, list, info)
		rightCardPanes = new JPanel(new CardLayout());
		rightCardPanes.add(beerListPane, BEERLISTPANEL);
		rightCardPanes.add(beerInfoPane, BEERINFOPANEL);
		rightCardPanes.add(emptyListPanel, EMPTYLISTPANEL);
		beerCardLayout = (CardLayout)(rightCardPanes.getLayout());
	    beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL); //initially set to empty screen on launch
	    
	    /*
	     * Create the name search pane that holds a textbox (for the query string), a
	     * search button, as well as a cancel button.  The cancel button hides the search
	     * bar.  In addition, the enter key canbe sued to activate the search rather than
	     * pressing the search button.
	     */
	    nameSearchPane = new JPanel();
	    nameSearchPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    nameSearchPane.setVisible(false);
	    nameSearchPane.setBackground(COLORSCHEME5);
	    nameSearchTextField = new JTextField("",20);
	    nameSearchTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				nameSearchButton.doClick();
			}
		});
	    nameSearchPane.add(nameSearchTextField);
	    nameSearchButton = new JButton("Search");
	    nameSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				nameSearchPane.setVisible(false);
				searchResults = brewApi.searchBeersByName(nameSearchTextField.getText(), 1);
				if(searchResults.data.size() == 0){
					statusMessage("Found zero results for \"" + nameSearchTextField.getText() + "\"");
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
				}
				else{
					statusMessage("Found " + searchResults.totalResults + " results for \"" + nameSearchTextField.getText() + "\"");
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
	    
	    /*
	     * Here starts the process of constructing the preference search bar.  This bar holds
	     * four different search options, each in their own pane.  The bitterness option has a checkbox,
	     * slider, and textbox.  The ABV option has the same constructs. The style option has a
	     * combobox and a checkbox, and the color option has the same.  The combobox for color
	     * uses a custom renderer so that colors can be chosen rather than text.
	     */
	    preferenceSearchPane = new JPanel();
	    preferenceSearchPane.setLayout(new GridBagLayout());
	    preferenceSearchPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    preferenceSearchPane.setVisible(false);
	    preferenceSearchPane.setBackground(COLORSCHEME5);
	    
	    //Bitterness option
	    JPanel bitternessSearchPane = new JPanel(new GridBagLayout());
	    bitternessSearchPane.setToolTipText("Adjust your search based on your preference of bitterness");
	    bitternessSearchPane.setBorder(BorderFactory.createTitledBorder("Bitterness"));
	    bitternessSearchPane.setBackground(COLORSCHEME5);
	    enableBitternessBox = new JCheckBox();
	    enableBitternessBox.setActionCommand("bitterness");
	    enableBitternessBox.setToolTipText("Check to include bitterness with your search criteria");
	    enableBitternessBox.setSelected(true);
	    enableBitternessBox.addItemListener(new optionCheckBoxListener());
	    bitternessOptionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	    bitternessOptionSlider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	            ibuCounter.setText(((JSlider)ce.getSource()).getValue() + " IBUs");
	        }
	    });
	    ibuCounter = new JTextField("20 IBUs",5);
	    ibuCounter.setEditable(false);
	    ibuCounter.setToolTipText("Bitterness is measuer in IBUs (international bitterness units). The higher the IBU, the more bitter the beer");
	    
	    c.gridx=0; c.gridy=0; c.insets=new Insets(0,0,0,0); c.gridwidth=1; c.anchor=GridBagConstraints.CENTER;
	    bitternessSearchPane.add(enableBitternessBox,c);
	    c.gridx=1; c.gridy=0;
	    bitternessSearchPane.add(bitternessOptionSlider,c);
	    c.gridx=2; c.gridy=0;
	    bitternessSearchPane.add(ibuCounter,c);
	    c.gridx=0; c.gridy=0; c.gridwidth=3;
	    preferenceSearchPane.add(bitternessSearchPane,c);
	    
	    //ABV option
	    JPanel abvSearchPane = new JPanel(new GridBagLayout());
	    abvSearchPane.setToolTipText("Adjust your search based on your preference of alcohol by volume");
	    abvSearchPane.setBorder(BorderFactory.createTitledBorder("Alcohol by Volume"));
	    abvSearchPane.setBackground(COLORSCHEME5);
	    enableAbvBox = new JCheckBox();
	    enableAbvBox.setActionCommand("abv");
	    enableAbvBox.setToolTipText("Check to include alcohol by volume with your search criteria");
	    enableAbvBox.setSelected(true);
	    enableAbvBox.addItemListener(new optionCheckBoxListener());
	    abvOptionSlider = new JSlider(JSlider.HORIZONTAL, 0, 25, 4);
	    abvOptionSlider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	            abvCounter.setText(((JSlider)ce.getSource()).getValue() + " ABV");
	        }
	    });
	    abvCounter = new JTextField("4 ABV",4);
	    abvCounter.setEditable(false);
	    abvCounter.setToolTipText("Alcohol by Volume (ABV) is the amount of alcohol present in the beer.  The higher the ABV, the higher alcohol content");
	    
	    c.gridx=0; c.gridy=0; c.gridwidth=1; c.insets=new Insets(0,0,0,0);
	    abvSearchPane.add(enableAbvBox,c);
	    c.gridx=1; c.gridy=0;
	    abvSearchPane.add(abvOptionSlider,c);
	    c.gridx=2; c.gridy=0;
	    abvSearchPane.add(abvCounter,c);
	    c.gridx=0; c.gridy=1; c.gridwidth=3;
	    preferenceSearchPane.add(abvSearchPane,c);
	    
	    //Beer style option
	    JPanel styleSearchPane = new JPanel(new GridBagLayout());
	    styleSearchPane.setToolTipText("Adjust your search based on your preference of beer style");
	    styleSearchPane.setBorder(BorderFactory.createTitledBorder("Style"));
	    styleSearchPane.setBackground(COLORSCHEME5);
	    enableStyleBox = new JCheckBox();
	    enableStyleBox.setActionCommand("style");
	    enableStyleBox.setToolTipText("Check to include beer style with your search criteria");
	    enableStyleBox.setSelected(true);
	    enableStyleBox.addItemListener(new optionCheckBoxListener());
	    styleOptionComboBox = new JComboBox(brewApi.getAllStyles());
	    
	    c.gridx=0; c.gridy=0; c.gridwidth=1;
	    styleSearchPane.add(enableStyleBox,c);
	    c.gridx=1; c.gridy=0;
	    styleSearchPane.add(styleOptionComboBox,c);
	    c.gridx=0; c.gridy=2;c.gridwidth=2;
	    preferenceSearchPane.add(styleSearchPane,c);
	    
	    //Beer color option (with custom renderer in combobox
	    JPanel srmSearchPane = new JPanel(new GridBagLayout());
	    srmSearchPane.setToolTipText("Adjust your search based on your preference of beer color");
	    srmSearchPane.setBorder(BorderFactory.createTitledBorder("Color"));
	    srmSearchPane.setBackground(COLORSCHEME5);
	    enableSrmBox = new JCheckBox();
	    enableSrmBox.setActionCommand("color");
	    enableSrmBox.setToolTipText("Check to include beer color with your search criteria");
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
	    
	    c.gridx=0; c.gridy=0;c.gridwidth=1;
	    srmSearchPane.add(enableSrmBox,c);
	    c.gridx=1; c.gridy=0;
	    srmSearchPane.add(srmOptionComboBox,c);
	    c.gridx=2; c.gridy=2;
	    preferenceSearchPane.add(srmSearchPane,c);
	    
	    //Search and cancel buttons in the preference search bar
	    JPanel preferenceButtonPane = new JPanel(new GridBagLayout());
	    preferenceButtonPane.setBackground(COLORSCHEME5);
	    preferenceSearchButton = new JButton("Search");
	    preferenceSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				preferenceSearchPane.setVisible(false);
				BeerPreference beerPreference = getBeerPreferenceFromFields();
				searchResults = brewApi.searchBeersByPreference(beerPreference, 1);
				if(searchResults.data.size() == 0){
					statusMessage("Found zero results for personal preference");
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
				}
				else{
					statusMessage("Found " + searchResults.totalResults + " from personal preference");
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
	    
	    c.gridx = 0; c.gridy = 0; c.gridheight = 1;
	    preferenceButtonPane.add(preferenceSearchButton,c);
	    c.gridx = 1; c.gridy = 0; c.gridheight = 1;
	    preferenceButtonPane.add(preferenceSearchCancelButton,c);
	    c.gridx = 0; c.gridy = 3; c.gridheight = 1; c.gridwidth=3;
	    preferenceSearchPane.add(preferenceButtonPane,c);
	    c.gridwidth=1;
	    
	    /*
	     * This pane sits at the bottom of the main panes and sends feedback
	     * to the user (when a search is complete, what screen they're in, etc.
	     */
	    communicationPane = new JPanel();
	    communicationPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    communicationPane.setVisible(false);
	    communicationPane.setBackground(COLORSCHEME5);
	    communicationLabel = new JLabel("");
	    communicationLabel.setForeground(Color.white);
	    communicationPane.add(communicationLabel);
	    
	    //Add all main panes to the right main pane
	    rightMainPane = new JPanel();
	    rightMainPane.setLayout(new BoxLayout(rightMainPane, BoxLayout.Y_AXIS));
	    rightMainPane.setBorder(BorderFactory.createLineBorder(Color.black,1));
	    rightMainPane.add(nameSearchPane);
	    rightMainPane.add(preferenceSearchPane);
		rightMainPane.add(rightCardPanes);
		rightMainPane.add(communicationPane);
		
		//Consruct the main pane using the left and right portions
		mainPane = new JPanel();
		mainPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
		mainPane.setBackground(new Color(0xCC7D4C));
		mainPane.add(leftMainPane);
		mainPane.add(rightMainPane);
		return mainPane;
	}
	
	/*
	 * This method is used to extract a BeerPreference object from each of the
	 * fields in the preference search bar
	 */
	public BeerPreference getBeerPreferenceFromFields(){
		//Negative values used for when this option was not used
		int ibuMin = -1;
		int ibuMax = -1;
		int abvMin = -1;
		int abvMax = -1;
		BeerStyle style = null;
		SrmValue srm = null;
		
		//If they used bitterness, get the right range of IBUs
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
		
		//If they used ABV, get the right range of ABV
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
		
		//If style was used, grab the BeerStyle object
		if(styleOptionComboBox.isEnabled()){
			style = (BeerStyle) styleOptionComboBox.getSelectedItem();
		}
		
		//If color was used, grab the SrmValue object
		if(srmOptionComboBox.isEnabled()){
			srm = (SrmValue) srmOptionComboBox.getSelectedItem();
		}
		return new BeerPreference("", ibuMin, ibuMax, abvMin, abvMax, srm, style);
	}
	
	//Custom combobox renderer to show color blocks instead of simply text
	public class SrmComboBoxRenderer extends DefaultListCellRenderer{
		private static final long serialVersionUID = 1L;
		
		//Use the hex value from within each SrmValue to provide the color
		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
			JLabel lbl = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			super.setBackground(Color.decode("0x" + ((SrmValue)value).hex));
			super.setForeground(Color.decode("0x" + ((SrmValue)value).hex)); 
			return lbl;  
		}
	}
	
	//Listener enable and disable preference options with actions of the checkboxes
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
	
	/*
	 * This pane type is used to display the list of beers found in a search. It constructs
	 * a pane for each beer and displays that beer's label, as well as a button to show
	 * more information, and a button to add or remove from saved beers. This list is held in a 
	 * scroll pane so it can hold a large number of beers.
	 */
	public class BeerListPanel extends JPanel{
		
		private static final long serialVersionUID = 1L;

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
			refreshBeerPanels();//get beers into the list

			scrollPane = new JScrollPane(beerInfoHolder);
			scrollPane.setViewportView(beerInfoHolder);
			scrollPane.setBackground(COLORSCHEME4);
			beerInfoHolder.setAutoscrolls(true);
			scrollPane.getVerticalScrollBar().setUnitIncrement(10);
			
			/*
			 * This listener checks to see if the scroll bar is at the bottom of the page. If so,
			 * then the next page of reaults will automatically be added to the end of the list.
			 */
			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
				public void adjustmentValueChanged(AdjustmentEvent event) {
					JScrollBar scrollBar = (JScrollBar) event.getAdjustable();
			        int extent = scrollBar.getModel().getExtent();
			        if((scrollBar.getValue() + extent) == (scrollBar.getMaximum())){
			        	if(searchResults != null && searchResults.hasPagesLeft()){
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
			panelConstraint.fill = GridBagConstraints.BOTH;
			panelConstraint.anchor = GridBagConstraints.NORTH;
			panelConstraint.weightx = 1.0; panelConstraint.weighty = 1.0;
			
			//Go through each beer that came from the api and add it to the list in its own pane
			for(Beer b: beers){
				JPanel beerPanel = new JPanel(new GridBagLayout());
				beerPanel.setMaximumSize(new Dimension(999999,70));
				beerPanel.setMinimumSize(new Dimension(100,70));
				beerPanel.setBackground(COLORSCHEME4);
				JButton moreInfoButton = new JButton("View Info");
				listConstraint = createCorrectGBC(0,0);
				listConstraint.insets = new Insets(5,10,5,5);
				
				//If there is no label, then use the boring, default one I made
				if(b.labels == null){
					java.net.URL placeholderIcon = BrewniverseMainUI.class.getResource("images/default_label_icon.png");
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
				
				//Create the toggleSaved button depending whether or not that beer is currently saved
				JButton toggleSavedBeerButton;
				if(isBeerSaved(b)){
					java.net.URL removeSaveURL = BrewniverseMainUI.class.getResource("images/remove.png");
					toggleSavedBeerButton = new JButton(new ImageIcon(removeSaveURL));
					toggleSavedBeerButton.setToolTipText("Remove beer from saved list");
				}
				else{
					java.net.URL addSaveURL = BrewniverseMainUI.class.getResource("images/add.png");
					toggleSavedBeerButton = new JButton(new ImageIcon(addSaveURL));
					toggleSavedBeerButton.setToolTipText("Add beer to saved list");
				}
				toggleSavedBeerButton.setActionCommand(b.id);
				toggleSavedBeerButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Beer beer = getBeerFromListById(e.getActionCommand());
						if(isBeerSaved(beer)){
							removeSavedBeer(beer);
						}
						else{
							saveBeer(beer);
						}
						removeStatusMessage();
						searchResults = null;
						beerListPane.replaceBeers(savedBeers);
						beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
						beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
						statusMessage("Currently Viewing: Saved Beers");
					}
				});
				
				listConstraint = createCorrectGBC(1,0);
				listConstraint.anchor = GridBagConstraints.EAST;
				listConstraint.insets = new Insets(0,0,0,105);
				beerPanel.add(toggleSavedBeerButton, listConstraint);
				
				listConstraint = createCorrectGBC(1,0);
				moreInfoButton.setPreferredSize(moreInfoButton.getPreferredSize());
				moreInfoButton.setActionCommand(b.id);
				moreInfoButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						beerInfoPane.setBeer(getBeerFromListById(e.getActionCommand()));
						nameSearchPane.setVisible(false);
						removeStatusMessage();
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
	
	/*
	 * This pane is used to display all available information about a given beer.  The
	 * user can save the beer, or press the back button to get back to the beer list.
	 */
	public class BeerInfoPanel extends JPanel{
		private static final long serialVersionUID = 1L;
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
		
		//Once the beer is set, input all data into the information pane
		public void setBeer(Beer newBeer){
			GridBagConstraints c = new GridBagConstraints();
			beer = newBeer;
			beerInfoMainPane.removeAll();
			
			/*
			 * Panes in the screen separated into top, middle, bottom. Top holds
			 * the larger label image and the description text are.  Middle holds
			 * all of the other granular data (IBUs, ABV, availability, color, style).
			 * Bottom holds the save and back buttons.
			 */
			JPanel beerInfoTopPane = new JPanel();
			beerInfoTopPane.setLayout(new GridBagLayout());
			beerInfoTopPane.setBackground(COLORSCHEME4);
			JPanel beerInfoMiddlePane = new JPanel();
			beerInfoMiddlePane.setLayout(new BoxLayout(beerInfoMiddlePane, BoxLayout.Y_AXIS));
			beerInfoMiddlePane.setBorder(BorderFactory.createTitledBorder(beer.name + " Characteristics"));
			beerInfoMiddlePane.setBackground(COLORSCHEME4);
			JPanel beerInfoBottomPane = new JPanel(new GridBagLayout());
			beerInfoBottomPane.setBackground(COLORSCHEME4);
			
			//If a label for the beer exists, use it.  If not, use the boring one I made.
			JLabel image = null;
			if(beer.labels == null){
				java.net.URL placeholderMedium = BrewniverseMainUI.class.getResource("images/default_label_medium.png");
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
			
			String descriptionText = (beer.description == null) ? "No Description" : beer.description;
			JTextArea description = new JTextArea(descriptionText,15,25);
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			description.setEditable(false);
			description.setBackground(COLORSCHEME3);
			JScrollPane sp = new JScrollPane(description);
			sp.setBackground(COLORSCHEME4);
			c.gridx = 0; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 10; c.ipady = 20;
			beerInfoTopPane.add(image,c);
			c.gridx = 1; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 10; c.ipady = 20;
			beerInfoTopPane.add(sp,c);
			
			//If these data values exist for the beer, then add them to the middle pane
			if(beer.ibu != 0){
				JLabel ibuLabel = new JLabel("<html><b>IBUs:</b> " + Float.toString(beer.ibu) + " (" + beer.getIBUDescription() + ")</html>");
				ibuLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
				beerInfoMiddlePane.add(ibuLabel);
			}
			
			if(beer.abv != 0){
				JLabel abvLabel = new JLabel("<html><b>ABV: </b>" + Float.toString(beer.abv) + " (" + beer.getABVDescription() + ")</html>");
				abvLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
				beerInfoMiddlePane.add(abvLabel);
			}
			
			if(beer.available != null){
				JLabel availableLabel = new JLabel("<html><b>Availability: </b>" + beer.available.name + " (" + beer.available.description + ")</html>");
				availableLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
				beerInfoMiddlePane.add(availableLabel);
			}
		
			if(beer.style != null){
				JLabel styleLabel = new JLabel("<html><b>Style: </b>" + beer.style.name + "</html>");
				styleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
				beerInfoMiddlePane.add(styleLabel);
			}
			
			if(beer.srm != null){
				JLabel colorLabel = new JLabel("    Color  ");
				colorLabel.setOpaque(true);
				colorLabel.setBackground(Color.decode("0x" + beer.srm.hex));
				if(beer.srm.id > 15){
					colorLabel.setForeground(Color.white);
				}
				beerInfoMiddlePane.add(colorLabel,c);
			}
			
			//Start adding buttons to the bottom pane
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
					removeStatusMessage();
					searchResults = null;
					beerListPane.replaceBeers(savedBeers);
					beerCardLayout.show(rightCardPanes, EMPTYLISTPANEL);
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			});
			c.gridx = 0; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 15; c.fill = GridBagConstraints.NONE;
			beerInfoBottomPane.add(beerInfoSaveButton,c);
			
			JButton beerInfoBackButton = new JButton("Back");
			beerInfoBackButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					beerCardLayout.show(rightCardPanes, BEERLISTPANEL);
				}
			});
			
			//Set final layout of beer info screen (lots of GridBagConstraint magic).
			c.gridx = 1; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 15;
			beerInfoBottomPane.add(beerInfoBackButton,c);
			
			c.gridx = 0; c.gridy = 0; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 10; c.anchor = GridBagConstraints.NORTH;
			beerInfoMainPane.add(name,c);
			c.gridx = 0; c.gridy = 1; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 15; c.anchor = GridBagConstraints.NORTHWEST;
			beerInfoMainPane.add(beerInfoTopPane,c);
			c.gridx = 0; c.gridy = 2; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 15; c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.HORIZONTAL;
			beerInfoMainPane.add(beerInfoMiddlePane,c);
			c.gridx = 0; c.gridy = 3; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 5; c.ipady = 15; c.anchor = GridBagConstraints.SOUTHWEST; c.fill = GridBagConstraints.NONE;
			beerInfoMainPane.add(beerInfoBottomPane,c);
			System.out.println(beerInfoMiddlePane.getPreferredSize());
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
	
	public void statusMessage(String message){
		communicationLabel.setText(message);
		communicationPane.setVisible(true);
	}
	
	public void removeStatusMessage(){
		communicationPane.setVisible(false);
	}
	
	//Method used to arrange images and buttons in the beer list elements
	public GridBagConstraints createCorrectGBC(int x, int y) {
	      GridBagConstraints gbc = new GridBagConstraints();
	      gbc.gridx = x;
	      gbc.gridy = y;
	      gbc.gridwidth = 1;
	      gbc.gridheight = 1;

	      gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;

	      gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	      gbc.weightx = (x == 0) ? 0.1 : 1.0;
	      gbc.weighty = 1.0;
	      return gbc;
	}
	
	//When the window closes, make sure we export all the saved beers to a file
	private WindowAdapter listener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            brewApi.exportSavedBeers(savedBeers);
            System.exit(0);
        }
    };
	
    //The ALL POWERFUL MAIN
	public static void main(String args[]){
		frame = new JFrame("Brewniverse");
		BrewniverseMainUI brewniverseMainUI = new BrewniverseMainUI();
		Component contents = brewniverseMainUI.initComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
