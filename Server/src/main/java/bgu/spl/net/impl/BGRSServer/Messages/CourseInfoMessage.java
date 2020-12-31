package bgu.spl.net.impl.BGRSServer.Messages;

public class CourseInfoMessage extends RGRSMessage{
    short courseNum;

    public CourseInfoMessage(){
        super((short) 0);
        this.courseNum = 0;
    }

    public CourseInfoMessage(short opCode, short courseNum) {
        super(opCode);
        this.courseNum = courseNum;
    }

    public short getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(short courseNum) {
        this.courseNum = courseNum;
    }
}
