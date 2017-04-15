package hci;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;

import manager.CourseManager;
import model.Course;
import utils.HCIUtils;

public class BookedCourseListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	
	// STATIC FIELDS
	private static BookedCourseListPanel instance = null;
	
	// NOT STATIC FIELDS
	private JList<Course> list;

	// CONSTRUCTORS
	public BookedCourseListPanel() {
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
	public static BookedCourseListPanel getInstance() {
		if (instance == null)
			instance = new BookedCourseListPanel();
		return instance;
	}
	
	public static void updateBookedCourseList(){
		HCIUtils.UpdateJList(getInstance().getJList(), CourseManager.getCourseList());
	}

}
