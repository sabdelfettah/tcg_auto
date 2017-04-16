package tcg_auto.hci;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.manager.CourseManager;
import tcg_auto.manager.SubscriptionManager;
import tcg_auto.model.Course;
import tcg_auto.model.Course.Day;
import tcg_auto.model.Subscription;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.HCIUtils.Action;
import tcg_auto.utils.MiscUtils;

public class SubsciptionDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7853549355551724576L;
	
	// STATIC FINAL FIELDS
	private static final int width = 400;
	private static final int height = 300;
	private static final int textFieldWidth = 30;
	private static final int textFieldHeight = 24;
	
	// STATIC FIELDS
	private static String dialogTitle = null;
	private static SubsciptionDialog instance = null;
	private static JTextField hourTextField = null;
	private static JTextField minuteTextField = null;
	private static JButton buttonConfirm = null;
	private static JComboBox<Course> courseComboBox = null;
	private static JComboBox<Day> dayComboBox = null;
	
	// CONSTRUCTORS
	public SubsciptionDialog(){
		super(null, ModalityType.APPLICATION_MODAL);
		this.setIconImage(HCI.getInstance().getIconImage());
		this.setTitle(getDialogTitle());
		this.setSize(width, height);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel panelInput = new JPanel(new GridLayout(4, 1));
		JPanel panelConfirm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelConfirm.add(getButtonConfirm());
		JPanel panelCourse = new JPanel(new FlowLayout());
		panelCourse.add(new JLabel(HCIUtils.getFullTitle(Messages.getString(Lang.LABELS_SUBSCRIPTION_LABEL_COURSE))));
		panelCourse.add(getCourseComboBox());
		JPanel panelDay = new JPanel(new FlowLayout());
		panelDay.add(new JLabel(HCIUtils.getFullTitle(Messages.getString(Lang.LABELS_SUBSCRIPTION_LABEL_DAY))));
		panelDay.add(getDayComboBox());
		JPanel panelHour = new JPanel(new FlowLayout());
		panelHour.add(new JLabel(HCIUtils.getFullTitle(Messages.getString(Lang.LABELS_SUBSCRIPTION_LABEL_HOUR))));
		panelHour.add(getHourTextField());
		JPanel panelMinute = new JPanel(new FlowLayout());
		panelMinute.add(new JLabel(HCIUtils.getFullTitle(Messages.getString(Lang.LABELS_SUBSCRIPTION_LABEL_MINUTE))));
		panelMinute.add(getMinuteTextField());
		panelInput.add(panelCourse);
		panelInput.add(panelDay);
		panelInput.add(panelHour);
		panelInput.add(panelMinute);
		mainPanel.add(panelInput, BorderLayout.CENTER);
		mainPanel.add(panelConfirm, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		this.setLocationRelativeTo(HCI.getInstance());
	}
	
	// STATIC METHODS
	private static SubsciptionDialog getInstance(){
		if(instance == null){
			instance = new SubsciptionDialog();
		}
		return instance;
	}
	
	private static final JComboBox<Course> getCourseComboBox(){
		if(courseComboBox == null){
			courseComboBox = new JComboBox<Course>();
		}
		return courseComboBox;
	}
	
	private static final JComboBox<Day> getDayComboBox(){
		if(dayComboBox == null){
			dayComboBox = new JComboBox<Day>();
			dayComboBox.addItem(Day.MONDAY);
			dayComboBox.addItem(Day.TUESDAY);
			dayComboBox.addItem(Day.WEDNESDAY);
			dayComboBox.addItem(Day.THURSDAY);
			dayComboBox.addItem(Day.FRIDAY);
		}
		return dayComboBox;
	}
	
	private static final JTextField getHourTextField(){
		if(hourTextField == null){
			hourTextField = new JTextField();
			Dimension size = new Dimension(textFieldWidth, textFieldHeight);
			hourTextField.setMinimumSize(size);
			hourTextField.setPreferredSize(size);
			hourTextField.setSize(size);
		}
		return hourTextField;
	}
	
	private static final JTextField getMinuteTextField(){
		if(minuteTextField == null){
			minuteTextField = new JTextField();
			Dimension size = new Dimension(textFieldWidth, textFieldHeight);
			minuteTextField.setMinimumSize(size);
			minuteTextField.setPreferredSize(size);
			minuteTextField.setSize(size);
		}
		return minuteTextField;
	}
	
	private static JButton getButtonConfirm(){
		if(buttonConfirm == null){
			buttonConfirm = new JButton(Messages.getString(Lang.CLICKABLES_SUBSCRIPTION_BUTTON_VALIDATION_SUBSCIPTION));
			buttonConfirm.setActionCommand(Action.ACTION_VALIDATE_SUBSCRIPTION.toString());
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
	
	public static void validateSubscrition(){
		short hour = -1;
		try{
			hour = Short.parseShort(getHourTextField().getText());
		}catch(NumberFormatException e){
			showErrorValidationHour();
			return;
		}
		if(hour < 0 || hour > 23){
			showErrorValidationHour();
			return;
		}
		short minute = -1;
		try{
			minute = Short.parseShort(getMinuteTextField().getText());
		}catch(NumberFormatException e){
			showErrorValidationMinute();
			return;
		}
		if(minute < 0 || minute > 59){
			showErrorValidationMinute();
			return;
		}
		Course selectedCourse = (Course) getCourseComboBox().getSelectedItem();
		Course.Day selectedDay = (Day) getDayComboBox().getSelectedItem();
		SubscriptionManager.addSubscriptionToSubscriptionList(new Subscription(selectedCourse, selectedDay, hour, minute));
		getInstance().dispose();
	}
	
	private static void showErrorValidationHour(){
		JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_SUBSCRIPTION_ERROR_VALIDATION_HOUR), getDialogTitle(), JOptionPane.ERROR_MESSAGE);
	}
	
	private static void showErrorValidationMinute(){
		JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_SUBSCRIPTION_ERROR_VALIDATION_MINUTE), getDialogTitle(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showDialog(){
		List<Course> notYetConfiguredCourseList = CourseManager.getNotYetConfiguredCourseList();
		if(MiscUtils.isNullOrEmpty(notYetConfiguredCourseList)){
			JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_SUBSCRIPTION_ERROR_NO_COURSE_FOUND), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
			return;
		}
		getCourseComboBox().removeAllItems();
		notYetConfiguredCourseList.forEach(course -> {
			getCourseComboBox().addItem(course);
		});
		getHourTextField().setText("10");
		getMinuteTextField().setText("00");
		getInstance().pack();
		getInstance().setVisible(true);
	}
	
	public static void disposeDialog(){
		getInstance().dispose();
	}
	
}
