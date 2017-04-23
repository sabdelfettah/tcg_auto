package tcg_auto.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tcg_auto.hci.BookedCourseListPanel;
import tcg_auto.hci.CourseBookingDialog;
import tcg_auto.hci.CourseListPanel;
import tcg_auto.hci.HCI;
import tcg_auto.hci.LogPanel;
import tcg_auto.hci.MainMenuBar;
import tcg_auto.hci.SubsciptionDialog;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.model.Course;
import tcg_auto.model.PersistentWebElement;
import tcg_auto.selenium.TCG;
import tcg_auto.selenium.TCG.WebAction;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.HCIUtils.Action;
import tcg_auto.utils.MiscUtils;
import tcg_auto.utils.TCGUtils;

public abstract class ActionManager {
	
	private static JCheckBox seeLogButtonInstance = null;

	// STATIC METHODS
	public static JCheckBox getSeeLogButtonInstance(){
		if(seeLogButtonInstance == null){
			seeLogButtonInstance = (JCheckBox) MainMenuBar.getMenuBarComponent(Messages.getString(Lang.MENU_ITEM_OPTIONS_SEE_LOG));
		}
		return seeLogButtonInstance;
	}
	
	public static void executeAction(String action) throws IOException, Exception {
		Action actionToExecute = Action.valueOf(action);
		switch (actionToExecute) {
		case ACTION_EXIT_APPLICATION:
			exit(true);
			break;
		case ACTION_SET_LOGIN_PASSWORD:
			LoginPasswordManager.getAndSaveLoginAndPassword();
			break;
		case ACTION_UPDATE_COURSES:
			updateCourseList();
			break;
		case ACTION_BOOKING_COURSE:
			CourseBookingDialog.showDialog();
			break;
		case ACTION_SEE_LOG:
			boolean isSeeLogsSelected = getSeeLogButtonInstance().isSelected();
			LogPanel.setPanelVisible(isSeeLogsSelected);
			ConfigManager.setSeeLogs(isSeeLogsSelected);
			break;
		case ACTION_OPEN_APPLICATION:
			HCIUtils.showApplication();
			break;
		case ACTION_ADD_SUBSCRIPTION:
			addSubscription();
			break;
		case ACTION_VALIDATE_SUBSCRIPTION:
			SubsciptionDialog.validateSubscrition();
			ConfigManager.saveConfig();
			break;
		case ACTION_CONFIRM_BOOKING_COURSE:
			CourseBookingDialog.confirmBooking();
			break;
		case ACTION_UPDATE_BOOKED_COURSES:
			updateBookedCourseList();
			break;
		case ACTION_SEE_ABOUT :
			Package.getPackages();
			String message = String.format("%s v%s\n%s", Messages.getString(Lang.APPLICATION_NAME), HCI.VERSION, "https://github.com/sabdelfettah/tcg_auto/releases");
			JOptionPane.showMessageDialog(null, message, Messages.getString(Lang.MENU_ITEM_HELP_ABOUT), JOptionPane.INFORMATION_MESSAGE);
			break;
		case ACTION_SELECT_WEB_DRIVER_PATH:
			ConfigManager.getAndSaveWebDrive();
			break;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static void updateCourseList() {
		Map<String, List> executionResults = MiscUtils.getListElementMap(TCGUtils.ACTION_GET_FULL_COURSE_LIST);
		updateCourseList(executionResults);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void updateCourseList(Map<String, List> executionResults) {
		LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_ACTION_UPDATING_COURSE_LIST));
		List<PersistentWebElement> listToUpdate = executionResults.get(PersistentWebElement.getPersistentMapKey(WebAction.ACTION_GET_COURSES_ROOM_1.name()));
		int numberOfTotalCourses = listToUpdate == null ? 0 : listToUpdate.size();
		CourseManager.initCourseList();
		if(MiscUtils.isNotNullOrEmpty(listToUpdate)){
			listToUpdate.forEach(course -> {
				short[] timeOptions = TCGUtils.getTimeOptionsFromPersistentWebElement(course);
				CourseManager.addCourseToCourseList(new Course(MiscUtils.removeRoomInLabel(course.getText()), Course.Room.ROOM_1, TCGUtils.getDayFromPersistentWebElement(course), timeOptions));
			});
		}
		listToUpdate = executionResults.get(PersistentWebElement.getPersistentMapKey(WebAction.ACTION_GET_COURSES_ROOM_2.name()));
		numberOfTotalCourses += listToUpdate == null ? 0 : listToUpdate.size();
		if(MiscUtils.isNotNullOrEmpty(listToUpdate)){
			listToUpdate.forEach(course -> {
				short[] timeOptions = TCGUtils.getTimeOptionsFromPersistentWebElement(course);
				CourseManager.addCourseToCourseList(new Course(MiscUtils.removeRoomInLabel(course.getText()), Course.Room.ROOM_2, TCGUtils.getDayFromPersistentWebElement(course), timeOptions));
			});
		}
		CourseManager.sortCourseList();
		CourseListPanel.updateCourseList();
		HCI.getInstance().repaint();
		LogManager.logInfoFinished(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_ACTION_UPDATE_COURSE_LIST_SUCCESS), numberOfTotalCourses));
	}
	
	@SuppressWarnings("rawtypes")
	protected static void updateBookedCourseList(){
		Map<String, List> executionResults = MiscUtils.getListElementMap(TCGUtils.ACTION_GET_BOOKED_COURSE_LIST);
		updateBookedCourseList(executionResults);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void updateBookedCourseList(Map<String, List> executionResults){
		List<String> bookedCourseList = new ArrayList<String>();
		List<WebElement> listToUpdate = executionResults.get(WebAction.ACTION_GET_MY_RESERVATIONS.name());
		for(WebElement webElement : listToUpdate){
			WebElement thElement = webElement.findElement(By.xpath(TCGUtils.XPATH_TABLE_RESERVATIONS_TH)); 
			List<WebElement> tdElements = webElement.findElements(By.xpath(TCGUtils.XPATH_TABLE_RESERVATIONS_TD)); 
			bookedCourseList.add(
					String.format("%s - %s", 	thElement.getText(),
												com.google.common.base.Joiner.on(" - ").join(
																								tdElements	.parallelStream()
																											.map(element -> element.getText())
																											.collect(Collectors.toList())
																								)));
		}
		BookedCourseListPanel.updateBookedCourseList(bookedCourseList);
	}
	
	public static void addSubscription(){
		SubsciptionDialog.showDialog();
	}

	public static void exit(boolean normalExit){
		if(normalExit){
			LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_EXIT_APP));
		}else{
			LogManager.logError(Messages.getString(Lang.LOG_MESSAGE_ERROR_EXIT_APP));
		}
		if(WebDriverManager.isDriverInitialized()){
			try {
				TCG.getNewTCGInstance(Arrays.asList(WebAction.ACTION_CLOSE)).execute();
			} catch (Exception e) {
				HCIUtils.showException(e, false);
			}
		}
		System.exit(0);
	}

}
