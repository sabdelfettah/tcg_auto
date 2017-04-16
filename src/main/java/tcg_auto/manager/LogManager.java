package tcg_auto.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import tcg_auto.hci.LogPanel;
import tcg_auto.utils.MiscUtils;

public abstract class LogManager {
	
	// STATIC FIELDS
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// STATIC METHODS
	public static void logInfo(String message) {
		logMessage(message, LOG_TYPE.INFO, LOG_STATUS.ALERT);
	}
	
	public static void logInfoRunning(String message) {
		logMessage(message, LOG_TYPE.INFO, LOG_STATUS.RUNNING);
	}
	
	public static void logInfoFinished(String message) {
		logMessage(message, LOG_TYPE.INFO, LOG_STATUS.FINISHED);
	}
	
	public static void logWarn(String message) {
		logMessage(message, LOG_TYPE.WARN, LOG_STATUS.ALERT);
	}
	
	public static void logWarnFinished(String message) {
		logMessage(message, LOG_TYPE.WARN, LOG_STATUS.FINISHED);
	}
	
	public static void logError(String message) {
		logMessage(message, LOG_TYPE.ERROR, LOG_STATUS.ALERT);
	}
	
	public static void logErrorFinished(String message) {
		logMessage(message, LOG_TYPE.ERROR, LOG_STATUS.FINISHED);
	}
	
	private static void logMessage(String message, LOG_TYPE logType, LOG_STATUS status){
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[3];
		Date now = new Date();
		String nowFormatted = dateFormatter.format(now);
		String[] callerClassPath = e.getClassName().split(Pattern.quote("."));
		String callerClass = callerClassPath.length > 0 ? callerClassPath[callerClassPath.length - 1] : "";
		callerClass = MiscUtils.getStringWithSpeceficLength(callerClass, 12);
		String lineNumber = MiscUtils.getStringWithSpeceficLength(String.valueOf(e.getLineNumber()), 3);
		String logToWrite = String.format("%s | %s | %s | %s | %s | %s", nowFormatted, logType, status, callerClass, lineNumber, message);
		System.out.println(logToWrite);
		FileManager.writelnLog(logToWrite);
		LogPanel.appendlnSessionAppLog(logToWrite);
	}
	
	// ENUMERATIONS
	private enum LOG_TYPE{
		INFO,
		WARN,
		ERROR;
		
		private static final int maxEnumStringLength = ERROR.name().length();
		
		@Override
		public String toString(){
			String result = super.toString();
			return MiscUtils.getStringWithSpeceficLength(result, maxEnumStringLength);
		}
	}

	private enum LOG_STATUS{
		ALERT,
		RUNNING,
		FINISHED;
		
		private static final int maxEnumStringLength = FINISHED.name().length();
		
		@Override
		public String toString(){
			String result = super.toString();;
			return MiscUtils.getStringWithSpeceficLength(result, maxEnumStringLength);
		}
	}
}
