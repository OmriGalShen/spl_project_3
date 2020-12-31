#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include <boost/thread.hpp>


void inputTask(ConnectionHandler& handler){
        while(1){
            try {
                const short bufsize = 1024;
                char buf[bufsize];
                std::cin.getline(buf, bufsize);
                std::string line(buf);

                int len = line.length();
                if (!handler.sendLine(line)) {
//                    std::cout << "Disconnected. Exiting...\n" << std::endl;
                    break;
                }

                // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
                std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
            }
            catch (boost::thread_interrupted&) {
                break;
            }
        }
}

int main (int argc, char *argv[]) {
    // --       Get server's host and port for connection    ---   //
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    // --       Connection to Server     ---   //
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    // --       Run input output with multithreading     ---   //
    boost::thread inputThread(&inputTask,boost::ref(connectionHandler));

    while(1){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;

        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            inputThread.interrupt();
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

//        auto len = answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
//        answer.resize(len - 1);
        if (answer == "TERMINATE") {
            inputThread.interrupt();
            std::cout << "Exiting... press enter to exit\n" << std::endl;
            break;
        }
        std::cout << "Reply: " << answer  << std::endl;
    }

    inputThread.join();
    std::cerr << "Client terminated" << std::endl;
    return 0;
}
