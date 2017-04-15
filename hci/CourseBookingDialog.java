package hci;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lang.Lang;
import lang.Messages;
import manager.CourseManager;
import model.Course;
import utils.HCIUtils.Action;
import utils.HCIUtils;
import utils.MiscUtils;
import utils.TCGUtils;

public class CourseBookingDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7853549355551724576L;
	
	// STATIC FINAL FIELDS
	private static final int width = 400;
	private static final int height = 300;
	
	// STATIC FIELDS
	private static String dialogTitle = null;
	private static CourseBookingDialog instance = null;
	private static JButton buttonConfirm = null;
	private static JComboBox<Course> courseComboBox = null;
	
	// CONSTRUCTORS
	public CourseBookingDialog(){
		super(null, ModalityType.APPLICATION_MODAL);
		this.setIconImage(HCI.getInstance().getIconImage());
		this.setTitle(getDialogTitle());
		this.setSize(width, height);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel panelInput = new JPanel(new GridLayout(1, 1));
		JPanel panelConfirm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelConfirm.add(getButtonConfirm());
		JPanel panelCourse = new JPanel(new FlowLayout());
		panelCourse.add(new JLabel(HCIUtils.getFullTitle(Messages.getString(Lang.LABELS_SUBSCRIPTION_LABEL_COURSE))));
		panelCourse.add(getCourseComboBox());
		panelInput.add(panelCourse);
		mainPanel.add(panelInput, BorderLayout.CENTER);
		mainPanel.add(panelConfirm, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		this.setLocationRelativeTo(HCI.getInstance());
	}
	
	// STATIC METHODS
	private static CourseBookingDialog getInstance(){
		if(instance == null){
			instance = new CourseBookingDialog();
		}
		return instance;
	}
	
	private static final JComboBox<Course> getCourseComboBox(){
		if(courseComboBox == null){
			courseComboBox = new JComboBox<Course>();
		}
		return courseComboBox;
	}
	
	private static JButton getButtonConfirm(){
		if(buttonConfirm == null){
			buttonConfirm = new JButton(Messages.getString(Lang.CLICKABLES_SUBSCRIPTION_BUTTON_VALIDATION_SUBSCIPTION));
			buttonConfirm.setActionCommand(Action.ACTION_CONFIRM_BOOKING_COURSE.toString());
			buttonConfirm.addActionListener(HCI.getInstance());
		}
		return buttonConfirm;
	}
	
	private static String getDialogTitle(){
		if(dialogTitle == null){
			dialogTitle = Messages.getString(Lang.TITLE_SUBSCRIPTION_ADD_SUBSCRIPTION);
		}
		return dialogTitle;
	}
	
	public static void confirmBooking(){
		Course selectedCourse = (Course) getCourseComboBox().getSelectedItem();
		getInstance().dispose();
		TCGUtils.bookingCourse(selectedCourse);
	}
	
	public static void showDialog(){
		List<Course> courseList = CourseManager.getCourseList();
		if(MiscUtils.isNullOrEmpty(courseList)){
			JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_SUBSCRIPTION_ERROR_NO_COURSE_FOUND), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
			return;
		}
		getCourseComboBox().removeAllItems();
		courseList.forEach(course -> {
			getCourseComboBox().addItem(course);
		});
		getInstance().pack();
		getInstance().setVisible(true);
	}
	
	public static void disposeDialog(){
		getInstance().dispose();
	}
	
}
