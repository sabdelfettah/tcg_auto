package tcg_auto.utils;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import tcg_auto.hci.HCI;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.manager.ActionManager;
import tcg_auto.manager.Initializator;
import tcg_auto.manager.LogManager;
import tcg_auto.manager.WebDriverManager;
import tcg_auto.model.Course;
import tcg_auto.selenium.TCG.WebAction;

public abstract class HCIUtils {

	// IMAGES
	public static final String PATH_APPLICATION_ICON = "tcg_auto/images/app.png";
	public static final String PATH_LOADING_IMAGE = "tcg_auto/images/loading.gif";
	public static final String PATH_PROPERTIES_FILE = "../../project.properties";
	
	// STATIC METHODS
	public static URL getUrlFromPath(String path){
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}
	
	public static <T> void UpdateJList(JList<T> listToUpdate, List<T> newList) {
		DefaultListModel<T> model = new DefaultListModel<T>();
		newList.forEach(element -> {
			model.addElement(element);
		});
		listToUpdate.setModel(model);
		listToUpdate.revalidate();
	}

	public static String getValueFromInputDialog(String title, String message, int size, boolean hidden) {
		String result = "";
		String[] options = { "OK" };
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel(getFullTitle(message));
		JTextField txt = null;
		if(hidden){
			txt = new JPasswordField(size);
		}else{
			txt = new JTextField(size);
		}
		panel.add(lbl);
		panel.add(txt);
		int selectedOption = JOptionPane.showOptionDialog(null, panel, title, JOptionPane.NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (selectedOption == 0) {
			result = txt.getText();
		}
		return result;
	}
	
	private static JFileChooser getWebDriverForWindowsOS(){
		JFileChooser result = new JFileChooser();
		result.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter windowsWebDriverFilterer = new WindowsFileFilter();
		result.setFileFilter(windowsWebDriverFilterer);
		result.setDialogTitle("Choose web driver file for Windows OS");
		return result;
	}
	
	public static String getWebDriverPath(){
		String result = null;
		JFileChooser fileChooser = getWebDriverForWindowsOS();
		int attempsLeft = 3;
		int choice = JFileChooser.CANCEL_OPTION;
		while(attempsLeft > 0 && choice != JFileChooser.APPROVE_OPTION){
			choice = fileChooser.showOpenDialog(null);
			attempsLeft--;
		}
		if(choice == JFileChooser.APPROVE_OPTION){
			result = fileChooser.getSelectedFile().getPath();
		}else if(!HCI.getInstance().isDataInitialized()){
			ActionManager.exit(false);
		}
		return result;
	}

	public static String getFullTitle(String title) {
		return title + " : ";
	}
	
	public static void showException(Exception e, boolean closeApplication){
		showException(e, closeApplication, false, null, null);
	}
	
	public static void showException(Exception e, boolean closeApplication, boolean logAsError, String logMessage){
		showException(e, closeApplication, logAsError, logMessage, null);
	}
	public static void showException(Exception e, boolean closeApplication, boolean logAsError, String logMessage, String optionalMessage){
		String exceptionMessage = e == null ? "?" : e.getMessage().replaceAll("\n", ", ");
		if(e != null){
			e.printStackTrace(System.err);
		}
		String message = "";
		if(logMessage != null){
			if(optionalMessage == null){
				message = String.format(logMessage, exceptionMessage);
			}else{
				message = String.format(logMessage, optionalMessage, exceptionMessage);
			}
			if(logAsError){
				LogManager.logError(message);
			}else{
				LogManager.logWarn(message);
			}
		}
		message = String.format(closeApplication ? Messages.getString(Lang.MESSAGE_EXCEPTION_WITH_CLOSE) : Messages.getString(Lang.MESSAGE_EXCEPTION_WITHOUT_CLOSE), exceptionMessage);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			private String message;
			
			public Runnable setMessage(String message){
				this.message = message;
				return this;
			}
			
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, message, Messages.getString(Lang.TITLE_EXCEPTION_OCCURRED), JOptionPane.ERROR_MESSAGE);
			}
		}.setMessage(message));
		if(closeApplication){
			ActionManager.exit(false);
		}
	}
	
	public static void showApplication(){
		if(!HCI.getInstance().isVisible()){
			if(!HCI.getInstance().isDataInitialized()){
				Initializator.initializeLists();
			}
			HCI.getInstance().setVisible(true);
		}
	}
	
	public static String getMessageOfWebAction(WebAction input){
		switch (input) {
		case ACTION_CONNECT: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CONNECT);
		case ACTION_SIGN_IN_LOGIN_PASSWORD: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_SIGN_IN_LOGIN_PASSWORD);
		case ACTION_CLOSE: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLOSE);
		case ACTION_CLICK_BOOKING: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_BOOKING);
		case ACTION_CLICK_ROOM_1: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_ROOM_1);
		case ACTION_CLICK_ROOM_2: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CLICK_ROOM_2);
		case ACTION_GET_COURSES_ROOM_1:
		case ACTION_GET_COURSES_ROOM_2: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_GET_COURSES);
		case ACTION_SELECT_COURSE: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_SELECT_COURSE);
		case ACTION_CONFIRM_BOOKING: return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_CONFIRM_BOOKING);
		case ACTION_GO_TO_MY_RESERVATIONS : return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_GO_TO_MY_RESERVATIONS);
		case ACTION_GET_MY_RESERVATIONS : return Messages.getString(Lang.MESSAGE_WAITING_DIALOG_WEBACTION_ACTION_GET_MY_RESERVATIONS);
		default: return "";
		}
	}
	
	public static String getLabelOfEnumDay(Course.Day day){
		switch(day){
		case MONDAY: return Messages.getString(Lang.ENUMS_ENUM_DAY_MONDAY);
		case THURSDAY: return Messages.getString(Lang.ENUMS_ENUM_DAY_THURSDAY);
		case WEDNESDAY: return Messages.getString(Lang.ENUMS_ENUM_DAY_WEDNESDAY);
		case TUESDAY: return Messages.getString(Lang.ENUMS_ENUM_DAY_TUESDAY);
		case FRIDAY: return Messages.getString(Lang.ENUMS_ENUM_DAY_FRIDAY);
		default: return "";
		}
	}
	
	// ENUMERATIONS
	public enum Action {
		ACTION_EXIT_APPLICATION,
		ACTION_SET_LOGIN_PASSWORD,
		ACTION_UPDATE_COURSES,
		ACTION_UPDATE_BOOKED_COURSES,
		ACTION_ADD_SUBSCRIPTION,
		ACTION_BOOKING_COURSE,
		ACTION_SEE_LOG,
		ACTION_SELECT_WEB_DRIVER_PATH,
		ACTION_SEE_ABOUT,
		ACTION_OPEN_APPLICATION,
		ACTION_VALIDATE_SUBSCRIPTION,
		ACTION_CONFIRM_BOOKING_COURSE;
	}
	
	// PRIVATE CLASSES
	private static class WindowsFileFilter extends javax.swing.filechooser.FileFilter{
		private static final String CHROME_DRIVER_FILTER = "chromedriver.exe";
		private static final String PHAONTOM_DRIVER_FILTER = "phantomjs.exe";
		private static final String CHROME_DRIVER_FILTER_DESCRIPTION = "ChromeDriver for Windows";
		private static final String PHAONTOM_DRIVER_FILTER_DESCRIPTION = "Phantom Driver for Windows";

		@Override
		public boolean accept(File f) {
			if(WebDriverManager.CHROME_MODE){
				return CHROME_DRIVER_FILTER.equals(f.getName().toLowerCase());
			}else{
				return PHAONTOM_DRIVER_FILTER.equals(f.getName().toLowerCase());
			}
		}

		@Override
		public String getDescription() {
			return WebDriverManager.CHROME_MODE ? CHROME_DRIVER_FILTER_DESCRIPTION : PHAONTOM_DRIVER_FILTER_DESCRIPTION;
		}
		
	}
	
}
