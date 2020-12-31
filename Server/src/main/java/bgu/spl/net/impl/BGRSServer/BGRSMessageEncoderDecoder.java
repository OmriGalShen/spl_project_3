package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.Messages.ACKMessage;
import bgu.spl.net.impl.BGRSServer.Messages.ErrorMessage;
import bgu.spl.net.impl.BGRSServer.Messages.RGRSMessage;
import bgu.spl.net.impl.BGRSServer.Messages.RequestMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<RGRSMessage> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public RGRSMessage decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\n') {
            return popMessage();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(RGRSMessage message) {

//        return (message + "\n").getBytes(); //uses utf8 by default
        byte[] result=null;

        byte[] opCode = shortToBytes(message.getOpCode());

        if(message instanceof ACKMessage){
            ACKMessage ackMessage = (ACKMessage)message;
            byte[] ackMessageBytes=ackMessage.getAckMessage().getBytes();
            byte[] messageOpCode = shortToBytes(ackMessage.getMessageOpCode());


            int messageLen = opCode.length+ackMessageBytes.length+messageOpCode.length+1;
            result = new byte[messageLen];
            System.arraycopy(opCode,0,result,0,2);
            System.arraycopy(ackMessageBytes,0,result,2,ackMessageBytes.length);
            System.arraycopy(messageOpCode,0,result,ackMessageBytes.length+2,2);
            result[result.length-1]=0; // end of message
        }
        else if(message instanceof ErrorMessage){
            byte[] messageOpCode = shortToBytes( ((ErrorMessage) message).getMessageOpCode());
            result = new byte[4];
            System.arraycopy(opCode,0,result,0,2);
            System.arraycopy(messageOpCode,0,result,2,2);
        }
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private RGRSMessage popMessage() {
        RequestMessage message = new RequestMessage();
        if(this.len<2) // invalid message
            return message;
        short opCode = bytesToShort(new byte[]{bytes[0], bytes[1]});
        // --    get operations --    //
        ArrayList<String> stringOperations = new ArrayList<>();
        // start from index 2 because the first 2 bytes are opCodes
        for (int i = 2,stringStart=2; i < len; i++) {
            if(bytes[i]==0xa){ // end of string operation
                stringOperations.add(new String(bytes, stringStart, i, StandardCharsets.UTF_8));
                stringStart=i+1;
            }
        }
        message.setOpCode(opCode);
        message.setOperations(stringOperations);
        len=0; //reset position on bytes array
        return message;

    }
}
