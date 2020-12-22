package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        System.out.println("In ReactorMain");
//        System.out.println(args[0]);
        // you can use any server...
//        Server.threadPerClient(
//                7777, //port
//                () -> new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new RemoteCommandInvocationProtocol<>(), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();
    }
}