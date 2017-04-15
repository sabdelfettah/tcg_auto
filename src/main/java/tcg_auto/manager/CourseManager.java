package manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import model.Course;

public class CourseManager {
	
	// STATIC FIELDS
	private static List<Course> courseListInstance;
	
	// STATIC METHODS
	public static List<Course> getCourseList(){
		if(courseListInstance == null){
			initCourseList();
		}
		return courseListInstance;
	}
	
	public static void initCourseList(){
		courseListInstance = new ArrayList<Course>();
	}
	
//	public static void updateCourseList(List<Course> newCourseList){
//		courseListInstance = newCourseList;
//	}
	
	public static boolean addCourseToCourseList(Course courseToAdd){
		return getCourseList().add(courseToAdd);
	}
	
	public static List<Course> getRoomOneCourseList(){
		return getFilteredCourseListByRoom(Course.Room.ROOM_1);
	}
	
	public static List<Course> getRoomTwoCourseList(){
		return getFilteredCourseListByRoom(Course.Room.ROOM_2);
	}
	
	private static List<Course> getFilteredCourseListByRoom(Course.Room filtredRoom){
		return getCourseList()	.parallelStream()
								.filter(course -> course != null & filtredRoom.equals(course.getRoom()))
								.collect(Collectors.toList());
	}
	
	public static void sortCourseList(){
		Comparator<Course> compareRoom = (course1, course2) -> course1.getRoom().compareTo(course2.getRoom());
		Comparator<Course> compareDay = (course1, course2) -> course1.getDay().compareTo(course2.getDay());
		Comparator<Course> compareHour = (course1, course2) -> Short.compare(course1.getHour(), course2.getHour());
		Comparator<Course> compareMinute = (course1, course2) -> Short.compare(course1.getMinute(), course2.getMinute());
		getCourseList().sort(compareRoom.thenComparing(compareDay).thenComparing(compareHour).thenComparing(compareMinute));
	}
	
	public static List<Course> getNotYetConfiguredCourseList(){
		List<String> alreadyConfiguredCourseList = SubscriptionManager
																		.getSubscriptionList()
																		.parallelStream()
																		.map(subscription -> subscription.getCourseName())
																		.collect(Collectors.toList());
		List<Course> result = getCourseList()	.parallelStream()
												.filter(course -> !alreadyConfiguredCourseList.contains(course.getName()))
												.collect(Collectors.toList());
		return result;
		
	}

}
