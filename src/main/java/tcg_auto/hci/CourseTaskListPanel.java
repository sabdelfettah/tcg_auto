package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.JPanel;

import tcg_auto.manager.SubscriptionManager;
import tcg_auto.manager.SubscriptionManager.CourseTask;
import tcg_auto.utils.HCIUtils;

public class CourseTaskListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	// STATIC FIELDS
	private static CourseTaskListPanel instance = null;
	
	// NOT STATIC FIELDS
	private JList<CourseTask> list;

	// CONSTRUCTORS
	public CourseTaskListPanel() {
		this.setLayout(new BorderLayout());
		this.list = new JList<CourseTask>();
		this.list.setFocusable(false);
		this.list.setEnabled(false);
		this.list.setForeground(Color.BLACK);
		this.add(BorderLayout.CENTER, this.list);
	}

	// STATIC METHODS
	public static CourseTaskListPanel getInstance() {
		if (instance == null)
			instance = new CourseTaskListPanel();
		return instance;
	}
	
	public static void updateCourseTaskList(){
		HCIUtils.UpdateJList(getInstance().list, SubscriptionManager.getCourseTasks());
	}

}
