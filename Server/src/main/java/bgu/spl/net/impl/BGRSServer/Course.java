package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private int courseNum, numOfMaxStudents, numOfRegStudents;
    private String courseName;
    private ArrayList<Integer> kdamCoursesList;
    private ConcurrentLinkedQueue<String> registeredUsers; //multiple users accesses this list!

    public Course(int courseNum, int numOfMaxStudents, String courseName, ArrayList<Integer> kdamCoursesList) {
        this.courseNum = courseNum;
        this.numOfMaxStudents = numOfMaxStudents;
        this.courseName = courseName;
        this.kdamCoursesList = kdamCoursesList;

    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public int getNumOfRegStudents() {
        return numOfRegStudents;
    }

    public void setNumOfMaxStudents(int numOfMaxStudents) {
        this.numOfMaxStudents = numOfMaxStudents;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<Integer> getKdamCoursesList() {
        return kdamCoursesList;
    }

    public void setKdamCoursesList(ArrayList<Integer> kdamCoursesList) {
        this.kdamCoursesList = kdamCoursesList;
    }

    /**
     * Add a username to the users list
     * should not be accesses directly this is used by the database
     * @param username
     */
    void registerUser(String username){
        this.registeredUsers.add(username);
        this.numOfRegStudents++;
    }

    /**
     * Remove a username to the users list
     * should not be accesses directly this is used by the database
     * @param username
     */
    public void unregisterUser(String username){
        this.registeredUsers.remove(username);
        this.numOfRegStudents--;
    }
    /*
    * Get the course's kdam courses in the following format
    * [82,12,30]. the courses list is sorted according to the order in the
    * courses file. (could be empty [])
     */
    public String getKdamString(){
        return Database.listToString(kdamCoursesList);
    }

    /**
     * Get string describing list of students registered to course ordered alphabetically
     * Example:
     * Students Registered: [ahufferson, hhhaddock, thevast] //if there are no students registered yet, simply print []
     * @return string describing list of stutends registred to course rdered alphabetically
     */
    public String listOfStudents(){
        ArrayList<String> userList = new ArrayList<>(registeredUsers);
        Collections.sort(userList); // sort alphabetically
        return Database.listToString(userList);
    }
}
