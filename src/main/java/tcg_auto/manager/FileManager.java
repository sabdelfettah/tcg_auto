package tcg_auto.manager;

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
import java.util.List;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.utils.HCIUtils;

public abstract class FileManager {
	// STATIC FINAL FIELDS
	private static final String PATH_LOGIN_PASSWORD = "tcg.auth";
	private static final String PATH_LOGS = "tcg.log";
	private static final String PATH_CONFIG = "tcg.cfg";
	
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
	
	protected static BufferedWriter getBufferedWriterInstance(BufferedWriter writer, String path, StandardOpenOption writeOption) throws IOException{
		if(writer == null){
			try {
				writer = Files.newBufferedWriter(getPathInstanceFromString(path), StandardCharsets.UTF_8, StandardOpenOption.CREATE, writeOption);
			} catch (IOException e) {
				throw e;
			}
		}
		return writer;
	}
	
	protected static BufferedReader getBufferedReaderInstance(BufferedReader reader, String path) throws IOException{
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
	
	
	protected static BufferedWriter getLoginPasswordBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(loginPasswordBufferedWriterInstance, PATH_LOGIN_PASSWORD, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	protected static BufferedReader getLoginPasswordBufferedReaderInstance() throws IOException{
		return getBufferedReaderInstance(loginPasswordBufferedReaderInstance, PATH_LOGIN_PASSWORD);
	}
	
	protected static BufferedWriter getConfigBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(configBufferedWriterInstance, PATH_CONFIG, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	protected static BufferedReader getConfigBufferedReaderInstance() throws IOException{
		BufferedReader result = getBufferedReaderInstance(configBufferedReaderInstance, PATH_CONFIG);
		if(result == null){
			ConfigManager.saveConfig();
			result = getConfigBufferedReaderInstance();
		}
		return result;
	}
	
	protected static BufferedWriter getLogBufferedWriterInstance() throws IOException{
		return getBufferedWriterInstance(logBufferedWriterInstance, PATH_LOGS, StandardOpenOption.APPEND);
	}
	
	protected static BufferedReader getLogBufferedReaderInstance() throws IOException{
		return getBufferedReaderInstance(logBufferedReaderInstance, PATH_LOGS);
	}
	
	protected static void writeLog(String logToWrite){
		try (BufferedWriter writer = getLogBufferedWriterInstance()){
			writer.append(logToWrite);
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
	}
	
	protected static void writelnLog(String logToWrite){
		writeLog(logToWrite + "\n");
	}
	
	public static List<String> readLogs() throws IOException, NullPointerException{
		List<String> result = new ArrayList<String>();
		try(BufferedReader reader = getLogBufferedReaderInstance()){
			if(reader == null){
				writeLog("");
				return readLogs();
			}
			readFileAndReturnListOfString(reader);
		} catch (IOException | NullPointerException e) {
			throw e;
		}
		return result;
	}
	
	protected static void writeConfig(String configFormattedToWrite){
		try (BufferedWriter writer = getConfigBufferedWriterInstance()){
			writer.append(configFormattedToWrite);
		} catch (IOException e) {
			HCIUtils.showException(e, false);
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
	}
	
	public static String readConfig() throws IOException, NullPointerException{
		String result = "";
		try(BufferedReader reader = getConfigBufferedReaderInstance()){
			result = readFileAndReturnString(reader);
		} catch (IOException | NullPointerException e) {
			throw e;
		}
		return result;
	}
	
	protected static void writeLoginPassword(String cipherLoginAndPassword){
		try (BufferedWriter writer = getLoginPasswordBufferedWriterInstance()) {
			writer.write(cipherLoginAndPassword);
		} catch (IOException e) {
			HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_FILE_WRITE_LOGIN_PASSWORD), null);
		}
	}
	
	public static String readLoginPassword() throws IOException, NullPointerException{
		String result = "";
		try(BufferedReader reader = getLoginPasswordBufferedReaderInstance()){
			result = readFileAndReturnString(reader);
		} catch (IOException | NullPointerException e) {
			throw e;
		}
		return result;
	}
	
	private static String readFileAndReturnString(BufferedReader reader) throws IOException, NullPointerException{
		String result = "";
		String line = null;
		while((line = reader.readLine()) != null){
			result += line + "\n";
		}
		return result;
	}
	
	private static List<String> readFileAndReturnListOfString(BufferedReader reader) throws IOException, NullPointerException{
		List<String> result = new ArrayList<String>();
		String line = null;
		while((line = reader.readLine()) != null){
			result.add(line);
		}
		return result;
	}

}
