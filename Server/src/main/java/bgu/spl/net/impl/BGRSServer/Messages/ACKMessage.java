package bgu.spl.net.impl.BGRSServer.Messages;

public class ACKMessage extends BGRSMessage {
    short messageOpCode;
    String ackMessage;

    public ACKMessage(short messageOpCode, String ackMessage) {
        super((short)12);
        this.messageOpCode = messageOpCode;
        this.ackMessage = ackMessage;
    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

    public void setMessageOpCode(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }
}
