package bgu.spl.net.impl.BGRSServer;


public class RunClientTests {

        public static void main(String[] args) {
                new Thread(new Tests()).start();
        }
}