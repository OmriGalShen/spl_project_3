package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private int courseNum,numOfMaxStudents;
    private String courseName;
    private ArrayList<Integer> kdamCoursesList;
    private ConcurrentLinkedQueue<String> registeredUsers;

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

    public void registerUser(String username){
        this.registeredUsers.add(username);
    }

    public void unregisterUser(String username){
        this.registeredUsers.remove(username);
    }

    /**
     * Get string describing list of stutends registred to course ordered alphabetically
     * Example:
     * Students Registered: [ahufferson, hhhaddock, thevast] //if there are no students registered yet, simply print []
     * @return string describing list of stutends registred to course rdered alphabetically
     */
    public String listOfStudents(){
        ArrayList<String> userList = new ArrayList<>(registeredUsers);
        Collections.sort(userList); // sort alphabetically
        String userString="[";
        for(String course:userList){
            userString+=course+",";
        }
        if(userString.length()>1) //edge case
            userString = userString.substring(0,userString.length()-1); // remove last ','
        userString+="]";
        return userString;
    }
}
