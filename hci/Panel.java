package hci;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6685797312533829070L;
	
	// STATIC FIELDS
	private static Panel instance = null;

	// CONSTRUCTORS
	public Panel() {
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, ShowingPanel.getInstance());
		this.add(BorderLayout.EAST, ConfiguratingPanel.getInstance());
	}
	
	// STATIC METHODS
	public static Panel getInstance() {
		if (instance == null)
			instance = new Panel();
		return instance;
	}

}
