package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class User {
    private String username,password;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
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

    public static String usersToString(ArrayList<String> userList){
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
