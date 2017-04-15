package selenium;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import hci.WaitingDialog;
import model.Course;
import model.PersistentWebElement;
import utils.HCIUtils;
import utils.MiscUtils;
import utils.TCGUtils;
import utils.TCGUtils.WebAction;

public class TCG {
	
	// STATIC FINAL FIELDS
	public static final String ARGUMENT_COURSE = "ARG_COURSE";
	
	// STATIC FIELDS
	private static String baseUrl;
	private static String login;
	private static String password;
	private static WebDriver driverInstance;
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
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	private List executeWebAction(WebAction actionToExecute) {
		try{
			switch (actionToExecute) {
			case ACTION_CONNECT:
				return (lastActionElemets = connectToTCG());
			case ACTION_SIGN_IN_LOGIN_PASSWORD:
				return (lastActionElemets = enterLoginAndPassword());
			case ACTION_CLOSE:
				return (lastActionElemets = closeConnection());
			case ACTION_CLICK_BOOKING:
				return (lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_BOOKING_SPACE));
			case ACTION_CLICK_ROOM_1:
				return (lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_ROOM_1_SPACE));
			case ACTION_CLICK_ROOM_2:
				return (lastActionElemets = findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_ROOM_2_SPACE));
			case ACTION_GET_COURSES_ROOM_1:
			case ACTION_GET_COURSES_ROOM_2:
				return (lastActionElemets = getElements(TCGUtils.XPATH_DIVS_COURSES));
			case ACTION_SELECT_COURSE : 
				Course course = getCourseArgument();
				return (lastActionElemets = selectCourse(course));
			case ACTION_CONFIRM_BOOKING :
				return (lastActionElemets = confirmBooking());
			default:
				return (lastActionElemets = MiscUtils.getFalseAsList());
			}
		}catch(WebDriverException e){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
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
	public static void setBaseUrl(String baseUrl) {
		TCG.baseUrl = baseUrl;
	}

	public static void setLogin(String login) {
		TCG.login = login;
	}

	public static void setPassword(String password) {
		TCG.password = password;
	}
	
	public static WebDriver getWebDriver(){
		if(driverInstance == null){
			try {
				String current = new java.io.File( "." ).getCanonicalPath();
				System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, current + "/chromedriver.exe");
				System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, current + "/chromedriver.log");
				//System.setProperty(ChromeDriverService.CHROME_DRIVER_VERBOSE_LOG_PROPERTY, "true");
				//System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
				//ChromeOptions chromeOptions= new ChromeOptions();
				//chromeOptions.addArguments("--no-startup-window");
				//chromeOptions.addArguments("--silent-launch");
				//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				//capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				driverInstance = new ChromeDriver();
			} catch (IOException e) {
				HCIUtils.showException(e, true);
			}
		}
		return driverInstance;
	}
	
	public static List<Boolean> connectToTCG() {
		getWebDriver().get(baseUrl);
		return MiscUtils.getTrueAsList();
	}
	
	public static List<Boolean> enterLoginAndPassword() {
		WebElement inputLogin = getWebDriver().findElement(By.xpath(TCGUtils.XPATH_INPUT_LOGIN));
		WebElement inputPassword = getWebDriver().findElement(By.xpath(TCGUtils.XPATH_INPUT_PASSWORD));
		WebElement buttonSubmit = getWebDriver().findElement(By.xpath(TCGUtils.XPATH_BUTTON_SUBMIT_LOGIN_PASSWORD));
		inputLogin.sendKeys(login);
		inputPassword.sendKeys(password);
		buttonSubmit.click();
		return MiscUtils.getTrueAsList();
	}

	public static List<Boolean> closeConnection() {
		getWebDriver().close();
		return MiscUtils.getTrueAsList();
	}

	public static List<Boolean> findButtonAndClick(String XPath) {
		WebElement button = getWebDriver().findElement(By.xpath(XPath));
		if(button == null){
			return MiscUtils.getFalseAsList();
		}
		button.click();
		return MiscUtils.getTrueAsList();
	}

	public static List<WebElement> getElements(String XPath) {
		List<WebElement> result = getWebDriver().findElements(By.xpath(XPath));
		return result;
	}
	
	public static List<WebElement> getDialogElement(String[] XPaths) {
		List<WebElement> result = null;
		int index = 0;
		while(MiscUtils.isNullOrEmpty(result) && index < XPaths.length){
			result = getWebDriver().findElements(By.xpath(XPaths[index]));
			index++;
		}
		if(result == null){
			return null;
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List selectCourse(Course course){
		if(course == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		List<WebElement> lastActionElemetsAsWebElementList = lastActionElemets;
		WebElement elementToClick = TCGUtils.getWebElementFilteredByTextContent(lastActionElemetsAsWebElementList, course.getName());
		if(elementToClick == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		Calendar nowCalendar = Calendar.getInstance();
		Calendar elementProgrammingCalendar =  TCGUtils.getCalendarFromElement(elementToClick);
		if(elementProgrammingCalendar.before(nowCalendar)){
			findButtonAndClick(TCGUtils.XPATH_BUTTON_GO_TO_NEXT_WEEK);
			lastActionElemets = getElements(TCGUtils.XPATH_DIVS_COURSES);
			lastActionElemetsAsWebElementList = lastActionElemets;
			elementToClick = TCGUtils.getWebElementFilteredByTextContent(lastActionElemetsAsWebElementList, course.getName());
		}
		if(elementToClick == null){
			return (lastActionElemets = MiscUtils.getFalseAsList());
		}
		elementToClick.click();
		int attemps = 1;
		List<WebElement> dialogElement = getDialogElement(TCGUtils.XPATHS_SIGN_IN_COURSE_DIALOG);
		System.out.println(MiscUtils.getValueOfObject(dialogElement) + " ; " + attemps);
		while(MiscUtils.isNullOrEmpty(dialogElement) && attemps < 5){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return null;
			}
			dialogElement = getDialogElement(TCGUtils.XPATHS_SIGN_IN_COURSE_DIALOG);
			attemps++;
			System.out.println(MiscUtils.getValueOfObject(dialogElement) + " ; " + attemps);
		}
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
		}
		return Arrays.asList(getWebDriver().getCurrentUrl().equals(TCGUtils.URL_BOOKING_SPACE));
	}
	
	public static TCG getNewTCGInstance(List<WebAction> webActionList){
		TCG newInstance = new TCG(webActionList);
		return newInstance;
	}
	
	public static boolean isDriverInitialized(){
		return driverInstance != null;
	}
}
