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
import tcg_auto.manager.CipherManager;
import tcg_auto.manager.FileManager;
import tcg_auto.manager.LogManager;
import tcg_auto.manager.SubscriptionManager;
import tcg_auto.model.Subscription;
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
	private static boolean seeLogs = false;
	
	// CONSTRUCTORS
	public HCI() {
		this.initializeFrame();
		this.initializeLogs();
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_STARTING_APP));
		this.initializeLoginAndPassword(true);
		this.initializeConfig();
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
			LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_LOGIN_PASSWORD));
		}
		try {
			String rawLoginAndPassword = FileManager.getLoginPasswordBufferedReaderInstance().readLine();
			String decryptedLoginAndPassword = CipherManager.decrypt(rawLoginAndPassword);
			Map<String, String> loginAndPassword = MiscUtils.getMapFromString(decryptedLoginAndPassword);
			TCG.setLogin(loginAndPassword.get(HCIUtils.FIELD_LOGIN));
			TCG.setPassword(loginAndPassword.get(HCIUtils.FIELD_PASSWORD));
			TCG.setBaseUrl(TCGUtils.URL_HOME);
			LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOGIN_PASSWORD_SUCCESS));
		} catch (IOException e) {
			LogManager.logWarn(String.format(Messages.getString(Lang.LOG_MESSAGE_WARN_INITIALIZATION_NO_LOGIN_PASSWORD_FOUND), e.getMessage()));
			boolean savedSuccess = HCIUtils.getAndSaveLoginAndPassword();
			if(!savedSuccess){
				ActionManager.exit(false);
			}else{
				initializeLoginAndPassword(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initializeConfig(){
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_LOOKING_FOR_CONFIG));
		Map<String, Object> config = FileManager.getConfig();
		seeLogs = (boolean) config.get(FileManager.CONFIG_SEE_LOGS);
		List<Subscription> subscriptionList = (List<Subscription>) config.get(FileManager.CONFIG_SUBSCRIPTION_LIST);
		try {
			SubscriptionManager.initializeSubscriptionList(subscriptionList);
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_CONFIG_SUCCESS));
	}
	
	public void initializeLogs(){
		List<String> oldLogs = FileManager.getLogs();
		for(String oldLog : oldLogs){
			LogPanel.appendlnAppLog(oldLog);
		}
	}

	public void initializeFrameComponents() {
		this.setJMenuBar(MainMenuBar.getInstance());
		this.setContentPane(Panel.getInstance());
		LogPanel.setPanelVisible(seeLogs);
		((JCheckBox) MainMenuBar.getMenuBarComponent(Messages.getString(Lang.MENU_ITEM_HELP_SEE_LOG))).setSelected(seeLogs);
	}
	
	// GETTERS
	public Image getImageFromPath(String path){
		Image result = images.get(path);
		if(result == null){
			System.out.println("getting image "+ path);
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
			HCIUtils.showException(e, false, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_ACTION_EXECUTE_ACTION), MiscUtils.getValueOfObject(actionEvent));
		} catch (Exception e) {
			HCIUtils.showException(e, false, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_ACTION_EXECUTE_ACTION), MiscUtils.getValueOfObject(actionEvent));
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
		LogManager.logInfo(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_APP_STARTED));
	}
	
}
