package tcg_auto.hci;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.manager.ActionManager;
import tcg_auto.manager.ConfigManager;
import tcg_auto.manager.FileManager;
import tcg_auto.manager.LogManager;
import tcg_auto.manager.LoginPasswordManager;
import tcg_auto.selenium.TCG;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.MiscUtils;
import tcg_auto.utils.TCGUtils;

public class HCI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2995273966047412862L;
	// STATIC FINAL FIELDS
	// IMAGES
	private static final Map<String, Image> images = new HashMap<String, Image>();
	
	// STATIC FIELDS
	private static HCI instance = null;
	private static boolean initialized= false;
	
	// CONSTRUCTORS
	public HCI() {
		this.initializeFrame();
		this.initializeLogs();
		LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_STARTING_APP));
		this.initializeLoginAndPassword(true);
		this.initializeConfig();
		this.initializeWebDriverPath(true);
	}

	private void initializeFrame(){
		// compute some properties
		int width = (int) (getToolkit().getScreenSize().getWidth() * 0.9);
		int height = (int) (getToolkit().getScreenSize().getHeight() * 0.9);
		// initializing frame
		this.setName(Messages.getString(Lang.APPLICATION_NAME));
		this.setTitle(Messages.getString(Lang.APPLICATION_TITLE));
		this.setSize(width, height);
		this.setResizable(false);
		this.setIconImage(getIconImage());
		this.setLocationRelativeTo(null);
	}
	
	private void initializeLoginAndPassword(boolean log){
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
	
	public void initializeConfig(){
		LogManager.logInfoRunning(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_CONFIG));
		try {
			ConfigManager.getConfig();
		} catch (Exception e) {
			HCIUtils.showException(e, true);
		}
		LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_CONFIG_SUCCESS));
	}
	
	public void initializeWebDriverPath(boolean logLooking){
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
	
	public void initializeLogs(){
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

	public void initializeFrameComponents() {
		this.setJMenuBar(MainMenuBar.getInstance());
		this.setContentPane(Panel.getInstance());
		LogPanel.setPanelVisible(ConfigManager.getSeeLogs());
		((JCheckBox) MainMenuBar.getMenuBarComponent(Messages.getString(Lang.MENU_ITEM_HELP_SEE_LOG))).setSelected(ConfigManager.getSeeLogs());
	}
	
	// GETTERS
	public Image getImageFromPath(String path){
		Image result = images.get(path);
		if(result == null){
			getClass();
			try {
				result = ImageIO.read(HCIUtils.getUrlFromPath(path));
				images.put(path, result);
			} catch (IOException | IllegalArgumentException e) {
				HCIUtils.showException(e, false);
			}
		}
		return result;
	}
	
	public Image getIconImage(){
		return getImageFromPath(HCIUtils.PATH_APPLICATION_ICON);
	}
	
	public Image getLoadingImage(){
		return getImageFromPath(HCIUtils.PATH_LOADING_IMAGE);
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	// SETTERS
	public void initilizationComplete(){
		initialized = true;
	}
	
	// OTHER METHODS
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		try {
			ActionManager.executeAction(actionEvent.getActionCommand());
		} catch (IOException e) {
			HCIUtils.showException(e, false, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_EXECUTING_ACTION), MiscUtils.getValueOfObject(actionEvent));
		} catch (Exception e) {
			HCIUtils.showException(e, false, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_EXECUTING_ACTION), MiscUtils.getValueOfObject(actionEvent));
		}
	}

	// STATIC METHODS
	public static HCI getInstance() {
		if (instance == null) {
			instance = new HCI();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		getInstance().initializeFrameComponents();
		ActionManager.initiliazeLists();
		TrayButton.getSystemTrayInstance();
		TrayButton.displayInfo(Messages.getString(Lang.APPLICATION_NAME), Messages.getString(Lang.MESSAGE_TRAY_APP_STARTED));
		LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_APP_STARTED));
	}
	
}
