import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


public class BeerListPanel extends JPanel{
	private List<Beer> beers = new ArrayList<Beer>();
	private int currentPageNumber;
	private int totalPageNumber;
	private int resultNumber;
	private JScrollPane scrollPane;
	private JList beerList;
	private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
	private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);
	
	public BeerListPanel(List<Beer> inBeerList){
		beers = inBeerList;
		beerList = new JList();
		beerList.setCellRenderer(new BeerListCellRenderer());
		refreshBeerPanels();
		
		beerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		beerList.setLayoutOrientation(JList.VERTICAL);
		beerList.setFixedCellHeight(70);

		scrollPane = new JScrollPane(beerList);
		this.add(scrollPane);
	}
	
	public void addBeers(List<Beer> newBeers){
		beers.addAll(newBeers);
		refreshBeerPanels();
	}
	
	private void refreshBeerPanels(){
		Vector<JPanel> allBeerPanels = new Vector<JPanel>();
		for(Beer b: beers){
			JPanel beerPanel = new JPanel(new GridBagLayout());
			GridBagConstraints c = createCorrectGBC(0,0);
			if(b.labels == null){
				beerPanel.add(new JLabel(b.name, JLabel.LEFT), c);
			}
			else{
				try {
					ImageIcon labelImage = new ImageIcon(Toolkit.getDefaultToolkit().getImage(new URL(b.labels.icon)));
					beerPanel.add(new JLabel(b.name, labelImage, JLabel.LEFT), c);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			beerPanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
			allBeerPanels.add(beerPanel);
		}
		beerList.setListData(allBeerPanels);
	}
	
	private GridBagConstraints createCorrectGBC(int x, int y) {
	      GridBagConstraints gbc = new GridBagConstraints();
	      gbc.gridx = x;
	      gbc.gridy = y;
	      gbc.gridwidth = 1;
	      gbc.gridheight = 1;

	      gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
	      gbc.fill = (x == 0) ? GridBagConstraints.BOTH
	            : GridBagConstraints.HORIZONTAL;

	      gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	      gbc.weightx = (x == 0) ? 0.1 : 1.0;
	      gbc.weighty = 1.0;
	      return gbc;
	   }
}

class BeerListCellRenderer implements ListCellRenderer
{
	public Component getListCellRendererComponent(JList jlist, Object value, int cellIndex, boolean isSelected, boolean cellHasFocus)
	{
		if (value instanceof JPanel)
		{
			Component component = (Component) value;
			component.setForeground (Color.white);
			component.setBackground (isSelected ? UIManager.getColor("Table.focusCellForeground") : Color.white);
			return component;
		}
		else
		{
			// TODO - I get one String here when the JList is first rendered; proper way to deal with this?
			//System.out.println("Got something besides a JPanel: " + value.getClass().getCanonicalName());
			return new JLabel("???");
		}
	}
}
