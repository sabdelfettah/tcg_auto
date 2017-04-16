package tcg_auto.utils;

import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import org.openqa.selenium.WebElement;

import tcg_auto.hci.WaitingDialog;
import tcg_auto.selenium.TCG;
import tcg_auto.selenium.TCG.WebAction;

@SuppressWarnings("rawtypes")
public class ActionWorker extends SwingWorker<Map<String, List>, List<WebElement>> {
	
	// NOT STATIC FIELDS
	private TCG tcg;
	
	// CONSTRUCTORS
	public ActionWorker(List<WebAction> webActionList){
		this.tcg = new TCG(webActionList);
	}
	
	public ActionWorker(List<WebAction> webActionList, Map<String, Object> arguments){
		this.tcg = new TCG(webActionList, arguments);
	}
	
	// NOT STATIC METHODS
	@Override
	protected Map<String, List> doInBackground() throws Exception {
		Map<String, List> result = tcg.execute();
		return result;
	}
	
	@Override
    protected void done() {
		WaitingDialog.disposeDialog();
    }
	
	// STATIC METHODS
	public static ActionWorker getNewActionWorkerInstance(List<WebAction> webActionList){
		ActionWorker newInstance = new ActionWorker(webActionList);
		return newInstance;
	}
	
	public static ActionWorker getNewActionWorkerInstance(List<WebAction> webActionList, Map<String, Object> arguments){
		ActionWorker newInstance = new ActionWorker(webActionList, arguments);
		return newInstance;
	}
	
}
