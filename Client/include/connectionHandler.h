#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__
                                           
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <algorithm>

using boost::asio::ip::tcp;
using namespace std;

class ConnectionHandler {
private:
	const std::string host_;
	const short port_;
	boost::asio::io_service io_service_;   // Provides core I/O functionality
	tcp::socket socket_; 
 
public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();
 
    // Connect to the remote machine
    bool connect();
 
    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);
 
	// Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);
	
    // Read an ascii line from the server
    // Returns false in case connection closed before a newline can be read.
    bool getLine(std::string& line,bool& terminate);
	
	// Send an ascii line from the server
    // Returns false in case connection closed before all the data is sent.
    bool sendLine(std::string& line);
	
    // Close down the connection properly.
    void close();

    static short stringToOpCode(const std::string& opCode);

    short bytesToShort(const char* bytesArr);

    static void shortToBytes(short num, char* bytesArr);

    string processInput(char* bytesArr);

}; //class ConnectionHandler
 
#endif