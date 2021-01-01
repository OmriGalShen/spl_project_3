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
        short opCode = msg.getOpCode();
        RGRSMessage response = new ACKMessage((short) 12,msg.getOpCode(),"your message was received");;
        //        RGRSMessage response = new ErrorMessage((short) 13,(short) 5);
        ArrayList<String> operations;
        short courseNum;
        switch (opCode){
            case 1: //ADMINREG
                System.out.println("ADMINREG");
                operations= ((RequestMessage) msg).getOperations();
                System.out.println("operations");
                operations.forEach(System.out::println);
                break;
            case 2: //STUDENTREG
                System.out.println("STUDENTREG");
                operations= ((RequestMessage) msg).getOperations();
                System.out.println("operations");
                operations.forEach(System.out::println);
                break;
            case 3: //LOGIN
                System.out.println("LOGIN");
                operations= ((RequestMessage) msg).getOperations();
                System.out.println("operations");
                operations.forEach(System.out::println);
                break;
            case 4: //LOGOUT
                System.out.println("LOGOUT");
                break;
            case 5: //COURSEREG
                System.out.println("COURSEREG");
                courseNum = ((CourseInfoMessage) msg).getCourseNum();
                System.out.println("courseNum:"+courseNum);
                break;
            case 6: //KDAMCHECK
                System.out.println("KDAMCHECK");
                courseNum = ((CourseInfoMessage) msg).getCourseNum();
                System.out.println("courseNum:"+courseNum);
                break;
            case 7: //COURSESTAT
                System.out.println("COURSESTAT");
                courseNum = ((CourseInfoMessage) msg).getCourseNum();
                System.out.println("courseNum:"+courseNum);
                break;
            case 8: //STUDENTSTAT
                System.out.println("STUDENTSTAT");
                operations= ((RequestMessage) msg).getOperations();
                System.out.println("operations");
                operations.forEach(System.out::println);
                break;
            case 9: //ISREGISTERED
                System.out.println("ISREGISTERED");
                courseNum = ((CourseInfoMessage) msg).getCourseNum();
                System.out.println("courseNum:"+courseNum);
                break;
            case 10: //UNREGISTER
                System.out.println("UNREGISTER");
                courseNum = ((CourseInfoMessage) msg).getCourseNum();
                System.out.println("courseNum:"+courseNum);
                break;
            case 11: //MYCOURSES
                System.out.println("MYCOURSES");
                break;
            default: // opcode not valid
                response = new ErrorMessage((short) 13,(short) 13);
                break;
        }
        return response;
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
