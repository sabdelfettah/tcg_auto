package tcg_auto.selenium;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import tcg_auto.hci.WaitingDialog;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.manager.LogManager;
import tcg_auto.manager.WebDriverManager;
import tcg_auto.model.Course;
import tcg_auto.model.PersistentWebElement;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.MiscUtils;
import tcg_auto.utils.TCGUtils;

public class TCG {
	
	// STATIC FINAL FIELDS
	public static final String ARGUMENT_COURSE = "ARG_COURSE";
	
	// STATIC FIELDS
	private static String baseUrl;
	private static String login;
	private static String password;
	@SuppressWarnings("rawtypes")
	private static List lastActionElemets;
	
	// NOT STATIC FIELDS
	private List<WebAction> webActionList;
	private Map<String, Object> arguments;

	// CONSTRUCTORS
	public TCG(List<WebAction> webActionList) {
		this.webActionList = webActionList;
	}
	
	public TCG(List<WebAction> webActionList, Map<String, Object> arguments) {
		this.webActionList = webActionList;
		this.arguments = arguments;
	}

	// NOT STATIC METHODS
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List> execute() {
		Map<String, List> result = new HashMap<String, List>();
		if (MiscUtils.isNotNullOrEmpty(webActionList)) {
			webActionList.forEach(webAction -> {
				WaitingDialog.setLabelText(webAction);
				List resultOfExecution = executeWebAction(webAction);
				result.put(webAction.name(), resultOfExecution);
				Boolean isWebElementList = TCGUtils.isWebElementList(resultOfExecution);
				if(isWebElementList != null && isWebElementList.booleanValue()){
					result.put(PersistentWebElement.getPersistentMapKey(webAction.name()), TCGUtils.getPersistentWebElementListFromWebElementList(resultOfExecution));
				}
				WaitingDialog.incrementProgressBarValue();
			});
		}
		WaitingDialog.incrementProgressBarValue();
		WebDriverManager.releaseWebDriver();
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	private List executeWebAction(WebAction actionToExecute) {
		while(!WebDriverManager.askForWebDriver(this)){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				HCIUtils.showException(e, false);
			}
		}
		LogManager.logInfoRunning(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_EXECUTING_WEB_ACTION), actionToExecute.name()));
		try{
			switch (actionToExecute) {
			case ACTION_CONNECT:
				lastActionElemets = connectToTCG();
				break;
			case ACTION_SIGN_IN_LOGIN_PASSWORD:
				lastActionElemets = enterLoginAndPassword();
				break;
			case ACTION_CLOSE:
				lastActionElemets = closeConnection();
				break;
			case ACTION_CLICK_BOOKING:
				lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_BOOKING_SPACE);
				break;
			case ACTION_CLICK_ROOM_1:
				lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_ROOM_1_SPACE);
				break;
			case ACTION_CLICK_ROOM_2:
				lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_ROOM_2_SPACE);
				break;
			case ACTION_GET_COURSES_ROOM_1:
			case ACTION_GET_COURSES_ROOM_2:
				lastActionElemets = getElements(TCGUtils.XPATH_DIVS_COURSES);
				break;
			case ACTION_SELECT_COURSE : 
				Course course = getCourseArgument();
				lastActionElemets = selectCourse(course);
				break;
			case ACTION_CONFIRM_BOOKING :
				lastActionElemets = confirmBooking();
				break;
			case ACTION_GO_TO_MY_RESERVATIONS :
				lastActionElemets = goToMyReservations();
				break;
			case ACTION_GET_MY_RESERVATIONS :
				lastActionElemets = getElements(TCGUtils.XPATH_TABLE_RESERVATIONS);
				break;
			default:
				LogManager.logErrorFinished(String.format(Messages.getString(Lang.LOG_MESSAGE_ERROR_EXECUTING_WEB_ACTION), actionToExecute.name(), "web action not recognized"));
				return (lastActionElemets = MiscUtils.getFalseAsList());
			}
		}catch(WebDriverException e){
			LogManager.logErrorFinished(String.format(Messages.getString(Lang.LOG_MESSAGE_ERROR_EXECUTING_WEB_ACTION), actionToExecute.name(), e.getMessage()));
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		LogManager.logInfoFinished(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_WEB_ACTION_EXECUTED_WITH_SUCCESS), actionToExecute.name()));
		return lastActionElemets;
	}
	
	private Object getArgument(String argument){
		if(arguments == null){
			return null;
		}
		return arguments.get(argument);
	}
	
	private Course getCourseArgument(){
		Object argument = getArgument(ARGUMENT_COURSE);
		if(argument == null || !(argument instanceof Course)){
			return null;
		}
		return (Course) argument;
	}

	// STATIC METHODS
	// GETTERS AND SETTERS
	public static void setBaseUrl(String baseUrl) {
		TCG.baseUrl = baseUrl;
	}

	public static void setLogin(String login) {
		TCG.login = login;
	}

	public static void setPassword(String password) {
		TCG.password = password;
	}
	
	// ACTION METHODS
	private static List<Boolean> connectToTCG() {
		WebDriverManager.getWebDriver().get(baseUrl);
		return MiscUtils.getTrueAsList();
	}
	
	private static List<Boolean> enterLoginAndPassword() {
		WebElement inputLogin = WebDriverManager.getWebDriver().findElement(By.xpath(TCGUtils.XPATH_INPUT_LOGIN));
		WebElement inputPassword = WebDriverManager.getWebDriver().findElement(By.xpath(TCGUtils.XPATH_INPUT_PASSWORD));
		WebElement buttonSubmit = WebDriverManager.getWebDriver().findElement(By.xpath(TCGUtils.XPATH_BUTTON_SUBMIT_LOGIN_PASSWORD));
		inputLogin.sendKeys(login);
		inputPassword.sendKeys(password);
		buttonSubmit.click();
		return MiscUtils.getTrueAsList();
	}

	private static List<Boolean> closeConnection() {
		WebDriverManager.getWebDriver().close();
		return MiscUtils.getTrueAsList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List selectCourse(Course course){
		if(course == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		List<WebElement> lastActionElemetsAsWebElementList = lastActionElemets;
		WebElement elementToClick = TCGUtils.getWebElementFilteredByCourse(lastActionElemetsAsWebElementList, course);
		if(elementToClick == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		Calendar nowCalendar = Calendar.getInstance();
		Calendar elementProgrammingCalendar =  TCGUtils.getCalendarFromElement(elementToClick);
		if(elementProgrammingCalendar.before(nowCalendar)){
			findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_NEXT_WEEK);
			lastActionElemets = getElements(TCGUtils.XPATH_DIVS_COURSES);
			lastActionElemetsAsWebElementList = lastActionElemets;
			elementToClick = TCGUtils.getWebElementFilteredByCourse(lastActionElemetsAsWebElementList, course);
		}
		if(elementToClick == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		elementToClick.click();
		waitJavaScriptLoading();
		List<WebElement> dialogElement = getDialogElement(TCGUtils.XPATHS_SIGN_IN_COURSE_DIALOG);
		return dialogElement;
	}
	
	@SuppressWarnings("rawtypes")
	private static List confirmBooking(){
		Boolean isLastElementsBoolean = TCGUtils.isBooleanList(lastActionElemets);
		if(isLastElementsBoolean != null && isLastElementsBoolean.booleanValue()){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		Boolean isLastElementsWebElement = TCGUtils.isWebElementList(lastActionElemets);
		if(isLastElementsWebElement != null && isLastElementsWebElement.booleanValue()){
			Object firstElement = lastActionElemets.get(0);
			WebElement elementToClick = (WebElement) firstElement;
			if(elementToClick.getTagName().equals("a")){
				elementToClick.click();
			}
			waitJavaScriptLoading();
		}
		return Arrays.asList(WebDriverManager.getWebDriver().getCurrentUrl().equals(TCGUtils.URL_BOOKING_SPACE));
	}
	
	@SuppressWarnings("rawtypes")
	private static List goToMyReservations(){
		WebDriverManager.getWebDriver().get(TCGUtils.URL_MY_RESERVATIONS);
		return MiscUtils.getTrueAsList();
	}
	
	// OTHER METHODS
	protected static List<Boolean> findButtonAndClick(String XPath) {
		WebElement button = WebDriverManager.getWebDriver().findElement(By.xpath(XPath));
		if(button == null){
			return MiscUtils.getFalseAsList();
		}
		button.click();
		return MiscUtils.getTrueAsList();
	}
	
	protected static List<WebElement> getElements(String XPath) {
		List<WebElement> result = WebDriverManager.getWebDriver().findElements(By.xpath(XPath));
		return result;
	}
	
	protected static List<WebElement> getDialogElement(String[] XPaths) {
		List<WebElement> result = null;
		int index = 0;
		while(MiscUtils.isNullOrEmpty(result) && index < XPaths.length){
			result = WebDriverManager.getWebDriver().findElements(By.xpath(XPaths[index]));
			index++;
		}
		if(result == null){
			return null;
		}
		return result;
	}
	
	protected static void waitJavaScriptLoading(){
		boolean finished = false;
		do{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				HCIUtils.showException(e, false);
			}
			List<WebElement> divLoadingElements = getElements(TCGUtils.XPATH_DIALOG_LOADING);
			if(MiscUtils.isNullOrEmpty(divLoadingElements)){
				return;
			}
			String style = divLoadingElements.iterator().next().getAttribute("style");
			finished = style.contains("display: none;");
		}while(!finished);
	}
	
	public static TCG getNewTCGInstance(List<WebAction> webActionList){
		TCG newInstance = new TCG(webActionList);
		return newInstance;
	}
	
	// ENUMERATIONS
	public enum WebAction {
		ACTION_CONNECT,
		ACTION_SIGN_IN_LOGIN_PASSWORD,
		ACTION_CLOSE,
		ACTION_CLICK_BOOKING,
		ACTION_CLICK_ROOM_1,
		ACTION_CLICK_ROOM_2,
		ACTION_GET_COURSES_ROOM_1,
		ACTION_GET_COURSES_ROOM_2,
		ACTION_SELECT_COURSE,
		ACTION_CONFIRM_BOOKING,
		ACTION_GO_TO_MY_RESERVATIONS,
		ACTION_GET_MY_RESERVATIONS;
	}
}
