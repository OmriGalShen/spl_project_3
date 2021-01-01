package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<RGRSMessage> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    /**
     * Simple helper function for conversion
     * @param byteArr array of bytes of length 2
     * @return short after conversion
     */
    public static short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    /**
     * Simple helper function for conversion
     * @param num short to convert
     * @return array of bytes of length 2
     */
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public RGRSMessage decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if(len>=2){ //check op code to determine pop condition
            short opCode = bytesToShort(new byte[]{bytes[0], bytes[1]}); //get op code
            switch (opCode){
                case 1: //ADMINREG
                case 2: //STUDENTREG
                case 3: //LOGIN
                    int zeroByteCounter=0;
                    for(int i=0;i<len&&zeroByteCounter<2;i++)
                        if(bytes[i]=='\0')
                            zeroByteCounter++;
                    if(zeroByteCounter==2) // termination condition
                        return popMessage();
                    break;
                case 4: //LOGOUT
                case 11: //MYCOURSES
                    return popMessage();
                case 5: //COURSEREG
                case 6: // KDAMCHECK
                case 7: //COURSESTAT
                case 9: //ISREGISTERED
                case 10: //UNREGISTER
                    if(len==5) // termination condition
                        return popMessage();
                    break;
                case 8: //STUDENTSTAT
                    for(int i=0;i<len;i++) {
                        if (bytes[i] == '\0') // termination condition
                            return popMessage();
                    }
                    break;
            }
        }

        return null; //not a line yet
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private RGRSMessage popMessage() {
        RGRSMessage message = new RequestMessage();
        if(this.len<2) // invalid message
            return null;
        short opCode = bytesToShort(new byte[]{bytes[0], bytes[1]});
        if(opCode==1||opCode==2||opCode==3||opCode==8||opCode==4||opCode==11) { // request message
            // --    get operations --    //
            ArrayList<String> stringOperations = new ArrayList<>();
            // start from index 2 because the first 2 bytes are opCodes
            for (int i = 2, stringStart = 2; i < len; i++) {
                if (bytes[i] == '\0') { // end of string operation
                    stringOperations.add(new String(bytes, stringStart, i, StandardCharsets.UTF_8));
                    stringStart = i + 1;
                }
            }
            message = new RequestMessage(opCode,stringOperations);
        }else if(opCode==5||opCode==6||opCode==7||opCode==9||opCode==10) {// messages with course number
            short courseNum = Short.parseShort(new String(bytes, 2,bytes.length, StandardCharsets.UTF_8));
            message = new CourseInfoMessage(opCode,courseNum);
        }
        len=0; //reset position on bytes array
        return message;
    }

    @Override
    public byte[] encode(RGRSMessage message) {

//        return (message + "\n").getBytes(); //uses utf8 by default
        byte[] result=null; // the return message

        byte[] opCode = shortToBytes(message.getOpCode()); // length 2

        if(message instanceof ACKMessage){
            ACKMessage ackMessage = (ACKMessage)message;
            byte[] ackMessageBytes=ackMessage.getAckMessage().getBytes();
            byte[] messageOpCode = shortToBytes(ackMessage.getMessageOpCode()); //length 2


            int messageLen = opCode.length+ackMessageBytes.length+messageOpCode.length+1;
            result = new byte[messageLen];
            System.arraycopy(opCode,0,result,0,2);
            System.arraycopy(messageOpCode,0,result,2,2);
            System.arraycopy(ackMessageBytes,0,result,4,ackMessageBytes.length);
            result[result.length-1]='\0'; // end of message
        }
        else if(message instanceof ErrorMessage){
            byte[] messageOpCode = shortToBytes( ((ErrorMessage) message).getMessageOpCode());
            result = new byte[4];
            System.arraycopy(opCode,0,result,0,2);
            System.arraycopy(messageOpCode,0,result,2,2);
        }
        return result;
    }
}
