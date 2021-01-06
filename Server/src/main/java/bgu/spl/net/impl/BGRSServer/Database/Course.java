package bgu.spl.net.impl.BGRSServer.Database;

import java.util.ArrayList;

public class Course {
    private int courseNum,numOfMaxStudents;
    private String courseName;
    private ArrayList<String> kdamCoursesList;

    public Course(int courseNum, int numOfMaxStudents, String courseName, ArrayList<String> kdamCoursesList) {
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

    public ArrayList<String> getKdamCoursesList() {
        return kdamCoursesList;
    }

    public void setKdamCoursesList(ArrayList<String> kdamCoursesList) {
        this.kdamCoursesList = kdamCoursesList;
    }

    public String getKdamCoursesString(){
        String kdamCourses="[";
        for(String course:kdamCoursesList){
            kdamCourses+=course;
        }
        kdamCourses+="]";
        return kdamCourses;
    }
}
