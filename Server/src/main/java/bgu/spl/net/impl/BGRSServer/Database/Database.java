package bgu.spl.net.impl.BGRSServer.Database;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ConcurrentLinkedQueue<Course> courses;

	private static class DatabaseHolder { // singleton pattern
		private static Database instance = new Database();
	}

	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return DatabaseHolder.instance;
	} // singleton pattern
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		try (Scanner fileScanner= new Scanner(coursesFilePath)){ //auto close if problem occur
			courses = new ConcurrentLinkedQueue<>();
			while (fileScanner.hasNextLine()){ //read line by line
				// split course data based on format
				// 42|How to Train Your Dragon|[43,2,32,39]|25
				String courseData[] = fileScanner.nextLine().split("|");
				int courseNum = Integer.parseInt(courseData[0]);
				String courseName = courseData[1];

				// -- get kdam courses from format [43,2,32,39]
				String temp = courseData[2].substring(1,courseData[2].length()-1); // removed []
				String[] kdamString = temp.split(","); // get array of course numbers
				ArrayList<Integer> kdamCoursesList = new ArrayList<>();
				for (String s : kdamString) { // "build" kdamCoursesList
					kdamCoursesList.add(Integer.parseInt(s));
				}

				int numOfMaxStudents = Integer.parseInt(courseData[3]);
				// Add new course
				courses.add(new Course(courseNum,numOfMaxStudents,courseName,kdamCoursesList));
			}



			return true; //file read successful
		}
		catch (Exception e){
			System.out.println("Problem reading coursesFile");
			e.printStackTrace();
		}
		return false;  //file read failed
	}


}
