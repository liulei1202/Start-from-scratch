// GS_Adapter.cpp: 定义控制台应用程序的入口点。
//

#include "stdafx.h"

#include <WinSock2.h>
#pragma comment(lib, "ws2_32.lib")

#include <iostream>
#include <string>
#include "handle.h"
using namespace std;
const int RECV_BUF_SIZE = 1024;
const int BUF_SIZE = 102400;
const int port = 8800;
const int listenCount = 16;
const string equipment = "GS25i_01";		//设备名称
typedef int(*DLLFUN)(char *request, char *buff, int *len);
//DLLFUN probeFun, queryFun, setFun, samplesFun;
string getData(string query_json, DLLFUN fun);

int main(int argc, char* argv[])
{

	WSADATA			wsd;			//WSADATA变量
	SOCKET			_sServer;		//服务器套接字
	SOCKET			_sClient;		//客户端套接字
	SOCKADDR_IN		addrServ;;		//服务器地址
	char			buf[RECV_BUF_SIZE];	//接收数据缓冲区
	char			sendBuf[BUF_SIZE];//返回给客户端得数据
	int				retVal;			//返回值

	//初始化套结字动态库
	if (WSAStartup(MAKEWORD(2, 2), &wsd) != 0)
	{
		cout << "WSAStartup failed!" << endl;
		return 1;
	}

	//创建套接字
	_sServer = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (INVALID_SOCKET == _sServer)
	{
		cout << "socket failed!" << endl;
		WSACleanup();//释放套接字资源;
		return  -1;
	}

	//服务器套接字地址 
	addrServ.sin_family = AF_INET;
	addrServ.sin_port = htons(port);
	addrServ.sin_addr.s_addr = INADDR_ANY;
	//绑定套接字
	retVal = bind(_sServer, (LPSOCKADDR)&addrServ, sizeof(SOCKADDR_IN));
	if (SOCKET_ERROR == retVal)
	{
		cout << "bind failed!" << endl;
		closesocket(_sServer);	//关闭套接字
		WSACleanup();			//释放套接字资源;
		return -1;
	}

	//开始监听 
	retVal = listen(_sServer, listenCount);
	if (SOCKET_ERROR == retVal)
	{
		cout << "listen failed!" << endl;
		closesocket(_sServer);	//关闭套接字
		WSACleanup();			//释放套接字资源;
		return -1;
	}
	cout << "Load success." << endl;
	cout << "port:" << port << endl;
	cout << "equipment:" << equipment << endl;

	while (true)
	{
		//accept客户端请求,创建socket
		sockaddr_in addrClient;
		int addrClientlen = sizeof(addrClient);
		_sClient = accept(_sServer, (sockaddr FAR*)&addrClient, &addrClientlen);
		if (INVALID_SOCKET == _sClient)
		{
			cout << "accept failed!" << endl;
			closesocket(_sServer);	//关闭套接字
			WSACleanup();			//释放套接字资源;
			return -1;
		}
		//recv客户端数据
		ZeroMemory(buf, RECV_BUF_SIZE);
		retVal = recv(_sClient, buf, RECV_BUF_SIZE, 0);
		if (SOCKET_ERROR == retVal)
		{
			cout << "recv failed!" << endl;
			closesocket(_sServer);	//关闭套接字
			closesocket(_sClient);	//关闭套接字		
			WSACleanup();			//释放套接字资源;
			return -1;
		}
		if (buf[0] == '0') {
			cout << "buf[0]" << endl;
			break;
		}
		cout << "recv: " << buf << endl;
		string message, req(buf);
		if (req.find(equipment) == string::npos)
		{
			message =  "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"path wrong.\"}";
		}
		else if (req.find("SAMPLES") != std::string::npos) message = getData(req, samples);
		else if ((req.find("SET") != std::string::npos) || (req.find("SetSampleProbe") != std::string::npos)) message = getData(req, set);
		else if (req.find("PROBE") != std::string::npos) message = getData(req, probe);
		else if (req.find("QUERY") != std::string::npos) message = getData(req, query);
		else message = R"({"code":"FAILED","description":"request error,has no such query type"})";

		//cout << "send:" << message << "\nsize:" << message.size() << endl;
		ZeroMemory(sendBuf, BUF_SIZE);
		memcpy(sendBuf, message.c_str(), message.size());
		//send结果
		send(_sClient, sendBuf, strlen(sendBuf), 0);
		closesocket(_sClient);	//关闭套接字
	}

	//退出
	closesocket(_sServer);	//关闭套接字
	WSACleanup();			//释放套接字资源;

	return 0;
}

string getData(string query_json, DLLFUN fun)
{
	char *buff = NULL, prebuff[BUF_SIZE];
	int len = BUF_SIZE, ret;
	prebuff[0] = '\0';
	--len;
	ret = fun((char*)query_json.c_str(), prebuff, &len);
	if (len > BUF_SIZE)
	{
		len += 128;
		buff = new char[len];
		--len;
		fun((char*)query_json.c_str(), buff, &len);
		buff[len] = '\0';
		string ret(buff);
		delete[] buff;
		return ret;
	}
	prebuff[len] = '\0';
	return string(prebuff);
}


