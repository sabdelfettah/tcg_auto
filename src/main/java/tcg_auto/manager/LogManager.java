package tcg_auto.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import tcg_auto.hci.LogPanel;

public abstract class LogManager {
	
	// STATIC FIELDS
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// STATIC METHODS
	public static void logInfo(String message) {
		logMessage(message, LOG_TYPE.INFO);
	}
	
	public static void logWarn(String message) {
		logMessage(message, LOG_TYPE.WARN);
	}
	
	public static void logError(String message) {
		logMessage(message, LOG_TYPE.ERROR);
	}
	
	private static void logMessage(String message, LOG_TYPE logType){
		Date now = new Date();
		String nowFormatted = dateFormatter.format(now);
		String logToWrite = String.format("%s | %s | %s", nowFormatted, logType, message);
		System.out.println(logToWrite);
		FileManager.writelnLog(logToWrite);
		LogPanel.appendlnSessionAppLog(logToWrite);
	}
	
	private enum LOG_TYPE{
		INFO,
		WARN,
		ERROR;
		
		public String toString(){
			if(ERROR.equals(this)){
				return this.name();
			}else{
				return this.name() + " ";
			}
		}
	}

}
