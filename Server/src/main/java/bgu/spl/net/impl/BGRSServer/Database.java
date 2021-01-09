package bgu.spl.net.impl.BGRSServer;


import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ArrayList<Integer> coursesFileOrder; // used to order kdam courses
	private HashMap<Integer,Course> courseDB; // courses database with course number as primary key
	private ConcurrentHashMap<String,User> userDB; // user database with string name as primary key
	private ConcurrentHashMap<String,ArrayList<Integer>> userCourses; // user database with list of registered courses by number
	private final String defaultPath = "Courses.txt";

	private static class DatabaseHolder { // singleton pattern
		private static Database instance = new Database(); // Only happens once on first call of getInstance()
	}

	//to prevent user from creating new Database
	private Database() { // Only called once on first call of getInstance()
		this.courseDB = new HashMap<>();
		this.userDB = new ConcurrentHashMap<>();
		this.userCourses = new ConcurrentHashMap<>();
		this.coursesFileOrder = new ArrayList<>();
		initialize(defaultPath);
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
		try (Scanner fileScanner= new Scanner(new File(coursesFilePath))){ //auto close if problem occur
			while (fileScanner.hasNextLine()) { //read line by line
				addCourse(fileScanner.nextLine());
			}
			sortKdamCourses();
			return true; //file read successful
		}
		catch (Exception e){
			System.out.println("Problem reading coursesFile");
			e.printStackTrace();
		}
		return false;  //file read failed
	}

	/**
	 * Every line the source file converted to new Course object
	 * which then added to the Database
	 * @param courseString line in file describing the course
	 */
	private void addCourse(String courseString){
		// split course data based on format
		// 42|How to Train Your Dragon|[43,2,32,39]|25
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
		// Add new course with courseNum as primary key
		courseDB.put(courseNum,new Course(courseNum,numOfMaxStudents,courseName,kdamCoursesList));
		coursesFileOrder.add(courseNum); // used to keep file order of courses
	}

	/**
	 * for every course sort kdam courses by the order in file
	 * used only when initializing the database
	 */
	private void sortKdamCourses(){
		courseDB.values().forEach(course ->
				course.getKdamCoursesList()
						.sort(Comparator.comparingInt(coursesFileOrder::indexOf)));

	}

	/**
	 * Example for course : 35|Swordsmanship: From Hero to King|[82,30,12]|1
	 * return [82,12,30] (the order is according to the order in the source file)
	 * @param courseNum the course number of the course whose kdam is required
	 * @return string describing the kdam courses
	 */
	public String getKdamCourses(int courseNum){
		return Course.coursesToString(courseDB.get(courseNum).getKdamCoursesList());
	}

	/**
	 * Return a String of the courses number(in the format:[<coursenum1>,<coursenum2>])
	 * that the user has registered to (could be empty []).
	 * @param username user to get courses from
	 * @return String of the courses numbers
	 */
	public String getUserCourses(String username){
		return Course.coursesToString(userCourses.get(username));
	}

	public boolean isRegistered(String username){
		// TODO
		return false;
	}

	public void userRegister(String username, String password){
		// TODO
	}

	public void adminRegister(String username, String password){
		// TODO
	}

	public void unRegister(String username,int courseNum){
		// TODO
	}

	/**
	 * for the received course return <numOfSeatsAvailable> / <maxNumOfSeats>
	 * Example:
	 * Course: (42) How To Train Your Dragon
	 * Seats Available: 22/25
	 * @param courseNum
	 * @return
	 */
	public String getnumOfSeatsAvailable(int courseNum){
		// TODO
		return "";
	}

	/**
	 * Get string describing list of stutends registred to course ordered alphabetically
	 * Example:
	 * Students Registered: [ahufferson, hhhaddock, thevast] //if there are no students registered yet, simply print []
	 * @param courseNum
	 * @return
	 */
	public String listOfStudents(int courseNum){
		ArrayList<String> userList = new ArrayList<>();
		userCourses.forEach((username,courses)->{
			if(courses.contains(courseNum)) // user registered to this course
				userList.add(username); // add his name
		});
		Collections.sort(userList); // sort alphabetically
		return User.usersToString(userList);
	}

}
