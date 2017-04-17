package tcg_auto.manager;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.selenium.TCG;
import tcg_auto.utils.HCIUtils;

public abstract class WebDriverManager {
	
	// STATIC FINAL FIELDS
	public static final boolean CHROME_MODE = false;
	
	// STATIC FIELDS
	private static WebDriver driverInstance;
	private static boolean isWebDriverFree = false;
	private static TCG owner = null;
	
	public static boolean askForWebDriver(TCG asker){
		if(!isDriverInitialized()){
			getWebDriver();
		}
		if(isWebDriverFree){
			isWebDriverFree = false;
			owner = asker;
			return true;
		}
		return owner.equals(asker);
	}
	
	public static WebDriver getWebDriver(){
		if(driverInstance == null){
			try {
				if(CHROME_MODE){
					String current = new java.io.File( "." ).getCanonicalPath();
					System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, ConfigManager.getWebDriverPath());
					System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, current + "/chromedriver.log");
					driverInstance = new ChromeDriver();
				}else{
					System.setProperty("phantomjs.binary.path", ConfigManager.getWebDriverPath());
					driverInstance = new PhantomJSDriver();
				}
				isWebDriverFree = true;
			} catch (IOException e) {
				isWebDriverFree = false;
				HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_WEB_DRIVER_INITIALIZATION));
			}
		}
		return driverInstance;
	}
	
	public static void releaseWebDriver(){
		owner = null;
		isWebDriverFree = true;
	}
	
	public static boolean isDriverInitialized(){
		return driverInstance != null;
	}

}
