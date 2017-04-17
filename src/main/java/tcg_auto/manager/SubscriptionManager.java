package tcg_auto.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import tcg_auto.hci.CourseTaskListPanel;
import tcg_auto.hci.SubscriptionListPanel;
import tcg_auto.hci.TrayButton;
import tcg_auto.lang.Lang;
import tcg_auto.lang.Messages;
import tcg_auto.model.Subscription;
import tcg_auto.utils.MiscUtils;
import tcg_auto.utils.TCGUtils;

public abstract class SubscriptionManager {
	
	// STATIC FIELDS
	private static Timer timerInstance = null;
	private static List<Subscription> subscriptionListInstance = null;
	private static boolean initialized = false;
	private static Map<Subscription, CourseTask> courseTasksOfSubscription;
	
	
	// STATIC METHODS
	private static Timer getTimerInstance(){
		if(timerInstance == null){
			timerInstance = new Timer(true);
		}
		return timerInstance;
	}
	
	public static List<Subscription> getSubscriptionList(){
		if(subscriptionListInstance == null){
			subscriptionListInstance = new ArrayList<Subscription>();
			courseTasksOfSubscription = new HashMap<Subscription, CourseTask>();
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
		boolean result = getSubscriptionList().add(subscriptionToAdd);
		ConfigManager.saveConfig();
		SubscriptionListPanel.updateSubscriptionList();
		return result;
	}
	
	public static boolean removeSubscriptionFromSubscriptionList(Subscription subscriptionToRemove){
		CourseTask courseToCancel = courseTasksOfSubscription.get(subscriptionToRemove);
		if(courseToCancel != null){
			courseToCancel.cancel();
		}
		subscriptionToRemove.cancelOrTerminateCourseTask();
		boolean result = getSubscriptionList().remove(subscriptionToRemove);
		ConfigManager.saveConfig();
		SubscriptionListPanel.updateSubscriptionList();
		return result;
	}
	
	public static void scheduleSigningCourse(Subscription subscriptionToSchedule){
		Date nextExecutingDate = subscriptionToSchedule.computeNextExecutingDate();
		CourseTask courseTask = new CourseTask(subscriptionToSchedule);
		courseTasksOfSubscription.put(subscriptionToSchedule, courseTask);
		subscriptionToSchedule.setNextExecutingDate(nextExecutingDate);
		try{
			getTimerInstance().schedule(courseTask, nextExecutingDate);
		}catch(IllegalStateException e){
			timerInstance = new Timer();
			getTimerInstance().schedule(courseTask, nextExecutingDate);
		}
		LogManager.logInfo(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_CREATE_TASK_SUCCESS), subscriptionToSchedule, MiscUtils.formatDateBeginWithDay(nextExecutingDate)));
	}
	
	public static List<CourseTask> getCourseTasks(){
		List<CourseTask> result = getSubscriptionList()	.parallelStream()
														.map(subscription -> courseTasksOfSubscription.get(subscription))
														.filter(course -> course != null)
														.collect(Collectors.toList());
		return result;
	}
	
	public static void scheduleTasksReInitializer(){
		Calendar now = Calendar.getInstance();
		Calendar targetDate = Calendar.getInstance();
		targetDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		targetDate.set(Calendar.HOUR_OF_DAY, 23);
		targetDate.set(Calendar.MINUTE, 59);
		targetDate.set(Calendar.SECOND, 0);
		targetDate.set(Calendar.MILLISECOND, 0);
		if(targetDate.before(now)){
			targetDate.add(Calendar.WEEK_OF_MONTH, 1);
		}
		Date executionDate = targetDate.getTime();
		getTimerInstance().schedule(new TasksReInitializer(), executionDate);
		LogManager.logInfo(String.format(Messages.getString(Lang.LOG_MESSAGE_INFO_CREATE_TASKS_INITIALIZER_SUCCESS), MiscUtils.formatDateBeginWithDay(executionDate)));
	}
	
	// CLASSES
	public static class CourseTask extends TimerTask {
		
		private Subscription subscriptionToSchedule;
		
		public CourseTask(Subscription subscriptionToSchedule){
			this.subscriptionToSchedule = subscriptionToSchedule;
			courseTasksOfSubscription.put(subscriptionToSchedule, this);
		}
		
		@Override
		public void run() {
			courseTasksOfSubscription.put(subscriptionToSchedule, null);
			subscriptionToSchedule.cancelOrTerminateCourseTask();
			TrayButton.displayInfo(Messages.getString(Lang.TITLE_COURSE_BOOKING), String.format(Messages.getString(Lang.MESSAGE_TRAY_INFO_TRY_BOOKING), subscriptionToSchedule.getCourseName()));
			TCGUtils.bookingCourse(subscriptionToSchedule.getCourse());
		}
		
		public String toString(){
			String result = String.format("%s - %s", MiscUtils.formatDateBeginWithDay(new Date(this.scheduledExecutionTime())), subscriptionToSchedule);
			return result;
		}
		
	}
	
	private static class TasksReInitializer extends TimerTask {
		
		@Override
		public void run() {
			SubscriptionManager.getCourseTasks().forEach(courseTask -> {
				courseTask.cancel();
			});
			SubscriptionManager.getSubscriptionList().forEach(subscription -> {
				subscription.programSigningCourse();
			});
			CourseTaskListPanel.updateCourseTaskList();
			scheduleTasksReInitializer();
		}
		
	}
}
