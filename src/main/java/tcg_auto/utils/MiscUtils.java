package tcg_auto.utils;

import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tcg_auto.hci.WaitingDialog;
import tcg_auto.model.ActionWorker;
import tcg_auto.model.Subscription;
import tcg_auto.selenium.TCG.WebAction;

public abstract class MiscUtils {
	
	// STATIC FINAL FIELDS
	private static final Gson gsonInstance = new Gson();
	private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// STATIC METHODS
	public static List<BufferedImage> getImageOfElements(WebDriver driver, List<WebElement> webElements) {
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = null;
		try {
			fullImg = ImageIO.read(screenshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fullImg == null) {
			return result;
		}
		for (WebElement webElement : webElements) {
			Point point = webElement.getLocation();
			int elementWidth = webElement.getSize().getWidth();
			int elementHeight = webElement.getSize().getHeight();
			BufferedImage elementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), elementWidth,
					elementHeight);
			result.add(elementScreenshot);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(List input) {
		return input == null || input.isEmpty();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotNullOrEmpty(List input) {
		return !isNullOrEmpty(input);
	}
	
	public static boolean isNullOrEmpty(String input) {
		return input == null || input.isEmpty();
	}

	public static boolean isNotNullOrEmpty(String input) {
		return !isNullOrEmpty(input);
	}

	public static KeyStroke getCtrlKeyStroke(int key) {
		return KeyStroke.getKeyStroke(key, InputEvent.CTRL_MASK);
	}

	public static List<Boolean> getTrueAsList() {
		return Arrays.asList(new Boolean(true));
	}

	public static List<Boolean> getFalseAsList() {
		return Arrays.asList(new Boolean(false));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<WebElement> getWebElementList(List<WebAction> webActionList, String key) {
		Map<String, List> executionResult = getListElementMap(webActionList);
		List<WebElement> list = executionResult.get(key);
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<WebElement> getWebElementList(List<WebAction> webActionList, Map<String, Object> arguments, String key) {
		Map<String, List> executionResult = getListElementMap(webActionList, arguments);
		List<WebElement> list = executionResult.get(key);
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getListElementMap(List<WebAction> webActionList){
		return getListElementMap(webActionList, null);
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getListElementMap(List<WebAction> webActionList, Map<String, Object> arguments) {
		WaitingDialog.setProgressBarMaxValue(webActionList.size());
		ActionWorker aw = null;
		if(arguments == null){
			aw = ActionWorker.getNewActionWorkerInstance(webActionList);
		}else{
			aw = ActionWorker.getNewActionWorkerInstance(webActionList, arguments);
		}
		aw.execute();
		WaitingDialog.showDialog();
		Map<String, List> result = null;
		try {
			result = aw.get();
		} catch (InterruptedException | ExecutionException | org.openqa.selenium.StaleElementReferenceException e) {
			HCIUtils.showException(e, false);
		}
		return result;
	}
	
	public static String removeRoomInLabel(String label){
		String result = label.split("\n")[0];
		result = result.replace(" - SALLE 1", "");
		result = result.replace(" - SALLE 2", "");
		result = result.replace(" - SALLE1", "");
		result = result.replace(" - SALLE2", "");
		return result;
	}
	
	public static String getStringFromSubscirptionList(List<Subscription> subscriptionList){
		String result = gsonInstance.toJson(subscriptionList);
		return result;
	}
	
	public static List<Subscription> getSubscriptionListFromString(String subscriptionList){
		if(isNullOrEmpty(subscriptionList)){
			return new ArrayList<Subscription>();
		}
		Type listType = new TypeToken<List<Subscription>>() {}.getType();
		List<Subscription> result = gsonInstance.fromJson(subscriptionList, listType);
		return result;
	}
	
	public static String getStringFromMap(Map<String, String> input){
		return gsonInstance.toJson(input);
	}
	
	public static Map<String, String> getMapFromString(String input){
		if(isNullOrEmpty(input)){
			return new HashMap<String, String>();
		}
		Type mapType = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> result = gsonInstance.fromJson(input, mapType);
		return result;
	}
	
	public static String getValueOfObject(Object input){
		return input == null ? "null" : input.toString();
	}
	
	public static String getTimeAsString(short timeValue){
		return timeValue > 9 ? ""+timeValue : "0"+timeValue;
	}
	
	public static String getStringWithSpeceficLength(String rawString, int specificLength){
		String result = new String(rawString);
		if(result.length() > specificLength){
			result = result.substring(0, specificLength);
		}else{
			while(result.length() < specificLength){
				result += " ";
			}
		}
		return result;
	}
	
	public static String formatDateBeginWithYear(Date date){
		return date == null ? null : yyyyMMdd.format(date);
	}
	
	public static String formatDateBeginWithDay(Date date){
		return date == null ? null : ddMMyyyy.format(date);
	}

}
