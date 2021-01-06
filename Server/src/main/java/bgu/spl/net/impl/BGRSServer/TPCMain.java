package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        Database db = Database.getInstance();//initialize Database
        if (args.length < 1) {
            System.out.println("You must supply port");
            return;
        }
        Server.threadPerClient(
                Integer.parseInt(args[0]), // port
                ()->new BGRSMessagingProtocol(),
                ()->new BGRSMessageEncoderDecoder()
        ).serve();
    }
}
