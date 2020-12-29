package bgu.spl.net.impl.echo;
import bgu.spl.net.srv.Server;

public class EchoMain {
    public static void main(String[] args) {
        Server.threadPerClient(7777,()->new EchoProtocol(),()->new LineMessageEncoderDecoder()).serve();
    }
}
