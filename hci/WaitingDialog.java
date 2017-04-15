package hci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import lang.Lang;
import lang.Messages;
import utils.HCIUtils;
import utils.TCGUtils.WebAction;

public class WaitingDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7853549355551724576L;
	// STATIC FINAL FIELDS
	private static final int width = 400;
	private static final int height = 300;
	
	// STATIC FIELDS
	private static WaitingDialog instance = null;
	private static JProgressBar progressBarInstance = null;
	private static JLabel labelInstance = null;
	
	// CONSTRUCTORS
	public WaitingDialog(){
		super(null, ModalityType.APPLICATION_MODAL);
		this.setIconImage(HCI.getInstance().getIconImage());
		this.setUndecorated(true);
		this.setSize(width, height);
		this.setAlwaysOnTop(true);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel panelProgress = new JPanel(new BorderLayout());
		JPanel panelLabel = new JPanel(new BorderLayout());
		ImageIcon icon = new ImageIcon(HCI.getInstance().getLoadingImage());
		JLabel waitingLabel = new JLabel(icon);
		waitingLabel.setMaximumSize(new Dimension(400, 300));
		panelLabel.add(waitingLabel, BorderLayout.CENTER);
		panelLabel.add(getLabelInstance(), BorderLayout.SOUTH);
		panelProgress.add(getProgressBarInstance(), BorderLayout.SOUTH);
		mainPanel.add(panelLabel, BorderLayout.CENTER);
		mainPanel.add(panelProgress, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(HCI.getInstance());
	}
	
	// STATIC METHODS
	private static WaitingDialog getInstance(){
		if(instance == null){
			instance = new WaitingDialog();
		}
		return instance;
	}
	
	private static final JProgressBar getProgressBarInstance(){
		if(progressBarInstance == null){
			progressBarInstance = new JProgressBar();
			progressBarInstance.setIndeterminate(true);
			progressBarInstance.setForeground(Color.GREEN);
		}
		return progressBarInstance;
	}
	
	private static final JLabel getLabelInstance(){
		if(labelInstance == null){
			labelInstance = new JLabel();
		}
		return labelInstance;
	}
	
	public static void showDialog(){
		getInstance().setVisible(true);
	}
	
	public static void disposeDialog(){
		getInstance().dispose();
	}
	
	public static void setProgressBarMaxValue(int newMaxValue){
		getProgressBarInstance().setMinimum(0);
		getProgressBarInstance().setMaximum(newMaxValue);
		getProgressBarInstance().setIndeterminate(false);
		getProgressBarInstance().setValue(0);
		setLabelText(Messages.getString(Lang.LABEL_PREPARING));
	}
	
	public static void incrementProgressBarValue(){
		getProgressBarInstance().setValue(getProgressBarInstance().getValue() + 1);
	}
	
	public static void setLabelText(String newLabelText){
		getLabelInstance().setText(String.format("%s ...", newLabelText));
		getInstance().pack();
	}
	
	public static void setLabelText(WebAction executingAction){
		setLabelText(HCIUtils.getMessageOfWebAction(executingAction));
	}

}
