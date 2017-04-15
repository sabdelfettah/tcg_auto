package tcg_auto.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import hci.HCI;
import lang.Lang;
import lang.Messages;
import manager.ActionManager;
import manager.FileManager;
import manager.LogManager;
import model.Course;
import selenium.TCG;
import utils.TCGUtils.WebAction;

public abstract class HCIUtils {

	// STATIC FINAL FIELDS
	// IMAGES
	public static final String PATH_APPLICATION_ICON = "/images/app.png";
	public static final String PATH_LOADING_IMAGE = "/images/loading.gif";
	// FIELDS
	public static final String FIELD_LOGIN = "login";
	public static final String FIELD_PASSWORD = "password";
	
	// STATIC METHODS
	public static Map<String, String> getInputLoginAndPassword() {
		Map<String, String> result = new HashMap<String, String>();
		String login = getValueFromInputDialog(Messages.getString(Lang.TITLE_SET_LOGIN), Messages.getString(Lang.LABELS_SET_LOGIN_PASSWORD_LABEL_LOGIN), 20, false);
		if(MiscUtils.isNullOrEmpty(login)){
			return null;
		}
		String password = getValueFromInputDialog(Messages.getString(Lang.TITLE_SET_PASSWORD), Messages.getString(Lang.LABELS_SET_LOGIN_PASSWORD_LABEL_PASSWORD), 10, true);
		if(MiscUtils.isNullOrEmpty(password)){
			return null;
		}
		result.put(FIELD_LOGIN, login);
		result.put(FIELD_PASSWORD, password);
		return result;
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

	public static String getFullTitle(String title) {
		return title + " : ";
	}
	
	public static boolean getAndSaveLoginAndPassword() {
		Map<String, String> loginAndPassword = getInputLoginAndPassword();
		if(loginAndPassword == null){
			return false;
		}
		TCG.setLogin(loginAndPassword.get(HCIUtils.FIELD_LOGIN));
		TCG.setPassword(loginAndPassword.get(HCIUtils.FIELD_PASSWORD));
		TCG.setBaseUrl(TCGUtils.URL_HOME);
		FileManager.saveLoginAndPassword(loginAndPassword);
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_ACTION_GET_SAVE_LOGIN_PASSWORD));
		return true;
	}
	
	public static void showException(Exception e, boolean closeApplication){
		showException(e, closeApplication, false, null, null);
	}
	
	public static void showException(Exception e, boolean closeApplication, boolean logAsError, String logMessage){
		showException(e, closeApplication, logAsError, logMessage, null);
	}
	public static void showException(Exception e, boolean closeApplication, boolean logAsError, String logMessage, String optionalMessage){
		e.printStackTrace(System.err);
		String message = "";
		if(logMessage != null){
			if(optionalMessage == null){
				message = String.format(logMessage, e.getMessage());
			}else{
				message = String.format(logMessage, optionalMessage, e.getMessage());
			}
			if(logAsError){
				LogManager.logError(message);
			}else{
				LogManager.logWarn(message);
			}
		}
		message = String.format(closeApplication ? Messages.getString(Lang.MESSAGE_EXCEPTION_WITH_CLOSE) : Messages.getString(Lang.MESSAGE_EXCEPTION_WITHOUT_CLOSE), e.getMessage());
		JOptionPane.showMessageDialog(null, message, Messages.getString(Lang.TITLE_EXCEPTION_OCCURRED), JOptionPane.ERROR_MESSAGE);
		if(closeApplication){
			ActionManager.exit(false);
		}
	}
	
	public static void showApplication(){
		if(!HCI.getInstance().isVisible()){
			if(!HCI.getInstance().isInitialized()){
				ActionManager.initiliazeLists();
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
		ACTION_ADD_SUBSCRIPTION,
		ACTION_BOOKING_COURSE,
		ACTION_SEE_LOG,
		ACTION_OPEN_APPLICATION,
		ACTION_VALIDATE_SUBSCRIPTION,
		ACTION_CONFIRM_BOOKING_COURSE;
	}
	
}
