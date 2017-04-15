package tcg_auto.model;

import java.util.Calendar;
import lang.Lang;
import lang.Messages;
import utils.HCIUtils;
import utils.MiscUtils;

public class Course {
	
	// NOT STATIC FIELDS
	private String name;
	private Room room;
	private Day day;
	private short hour;
	private short minute;
	
	// CONSTRUCTORS
	public Course(String name, Room room, Day day, short hour, short minute){
		this.day = day;
		this.name = name;
		this.room = room;
		this.hour = hour;
		this.minute = minute;
	}
	
	public Course(String name, Room room, Day day, short[] timeOptions){
		this.day = day;
		this.name = name;
		this.room = room;
		this.hour = timeOptions != null && timeOptions.length > 0 ? timeOptions[0] : 0;
		this.minute = timeOptions != null && timeOptions.length > 1 ? timeOptions[1] : 0;
	}
	
	// GETTERS
	public String getName(){
		return name;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public Day getDay(){
		return day;
	}
	
	public String getRoomName(){
		return room.toString();
	}
	
	public String getDayAsString(){
		return day == null ? "" : day.toString();
	}
	
	public int getCalendarDay(){
		return day == null ? -1 : day .getCalendarDay();
	}
	
	public short getHour(){
		return hour;
	}
	
	public short getMinute(){
		return minute;
	}
	
	public String toString(){
		String roomLabel = Room.ROOM_1.equals(room) ? Messages.getString(Lang.LABELS_LABEL_ROOM_1) : Messages.getString(Lang.LABELS_LABEL_ROOM_2);
		String result = String.format("%s - %s - %s:%s - %s", roomLabel, getDayAsString(), MiscUtils.getTimeAsString(hour), MiscUtils.getTimeAsString(minute), name);
		return result;
	}
	
	// ENUMERATIONS
	public enum Room {
		ROOM_1, ROOM_2,
	}
	
	public enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY;
		
		public String toString(){
			return HCIUtils.getLabelOfEnumDay(this);
		}
		
		public int getCalendarDay(){
			switch(this){
			case MONDAY: return Calendar.MONDAY;
			case TUESDAY: return Calendar.TUESDAY;
			case WEDNESDAY: return Calendar.WEDNESDAY;
			case THURSDAY: return Calendar.THURSDAY;
			case FRIDAY: return Calendar.FRIDAY;
			default: return -1;
			}
		}
		
		public static Day getDayFromCalendarDay(int day){
			switch(day){
			case Calendar.MONDAY: return Day.MONDAY;
			case Calendar.TUESDAY: return Day.TUESDAY;
			case Calendar.WEDNESDAY: return Day.WEDNESDAY;
			case Calendar.THURSDAY: return Day.THURSDAY;
			case Calendar.FRIDAY: return Day.FRIDAY;
			default: return null;
			}
		}
	}

}
