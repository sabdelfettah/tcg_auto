package hci;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hci.MenuData.COMPOENENT_TYPE;
import lang.Lang;
import lang.Messages;
import utils.HCIUtils.Action;
import utils.MiscUtils;

public class TrayPopup extends PopupMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8146002610974968673L;
	// STATIC FIELDS
	private static TrayPopup instance = null;
	
	// NOT STATIC FIELDS
	private Map<MenuData, List<MenuData>> myConfig;
	private List<MenuItem> menuList;
	
	// CONSTRUCTORS
	public TrayPopup(){
		this.initializeMenusData();
		this.initializeMenus();
	}
	
	// NOT STATIC METHODS
	private void initializeMenusData(){
		myConfig = new HashMap<MenuData, List<MenuData>>();
		myConfig.put(new MenuData(0, Messages.getString(Lang.MENU_OPEN_APPLICATION), COMPOENENT_TYPE.MENUITEM, Action.ACTION_OPEN_APPLICATION), null);
		myConfig.put(new MenuData(1, Messages.getString(Lang.MENU_ITEM_FILE_EXIT), COMPOENENT_TYPE.MENUITEM, Action.ACTION_EXIT_APPLICATION), null);
	}
	
	private void initializeMenus(){
		menuList = new ArrayList<MenuItem>();
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
		if(MiscUtils.isNullOrEmpty(menuItems)){
			MenuItem newMenuItem = menu.getMenuItem();
			menuList.set(menu.getIndex(), newMenuItem);
			return;
		}
		Menu newMenu = menu.getMenu();
		menuItems.forEach(menuItem -> {
			newMenu.add(menuItem.getMenuItem());
		});
		menuList.set(menu.getIndex(), newMenu);
	}
	
	// STATIC METHODS
	public static TrayPopup getInstance(){
		if(instance == null){
			instance = new TrayPopup();
		}
		return instance;
	}

}
