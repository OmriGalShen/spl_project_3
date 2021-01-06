package bgu.spl.net.impl.BGRSServer;


import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ArrayList<Course> courses;
	private ArrayList<Integer> coursesFileOrder;

	private static class DatabaseHolder { // singleton pattern
		private static Database instance = new Database(); // Only happens once on first call of getInstance()
	}

	//to prevent user from creating new Database
	private Database() {
		initialize("Courses.txt"); // Only happens once on first call of getInstance()
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
		coursesFileOrder = new ArrayList<>();// save the course number by the order in file
		try (Scanner fileScanner= new Scanner(new File(coursesFilePath))){ //auto close if problem occur
			courses = new ArrayList<>();
			while (fileScanner.hasNextLine()){ //read line by line
				// split course data based on format
				// 42|How to Train Your Dragon|[43,2,32,39]|25
				String courseString = fileScanner.nextLine();
				String[] courseData = courseString.split("\\|");
				int courseNum = Integer.parseInt(courseData[0]);
				String courseName = courseData[1];

				// -- get kdam courses from format [43,2,32,39]
				String temp = courseData[2].substring(1,courseData[2].length()-1); // removed []
				String[] kdamString = temp.split(","); // get array of course numbers
				ArrayList<Integer> kdamCoursesList = new ArrayList<>();
				for (String s : kdamString) { // "build" kdamCoursesList
					if(s.length()>0)
						kdamCoursesList.add(Integer.parseInt(s));
				}

				int numOfMaxStudents = Integer.parseInt(courseData[3]);
				// Add new course
				courses.add(new Course(courseNum,numOfMaxStudents,courseName,kdamCoursesList));
				coursesFileOrder.add(courseNum);
			}
		courses.forEach(course -> // for every course sort kdam courses by the order in file
				course.getKdamCoursesList()
				.sort(Comparator.comparingInt(coursesFileOrder::indexOf)));

			return true; //file read successful
		}
		catch (Exception e){
			System.out.println("Problem reading coursesFile");
			e.printStackTrace();
		}
		return false;  //file read failed
	}


}
