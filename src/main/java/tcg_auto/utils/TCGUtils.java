package tcg_auto.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.model.Course;
import tcg_auto.model.Course.Day;
import tcg_auto.model.Course.Room;
import tcg_auto.model.PersistentWebElement;
import tcg_auto.selenium.TCG;
import tcg_auto.selenium.TCG.WebAction;

public abstract class TCGUtils {

	// STATIC FINAL FIELDS
	public static final String URL_HOME = "http://center.thecorporategym.com/citylights/";
	public static final String URL_BOOKING_SPACE = "https://citylights.thecorporategym.com/fr/poliwin/reservas";
	public static final String URL_MY_RESERVATIONS = "https://citylights.thecorporategym.com/fr/poliwin/misreservas";
	// XPATHS
	public static final String XPATH_INPUT_LOGIN = "//input[@name='myusername']";
	public static final String XPATH_INPUT_PASSWORD = "//input[@name='mypassword']";
	public static final String XPATH_BUTTON_SUBMIT_LOGIN_PASSWORD = "//input[@type='submit']";
	public static final String XPATH_BUTTON_GO_TO_BOOKING_SPACE = "//a[@href='/fr/poliwin/reservas']";
	public static final String XPATH_BUTTON_GO_TO_ROOM_1_SPACE = "//a[contains(text(), 'SALLE 1')]";
	public static final String XPATH_BUTTON_GO_TO_ROOM_2_SPACE = "//a[contains(text(), 'SALLE 2')]";
	public static final String XPATH_BUTTON_GO_TO_NEXT_WEEK = "//a[contains(text(), 'Suivant')]";
	public static final String XPATH_BUTTON_CONFIRM_BOOKING = "//a[contains(text(), 'Réserver')]";
	public static final String XPATH_DIALOG_LOADING = "//div[@id='loading']";
	public static final String XPATH_DIVS_COURSES = "//div[@class='groupclasse']";
	public static final String XPATH_TABLE_RESERVATIONS = "//table[@id='dades-reserva']";
	public static final String XPATH_TABLE_RESERVATIONS_TH = "//th[@class='text']";
	public static final String XPATH_TABLE_RESERVATIONS_TD = "//td[@class='text']";
	public static final String XPATH_SPAN_SIGN_IN_IMPOSSIBLE = "//span[contains(text(), 'Réservation impossible')]";
	public static final String[] XPATHS_SIGN_IN_COURSE_DIALOG = {XPATH_SPAN_SIGN_IN_IMPOSSIBLE, XPATH_BUTTON_CONFIRM_BOOKING};
	// WEBACTION LISTS
	public static final List<WebAction> ACTION_GET_ROOM_1_COURSE_LIST = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_1,
			WebAction.ACTION_GET_COURSES_ROOM_1
			);
	public static final List<WebAction> ACTION_GET_ROOM_2_COURSE_LIST = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_2,
			WebAction.ACTION_GET_COURSES_ROOM_2
			);
	public static final List<WebAction> ACTION_GET_FULL_COURSE_LIST = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_1,
			WebAction.ACTION_GET_COURSES_ROOM_1,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_2,
			WebAction.ACTION_GET_COURSES_ROOM_2
			);
	public static final List<WebAction> ACTION_GET_BOOKED_COURSE_LIST = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_GO_TO_MY_RESERVATIONS,
			WebAction.ACTION_GET_MY_RESERVATIONS
			);
	public static final List<WebAction> ACTION_GET_FULL_LISTS = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_1,
			WebAction.ACTION_GET_COURSES_ROOM_1,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_2,
			WebAction.ACTION_GET_COURSES_ROOM_2,
			WebAction.ACTION_GO_TO_MY_RESERVATIONS,
			WebAction.ACTION_GET_MY_RESERVATIONS
			);
	public static final List<WebAction> ACTION_BOOKING_COURSE_ROOM_1 = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_1,
			WebAction.ACTION_GET_COURSES_ROOM_1,
			WebAction.ACTION_SELECT_COURSE,
			WebAction.ACTION_CONFIRM_BOOKING
			);
	public static final List<WebAction> ACTION_BOOKING_COURSE_ROOM_2 = Arrays.asList(
			WebAction.ACTION_CONNECT,
			WebAction.ACTION_SIGN_IN_LOGIN_PASSWORD,
			WebAction.ACTION_CLICK_BOOKING,
			WebAction.ACTION_CLICK_ROOM_2,
			WebAction.ACTION_GET_COURSES_ROOM_2,
			WebAction.ACTION_SELECT_COURSE,
			WebAction.ACTION_CONFIRM_BOOKING
			);
	
	// STATIC METHODS
	public static WebElement getParentElement(WebElement child){
		if(child == null){
			return null;
		}
		return child.findElement(By.xpath("./.."));
	}
	
	public static PersistentWebElement getPersistentWebElementFromWebElement(WebElement input, boolean withParentElement){
		PersistentWebElement output = new PersistentWebElement();
		output.setTagName(input.getTagName());
		output.setText(input.getText());
		Map<String, String> attributes = new HashMap<String, String>();
		for(String attributeToCopy : PersistentWebElement.ATTRIBUTES_TO_COPY){
			attributes.put(attributeToCopy, input.getAttribute(attributeToCopy));
		}
		output.setAttributes(attributes);
		if(withParentElement){
			output.setPersistentParentElement(getPersistentWebElementFromWebElement(getParentElement(input), false));
		}
		return output;
	}
	
	public static PersistentWebElement getPersistentWebElementFromWebElement(WebElement input){
		return getPersistentWebElementFromWebElement(input, true);
	}
	
	public static List<PersistentWebElement> getPersistentWebElementListFromWebElementList(List<WebElement> webElementList){
		List<PersistentWebElement> result = new ArrayList<PersistentWebElement>();
		if(MiscUtils.isNotNullOrEmpty(webElementList)){
			webElementList	.parallelStream()
							.filter(webElement -> webElement != null)
							.forEach(webElement -> {
								result.add(getPersistentWebElementFromWebElement(webElement));
							});
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static Boolean isWebElementList(List inputList){
		if(MiscUtils.isNullOrEmpty(inputList)){
			return null;
		}
		Object firstInstance = inputList.iterator().next();
		return new Boolean(firstInstance instanceof WebElement);
	}
	
	@SuppressWarnings("rawtypes")
	public static Boolean isBooleanList(List inputList){
		if(MiscUtils.isNullOrEmpty(inputList)){
			return null;
		}
		Object firstInstance = inputList.iterator().next();
		return new Boolean(firstInstance instanceof Boolean);
	}
	
	private static String getParentOnClickAttribute(PersistentWebElement course){
		PersistentWebElement parentCourse = course.getPersistentParentElement();
		if(parentCourse == null){
			return null;
		}
		String onclickAttribute = parentCourse.getAttribute("onclick");
		return onclickAttribute;
	}
	
	private static String getParentOnClickAttribute(WebElement course){
		WebElement parentCourse = getParentElement(course);
		if(parentCourse == null){
			return null;
		}
		String onclickAttribute = parentCourse.getAttribute("onclick");
		return onclickAttribute;
	}
	
	private static String[] parseOnClickValue(String value){
		String onclickAttribute = value.split(Pattern.quote("("))[1];
		onclickAttribute = onclickAttribute.split(Pattern.quote(")"))[0];
		String[] result = onclickAttribute.replaceAll(" ", "").replaceAll("'", "").split(",");
		return result;
	}
	
	private static Calendar getCalendarFromDateOptions(String[] dateOptions){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateOptions[1]));
		calendar.set(Calendar.MONTH, Integer.parseInt(dateOptions[2]) - 1);
		calendar.set(Calendar.YEAR, Integer.parseInt(dateOptions[3]));
		return calendar;
	}
	
	public static Calendar getCalendarFromElement(WebElement course){
		String onclickAttribute = getParentOnClickAttribute(course);
		if(onclickAttribute == null){
			return null;
		}
		String[] dateOptions = parseOnClickValue(onclickAttribute);
		Calendar result = getCalendarFromDateOptions(dateOptions);
		return result;
	}
	
	public static Day getDayFromPersistentWebElement(PersistentWebElement course){
		String onclickAttribute = getParentOnClickAttribute(course);
		if(onclickAttribute == null){
			return null;
		}
		String[] dateOptions = parseOnClickValue(onclickAttribute);
		Calendar calendar = getCalendarFromDateOptions(dateOptions);
		return Day.getDayFromCalendarDay(calendar.get(Calendar.DAY_OF_WEEK));
	}
	
	public static short[] getTimeOptionsFromPersistentWebElement(PersistentWebElement course){
		String onclickAttribute = getParentOnClickAttribute(course);
		if(onclickAttribute == null){
			return null;
		}
		String[] dateOptions = parseOnClickValue(onclickAttribute);
		String timeOptions = dateOptions != null && dateOptions.length > 3 ? dateOptions[4] : "0000";
		short hour = Short.parseShort(timeOptions.substring(0, 2));
		short minute = Short.parseShort(timeOptions.substring(2));
		short[] result = {hour, minute};
		return result;
	}
	
	public static WebElement getWebElementFilteredByTextContent(List<WebElement> webElements, String textContent){
		if(MiscUtils.isNullOrEmpty(webElements) || MiscUtils.isNullOrEmpty(textContent)){
			return null;
		}
		WebElement result = webElements	.parallelStream()
										.filter(webElement -> webElement.getText().contains(textContent))
										.findFirst()
										.orElse(null);
		return result;
	}
	
	public static WebElement getWebElementFilteredByCourse(List<WebElement> webElements, Course course){
		if(MiscUtils.isNullOrEmpty(webElements) || course == null){
			return null;
		}
		List<WebElement> resultsByTextContentFilter = webElements	.parallelStream()
																	.filter(webElement -> webElement.getText().contains(course.getName()))
																	.collect(Collectors.toList());
		WebElement result = null;
		if(resultsByTextContentFilter.size() == 1){
			result = resultsByTextContentFilter.iterator().next();
		}else{
			result = resultsByTextContentFilter	.parallelStream()
												.filter(resultByTextContentFilter -> course.getDay().equals(Day.getDayFromCalendarDay(getCalendarFromElement(resultByTextContentFilter).get(Calendar.DAY_OF_WEEK))))
												.findFirst()
												.orElse(null);
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static void bookingCourse(Course course) {
		List<WebAction> actionsToExecute = Room.ROOM_1.equals(course.getRoom()) ? TCGUtils.ACTION_BOOKING_COURSE_ROOM_1 : TCGUtils.ACTION_BOOKING_COURSE_ROOM_2;
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put(TCG.ARGUMENT_COURSE, course);
		Map<String, List> resultMap = MiscUtils.getListElementMap(actionsToExecute, arguments);
		List result = resultMap.get(WebAction.ACTION_CONFIRM_BOOKING.name());
		Object firstElement = result.iterator().next();
		Boolean finalResult = (Boolean) firstElement;
		if(finalResult != null && finalResult.booleanValue()){
			JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_BOOKING_INFO_BOOKING_SUCCESS), Messages.getString(Lang.TITLE_COURSE_BOOKING), JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, Messages.getString(Lang.MESSAGE_BOOKING_ERROR_BOOKING), Messages.getString(Lang.TITLE_COURSE_BOOKING), JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
