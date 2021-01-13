package bgu.spl.net.impl.BGRSServer.Messages;

import java.util.ArrayList;

public class RequestMessage extends BGRSMessage {
    ArrayList<String> operations;
    short courseNum;

    public RequestMessage(){
        super((short) 0);
        this.operations = null;
        this.courseNum = 0;
    }

    public RequestMessage(short opCode, ArrayList<String> operations, short courseNum) {
        super(opCode);
        this.operations = operations;
        this.courseNum = courseNum;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<String> operations) {
        this.operations = operations;
    }

    public short getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(short courseNum) {
        this.courseNum = courseNum;
    }
}
