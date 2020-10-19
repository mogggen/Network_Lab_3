#include <iostream>
#include <string>
#include <WS2tcpip.h>
#pragma comment(lib, "ws2_32.lib")

using namespace std;

string DataToCharArr(string val, int x, int y, int color)
{
	return val + string({(char)x, (char)y, (char)color});
}

void main()
{
	string ipAddress = "127.0.0.1";			// IP Address of the server
	int port = 4999;						// Listening port # on the server

	// Initialize WinSock
	WSAData data;
	WORD ver = MAKEWORD(2, 2);
	int wsResult = WSAStartup(ver, &data);
	if (wsResult != 0)
	{
		cerr << "Can't start Winsock, Err #" << wsResult << endl;
		return;
	}

	// Create socket
	SOCKET sock = socket(AF_INET, SOCK_STREAM, 0);

	// Fill in a hint structure
	sockaddr_in hint;
	hint.sin_family = AF_INET;
	hint.sin_port = htons(port);
	inet_pton(AF_INET, ipAddress.c_str(), &hint.sin_addr);

	// Connect to server
	int connResult = connect(sock, (sockaddr*)&hint, sizeof(hint));
	if (connResult == SOCKET_ERROR)
	{
		int error = WSAGetLastError();
		if (error == 10061)
			cerr << "Can't connect to server, Error: timeout" << endl;
		else
			cerr << "Can't connect to server, Error: #" << error << endl;
		closesocket(sock);
		WSACleanup();
		return;
	}

	// Do-while loop to send and receive data
	string input = "";
	input = DataToCharArr(input, 30, 30, 1);
	while (true)
	{

		cout << "> ";
		//getline(cin, input);

		if (input.size() > 0)		// Make sure the user has typed in something
		{
			// Send the text
			send(sock, input.c_str(), input.size() + 1, 0);
		}
		else
		{
			std::cout << "\"\"";
			return;
		}
		int t;
		cin >> t;
	}
	
	// Gracefully close down everything
	closesocket(sock);
	WSACleanup();
}