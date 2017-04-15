package tcg_auto.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcg_auto.model.Subscription;
import tcg_auto.utils.MiscUtils;

public class ConfigManager {
	// STATIC FINAL FIELDS
	public static final String CONFIG_SEE_LOGS = "see_logs";
	public static final String CONFIG_SUBSCRIPTION_LIST = "subscription_list";
	public static final String CONFIG_WEB_DRIVER_PATH = "web_driver_path";
	
	// STATIC FIELDS
	private static boolean seeLogs = false;
	private static String webDriverPath = "";
	
	
	// STATIC METHODS
	public static void saveConfig(){
		Map<String, String> configToWrite = new HashMap<String, String>();
		configToWrite.put(CONFIG_SEE_LOGS, String.valueOf(seeLogs));
		configToWrite.put(CONFIG_WEB_DRIVER_PATH, webDriverPath);
		List<Subscription> subscritpionList = SubscriptionManager.getSubscriptionList();
		if(MiscUtils.isNotNullOrEmpty(subscritpionList)){
			configToWrite.put(CONFIG_SUBSCRIPTION_LIST, MiscUtils.getStringFromSubscirptionList(subscritpionList));
		}
		String configFormattedToWrite = MiscUtils.getStringFromMap(configToWrite);
		FileManager.writeConfig(configFormattedToWrite);
	}
	
	public static void setSeeLogs(boolean isSeeLogsSelected){
		seeLogs = isSeeLogsSelected;
		saveConfig();
	}
	
	public static void setWebDriver(String newWebDriverPath){
		webDriverPath = newWebDriverPath;
		saveConfig();
	}
	
	public static Map<String, Object> getConfig() throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		String configContent = FileManager.readConfig();
		if(!configContent.isEmpty()){
			Map<String, String> tempResult = MiscUtils.getMapFromString(configContent);
			initializeValuesFromMap(tempResult);
		}else{
			initializeDefaultValues();
		}
		result.put(CONFIG_SEE_LOGS, seeLogs);
		result.put(CONFIG_WEB_DRIVER_PATH, webDriverPath);
		result.put(CONFIG_SUBSCRIPTION_LIST, SubscriptionManager.getSubscriptionList());
		return result;
	}
	
	private static void initializeDefaultValues(){
		seeLogs = false;
		webDriverPath = "";
	}
	
	private static void initializeValuesFromMap(Map<String, String> readedValues) throws Exception{
		String configSeeLogs = readedValues.get(CONFIG_SEE_LOGS);
		seeLogs = configSeeLogs == null ? false : Boolean.valueOf(configSeeLogs).booleanValue();
		webDriverPath = readedValues.get(CONFIG_WEB_DRIVER_PATH) == null ? "" : readedValues.get(CONFIG_WEB_DRIVER_PATH);
		SubscriptionManager.initializeSubscriptionList(MiscUtils.getSubscriptionListFromString(readedValues.get(CONFIG_SUBSCRIPTION_LIST)));
	}
}
