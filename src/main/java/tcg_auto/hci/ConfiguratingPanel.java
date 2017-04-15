package tcg_auto.hci;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ConfiguratingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6032441833715783211L;
	
	// STATIC FIELDS
	private static ConfiguratingPanel instance = null;

	// CONSTRUCTORS
	public ConfiguratingPanel() {
		this.setLayout(new BorderLayout());
	}
	
	// STATIC METHODS
	public static ConfiguratingPanel getInstance() {
		if (instance == null)
			instance = new ConfiguratingPanel();
		return instance;
	}

}
