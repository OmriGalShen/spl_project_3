package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.Messages.*;

import java.util.ArrayList;

public class BGRSMessagingProtocol implements MessagingProtocol<RGRSMessage>{
    private boolean shouldTerminate = false;
    private User currentUser=null;

    @Override
    public RGRSMessage process(RGRSMessage msg) {
        RequestMessage requestMessage = (RequestMessage) msg;
        short opCode = msg.getOpCode();
        RGRSMessage response = new ErrorMessage(opCode);
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

    private RGRSMessage adminRegistration(RequestMessage requestMessage){
        // should let register if user logged in?
        short opCode = 1;
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        System.out.println("ADMINREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage(opCode,"ADMINREG was received");
    }

    private RGRSMessage studentRegistration(RequestMessage requestMessage){
        // should let register if user logged in?
        short opCode = 2;
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        System.out.println("STUDENTREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage(opCode,"STUDENTREG was received");
    }

    private RGRSMessage login(RequestMessage requestMessage) {
        // should let login if user already logged in?
        short opCode = 3;
        ArrayList<String> operations = requestMessage.getOperations();
        String username = operations.get(0);
        String password = operations.get(1);
        System.out.println("LOGIN"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage(opCode,"LOGIN was received");
    }

    private RGRSMessage logout(){
        short opCode = 4;
        System.out.println("LOGOUT");// debugging!
        if(currentUser!=null){ //user is logged in
            this.shouldTerminate=true;
            return new ACKMessage(opCode,"LOGOUT was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }

    private RGRSMessage courseRegistration(RequestMessage requestMessage){
        // return error if course doesn't exist
        short opCode = 5;
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("COURSEREG");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"COURSEREG was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }

    private RGRSMessage kdamCheck(RequestMessage requestMessage){
        // should let check if user logged in?
        // return error if course doesn't exist
        short opCode = 6;
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("KDAMCHECK ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"KDAMCHECK was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }

    private RGRSMessage courseState(RequestMessage requestMessage){
        // return error if course doesn't exist
        short opCode = 7;
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("COURSESTAT  ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        if(currentUser!=null){ //user is logged in
            return new ACKMessage(opCode,"COURSESTAT was received");
        }
        else{ // user is not logged in
            return new ErrorMessage(opCode);
        }
    }

    private RGRSMessage studentStatus(RequestMessage requestMessage) {
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

    private RGRSMessage isRegistered(RequestMessage requestMessage){
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

    private RGRSMessage unRegister(RequestMessage requestMessage){
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

    private RGRSMessage myCourses(){
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
