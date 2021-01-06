package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.Messages.*;

import java.util.ArrayList;

public class BGRSMessagingProtocol implements MessagingProtocol<RGRSMessage>{
    private boolean shouldTerminate = false;
    private boolean isLogin = false;
    private boolean isAdmin = false;

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
        // TODO
        ArrayList<String> operations = requestMessage.getOperations();
        System.out.println("ADMINREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage((short)1,"ADMINREG was received");
    }

    private RGRSMessage studentRegistration(RequestMessage requestMessage){
        // TODO
        ArrayList<String> operations = requestMessage.getOperations();
        System.out.println("STUDENTREG"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage((short)2,"STUDENTREG was received");
    }

    private RGRSMessage login(RequestMessage requestMessage) {
        ArrayList<String> operations = requestMessage.getOperations();
        System.out.println("LOGIN"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage((short)3,"LOGIN was received");
    }

    private RGRSMessage logout(){
        System.out.println("LOGOUT");// debugging!
        return new ACKMessage((short)4,"LOGOUT was received");
    }

    private RGRSMessage courseRegistration(RequestMessage requestMessage){
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("COURSEREG");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        return new ACKMessage((short)5,"COURSEREG was received");
    }

    private RGRSMessage kdamCheck(RequestMessage requestMessage){
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("KDAMCHECK ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        return new ACKMessage((short)6,"KDAMCHECK was received");
    }

    private RGRSMessage courseState(RequestMessage requestMessage){
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("COURSESTAT  ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        return new ACKMessage((short)7,"COURSESTAT was received");
    }

    private RGRSMessage studentStatus(RequestMessage requestMessage) {
        ArrayList<String> operations = requestMessage.getOperations();
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("STUDENTSTAT"); // debugging!
        System.out.println("operations"); // debugging!
        operations.forEach(System.out::println); // debugging!
        return new ACKMessage((short)8,"STUDENTSTAT  was received");
    }

    private RGRSMessage isRegistered(RequestMessage requestMessage){
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("ISREGISTERED");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        return new ACKMessage((short)9,"ISREGISTERED  was received");
    }

    private RGRSMessage unRegister(RequestMessage requestMessage){
        short courseNumber = requestMessage.getCourseNum();
        System.out.println("UNREGISTER ");// debugging!
        System.out.println("courseNum:"+courseNumber);// debugging!
        return new ACKMessage((short)10,"UNREGISTER was received");
    }

    private RGRSMessage myCourses(){
        System.out.println("MYCOURSES");// debugging!
        return new ACKMessage((short)11,"MYCOURSES  was received");
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
