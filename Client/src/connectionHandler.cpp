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
 
bool ConnectionHandler::getLine(std::string& line) {
    char ch;
    vector<char> bytes;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if(!getBytes(&ch, 1))
            {
                return false;
            }
            bytes.push_back(ch);
        }while ('\n' != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    char opCodeBytes[] = {bytes[0],bytes[1]};
    short opCode = bytesToShort(opCodeBytes);
    if(opCode==12) {// ACK message
        line = "ACK ";
        for(unsigned i=4;i<bytes.size();i++)
            line.append(1,bytes[i]);
    }
    else if(opCode==13){// Error message
        char messageCodeBytes[] = {bytes[2],bytes[3]};
        short messageCode = bytesToShort(messageCodeBytes);
        line = "ERROR "+std::to_string(messageCode);
    }
    else return false;
    return true;
}

bool ConnectionHandler::sendLine(std::string& line) {

    // --     get op code -- //
    unsigned int index =line.find(" "); //find first space
    string strOpCode=line; // string with op code

    if(index != string::npos) {// a space was found
        strOpCode = line.substr(0, line.find(" ")); // get substring with op code
        line = line.substr(index + 1, line.length() - index); //remove op code from rest of the line
    }
    short opCode = stringToOpCode(strOpCode); // get op code
    if(opCode==0) return false; // op code not valid
    std::cerr << "opcode: " << opCode << std::endl;
    // -----------------------------//

    // --    send op code --  //
    char codeBytesArr[2];
    shortToBytes(opCode,codeBytesArr);
    bool result=sendBytes(codeBytesArr,2);
    if(!result) return false;
    // ----------------------------//


    if(opCode==1||opCode==2||opCode==3||opCode==8) { // messages with strings
        std::replace(line.begin(), line.end(), ' ', '\0'); //Replace spaces with \0
        line += '\0'; // add ending character
//        std::cerr << "strings: " << line << std::endl;

        result=sendBytes(line.c_str(),line.length()); // send strings
        if(!result) return false;
    }
    else if(opCode==5||opCode==6||opCode==7||opCode==9||opCode==10){  // messages with course number
        short courseNumber = short(atoi( line.c_str() )); // //get course number as short
        char codeBytesArr[2];
        shortToBytes(courseNumber,codeBytesArr);
        bool result=sendBytes(codeBytesArr,2);
        if(!result) return false;
//        std::cerr << "course number: " << courseNumber << std::endl;
    }
    // --   end message    -- //
    char endByte={'\n'};
    return sendBytes(&endByte,1);
    // ----------------------------//

}
 

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    vector<char> bytes;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
	do{
		if(!getBytes(&ch, 1))
		{
			return false;
		}
		frame.append(1, ch);
        bytes.push_back(ch);
	}while (delimiter != ch);
    } catch (std::exception& e) {
	std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
	return false;
    }
    return true;
}
 
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
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



