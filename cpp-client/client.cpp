#include <iostream>
#include <ctime>
#include <string>
#include <cstring>
#include <WS2tcpip.h>
#include <thread>
#pragma comment(lib, "ws2_32.lib")

using namespace std;

char* DataToCharArr(int x, int y, int c)
{
	char* temp = new char[3] { (char)x, (char)y, (char)c };

	return temp;
}

void main()
{
	srand((unsigned)time(0));
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

	// while loop to send data
	char* input;
	while (true)
	{
		Sleep(1000);
		input = DataToCharArr(rand() % 201, rand() % 201, rand() % 9);
		send(sock, input, 3, 0);
	}
	
	// Gracefully close down everything
	closesocket(sock);
	WSACleanup();
}