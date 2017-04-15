package hci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import hci.MenuData.COMPOENENT_TYPE;
import lang.Lang;
import lang.Messages;
import utils.HCIUtils.Action;
import utils.MiscUtils;

public class MainMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3398553154801791069L;
	
	// STATIC FIELDS
	private static MainMenuBar instance = null;
	
	// NOT STATIC FIELDS
	private Map<MenuData, List<MenuData>> myConfig;
	private Map<String, JComponent> components;
	private List<JMenu> menuList;

	// CONSTRUCTORS
	public MainMenuBar() {
		this.initializeMenusData();
		this.initializeMenus();
	}

	// NOT STATIC METHODS
	private void initializeMenusData() {
		myConfig = new HashMap<MenuData, List<MenuData>>();
		myConfig.put(new MenuData(0, Messages.getString(Lang.MENU_FILE), COMPOENENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_FILE_EXIT), COMPOENENT_TYPE.JMENUITEM, Action.ACTION_EXIT_APPLICATION, MiscUtils.getCtrlKeyStroke('Q'))
				));
		myConfig.put(new MenuData(1, Messages.getString(Lang.MENU_ACTIONS), COMPOENENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_SET_LOGIN_PASSWORD), COMPOENENT_TYPE.JMENUITEM, Action.ACTION_SET_LOGIN_PASSWORD),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_UPDATE_COURSES), COMPOENENT_TYPE.JMENUITEM, Action.ACTION_UPDATE_COURSES),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_ADD_SUBSCRIPTION), COMPOENENT_TYPE.JMENUITEM, Action.ACTION_ADD_SUBSCRIPTION),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_BOOKING), COMPOENENT_TYPE.JMENUITEM, Action.ACTION_BOOKING_COURSE)
				));
		myConfig.put(new MenuData(2, Messages.getString(Lang.MENU_HELP), COMPOENENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_HELP_SEE_LOG), COMPOENENT_TYPE.JCHECKBOX, Action.ACTION_SEE_LOG, MiscUtils.getCtrlKeyStroke('L'))
				));
	}

	private void initializeMenus() {
		components = new HashMap<String, JComponent>();
		menuList = new ArrayList<JMenu>();
		myConfig.forEach((menu, menuItems) -> {
			menuList.add(null);
		});
		myConfig.forEach((menu, menuItems) -> {
			this.addMenu(menu, menuItems);
		});
		menuList.forEach(menu -> {
			this.add(menu);
		});
	}

	private void addMenu(MenuData menu, List<MenuData> menuItems) {
		JMenu newMenu = menu.getJMenu();
		components.put(menu.getTitle(), newMenu);
		if (MiscUtils.isNotNullOrEmpty(menuItems)) {
			menuItems.forEach(menuItem -> {
				JComponent menuItemComponent = menuItem.getJComponent();
				newMenu.add(menuItemComponent);
				if(menuItem.hasTitle()){
					components.put(menuItem.getTitle(), menuItemComponent);
				}
			});
		}
		menuList.set(menu.getIndex(), newMenu);
	}
	
	// STATIC METHODS
	public static MainMenuBar getInstance() {
		if (instance == null)
			instance = new MainMenuBar();
		return instance;
	}
	
	public static JComponent getMenuBarComponent(String title){
		return getInstance().components.get(title);
	}

}
