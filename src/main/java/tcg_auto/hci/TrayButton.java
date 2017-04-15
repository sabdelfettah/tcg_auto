package hci;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import lang.Lang;
import lang.Messages;
import utils.HCIUtils;

public class TrayButton extends TrayIcon {
	
	// STATIC FIELDS
	private static SystemTray trayInstance = null;
	private static TrayButton trayIconInstance = null;
	
	// CONSTRUCTORS
	public TrayButton(){
		this(HCI.getInstance().getIconImage(), Messages.getString(Lang.APPLICATION_NAME));
	}

	private TrayButton(Image arg0, String arg1) {
		super(arg0, arg1);
	}
	
	// STATIC METHODS
	public static SystemTray getSystemTrayInstance(){
		if(trayInstance == null){
			trayInstance = SystemTray.getSystemTray();
			try {
				trayInstance.add(getTrayIconInstance());
			} catch (AWTException e) {
				HCIUtils.showException(e, false, false, Messages.getString(Lang.LOG_MESSAGE_ERROR_HCI_INITIALIZATION_TRAYICON));
			}
		}
		return trayInstance;
	}
	
	private static TrayButton getTrayIconInstance(){
		if(trayIconInstance == null){
			trayIconInstance = new TrayButton();
			trayIconInstance.setImageAutoSize(true);
			trayIconInstance.setToolTip(Messages.getString(Lang.APPLICATION_TITLE));
			trayIconInstance.setPopupMenu(TrayPopup.getInstance());
		}
		return trayIconInstance;
	}
	
	public static void displayInfo(String title, String message) {
		displayAnyMessage(title, message, MessageType.INFO);
    }
	
	public static void displayWarn(String title, String message) {
		displayAnyMessage(title, message, MessageType.WARNING);
    }
	
	public static void displayError(String title, String message) {
		displayAnyMessage(title, message, MessageType.ERROR);
    }
	
	private static void displayAnyMessage(String title, String message, MessageType messageType){
		if(HCI.getInstance().isVisible()){
			return;
		}
		getTrayIconInstance().displayMessage(title, message, messageType);
	}

}
