#include <SDKDDKVer.h>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <stdio.h>
#include <tchar.h>
#include <Windows.h>
#include <time.h>
#include <strsafe.h>
#include <Shlwapi.h>
#include <winsvc.h>

#include "include/json.h"
#ifdef _DEBUG
#pragma comment(lib,"lib_json.lib")
#endif
/*
#ifdef _DEBUG
#pragma comment(lib,"json_vc71_libmtd.lib")
#else
#pragma comment(lib,"json_vc71_libmt.lib")
#endif
*/
#pragma comment(lib, "Shlwapi.lib")
#pragma comment(lib, "Advapi32.lib")
using namespace std;

TCHAR szServiceName[32] = "opcService";
TCHAR szBinPath[32] = "E:\\opcService.exe";

#define DATA_COUNT 200
#define DATA_LENGTH 5000	//ÿ���������5000byte,
#define FULL_MAP_NAME	"Global\\MyFileMappingObject"	//	Local	Global
#define MAP_SIZE 65536*16
HANDLE hMapFile = NULL;   

string sampleProbe, sampleType;
//string *collectDataID;
int C0LLECT_DATA_COUNT;	//sampleprobe��Ҫ�ɼ������ݵĸ���
const int RETURN_COUNT = 10;	//һ��sample()������෵�����ݸ���
string Machine = "/NC_LINK_BH/840D_01";

struct OPCData{	//�������ݽṹ
	int front;
	int rear;
	char data[DATA_COUNT][DATA_LENGTH];	
};
struct OPCData* opcData;

extern "C" _declspec(dllexport) int  PROBE(char *request, char* buff, int *len);
extern "C" _declspec(dllexport) int  QUERY(char *request, char* buff, int *len);
extern "C" _declspec(dllexport) int  SAMPLES(char *request, char* buff, int *len);
extern "C" _declspec(dllexport) int  SET(char *request, char* buff, int *len);

extern "C" int WriteToLog(const char* str)
{

	FILE* pfile;
	fopen_s(&pfile, "E:\\opc.txt", "a+");
	if (pfile == NULL)
	{
		return -1;
	}
	fprintf_s(pfile, "%s\n", str);
	fclose(pfile);
	return 0;
}

extern "C" Json::Value inquireID(string reqID, Json::Value root,string ingore) {

	//WriteToLog(root.toStyledString().c_str());
	Json::Value::Members mem = root.getMemberNames();
	for (Json::Value::Members::iterator iter = mem.begin(); iter != mem.end(); iter++)
	{
		if ((*iter) == ingore)
			continue;
		if ((*iter) == reqID)
		{
			Json::Value temp;
			if (root[*iter].type() == Json::stringValue)
			{
				temp["data"] = root[*iter].asString();
			}
			else if (root[*iter].type() == Json::realValue)
			{
				temp["data"] = root[*iter].asDouble();
			}
			else if (root[*iter].type() == Json::objectValue)
			{
				temp["data"] = root[*iter];
			}
			else if (root[*iter].type() == Json::arrayValue)
			{
				temp["data"] = root[*iter];
			}
			else if (root[*iter].type() == Json::intValue)
			{
				temp["data"] = root[*iter].asInt();
			}
			else
			{
				temp["data"] = "Unknown data type";
			}
			return temp;
		}
		if (root[*iter].type() == Json::arrayValue)
		{
			int nArraySize = root[*iter].size();
			Json::Value temp;
			for (int i = 0; i < nArraySize; i++)
			{
				if (root[*iter][i].type() == Json::objectValue)
				{
					temp = inquireID(reqID, root[*iter][i],"");
					if (temp != NULL)
					{
						return temp;
					}
				}
			}
		}
	}
	//δ�ҵ�����
	return NULL;
}

