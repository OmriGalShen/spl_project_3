package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.Messages.*;

import java.util.ArrayList;

public class BGRSMessagingProtocol implements MessagingProtocol<BGRSMessage> {
    private boolean shouldTerminate = false;
    private User currentUser = null;
    private Database db;

    @Override
    public BGRSMessage process(BGRSMessage msg) {
        this.db = Database.getInstance();
        RequestMessage requestMessage = (RequestMessage) msg;
        short opCode = msg.getOpCode();
        BGRSMessage response = new ErrorMessage(opCode);
        switch (opCode){
            case 1: //ADMINREG
                return adminRegistration(requestMessage);
            case 2: //STUDENTREG
                return studentRegistration(requestMessage);
            case 3: //LOGIN
                return login(requestMessage);
            case 4: //LOGOUT
                return logout();
            case 5: //COURSEREG
                return courseRegistration(requestMessage);
            case 6: //KDAMCHECK
                return kdamCheck(requestMessage);
            case 7: //COURSESTAT
                return courseState(requestMessage);
            case 8: //STUDENTSTAT
                return studentStatus(requestMessage);
            case 9: //ISREGISTERED
                return isRegistered(requestMessage);
            case 10: //UNREGISTER
                return unRegister(requestMessage);
            case 11: //MYCOURSES
                return myCourses();
            default: // opcode not valid
                response = new ErrorMessage(msg.getOpCode());
                break;
        }
        response = new ErrorMessage(opCode);
        return response;
    }




