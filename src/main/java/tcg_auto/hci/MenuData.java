package hci;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import utils.HCIUtils;
import utils.HCIUtils.Action;

public class MenuData {

	// NOT STATIC FIELDS
	private Integer index;
	private String title;
	private COMPOENENT_TYPE componentType;
	private Action action;
	private KeyStroke accelerator;
	private JComponent swingResult;
	private MenuItem awtResult;
	
	// CONSTRUCTORS
	public MenuData(COMPOENENT_TYPE componentType) throws Exception{
		if(componentType == COMPOENENT_TYPE.SEPARATOR || componentType == COMPOENENT_TYPE.JSEPARATOR){
			this.componentType = componentType;
		}else{
			throw new Exception("Bad use of the constructor");
		}
	}
	
	private MenuData(String title, COMPOENENT_TYPE componentType){
		this.title = title;
		this.componentType = componentType;
	}
	
	public MenuData(String title, COMPOENENT_TYPE componentType, Action action) {
		this(title, componentType);
		this.action = action;
	}

	public MenuData(String title, COMPOENENT_TYPE componentType, Action action, KeyStroke accelerator) {
		this(title, componentType, action);
		this.accelerator = accelerator;
	}

	public MenuData(Integer index, String title, COMPOENENT_TYPE componentType) {
		this(title, componentType);
		this.index = index;
	}
	
	public MenuData(Integer index, String title, COMPOENENT_TYPE componentType, Action action) {
		this(index, title, componentType);
		this.action = action;
	}
	
	// NOT STATIC METHODS
	public String getTitle(){
		return title;
	}
	
	public boolean hasTitle(){
		return title != null;
	}
	public Menu getMenu(){
		awtResult = new Menu(title);
		setActionToAWTResult();
		return (Menu) awtResult;
	}
	
	public MenuItem getMenuItem(){
		awtResult = new MenuItem(title);
		setActionToAWTResult();
		return awtResult;
	}
	
	public JComponent getJComponent(){
		switch(componentType){
		case JMENU : return getJMenu();
		case JMENUITEM : return getJMenuItem();
		case JSEPARATOR : return new JSeparator();
		case JCHECKBOX : return getJCheckBox();
		default : return null;
		}
	}

	public JMenu getJMenu() {
		swingResult = new JMenu(title);
		setActionToSwingResult();
		return (JMenu) swingResult;
	}

	public JMenuItem getJMenuItem() {
		swingResult = new JMenuItem(title);
		setActionToSwingResult();
		setAcceleratorToSwingResult();
		return (JMenuItem) swingResult;
	}
	
	public JCheckBox getJCheckBox(){
		swingResult = new JCheckBox(title);
		setActionToSwingResult();
		return (JCheckBox) swingResult;
	}

	private void setActionToSwingResult() {
		if (action == null)
			return;
		Method setActionCommandMethod = null;
		try {
			setActionCommandMethod = swingResult.getClass().getMethod("setActionCommand", String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			HCIUtils.showException(e, true);
		}
		if(setActionCommandMethod != null){
			try {
				setActionCommandMethod.invoke(swingResult, action.name());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				HCIUtils.showException(e, true);
			}
		}
		Method setActionListenerMethod = null;
		try {
			setActionListenerMethod = swingResult.getClass().getMethod("addActionListener", ActionListener.class);
		} catch (NoSuchMethodException | SecurityException e) {
			HCIUtils.showException(e, true);
		}
		if(setActionListenerMethod != null){
			try {
				setActionListenerMethod.invoke(swingResult, HCI.getInstance());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				HCIUtils.showException(e, true);
			}
		}
	}
	
	private void setActionToAWTResult() {
		if (action == null)
			return;
		awtResult.setActionCommand(action.name());
		awtResult.addActionListener(HCI.getInstance());
	}

	private void setAcceleratorToSwingResult() {
		if (accelerator == null)
			return;
		Method setAcceleratorMethod = null;
		try {
			setAcceleratorMethod = swingResult.getClass().getMethod("setAccelerator", KeyStroke.class);
		} catch (NoSuchMethodException | SecurityException e) {
			HCIUtils.showException(e, true);
		}
		if(setAcceleratorMethod != null){
			try {
				setAcceleratorMethod.invoke(swingResult, accelerator);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				HCIUtils.showException(e, true);
			}
		}
	}
	
	public Action getAction() {
		return action;
	}

	public int getIndex() {
		if (index == null) {
			return 0;
		} else {
			return index;
		}
	}
	
	public enum COMPOENENT_TYPE {
		MENU,
		JMENU,
		MENUITEM,
		JMENUITEM,
		SEPARATOR,
		JSEPARATOR,
		CHECKBOX,
		JCHECKBOX;
	}
}
