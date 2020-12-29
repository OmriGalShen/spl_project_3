#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include <boost/thread.hpp>


void inputTask(ConnectionHandler& handler){
        while(1){

            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);

            int len=line.length();
            if (!handler.sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
            std::cout << "Sent " << len+1 << " bytes to server" << std::endl;
        }
}

void outputTask(ConnectionHandle& handler){
    while(1){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!handler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

		int len=answer.length();
		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
}



//class ReadTask{
//private:
//    ConnectionHandler& _handler;
//public:
//    ReadTask (ConnectionHandler& handler) : _handler(handler){}
//
//    void operator()(){
//        while(1){
//
//            const short bufsize = 1024;
//            char buf[bufsize];
//            std::cin.getline(buf, bufsize);
//            std::string line(buf);
//
//            int len=line.length();
//            if (!_handler.sendLine(line)) {
//                std::cout << "Disconnected. Exiting...\n" << std::endl;
//                break;
//            }
//            // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
//            std::cout << "Sent " << len+1 << " bytes to server" << std::endl;
//        }
//    }
//};
//
//class ReceiveTask{
//private:
//    ConnectionHandler& _handler;
//public:
//    ReceiveTask (ConnectionHandler& handler) : _handler(handler) {}
//    void operator()(){
//    while(1){
//        // We can use one of three options to read data from the server:
//        // 1. Read a fixed number of characters
//        // 2. Read a line (up to the newline character using the getline() buffered reader
//        // 3. Read up to the null character
//        std::string answer;
//        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
//        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
//        if (!_handler.getLine(answer)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }
//
//		int len=answer.length();
//		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
//		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
//        answer.resize(len-1);
//        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
//        if (answer == "bye") {
//            std::cout << "Exiting...\n" << std::endl;
//            break;
//        }
//    }
//    }
//};

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	
	//From here we will see the rest of the ehco client implementation:
//    ReadTask read(connectionHandler);
//    ReceiveTask receive(connectionHandler);
//    std::thread th1(std::ref(read));
//    std::thread th2(std::ref(ReceiveTask));
//    th1.join();
//    th2.join();

    boost::thread inputThread(&inputTask,&connectionHandler);
    boost::thread outputThread(&outputTask,&connectionHandler);
    inputThread.join();
    outputThread.join();
    return 0;
}
