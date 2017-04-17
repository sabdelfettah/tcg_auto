package tcg_auto.model;

import java.util.Calendar;
import java.util.Date;

import tcg_auto.manager.SubscriptionManager;
import tcg_auto.utils.MiscUtils;

public class Subscription {
	
	// NOT STATIC FIELDS
	private Course course;
	private Course.Day configDay;
	private short configHour;
	private short configMinute;
	private Date nextExecutingDate;
	
	// CONSTRUCTORS
	public Subscription(Course course, Course.Day configDay, short configHour, short configMinute){
		this.course = course;
		this.configDay = configDay;
		this.configHour = configHour;
		this.configMinute = configMinute;
	}
	
	// SETTERS
	public void cancelOrTerminateCourseTask(){
		this.nextExecutingDate = null;
	}
	
	public void setNextExecutingDate(Date nextExecutingDate){
		this.nextExecutingDate = nextExecutingDate;
	}
	
	// GETTERS
	public Course getCourse(){
		return course;
	}
	public String getCourseName(){
		return course.getName();
	}
	
	public Date getNextExecutingDate(){
		return this.nextExecutingDate;
	}
	
	// OTHER NOT STATIC METHODS
	public void programSigningCourse(){
		SubscriptionManager.scheduleSigningCourse(this);
	}
	
	public Date computeNextExecutingDate(){
		Calendar nowCalendar = Calendar.getInstance();
		Calendar resultCalendar = Calendar.getInstance();
		switch(configDay){
		case MONDAY    : resultCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);    break;
		case THURSDAY  : resultCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);  break;
		case WEDNESDAY : resultCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); break;
		case TUESDAY   : resultCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);   break;
		case FRIDAY    : resultCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);    break;
		}
		if(nowCalendar.after(resultCalendar)){
			resultCalendar.add(Calendar.WEEK_OF_MONTH, 1);
		}
		resultCalendar.set(Calendar.HOUR_OF_DAY, configHour);
		resultCalendar.set(Calendar.MINUTE, configMinute);
		resultCalendar.set(Calendar.SECOND, 0);
		resultCalendar.set(Calendar.MILLISECOND, 0);
		return resultCalendar.getTime();
	}
	
	public String toString(){
		String result = String.format("%s - %s:%s - %s", configDay, MiscUtils.getTimeAsString(configHour), MiscUtils.getTimeAsString(configMinute), course.getName());
		return result;
	}
	
}
