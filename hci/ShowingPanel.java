package hci;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lang.Lang;
import lang.Messages;

public class ShowingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 862729093831886462L;
	
	// STATIC FIELDS
	private static ShowingPanel instance = null;

	// CONSTRUCTORS
	public ShowingPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel coursePanel = new JPanel(new GridLayout(1, 3));
		JPanel panelBookedCourseList = new JPanel(new BorderLayout());
		panelBookedCourseList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_BOOKED_COURSE_LIST)));
		panelBookedCourseList.add(BorderLayout.CENTER, BookedCourseListPanel.getInstance());
		JPanel panelSubscriptionList = new JPanel(new BorderLayout());
		panelSubscriptionList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_SUBSCRIPTION_LIST)));
		panelSubscriptionList.add(BorderLayout.CENTER, SubscriptionListPanel.getInstance());
		JPanel panelCourseList = new JPanel(new BorderLayout());
		panelCourseList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_COURSE_LIST)));
		panelCourseList.add(BorderLayout.CENTER, CourseListPanel.getInstance());
		coursePanel.add(panelBookedCourseList);
		coursePanel.add(panelSubscriptionList);
		coursePanel.add(panelCourseList);
		this.add(coursePanel);
		this.add(LogPanel.getInstance());
	}
	
	// STATIC METHODS
	public static ShowingPanel getInstance() {
		if (instance == null)
			instance = new ShowingPanel();
		return instance;
	}

}
