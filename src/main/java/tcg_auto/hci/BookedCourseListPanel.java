package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

import tcg_auto.utils.HCIUtils;

public class BookedCourseListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	
	// STATIC FIELDS
	private static BookedCourseListPanel instance = null;
	
	// NOT STATIC FIELDS
	private JList<String> list;

	// CONSTRUCTORS
	public BookedCourseListPanel() {
		this.setLayout(new BorderLayout());
		this.list = new JList<String>();
		this.list.setFocusable(false);
		this.list.setEnabled(false);
		this.list.setForeground(Color.BLACK);
		this.add(BorderLayout.CENTER, this.list);
	}

	// NOT STATIC FIELDS
	public JList<String> getJList() {
		return list;
	}
	
	// STATIC METHODS
	public static BookedCourseListPanel getInstance() {
		if (instance == null)
			instance = new BookedCourseListPanel();
		return instance;
	}
	
	public static void updateBookedCourseList(List<String> bookedCourseList){
		HCIUtils.UpdateJList(getInstance().getJList(), bookedCourseList);
	}

}