    /**
     * An ADMINREG message is used to register an admin in the service. If the username is already registered in the server,
     * an ERROR message is returned. If successful an ACK message will be sent in return. Both string parameters are a sequence
     * of bytes in UTF-8 terminated by a zero byte (also known as the ‘\0’ char).
     * @param requestMessage
     * @return
     */
    private BGRSMessage adminRegistration(RequestMessage requestMessage) {
        // should let register if user logged in?                           ///// no. handled - Eden /////
        short opCode = 1;
        if (currentUser != null) {
            System.out.println("ADMINREG - can't register: someone is already logged in"); // debugging!
            return new ErrorMessage(opCode);
        }
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        if (db.isRegistered(username)) {
            System.out.println("ADMINREG - this user already registered"); // debugging!
            return new ErrorMessage(opCode);
        }
        db.userRegister(username,password,true);


        System.out.println("ADMINREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!


        return new ACKMessage(opCode,"ADMINREG was received");
    }




    /**s
     * A STUDENTREG message is used to register a student in the service. If the username is already registered in the server,
     * an ERROR message is returned. If successful an ACK message will be sent in return.
     * @param requestMessage
     * @return
     */
    private BGRSMessage studentRegistration(RequestMessage requestMessage) {
        // should let register if user logged in?                       ///// no. handled - Eden /////
        short opCode = 2;
        if (currentUser != null) {
            System.out.println("STUDENTREG - can't register: someone is already logged in"); // debugging!
            return new ErrorMessage(opCode);
        }
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        if (db.isRegistered(username)) {
            System.out.println("STUDENTREG - can't register: this user is already registered"); // debugging!
            return new ErrorMessage(opCode);
        }
        db.userRegister(username,password,false);


        System.out.println("STUDENTREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!


        return new ACKMessage(opCode,"STUDENTREG was received");
    }




    /**
     * A LOGIN message is used to login a user into the server. If the user doesn’t exist or the password
     * doesn’t match the one entered for the username, sends an ERROR message.
     * An ERROR message should also appear if the current client has already successfully logged in.
     * @param requestMessage
     * @return
     */
    private BGRSMessage login(RequestMessage requestMessage) {
        // should let login if user already logged in?                                               ///// no. handled - Eden /////
        short opCode = 3;
        if (currentUser != null) { // another user is already logged in
            System.out.println("LOGIN - someone is already logged in"); // debugging!
            return new ErrorMessage(opCode);
        }
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        if (!db.isRegistered(username)) { // this user is not registered
            System.out.println("LOGIN - this user is not registered"); // debugging!
            return new ErrorMessage(opCode);
        }
        if(!db.getUser(username).getPassword().equals(password)) { // wrong password
            System.out.println("LOGIN - wrong password"); // debugging!
            return new ErrorMessage(opCode);
        }
        this.currentUser = db.getUser(username);


        System.out.println("LOGIN"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!


        return new ACKMessage(opCode,"LOGIN was received");
    }




    /**
     * Messages that appear only in a Client-to-Server communication. Informs the server on client disconnection.
     * Client may terminate only after receiving an ACK message in replay. If no user is logged in, sends an ERROR message.
     * @return
     */
    private BGRSMessage logout() {
        short opCode = 4;
        if (currentUser == null) { // no user is logged in
            System.out.println("LOGOUT - no user is logged in"); // debugging!
            return new ErrorMessage(opCode);
        }
        this.shouldTerminate = true;


        System.out.println("LOGOUT"); // debugging!


        return new ACKMessage(opCode,"LOGOUT was received");   ///// the instructions says "Client may terminate only after receiving an ACK message in reply" so is that ok? - Eden /////
    }




    /**
     * Messages that appear only in a Client-to-Server communication.
     * Inform the server about the course the student want to register to, if the registration done successfully,
     * an ACK message will be sent back to the client, otherwise, (e.g. no such course is exist,
     * no seats are available in this course, the student does not have all the Kdam courses, the student is not logged in)
     * ERR message will be sent back.
     * (Note: the admin can’t register to courses, in case the admin sends a COURSEREG message, and ERR message will be sent back to the client).
     * @param requestMessage
     * @return
     */
    private BGRSMessage courseRegistration(RequestMessage requestMessage) {
        // return error if course doesn't exist
        short opCode = 5;
        if (currentUser == null || currentUser.isAdmin()) { // no student is logged in
            System.out.println("COURSEREG - no student is logged in"); // debugging!
            return new ErrorMessage(opCode);
        }

        short courseNumber = requestMessage.getCourseNum();
        if (db.getCourse(courseNumber) == null) { // this course doesn't exist
            System.out.println("COURSEREG - there is not such course"); // debugging!
            return new ErrorMessage(opCode);
        }

        Course currCourse = db.getCourse(courseNumber);
        int numOfMaxStudents = currCourse.getNumOfMaxStudents();
        int numOfRegStudents = currCourse.currNumOfStudents();
        if (numOfMaxStudents <= numOfRegStudents) { // no seats are available in this course
            System.out.println("COURSEREG - no seats are available in this course"); // debugging!
            return new ErrorMessage(opCode);
        }

        String username = requestMessage.getOperations().get(0);
        String studentKdams = currentUser.getCoursesString(username);
        String neededKdams = currCourse.getKdamString();
        if (!studentKdams.equals(neededKdams)) { // the student doesn't have all the Kdam courses
            System.out.println("COURSEREG - the student doesn't have all the Kdam courses"); // debugging!
            return new ErrorMessage(opCode);
        }

        db.registerToCourse(username, courseNumber);


        System.out.println("COURSEREG"); // debugging!
        System.out.println("courseNum:"+courseNumber); // debugging!


        return new ACKMessage(opCode,"COURSEREG was received");
    }




    /**
     *
     Messages that appear only in a Client-to-Server communication.
     KDAMCHECK this message checks what are the KDAM courses of the specified course.
     If student registered to a course successfully, we consider him having this course as KDAM
     When the server gets the message it returns the list of the KDAM courses, in the SAME ORDER as in the courses file
     (if there are now KDAM courses it returns empty string)
     * @param requestMessage
     * @return
     */
    private BGRSMessage kdamCheck(RequestMessage requestMessage) {
        // should let check if user logged in?
        // return error if course doesn't exist
        short opCode = 6;
        short courseNumber = requestMessage.getCourseNum();
        if (db.getCourse(courseNumber) == null) { // this course doesn't exist
            System.out.println("COURSEREG - there is not such course"); // debugging!
            return new ErrorMessage(opCode);
        }

        System.out.println("KDAMCHECK - NOT WORKING YET");// debugging!
//        System.out.println("courseNum:"+courseNumber);// debugging!
//        if(currentUser!=null){ //user is logged in
//            return new ACKMessage(opCode,"KDAMCHECK was received");
//        }
//        else{ // user is not logged in
//
//        }
        return new ErrorMessage(opCode);
    }




    /**
     * The admin sends this message to the server to get the state of a specific course.
     * the client should prints the state of the course as followed:
     * Course: (<courseNum>) <courseName>
     * Seats Available: <numOfSeatsAvailable> / <maxNumOfSeats>
     * Students Registered: <listOfStudents> //ordered alphabetically
     * Example:
     * Course: (42) How To Train Your Dragon
     * Seats Available: 22/25
     * Students Registered: [ahufferson, hhhaddock, thevast] //if there are no students registered yet, simply print []
     * @param requestMessage
     * @return
     */
    private BGRSMessage courseState(RequestMessage requestMessage) {
        // return error if course doesn't exist
        short opCode = 7;
        if (currentUser == null || !currentUser.isAdmin()) { // no admin is logged in at the moment
            System.out.println("COURSESTAT - no admin is logged in at the moment"); // debugging!
            return new ErrorMessage(opCode);
        }

        short courseNumber = requestMessage.getCourseNum();
        if (db.getCourse(courseNumber) == null) { // there is no such course
            System.out.println("COURSESTAT - there is no such course"); // debugging!
            return new ErrorMessage(opCode);
        }

        Course currCourse = db.getCourse(courseNumber);
        String courseName = currCourse.getCourseName();
        String regStudents = currCourse.listOfStudents();
        int numOfReg = currCourse.currNumOfStudents();
        int maxNumOfReg = currCourse.getNumOfMaxStudents();



        System.out.println("COURSESTAT  ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!


        return new ACKMessage(opCode,"COURSESTAT was received");
    }




    /**
     * A STUDENTSTAT message is used to receive a status about a specific student.
     * the client should print the state of the course as followed:
     * Student: <studentUsername>
     * Courses: <listOfCoursesNumbersStudentRegisteredTo> //ordered in the same order as in the courses file
     * Example:
     *         Student: hhhaddock
     *         Courses: [42] // if the student hasn’t registered to any course yet, simply print []
     * @param requestMessage
     * @return
     */
    private BGRSMessage studentStatus(RequestMessage requestMessage) {
        short opCode = 8;
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        System.out.println("STUDENTSTAT"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"STUDENTSTAT  was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }




    /**
     * An ISREGISTERED message is used to know if the student is registered to the specified course.
     * The server send back “REGISTERED” if the student is already registered to the course,
     * otherwise, it sends back “NOT REGISTERED”.
     * @param requestMessage
     * @return
     */
    private BGRSMessage isRegistered(RequestMessage requestMessage) {
        // return error if course doesn't exist
        short opCode = 9;
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("ISREGISTERED");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"ISREGISTERED  was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }





    /**
     * An UNREGISTER message is used to unregister to a specific course
     * The server sends back an ACK message if the registration process successfully done, otherwise, it sends back an ERR message.
     * @param requestMessage
     * @return
     */
    private BGRSMessage unRegister(RequestMessage requestMessage) {
        // return error if course doesn't exist
        short opCode = 10;
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("UNREGISTER ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"UNREGISTER was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }




    /**
     * A MYCOURSES message is used to know the courses the student has registered to.
     * The server sends back a list of the courses number(in the format:[<coursenum1>,<coursenum2>])
     * that the student has registered to (could be empty []).
     * @return
     */
    private BGRSMessage myCourses() {
        short opCode = 11;
        System.out.println("MYCOURSES");// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"MYCOURSES  was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}