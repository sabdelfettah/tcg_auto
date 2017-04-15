package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;

import tcg_auto.manager.CourseManager;
import tcg_auto.model.Course;
import tcg_auto.utils.HCIUtils;

public class CourseListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	
	// STATIC FIELDS
	private static CourseListPanel instance = null;
	
	// NOT STATIC FIELDS
	private JList<Course> list;

	// CONSTRUCTORS
	public CourseListPanel() {
		this.setLayout(new BorderLayout());
		this.list = new JList<Course>();
		this.list.setFocusable(false);
		this.list.setEnabled(false);
		this.list.setForeground(Color.BLACK);
		this.add(BorderLayout.CENTER, this.list);
	}

	// NOT STATIC FIELDS
	public JList<Course> getJList() {
		return list;
	}
	
	// STATIC METHODS
	public static CourseListPanel getInstance() {
		if (instance == null)
			instance = new CourseListPanel();
		return instance;
	}
	
	public static void updateCourseList(){
		HCIUtils.UpdateJList(getInstance().getJList(), CourseManager.getCourseList());
	}

}
