package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lang.Lang;
import lang.Messages;
import model.Subscription;
import utils.HCIUtils;
import utils.MiscUtils;

public abstract class FileManager {
	// STATIC FINAL FIELDS
	private static final String PATH_LOGIN_PASSWORD = "tcg.auth";
	private static final String PATH_LOGS = "tcg.log";
	private static final String PATH_CONFIG = "tcg.cfg";
	public static final String CONFIG_SEE_LOGS = "see_logs";
	public static final String CONFIG_SUBSCRIPTION_LIST = "subscription_list";
	
	// STATIC FIELDS
	private static BufferedWriter loginPasswordBufferedWriterInstance = null;
	private static BufferedReader loginPasswordBufferedReaderInstance = null;
	private static BufferedWriter configBufferedWriterInstance = null;
	private static BufferedReader configBufferedReaderInstance = null;
	private static BufferedWriter logBufferedWriterInstance = null;
	private static BufferedReader logBufferedReaderInstance = null;
	
	// STATIC METHODS
	private static Path getPathInstanceFromString(String path){
		return FileSystems.getDefault().getPath(path);
	}
	
	private static BufferedWriter getBufferedWriterInstance(BufferedWriter writer, String path, StandardOpenOption writeOption) throws IOException{
		if(writer == null){
			try {
				writer = Files.newBufferedWriter(getPathInstanceFromString(path), StandardCharsets.UTF_8, StandardOpenOption.CREATE, writeOption);
			} catch (IOException e) {
				throw e;
			}
		}
		return writer;
	}
	
	private static BufferedReader getBufferedReaderInstance(BufferedReader reader, String path) throws IOException{
		if(reader == null){
			try {
				reader = Files.newBufferedReader(getPathInstanceFromString(path), StandardCharsets.UTF_8);
			} catch (IOException e) {
				if(e instanceof NoSuchFileException){
					return null;
				}else{
					throw e;
				}
			}
		}
		return reader;
	}
	
	
	public static BufferedWriter getLoginPasswordBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(loginPasswordBufferedWriterInstance, PATH_LOGIN_PASSWORD, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public static BufferedReader getLoginPasswordBufferedReaderInstance() throws IOException{
		return getBufferedReaderInstance(loginPasswordBufferedReaderInstance, PATH_LOGIN_PASSWORD);
	}
	
	private static BufferedWriter getConfigBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(configBufferedWriterInstance, PATH_CONFIG, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	private static BufferedReader getConfigBufferedReaderInstance() throws IOException{
		BufferedReader result = getBufferedReaderInstance(configBufferedReaderInstance, PATH_CONFIG);
		if(result == null){
			writeConfig(null, false);
			result = getConfigBufferedReaderInstance();
		}
		return result;
	}
	
	private static BufferedWriter getLogBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(logBufferedWriterInstance, PATH_LOGS, StandardOpenOption.APPEND);
	}
	
	private static BufferedReader getLogBufferedReaderInstance() throws IOException{
		return getBufferedReaderInstance(logBufferedReaderInstance, PATH_LOGS);
	}
	
	public static void saveLoginAndPassword(Map<String, String> loginAndPassword){
		String loginAndPasswordString = MiscUtils.getStringFromMap(loginAndPassword);
		String cipherLoginAndPassword = CipherManager.encrypt(loginAndPasswordString);
		try (BufferedWriter writer = FileManager.getLoginPasswordBufferedWriterInstance()) {
			writer.write(cipherLoginAndPassword);
		} catch (IOException e) {
			HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_FILE_WRITE_LOGIN_PASSWORD), null);
		}
	}
	
	public static void writeLog(String logToWrite){
		try (BufferedWriter writer = getLogBufferedWriterInstance()){
			writer.append(logToWrite);
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
	}
	
	public static void writelnLog(String logToWrite){
		writeLog(logToWrite + "\n");
	}
	
	public static List<String> getLogs(){
		List<String> result = new ArrayList<String>();
		String line = null;
		try(BufferedReader reader = getLogBufferedReaderInstance()){
			while((line = reader.readLine()) != null){
				result.add(line);
			}
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		}
		return result;
	}
	
	public static void writeConfig(List<Subscription> subscritpionList, boolean seeLogs){
		Map<String, String> configToWrite = new HashMap<String, String>();
		configToWrite.put(CONFIG_SEE_LOGS, String.valueOf(seeLogs));
		if(subscritpionList != null){
			configToWrite.put(CONFIG_SUBSCRIPTION_LIST, MiscUtils.getStringFromSubscirptionList(subscritpionList));
		}
		String configFormattedToWrite = MiscUtils.getStringFromMap(configToWrite);
		try (BufferedWriter writer = getConfigBufferedWriterInstance()){
			writer.append(configFormattedToWrite);
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
	}
	
	public static Map<String, Object> getConfig(){
		Map<String, Object> result = new HashMap<String, Object>();
		String configContent = "";
		String line = null;
		try(BufferedReader reader = getConfigBufferedReaderInstance()){
			while((line = reader.readLine()) != null){
				configContent += line + "\n";
			}
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		}
		if(!configContent.isEmpty()){
			Map<String, String> tempResult = MiscUtils.getMapFromString(configContent);
			String configSeeLogs = tempResult.get(CONFIG_SEE_LOGS);
			result.put(CONFIG_SEE_LOGS, configSeeLogs == null ? false : Boolean.valueOf(configSeeLogs).booleanValue());
			result.put(CONFIG_SUBSCRIPTION_LIST, MiscUtils.getSubscriptionListFromString(tempResult.get(CONFIG_SUBSCRIPTION_LIST)));
		}else{
			result.put(CONFIG_SEE_LOGS, false);
			result.put(CONFIG_SUBSCRIPTION_LIST, new ArrayList<Subscription>());
		}
		return result;
	}

}
