#pragma once
//json¥¶¿Ì
#include "json//json.h" 
#pragma comment(lib, "json_vc71_libmtd.lib")
#include <string>
#include "gsk25inetfun.h"
using namespace std;

int probe(char *request, char *buff, int *len);
int query(char *request, char *buff, int *len);
int set(char *request, char *buff, int *len);
int samples(char *request, char *buff, int *len);

Json::Value inquireID(string reqID, Json::Value root);
Json::Value setID(string reqID, Json::Value root, Json::Value newValue);
DWORD WINAPI ThreadProFunc(LPVOID lpParam);


