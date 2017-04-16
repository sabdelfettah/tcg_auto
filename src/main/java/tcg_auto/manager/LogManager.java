package tcg_auto.manager;

import java.util.Date;
import java.util.regex.Pattern;

import tcg_auto.hci.LogPanel;
import tcg_auto.utils.MiscUtils;

public abstract class LogManager {
	
	// STATIC FINAL FIELDS
	private static final int MAX_CLASS_NAME_LENGTH = 15;
	private static final int MAX_LINE_NUMBER_LENGTH = 3;
	
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
		String nowFormatted = MiscUtils.formatDateBeginWithYear(new Date());
		String[] callerClassPath = e.getClassName().split(Pattern.quote("."));
		String callerClass = callerClassPath.length > 0 ? callerClassPath[callerClassPath.length - 1] : "";
		callerClass = MiscUtils.getStringWithSpeceficLength(callerClass, MAX_CLASS_NAME_LENGTH);
		String lineNumber = MiscUtils.getStringWithSpeceficLength(String.valueOf(e.getLineNumber()), MAX_LINE_NUMBER_LENGTH);
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
