#include <connectionHandler.h>
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line,bool& terminate) {
    char ch; // current byte
    vector<char> bytes; //current bytes received by server
    short opCode = 0, messageCode=0;
    char opCodeBytes[2]; // temp array to get opcode
    char messageCodeBytes[2]; // temp array to get message op code
    try {
        do{
            if(!getBytes(&ch, 1))
                return false; // problem reading bytes

            bytes.push_back(ch); // store the received bytes one by one

            if(bytes.size()==4){ //determine opCode for termination condition
                opCodeBytes[0] = bytes[0];
                opCodeBytes[1] = bytes[1];
                messageCodeBytes[0]=bytes[2];
                messageCodeBytes[1]=bytes[3];
                opCode = bytesToShort(opCodeBytes); // get opCode as short
                messageCode = bytesToShort(messageCodeBytes); // get message opCode as short
                if(opCode==13) //error message end condition
                    break;
            }
        }while (bytes.size()<=4||'\0' != ch); //first 4 bytes may contain '\0' byte, check for ACK message end condition
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    if(opCode!=12&&opCode!=13)
        return false; //invalid Server to client message

    if(opCode==12) {// ACK message
         line = "ACK "+std::to_string(messageCode);
        if(messageCode==4)// ACK message from logout -> termination condition
            terminate=true;
        if(bytes.size()>5){ // optional message was received
            line += "\n";
            for(unsigned i=4;i<bytes.size();i++) // first 4 bytes reserved for op codes
                line.append(1,bytes[i]); // ACK message additional string
        }
    }
    else if(opCode==13){// Error message
        line = "ERROR "+std::to_string(messageCode);
    }

    return true;
}

bool ConnectionHandler::sendLine(std::string& line) {

    // --     get op code -- //
    unsigned int index =line.find(" "); //find first space
    string strOpCode=line; // string with op code

    if(index != string::npos) {// a space was found
        strOpCode = line.substr(0, index); // get substring with op code
        line = line.substr(index + 1, line.length() - index); //remove op code from rest of the line
    }
    short opCode = stringToOpCode(strOpCode); // get op code
    if(opCode==0) return false; // op code not valid
    // -----------------------------//

    if(opCode==1||opCode==2||opCode==3||opCode==8) { // messages with strings
        int initialLength = line.length()+1; // how many bytes for strings

        std::replace(line.begin(), line.end(), ' ', '\0'); //Replace spaces with \0
        line += '\0'; // add ending character

        const char* strBytes = line.c_str(); // bytes array of the strings
        unsigned messageLength = 2+initialLength; // full length of message with op code
        char messageBytes[messageLength]; // the full bytes array to send
        shortToBytes(opCode,messageBytes); // put opcode as first 2 bytes
        for(unsigned int i=2;i<messageLength;i++){
            messageBytes[i]=strBytes[i-2];
        }

        bool result=sendBytes(messageBytes,messageLength); // send full message
        if(!result) return false;
    }
    else if(opCode==5||opCode==6||opCode==7||opCode==9||opCode==10){  // messages with course number
        short courseNumber = short(atoi( line.c_str() )); // //get course number as short
        char courseNumBytes[2];
        shortToBytes(courseNumber,courseNumBytes);

        int messageLength = 4; // 2 bytes for op code 2 bytes for course number
        char messageBytes[messageLength]; // the full bytes array to send
        shortToBytes(opCode,messageBytes); // put opcode as first 2 bytes
        // put course number as last 2 bytes
        messageBytes[2]=courseNumBytes[0];
        messageBytes[3]=courseNumBytes[1];

        bool result=sendBytes(messageBytes,messageLength); // send full message
        if(!result) return false;
    }
    else if(opCode==11||opCode==4){ // messages with only opcode to send
        char opcodeBytes[2];
        shortToBytes(opCode,opcodeBytes);
        bool result=sendBytes(opcodeBytes,2); // send full message
        if(!result) return false;
    }
    return true;
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

short ConnectionHandler::stringToOpCode(const std::string& opCode) {
    if(opCode=="ADMINREG") return 1;
    if(opCode=="STUDENTREG") return 2;
    if(opCode=="LOGIN") return 3;
    if(opCode=="LOGOUT") return 4;
    if(opCode=="COURSEREG") return 5;
    if(opCode=="KDAMCHECK") return 6;
    if(opCode=="COURSESTAT") return 7;
    if(opCode=="STUDENTSTAT") return 8;
    if(opCode=="ISREGISTERED") return 9;
    if(opCode=="UNREGISTER") return 10;
    if(opCode=="MYCOURSES") return 11;
    return 0;
}

short ConnectionHandler::bytesToShort(const char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}



