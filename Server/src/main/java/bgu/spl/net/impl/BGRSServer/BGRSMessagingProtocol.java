package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.Messages.ACKMessage;
import bgu.spl.net.impl.BGRSServer.Messages.ErrorMessage;
import bgu.spl.net.impl.BGRSServer.Messages.RGRSMessage;

public class BGRSMessagingProtocol implements MessagingProtocol<RGRSMessage>{
    private boolean shouldTerminate = false;
    private boolean isLogin = false;
    private boolean isAdmin = false;

    @Override
    public RGRSMessage process(RGRSMessage msg) {
        short opCode = msg.getOpCode();
        System.out.println("In protocol, received opcode: "+opCode);
        RGRSMessage response = new ACKMessage((short) 12,msg.getOpCode(),"a");;
        //        RGRSMessage response = new ErrorMessage((short) 13,(short) 5);
        switch (opCode){
            case 1: //ADMINREG
                System.out.println("ADMINREG");
                break;
            case 2: //STUDENTREG
                System.out.println("STUDENTREG");
                break;
            case 3: //LOGIN
                System.out.println("LOGIN");
                break;
            case 4: //LOGOUT
                System.out.println("LOGOUT");
                break;
            case 5: //COURSEREG
                System.out.println("COURSEREG");
                break;
            case 6: //KDAMCHECK
                System.out.println("KDAMCHECK");
                break;
            case 7: //COURSESTAT
                System.out.println("COURSESTAT");
                break;
            case 8: //STUDENTSTAT
                System.out.println("STUDENTSTAT");
                break;
            case 9: //ISREGISTERED
                System.out.println("ISREGISTERED");
                break;
            case 10: //UNREGISTER
                System.out.println("UNREGISTER");
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