extern "C" Json::Value setID(string reqID, Json::Value root, Json::Value newValue, string ingore) {

	//WriteToLog(root.toStyledString().c_str());
	Json::Value::Members mem = root.getMemberNames();
	for (Json::Value::Members::iterator iter = mem.begin(); iter != mem.end(); iter++)
	{
		if ((*iter) == ingore)
			continue;
		if ((*iter) == reqID)
		{
			root[*iter] = newValue;
			return root;
		}
		if (root[*iter].type() == Json::arrayValue)
		{
			int nArraySize = root[*iter].size();
			Json::Value temp;
			for (int i = 0; i < nArraySize; i++)
			{
				if (root[*iter][i].type() == Json::objectValue)
				{
					temp = setID(reqID, root[*iter][i], newValue, "");
					if (temp != NULL)
					{
						root[*iter][i] = temp;
						return root;
					}
				}
			}
		}
	}
	//δ�ҵ�����
	return NULL;
}

int  PROBE(char *request, char* buff, int *len){

	string req(request),response;
	//json����������
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	//��ȡ�ļ�����
	ifstream fin("probe_bh.json");
	if (!fin)
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File not exist\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	if (!reader.parse(fin, root))
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File can not open\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	Json::Value temp;
	reader.parse(req, temp);
	//���ȫ��������
	if (temp["path"].asString() == "/NC_LINK_BH")
	{
		response = "{\"path\":\"/NC_LINK_BH\",\"code\":\"SUCCESS\",\"probe\":[" + root.toStyledString() + "]}";	//writerinfo.write(root)
	}
	else if(temp["path"].asString() == Machine)//ֻ���machine������
	{
		Json::Value device;
		device = root["devices"][0];
		response = "{\"path\":\"/NC_LINK_BH/840D_01\",\"code\":\"SUCCESS\",\"probe\":[" + device.toStyledString() + "]}";
	}
	else
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Don't find the machine\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	strcpy_s(buff, *len, response.c_str());
	*len = response.size();
	return 0;
}

int  QUERY(char *request, char* buff, int *len)
{
	string req(request), response;
	//json����������
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	Json::Value jreq;
	reader.parse(request, req_json);
	jreq = req_json["ids"];
	int nArraySize = jreq.size();
	//��ȡ�ļ�����
	ifstream fin("probe_bh.json");
	if (!fin)
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File not exist\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	if (!reader.parse(fin, root))
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File can not open\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	bool flag = false;
	if (req_json["path"].asString() == "/NC_LINK_BH")	//��ѯȫ�ֲ���
	{
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = inquireID(jreq[i]["id"].asString(), root, "devices");
			if (temp == NULL)
			{
				jreq[i]["code"] = "FAILED";
			}
			else
			{
				flag = true;
				jreq[i]["code"] = "SUCCESS";
				jreq[i]["data"] = temp["data"];
			}
		}
	}
	else if (req_json["path"].asString() == Machine)
	{
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = inquireID(jreq[i]["id"].asString(), root["devices"][0], "");
			if (temp == NULL)
			{
				jreq[i]["code"] = "FAILED";
			}
			else
			{
				flag = true;
				jreq[i]["code"] = "SUCCESS";
				jreq[i]["data"] = temp["data"];
			}
		}
	}
	else
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Don't find the machine\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	
	Json::Value json_response;
	if(flag)
		json_response["code"] = "SUCCESS";
	else
		json_response["code"] = "FAILED";
	json_response["data"] = jreq;
	response =  json_response.toStyledString();

	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	strcpy_s(buff, *len, response.c_str());
	*len = response.size();
	return 0;
}

