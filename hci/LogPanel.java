package hci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import lang.Lang;
import lang.Messages;

public class LogPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364578652499532033L;
	
	// STATIC FIELDS
	private static LogPanel instance = null;
	
	// NOT STATIC FIELDS
	private JTabbedPane subPanel;
	private JPanel appLogPanel;
	private JPanel appSessionLogPanel;
	private JPanel seleniumLogPanel;
	private JTextArea appLogTextArea;
	private JTextArea appSessionLogTextArea;
	private JTextArea seleniumLogTextArea;

	// CONSTRUCTORS
	public LogPanel() {
		this.setLayout(new BorderLayout());
		this.subPanel = new JTabbedPane();
		this.appLogPanel = new JPanel(new BorderLayout());
		this.seleniumLogPanel = new JPanel(new BorderLayout());
		this.appSessionLogPanel = new JPanel(new BorderLayout());
		this.appLogTextArea = new JTextArea();
		this.appLogTextArea.setEditable(false);
		JScrollPane appLogScrollPane = new JScrollPane(appLogTextArea);
		appLogScrollPane.setPreferredSize(new Dimension(280, 200));
		appLogScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
		this.appLogPanel.setMinimumSize(new Dimension(300, 300));
		this.appSessionLogTextArea = new JTextArea();
		this.appSessionLogTextArea.setEditable(false);
		this.appSessionLogTextArea.setMinimumSize(new Dimension(300, 300));
		JScrollPane appSessionLogScrollPane = new JScrollPane(appSessionLogTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		appSessionLogScrollPane.setPreferredSize(new Dimension(280, 200));
		appSessionLogScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
		this.seleniumLogTextArea = new JTextArea();
		this.seleniumLogTextArea.setEditable(false);
		this.seleniumLogTextArea.setMinimumSize(new Dimension(300, 300));
		JScrollPane seleniumLogScrollPane = new JScrollPane(seleniumLogTextArea);
		seleniumLogScrollPane.setPreferredSize(new Dimension(280, 200));
		seleniumLogScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
		this.appLogPanel.add(BorderLayout.CENTER, appLogScrollPane);
		this.appSessionLogPanel.add(BorderLayout.CENTER, appSessionLogScrollPane);
		this.seleniumLogPanel.add(BorderLayout.CENTER, seleniumLogScrollPane);
		this.subPanel.addTab(Messages.getString(Lang.TITLE_TITLE_PANEL_APPLICATION_SESSION_LOGS), null, this.appSessionLogPanel, Messages.getString(Lang.TIPS_TITLE_PANEL_APPLICATION_SESSION_LOGS));
		this.subPanel.addTab(Messages.getString(Lang.TITLE_TITLE_PANEL_APPLICATION_LOGS), null, this.appLogPanel, Messages.getString(Lang.TIPS_TITLE_PANEL_APPLICATION_LOGS));
		this.subPanel.addTab(Messages.getString(Lang.TITLE_TITLE_PANEL_SELENIUM_LOGS), null, this.seleniumLogPanel, Messages.getString(Lang.TIPS_TITLE_PANEL_SELENIUM_LOGS));
		this.add(BorderLayout.CENTER, this.subPanel);
		this.setVisible(false);
	}
	
	// STATIC METHODS
	public static LogPanel getInstance() {
		if (instance == null)
			instance = new LogPanel();
		return instance;
	}
	
	public static boolean setPanelVisible(boolean visibility){
		getInstance().setVisible(visibility);
		return getInstance().isVisible();
	}
	
	public static void appendAppLog(String newLog) {
		getInstance().appLogTextArea.append(newLog);
	}
	
	public static void appendSessionAppLog(String newLog) {
		getInstance().appSessionLogTextArea.append(newLog);
		appendAppLog(newLog);
	}
	
	public static void appendSeleniumLog(String newLog) {
		getInstance().seleniumLogTextArea.append(newLog);
	}
	
	public static void appendlnAppLog(String newLog) {
		appendAppLog(newLog + "\n");
	}
	
	public static void appendlnSessionAppLog(String newLog) {
		appendSessionAppLog(newLog + "\n");
	}
	
	public static void appendlnSeleniumLog(String newLog) {
		appendSeleniumLog(newLog + "\n");
	}
	
}
