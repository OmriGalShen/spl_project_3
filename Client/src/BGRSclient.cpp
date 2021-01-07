#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include <boost/thread.hpp>

// This task is run by it's own thread, it handles the input reading from user loop
void inputTask(ConnectionHandler& handler){
        while(1){
            try {
                const short bufsize = 1024;
                char buf[bufsize];
                std::cin.getline(buf, bufsize);
                std::string line(buf);

                if (!handler.sendLine(line)) {
                    break;
                }
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

    // --       Read user input on it's own thread     ---   //
    boost::thread inputThread(&inputTask,boost::ref(connectionHandler));

    // --       Read server responses loop     ---   //
    std::string answer; //server response to printed on client
    while(1){
        answer = ""; // initialize string
        bool terminate=false; //terminate condition for client

        if (!connectionHandler.getLine(answer,terminate)) { // read server response
            // if this code was reached problem with receiving server response was occurred
            inputThread.interrupt(); // stop reading user input
            break;
        }

        std::cout << answer  << std::endl; //print server response
        if (terminate) { //
            inputThread.interrupt(); // stop reading user input
            std::cout << "Exiting... press enter to exit\n" << std::endl;
            break;
        }
    }

    inputThread.join(); // stop reading user input
    std::cerr << "Client terminated" << std::endl;
    return 0;
}