int  SET(char *request, char* buff, int *len)
{
	string response,req(request);
	if (req.find("sampleProbe")!=string::npos)
	{
		/*
			......
			�����������,sampleProbeͬ�����ֻ�����result�з��ظ������Ƿ�����ɹ���
			����"result":[{"id":"Register","index":"xxx01","code":"SUCCESS"},{"id":"x2","code":"SUCCESS"}]
			......
		*/
		
		//���������з���
		//����������Ƿ����
		if (!PathFileExists(szBinPath))
		{
			response = "Don't find the service.";
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
	
		SC_HANDLE schSCManager;
		SC_HANDLE schService;
		SERVICE_STATUS_PROCESS ssStatus;
		DWORD dwBytesNeeded;
		//�򿪷��������
		schSCManager = OpenSCManager(
			NULL,                    // ����Ǳ�������NULL�������Զ��дԶ�˻�����
			NULL,					 //SERVICES_ACTIVE_DATABASE
			SC_MANAGER_ALL_ACCESS	//SC_MANAGER_ALL_ACCESS	SC_MANAGER_ALL_ACCESS
		);  
		if (NULL == schSCManager)
		{
			response = "OpenSCManager failed."; //����GetLastError()�鿴��������
			char temp[64];
			sprintf_s(temp, 64, "OpenSCManager failed(%d)",GetLastError());
			WriteToLog(temp);
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
		//�򿪷���
		schService = OpenService(
			schSCManager,
			szServiceName,
			SERVICE_ALL_ACCESS
		);
		if (schService != NULL)
		{

			//��ȡ����״̬
			if (!QueryServiceStatusEx(schService, SC_STATUS_PROCESS_INFO, (LPBYTE)&ssStatus, sizeof(SERVICE_STATUS_PROCESS), &dwBytesNeeded)) {

				CloseServiceHandle(schService);
				CloseServiceHandle(schSCManager);
				response = "QueryServiceStatusEx failed1."; //����GetLastError()�鿴��������
				WriteToLog(response.c_str());
				if (response.size() >= (*len))
				{
					*len = response.size();
					return -4;
				}
				strcpy_s(buff, *len, response.c_str());
				*len = response.size();
				return -1;
			}

			//�����������������ֹͣ����
			if (ssStatus.dwCurrentState == SERVICE_RUNNING)
			{
				WriteToLog("Service alreadly runed.");
				// ֹͣ����
				if (ControlService(schService, SERVICE_CONTROL_STOP, (LPSERVICE_STATUS)&ssStatus) == FALSE)
				{
					CloseServiceHandle(schService);
					CloseServiceHandle(schSCManager);
					response = "QueryServiceStatusEx failed1."; //����GetLastError()�鿴��������
					WriteToLog(response.c_str());
					if (response.size() >= (*len))
					{
						*len = response.size();
						return -4;
					}
					strcpy_s(buff, *len, response.c_str());
					*len = response.size();
					return -1;
				}
				// �ȴ�����ֹͣ
				DWORD dwStartTime = GetTickCount();
				DWORD dwTimeout = 30000;
				while (ssStatus.dwCurrentState != SERVICE_STOPPED)
				{
					Sleep(ssStatus.dwWaitHint);
					if (!QueryServiceStatusEx(schService, SC_STATUS_PROCESS_INFO, (LPBYTE)&ssStatus, sizeof(SERVICE_STATUS_PROCESS), &dwBytesNeeded))
					{
						CloseServiceHandle(schService);
						CloseServiceHandle(schSCManager);
						response = "QueryServiceStatusEx failed."; //����GetLastError()�鿴��������
						WriteToLog(response.c_str());
						if (response.size() >= (*len))
						{
							*len = response.size();
							return -4;
						}
						strcpy_s(buff, *len, response.c_str());
						*len = response.size();
						return -1;
					}

					if (ssStatus.dwCurrentState == SERVICE_STOPPED)
					{
						WriteToLog("StopService successed.");
						break;
					}

					if (GetTickCount() - dwStartTime > dwTimeout)
					{
						CloseServiceHandle(schService);
						CloseServiceHandle(schSCManager);
						response = "Wait timed out."; //����GetLastError()�鿴��������
						WriteToLog(response.c_str());
						if (response.size() >= (*len))
						{
							*len = response.size();
							return -4;
						}
						strcpy_s(buff, *len, response.c_str());
						*len = response.size();
						return -1;
					}
				}
				WriteToLog("StopService successed2.");
				//ж�ط���
				if (!DeleteService(schService))
				{
					CloseServiceHandle(schService);
					CloseServiceHandle(schSCManager);
					response = "DeleteService failed."; //����GetLastError()�鿴��������
					WriteToLog(response.c_str());
					if (response.size() >= (*len))
					{
						*len = response.size();
						return -4;
					}
					strcpy_s(buff, *len, response.c_str());
					*len = response.size();
					return -1;
				}
				else {
					WriteToLog("DeleteService successed.");
				}
			}

			CloseServiceHandle(schService);
		}
		

		//��������
		schService = CreateService(
			schSCManager,              // SCM��� 
			szServiceName,             // �������� 
			szServiceName,             // ��ʾ���� 
			SERVICE_ALL_ACCESS,        // ����Ȩ��
			SERVICE_WIN32_SHARE_PROCESS, // service type �����Ƿ�����������������SERVICE_WIN32_OWN_PROCESS����ʾ�����κν����������
									   // �����ȷ����ķ�����Ҫ��ĳЩ����������������ó�SERVICE_WIN32_SHARE_PROCESS������ķ���Ҫ�������������ʱ����Ҫ���ó�SERVICE_INTERACTIVE_PROCESS��
			SERVICE_DEMAND_START,      // ����������������ʽ���ֱ��ǡ��Զ�(SERVICE_AUTO_START)�����ֶ�(SERVICE_DEMAND_START)���͡�����(SERVICE_DISABLED)����
			SERVICE_ERROR_NORMAL,      // �������������� ��¼���󣬼�������
			szBinPath,                 // ����exe����·�� ·�����пո���·��Ҫ������"
			NULL,                      // no load ordering group һ����������˳��
			NULL,                      // no tag identifier �����������أ�ר��������
			NULL,                      // no dependencies �ַ������飬ָ��һ����������ֻ�һ������˳����
			NULL,                      // LocalSystem account ����������˺š��Ա���ϵͳ�˻���½
			NULL);                     // no password 

		if (schService == NULL)
		{
			CloseServiceHandle(schSCManager);
			response = "CreateService failed."; //����GetLastError()�鿴��������
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
		CloseServiceHandle(schService);
		//�򿪷���
		schService = OpenService(
        schSCManager,        
        szServiceName,            
        SERVICE_ALL_ACCESS);  

		if (schService == NULL)
		{
			CloseServiceHandle(schSCManager);
			response = "OpenService failed."; //����GetLastError()�鿴��������
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
		//�������񲢴���
		LPCTSTR lpctstr[1];
		//lpctstr[0] = (LPCTSTR)req.c_str();
		lpctstr[0] = (LPCTSTR)(LPTSTR)request;
		//WriteToLog(request);
		if (!StartService(schService,1,lpctstr))    // ����:����������������������ֵ
		{    
			CloseServiceHandle(schService);
			CloseServiceHandle(schSCManager);
			response = "OpenService failed."; //����GetLastError()�鿴��������
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
		// ������״ֱ̬������������Ϊֹ 
		
		DWORD dwOldCheckPoint;
		DWORD dwStartTickCount;
		DWORD dwWaitTime;
		if (!QueryServiceStatusEx(schService,SC_STATUS_PROCESS_INFO,(LPBYTE)&ssStatus,sizeof(SERVICE_STATUS_PROCESS),&dwBytesNeeded)){

			CloseServiceHandle(schService);
			CloseServiceHandle(schSCManager);
			response = "QueryServiceStatusEx failed."; //����GetLastError()�鿴��������
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}

		dwStartTickCount = GetTickCount();
		dwOldCheckPoint = ssStatus.dwCheckPoint;
		while (ssStatus.dwCurrentState == SERVICE_START_PENDING)
		{
			dwWaitTime = ssStatus.dwWaitHint / 10;
			if (dwWaitTime < 1000)
				dwWaitTime = 1000;
			else if (dwWaitTime > 10000)
				dwWaitTime = 10000;

			Sleep(dwWaitTime);

			if (!QueryServiceStatusEx(schService,SC_STATUS_PROCESS_INFO,(LPBYTE)&ssStatus,sizeof(SERVICE_STATUS_PROCESS),&dwBytesNeeded)){

				CloseServiceHandle(schService);
				CloseServiceHandle(schSCManager);
				response = "QueryServiceStatusEx failed."; //����GetLastError()�鿴��������
				if (response.size() >= (*len))
				{
					*len = response.size();
					return -4;
				}
				strcpy_s(buff, *len, response.c_str());
				*len = response.size();
				return -1;
			}

			if (ssStatus.dwCheckPoint > dwOldCheckPoint)
			{
				dwStartTickCount = GetTickCount();
				dwOldCheckPoint = ssStatus.dwCheckPoint;
			}
			else
			{
				if (GetTickCount() - dwStartTickCount > ssStatus.dwWaitHint)
				{
					break;
				}
			}
		}
	
		// �������Ƿ�����
		if (ssStatus.dwCurrentState != SERVICE_RUNNING)
		{
			CloseServiceHandle(schService);
			CloseServiceHandle(schSCManager);
			response = "Service started failed."; //����GetLastError()�鿴��������
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
		//������ɣ�����
		CloseServiceHandle(schService);
		CloseServiceHandle(schSCManager);
		
		//����
		sampleProbe = req;
		if (req.find("BLOCK_SAMPLE") != string::npos) sampleType = "BLOCK_SAMPLE";
		else if (req.find("SEQUENCE_SAMPLE") != string::npos) sampleType = "SEQUENCE_SAMPLE";

		if (!sampleType.empty())
		{
			Json::Reader reader;
			Json::FastWriter writerinfo;
			Json::Value root;
			Json::Value req_json;
			Json::Value res_json;
			reader.parse(sampleProbe, root);
			req_json = root["setValues"][0]["data"]["samples"];
			C0LLECT_DATA_COUNT = req_json.size();

			res_json["code"] = "SUCCESS";
			for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
			{
				Json::Value unit;
				unit["id"] = req_json[i]["id"].asString();
				unit["code"] = "SUCCESS";
				res_json["result"].append(unit);
			}
			//response = "{\"code\":\"SUCCESS\",\"result\":[{\"id\":\"x2\",\"code\":\"SUCCESS\"}]}";
			response = writerinfo.write(res_json);
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return 0;
		}
		else
		{
			response = "{\"code\":\"FAILED\",\"description\":\"Has no type of sample\"}";
			if (response.size() >= (*len))
			{
				*len = response.size();
				return -4;
			}
			strcpy_s(buff, *len, response.c_str());
			*len = response.size();
			return -1;
		}
	}

	//set�����
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	Json::Value jreq;

	reader.parse(request, req_json);
	jreq = req_json["setValues"];
	int nArraySize = jreq.size();

	//��ȡ�ļ�����
	ifstream fin("probe_bh.json");
	if (!fin)
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File not exist\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	if (!reader.parse(fin, root))
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Json File can not open\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}

	bool flag = false;  
	if (req_json["path"].asString() == "/NC_LINK_BH")	//����ȫ�ֲ���
	{
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = setID(jreq[i]["id"].asString(), root, jreq[i]["data"], "devices");
			if (temp == NULL)
			{
				jreq[i]["code"] = "FAILED";
				jreq[i].removeMember("data");
			}
			else
			{
				flag = true;
				root = temp;
				jreq[i]["code"] = "SUCCESS";
				jreq[i].removeMember("data");
			}
		}
	}
	else if (req_json["path"].asString() == Machine)
	{
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = setID(jreq[i]["id"].asString(), root["devices"][0], jreq[i]["data"],"");
			if (temp == NULL)
			{
				jreq[i]["code"] = "FAILED";
				jreq[i].removeMember("data");
			}
			else
			{
				flag = true;
				root["devices"][0] = temp;
				jreq[i]["code"] = "SUCCESS";
				jreq[i].removeMember("data");
			}
		}
	}
	else
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Don't find the machine\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	ofstream fout("probe_bh.json");
	if (fout.is_open())
	{
		fout << root;
		fout.close();
	}
	Json::Value json_response;
	if(flag)
		json_response["code"] = "SUCCESS";
	else
		json_response["code"] = "FAILED";
	json_response["result"] = jreq;
	response = json_response.toStyledString();

	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	strcpy_s(buff, *len, response.c_str());
	*len = response.size();
	return 0;
}

int  SAMPLES(char *request, char* buff, int *len)
{
	string req(request),response;
	
	if (req.find(Machine) == string::npos)
	{
		response = "{\"code\":\"EPARAM\",\"description\":\"Don't find the machine\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}
	
    // Try to open the named file mapping identified by the map name.  
    hMapFile = OpenFileMapping(  
		FILE_MAP_ALL_ACCESS,          // Read access  FILE_MAP_ALL_ACCESS
        FALSE,                  // Do not inherit the name  
        FULL_MAP_NAME           // File mapping name   
        );  
    if (hMapFile == NULL)   
    {  
		response = GetLastError();
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
        return -1;
    }  
	
    // Map a view of the file mapping into the address space of the current process.  
    opcData = (OPCData*)MapViewOfFile(  
        hMapFile,               // Handle of the map object  
		FILE_MAP_ALL_ACCESS,          // Read access  
        0,                      // High-order DWORD of the file offset   
        0,            // Low-order DWORD of the file offset  
        MAP_SIZE               // The number of bytes to map to view  
        ); 
	int nowfront = opcData->front;
	int nowrear = opcData->rear;
	char temp[1000];
	//sprintf_s(temp, 1000, "nowfront = %d,nowrear = %d", nowfront, nowrear);
	//WriteToLog(temp);
	
	opcData->rear = nowfront;
	
	int length = (nowfront + DATA_COUNT - nowrear)%DATA_COUNT;//�ӳ�

	if (length <= 0)
	{
		response = "";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		CloseHandle(hMapFile);
		return 0;
	}
	if (length >= RETURN_COUNT)
	{
		length = RETURN_COUNT;
	}

	//sprintf_s(temp, 1000, "length = %d", length);
	//WriteToLog(temp);

	Json::Reader reader;
	Json::Value *data = new Json::Value [length];
	for (int i = 0; i < length; i++)
	{
		reader.parse(opcData->data[(nowfront - length + i + 1 + DATA_COUNT) % DATA_COUNT], data[i]);

		//sprintf_s(temp, 1000, "data[%d] = %s",i, data[i].toStyledString().c_str());
		//WriteToLog(temp);
	}
	Json::Value *result = new Json::Value[C0LLECT_DATA_COUNT];
	Json::Value jresponse;
	jresponse["beginDate"] = data[0]["time"].asString();

	if (sampleType == "BLOCK_SAMPLE")
	{
		jresponse["sampleInterval"] = 50;
		for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
		{
			result[i]["sampleInterval"] = 50;
		}
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < C0LLECT_DATA_COUNT; j++)
			{
				if (data[i]["data"][j]["data"].type() == Json::stringValue)
				{
					result[j]["data"].append(data[i]["data"][j]["data"].asString());	//***
				}
				else if (data[i]["data"][j]["data"].type() == Json::intValue)
				{
					result[j]["data"].append(data[i]["data"][j]["data"].asInt());	//***
				}
				else
				{
					result[j]["data"].append(data[i]["data"][j]["data"].asDouble());	//***
				}
			}
		}
	}
	else if (sampleType == "SEQUENCE_SAMPLE")
	{
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < C0LLECT_DATA_COUNT; j++)
			{
				Json::Value unit;
				unit.append(data[i]["time"].asString());
				if (data[i]["data"][j]["data"].type() == Json::stringValue)
				{
					unit.append(data[i]["data"][j]["data"].asString());
				}
				else if (data[i]["data"][j]["data"].type() == Json::intValue)
				{
					unit.append(data[i]["data"][j]["data"].asInt());
				}
				else
				{
					unit.append(data[i]["data"][j]["data"].asDouble());
				}
				//WriteToLog(unit.toStyledString().c_str());
				result[j]["data"].append(unit);
				//WriteToLog(result[j].toStyledString().c_str());
			}
		}
	}
	else
	{
		response = "{\"code\":\"FAILED\",\"description\":\"Has no type of sample\"}";
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		strcpy_s(buff, *len, response.c_str());
		*len = response.size();
		return -1;
	}

	for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
	{
		result[i]["id"] = data[0]["data"][i]["id"].asString();
		jresponse["data"].append(result[i]);
	}
	Json::Value root;
	root["path"] = Machine;
	root["code"] = "SUCCESS";
	root["samples"] = jresponse;
	root["events"] = "[]";
	root["exceptons"] = "[]";

	Json::FastWriter fastWriter;
	response = fastWriter.write(root);

	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	strcpy_s(buff, *len, response.c_str());
	*len = response.size();
	CloseHandle(hMapFile);
	return 0;
}