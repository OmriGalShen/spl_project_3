package bgu.spl.net.impl.BGRSServer.Messages;

public class ErrorMessage extends RGRSMessage {
    short messageOpCode;

    public ErrorMessage(short messageOpCode) {
        super((short) 13);
        this.messageOpCode = messageOpCode;
    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

    public void setMessageOpCode(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }
}
