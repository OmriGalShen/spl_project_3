package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.api.Message;

public abstract class RGRSMessage implements Message<Short> {
    short opCode;

    public RGRSMessage(short opCode) {
        this.opCode = opCode;
    }

    public short getOpCode() {
        return opCode;
    }

    public void setOpCode(short opCode) {
        this.opCode = opCode;
    }
}
