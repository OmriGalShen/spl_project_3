package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private String username,password;
    private boolean isAdmin;
    ArrayList<Integer> userCourses; // user database with list of registered courses by number
    private boolean loggedIn;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.userCourses = new ArrayList<>();
        this.loggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void registerCourse(int courseNum){
        userCourses.add(courseNum);
    }

    public void unregisterCourse(int courseNum){
        userCourses.remove((Integer) courseNum);
    }

    public boolean getStat() {
        return loggedIn;
    }

    public void setStat(boolean stat) {
        this.loggedIn = stat;
    }



    /**
     * Return a String of the courses number(in the format:[<coursenum1>,<coursenum2>])
     * that the user has registered to (could be empty []).
     * @param username user to get courses from
     * @return String of the courses numbers
     */
    public String getCoursesString(String username){
        Database.getInstance().sortCourses(this.userCourses); // sort by file order
        return Database.listToString(userCourses);
    }

    public ArrayList getCourses(String username){
        return userCourses;
    }

    public boolean isRegistered(int courseNum) {
        return this.userCourses.contains(courseNum);
    }

}
