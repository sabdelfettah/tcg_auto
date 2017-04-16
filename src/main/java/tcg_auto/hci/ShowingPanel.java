package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;

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
		JPanel coursePanel = new JPanel(new GridLayout(1, 4));
		JPanel panelBookedCourseList = new JPanel(new BorderLayout());
		panelBookedCourseList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_BOOKED_COURSE_LIST)));
		panelBookedCourseList.add(BorderLayout.CENTER, BookedCourseListPanel.getInstance());
		JPanel panelSubscriptionList = new JPanel(new BorderLayout());
		panelSubscriptionList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_SUBSCRIPTION_LIST)));
		panelSubscriptionList.add(BorderLayout.CENTER, SubscriptionListPanel.getInstance());
		JPanel panelCourseList = new JPanel(new BorderLayout());
		panelCourseList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_COURSE_LIST)));
		panelCourseList.add(BorderLayout.CENTER, CourseListPanel.getInstance());
		JPanel panelCourseTaskList = new JPanel(new BorderLayout());
		panelCourseTaskList.add(BorderLayout.NORTH, new JLabel(Messages.getString(Lang.LABELS_COURSE_TASK_LIST)));
		panelCourseTaskList.add(BorderLayout.CENTER, CourseTaskListPanel.getInstance());
		// adding panels
		coursePanel.add(panelCourseList);
		coursePanel.add(panelSubscriptionList);
		coursePanel.add(panelCourseTaskList);
		coursePanel.add(panelBookedCourseList);
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
