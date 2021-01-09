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
	private final String defaultPath = "Courses.txt";

	private static class DatabaseHolder { // singleton pattern
		private static Database instance = new Database(); // Only happens once on first call of getInstance()
	}

	//to prevent user from creating new Database
	private Database() { // Only called once on first call of getInstance()
		this.courseDB = new HashMap<>();
		this.userDB = new ConcurrentHashMap<>();
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

	public void sortCourses(ArrayList<Integer> userCourses){
		userCourses.sort(Comparator.comparingInt(coursesFileOrder::indexOf));
	}

	public boolean isRegistered(String username){
		return userDB.containsKey(username);
	}

	public void userRegister(String username, String password,boolean isAdmin){
		userDB.putIfAbsent(username,new User(username,password,isAdmin));
	}

	public void unRegisterCourse(String username,int courseNum){
		courseDB.get(courseNum).unregisterUser(username);
		userDB.get(username).unregisterCourse(courseNum);
	}

	public void registerToCourse(String username,int courseNum){
		courseDB.get(courseNum).registerUser(username);
		userDB.get(username).registerCourse(courseNum);
	}

	public Course getCourse(int courseNum){return courseDB.get(courseNum);}

	public User getUser(String username){return userDB.get(username);}

	public static <T> String  listToString(ArrayList<T> list){
		String listString="[";
		for(Object obj:list){
			listString+=obj+",";
		}
		if(listString.length()>1) //edge case
			listString = listString.substring(0,listString.length()-1); // remove last ','
		listString+="]";
		return listString;
	}

}
