package tcg_auto.hci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.model.MenuData;
import tcg_auto.model.MenuData.COMPONENT_TYPE;
import tcg_auto.utils.HCIUtils;
import tcg_auto.utils.HCIUtils.Action;
import tcg_auto.utils.MiscUtils;

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
		try {
			this.initializeMenusData();
		} catch (Exception e) {
			HCIUtils.showException(e, false);
		}
		this.initializeMenus();
	}

	// NOT STATIC METHODS
	private void initializeMenusData() throws Exception {
		myConfig = new HashMap<MenuData, List<MenuData>>();
		myConfig.put(new MenuData(0, Messages.getString(Lang.MENU_FILE), COMPONENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_FILE_EXIT), COMPONENT_TYPE.JMENUITEM, Action.ACTION_EXIT_APPLICATION, MiscUtils.getCtrlKeyStroke('Q'))
				));
		myConfig.put(new MenuData(1, Messages.getString(Lang.MENU_ACTIONS), COMPONENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_SET_LOGIN_PASSWORD), COMPONENT_TYPE.JMENUITEM, Action.ACTION_SET_LOGIN_PASSWORD),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_UPDATE_COURSES), COMPONENT_TYPE.JMENUITEM, Action.ACTION_UPDATE_COURSES),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_UPDATE_BOOKED_COURSES), COMPONENT_TYPE.JMENUITEM, Action.ACTION_UPDATE_BOOKED_COURSES),
						new MenuData(COMPONENT_TYPE.JSEPARATOR),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_ADD_SUBSCRIPTION), COMPONENT_TYPE.JMENUITEM, Action.ACTION_ADD_SUBSCRIPTION),
						new MenuData(Messages.getString(Lang.MENU_ITEM_ACTIONS_BOOKING), COMPONENT_TYPE.JMENUITEM, Action.ACTION_BOOKING_COURSE)
				));
		myConfig.put(new MenuData(2, Messages.getString(Lang.MENU_OPTIONS), COMPONENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_OPTIONS_SEE_LOG), COMPONENT_TYPE.JCHECKBOX, Action.ACTION_SEE_LOG, MiscUtils.getCtrlKeyStroke('L')),
						new MenuData(Messages.getString(Lang.MENU_ITEM_OPTIONS_SELECT_WEB_DRIVER_PATH), COMPONENT_TYPE.JMENUITEM, Action.ACTION_SELECT_WEB_DRIVER_PATH, MiscUtils.getCtrlKeyStroke('W'))
				));
		myConfig.put(new MenuData(3, Messages.getString(Lang.MENU_HELP), COMPONENT_TYPE.JMENU),
				Arrays.asList(
						new MenuData(Messages.getString(Lang.MENU_ITEM_HELP_ABOUT), COMPONENT_TYPE.JMENUITEM, Action.ACTION_SEE_ABOUT, MiscUtils.getCtrlKeyStroke('A'))
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
