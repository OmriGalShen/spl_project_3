package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.api.Message;

public class ErrorMessage extends RGRSMessage {
    short messageOpCode;

    public ErrorMessage(short opCode, short messageOpCode) {
        super(opCode);
        this.messageOpCode = messageOpCode;
    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

    public void setMessageOpCode(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }
}
