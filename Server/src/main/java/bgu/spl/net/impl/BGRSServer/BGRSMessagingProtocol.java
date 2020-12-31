package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;

public class BGRSMessagingProtocol implements MessagingProtocol<String>{
    private boolean shouldTerminate = false;

    @Override
    public String process(String msg) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
