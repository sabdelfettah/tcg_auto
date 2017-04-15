package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;

import tcg_auto.manager.SubscriptionManager;
import tcg_auto.model.Subscription;
import tcg_auto.utils.HCIUtils;

public class SubscriptionListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	// STATIC FIELDS
	private static SubscriptionListPanel instance = null;
	
	// NOT STATIC FIELDS
	private JList<Subscription> list;

	// CONSTRUCTORS
	public SubscriptionListPanel() {
		this.setLayout(new BorderLayout());
		this.list = new JList<Subscription>();
		this.list.setFocusable(false);
		this.list.setEnabled(false);
		this.list.setForeground(Color.BLACK);
		this.add(BorderLayout.CENTER, this.list);
	}

	// STATIC METHODS
	public static SubscriptionListPanel getInstance() {
		if (instance == null)
			instance = new SubscriptionListPanel();
		return instance;
	}
	
	public static void updateSubscriptionList(){
		HCIUtils.UpdateJList(getInstance().list, SubscriptionManager.getSubscriptionList());
	}

}
