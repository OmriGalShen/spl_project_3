package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You must supply port and number of threads");
            return;
        }
        Server.reactor(
                Integer.parseInt(args[1]), // number of threads
                Integer.parseInt(args[0]), // port
                () -> new BGRSMessagingProtocol(),
                () -> new BGRSMessageEncoderDecoder()
        ).serve();
    }
}