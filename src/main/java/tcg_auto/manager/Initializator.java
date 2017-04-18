package tcg_auto.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import tcg_auto.hci.HCI;
import tcg_auto.hci.LogPanel;
import tcg_auto.hci.SubscriptionListPanel;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.selenium.TCG;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.MiscUtils;
import tcg_auto.utils.TCGUtils;

public abstract class Initializator {
	
	// STATIC METHODS
	public static void initializeData(){
		initializeLogs();
		LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_STARTING_APP));
		initializeLoginAndPassword(true);
		initializeConfig();
		initializeWebDriverPath(true);
		SubscriptionManager.scheduleTasksReInitializer();
		HCI.getInstance().initilizationOfDataComplete();
	}
	
	@SuppressWarnings("rawtypes")
	public static void initiliazeLists(){
		Map<String, List> executionResults = MiscUtils.getListElementMap(TCGUtils.ACTION_GET_FULL_LISTS);
		ActionManager.updateCourseList(executionResults);
		ActionManager.updateBookedCourseList(executionResults);
		SubscriptionListPanel.updateSubscriptionList();
	}
	
	private static void initializeLogs(){
		List<String> oldLogs;
		try {
			oldLogs = FileManager.readLogs();
			for(String oldLog : oldLogs){
				LogPanel.appendlnAppLog(oldLog);
			}
		} catch (NullPointerException | IOException e) {
			HCIUtils.showException(e, false);
		}
	}
	
	private static void initializeLoginAndPassword(boolean log){
		if(log){
			LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_LOGIN_PASSWORD));
		}
		try {
			LoginPasswordManager.getAndInitializeLoginAndPassword();
			TCG.setBaseUrl(TCGUtils.URL_HOME);
			LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOGIN_PASSWORD_SUCCESS));
		} catch (IOException | NullPointerException e) {
			LogManager.logWarn(String.format(Messages.getString(Lang.LOG_MESSAGE_WARN_INITIALIZATION_NO_LOGIN_PASSWORD_FOUND), e.getMessage()));
			boolean savedSuccess = LoginPasswordManager.getAndSaveLoginAndPassword();
			if(!savedSuccess){
				ActionManager.exit(false);
			}else{
				initializeLoginAndPassword(false);
			}
		}
	}
	
	private static void initializeConfig(){
		LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_CONFIG));
		try {
			ConfigManager.getConfig();
		} catch (Exception e) {
			HCIUtils.showException(e, true);
		}
		LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_CONFIG_SUCCESS));
	}
	
	private static void initializeWebDriverPath(boolean logLooking){
		if(logLooking){
			LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_WEB_DRIVER_PATH));
		}
		if(MiscUtils.isNullOrEmpty(ConfigManager.getWebDriverPath())){
			ConfigManager.getAndSaveWebDrive();
			initializeWebDriverPath(false);
		}else{
			LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_WEB_DRIVER_PATH_SUCCESS));
		}
	}
	
}
