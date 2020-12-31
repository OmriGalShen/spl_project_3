package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.Messages.ACKMessage;
import bgu.spl.net.impl.BGRSServer.Messages.ErrorMessage;
import bgu.spl.net.impl.BGRSServer.Messages.RGRSMessage;

public class BGRSMessagingProtocol implements MessagingProtocol<RGRSMessage>{
    private boolean shouldTerminate = false;

    @Override
    public RGRSMessage process(RGRSMessage msg) {
        System.out.println("op code:"+msg.getOpCode());
//        RGRSMessage response = new ErrorMessage((short) 13,(short) 5);
        RGRSMessage response = new ACKMessage((short) 12,msg.getOpCode(),"a");
        return response;
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
