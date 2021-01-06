package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class Course {
    private int courseNum,numOfMaxStudents;
    private String courseName;
    private ArrayList<Integer> kdamCoursesList;

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

    public static String coursesToString(ArrayList<Integer> courseList){
        String courseString="[";
        for(Integer course:courseList){
            courseString+=course+",";
        }
        if(courseString.length()>1) //edge case
            courseString = courseString.substring(0,courseString.length()-1); // remove last ','
        courseString+="]";
        return courseString;

    }
}
