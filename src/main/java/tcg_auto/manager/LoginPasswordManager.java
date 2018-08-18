package tcg_auto.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.selenium.TCG;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.MiscUtils;

public abstract class LoginPasswordManager {
	
	// STATIC FINAL FIELDS
	public static final String FIELD_LOGIN = "login";
	public static final String FIELD_PASSWORD = "password";
	
	// STATIC METHODS
	public static void saveLoginAndPassword(Map<String, String> loginAndPassword){
		String loginAndPasswordString = MiscUtils.getStringFromMap(loginAndPassword);
		String cipherLoginAndPassword = CipherManager.encrypt(loginAndPasswordString);
		FileManager.writeLoginPassword(cipherLoginAndPassword);
	}
	
	public static boolean getAndSaveLoginAndPassword() {
		Map<String, String> loginAndPassword = getInputLoginAndPassword();
		if(loginAndPassword == null){
			return false;
		}
		initializeLoginPassword(
								loginAndPassword.get(FIELD_LOGIN),
								loginAndPassword.get(FIELD_PASSWORD)
								);
		LoginPasswordManager.saveLoginAndPassword(loginAndPassword);
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_ACTION_GET_SAVE_LOGIN_PASSWORD));
		return true;
	}
	
	private static Map<String, String> getInputLoginAndPassword() {
		Map<String, String> result = new HashMap<String, String>();
		String login = HCIUtils.getValueFromInputDialog(Messages.getString(Lang.TITLE_SET_LOGIN), Messages.getString(Lang.LABELS_SET_LOGIN_PASSWORD_LABEL_LOGIN), 20, false);
		if(StringUtils.isEmpty(login)){
			return null;
		}
		String password = HCIUtils.getValueFromInputDialog(Messages.getString(Lang.TITLE_SET_PASSWORD), Messages.getString(Lang.LABELS_SET_LOGIN_PASSWORD_LABEL_PASSWORD), 10, true);
		if(StringUtils.isEmpty(password)){
			return null;
		}
		result.put(FIELD_LOGIN, login);
		result.put(FIELD_PASSWORD, password);
		return result;
	}
	
	public static Map<String, String> getAndInitializeLoginAndPassword() throws NullPointerException, IOException{
		String rawLoginAndPassword = FileManager.readLoginPassword();
		String decryptedLoginAndPassword = CipherManager.decrypt(rawLoginAndPassword);
		Map<String, String> loginAndPassword = MiscUtils.getMapFromString(decryptedLoginAndPassword);
		initializeLoginPassword(
								loginAndPassword.get(FIELD_LOGIN),
								loginAndPassword.get(FIELD_PASSWORD)
								);
		return loginAndPassword;
	}
	
	private static void initializeLoginPassword(String login, String password){
		TCG.setLogin(login);
		TCG.setPassword(password);
	}

}
