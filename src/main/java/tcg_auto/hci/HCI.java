package tcg_auto.hci;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.manager.ActionManager;
import tcg_auto.manager.ConfigManager;
import tcg_auto.manager.Initializator;
import tcg_auto.manager.LogManager;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.MiscUtils;

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
	private static boolean hciInitialized = false;
	private static boolean dataInitialized= false;
	
	// CONSTRUCTORS
	public HCI() {
		this.initializeFrame();
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
	
	public void initializeFrameComponents() {
		this.setJMenuBar(MainMenuBar.getInstance());
		this.setContentPane(Panel.getInstance());
		hciInitialized = true;
	}
	
	public void postInitializationFrame(){
		LogPanel.setPanelVisible(ConfigManager.getSeeLogs());
		((JCheckBox) MainMenuBar.getMenuBarComponent(Messages.getString(Lang.MENU_ITEM_OPTIONS_SEE_LOG))).setSelected(ConfigManager.getSeeLogs());
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
	
	public boolean isHCIInitialized(){
		return hciInitialized;
	}
	
	public boolean isDataInitialized(){
		return dataInitialized;
	}
	
	// SETTERS
	public void initilizationOfHCIComplete(){
		hciInitialized = true;
	}
	
	public void initilizationOfDataComplete(){
		dataInitialized = true;
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
		Initializator.initializeData();
		ActionManager.initiliazeLists();
		getInstance().postInitializationFrame();
		TrayButton.getSystemTrayInstance();
		TrayButton.displayInfo(Messages.getString(Lang.APPLICATION_NAME), Messages.getString(Lang.MESSAGE_TRAY_APP_STARTED));
		LogManager.logInfoFinished(Messages.getString(Lang.LOG_MESSAGE_INFO_INITIALIZATION_APP_STARTED));
	}
	
}
