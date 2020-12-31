package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.api.Message;

import java.util.ArrayList;

public class RequestMessage extends RGRSMessage {
    ArrayList<String> operations;

    public RequestMessage(){
        super((short) 0);
        this.operations = null;
    }

    public RequestMessage(short opCode, ArrayList<String> operations) {
        super(opCode);
        this.operations = operations;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<String> operations) {
        this.operations = operations;
    }
}
