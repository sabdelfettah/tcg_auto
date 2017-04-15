package tcg_auto.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tcg_auto.hci.SubscriptionListPanel;
import tcg_auto.hci.TrayButton;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.model.Subscription;
import tcg_auto.utils.TCGUtils;

public abstract class SubscriptionManager {
	
	// STATIC FIELDS
	private static Timer timerInstance = null;
	private static List<Subscription> subscriptionListInstance = null;
	private static boolean initialized = false;
	
	
	// STATIC METHODS
	private static Timer getTimerInstance(){
		if(timerInstance == null){
			timerInstance = new Timer();
		}
		return timerInstance;
	}
	
	public static List<Subscription> getSubscriptionList(){
		if(subscriptionListInstance == null){
			subscriptionListInstance = new ArrayList<Subscription>();
		}
		return subscriptionListInstance;
	}
	
	public static void initializeSubscriptionList(List<Subscription> subscriptionList) throws Exception{
		if(initialized){
			throw new Exception("Subscription list already initialized");
		}
		initialized = true;
		getSubscriptionList().addAll(subscriptionList);
		for(Subscription subscription : subscriptionList){
			subscription.programSigningCourse();
		}
		SubscriptionListPanel.updateSubscriptionList();
	}
	
	public static boolean addSubscriptionToSubscriptionList(Subscription subscriptionToAdd){
		subscriptionToAdd.programSigningCourse();
		return getSubscriptionList().add(subscriptionToAdd);
	}
	
	public static boolean removeSubscriptionFromSubscriptionList(Subscription subscriptionToRemove){
		return getSubscriptionList().remove(subscriptionToRemove);
	}
	
	public static void scheduleSigningCourse(Subscription subscriptionToSchedule){
		Date nextExecutingDate = subscriptionToSchedule.nextExecutingDate();
		getTimerInstance().schedule(new CourseTask(subscriptionToSchedule), nextExecutingDate);
		LogManager.logInfo(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_CREATE_TASK_SUCCESS), subscriptionToSchedule, nextExecutingDate));
	}
	
	// CLASSES
	private static class CourseTask extends TimerTask {
		
		private Subscription subscriptionToSchedule;
		
		public CourseTask(Subscription subscriptionToSchedule){
			this.subscriptionToSchedule = subscriptionToSchedule;
		}
		
		@Override
		public void run() {
			TrayButton.displayInfo(Messages.getString(Lang.TITLE_COURSE_BOOKING), String.format(Messages.getString(Lang.MESSAGE_TRAY_INFO_TRY_BOOKING), subscriptionToSchedule.getCourseName()));
			TCGUtils.bookingCourse(subscriptionToSchedule.getCourse());
		}
		
	}
	
}
