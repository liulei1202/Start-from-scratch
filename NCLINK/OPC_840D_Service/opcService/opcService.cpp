// opcService.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include <stdio.h>
#include <iostream>
#include <string>
#include <comdef.h>	//����BSTR��������
#include <ctime> 
#include <windows.h>
#include <sstream>

#include "OpcError.h"
#include "opcda.h"
#include "opccomn.h"
#include "opcda_i.c"
#include "opccomn_i.c"

#include "json.h"
#ifdef _DEBUG
#pragma comment(lib,"json_vc71_libmtd.lib")
#else
#pragma comment(lib,"json_vc71_libmt.lib")
#endif

using namespace std;

//�����ڴ���ر���
#define FILE_PATH "E:\\OPClog.txt" //��Ϣ����ļ�
#define FULL_MAP_NAME	"Global\\MyFileMappingObject"  //	Local	Global

const int DATA_COUNT =200;
const int DATA_LENGTH = 5000;
int MAP_SIZE;

struct OPCData {
	int front;
	int rear;
	char data[200][5000];
}opcData;

struct DataDefine {
	char ID[10];
	char itemID[100];
	int type;
};
DataDefine* dataDefine;

//������ر���
HANDLE hMapFile = NULL;  
PVOID pView = NULL;  
bool brun=false;	//������ֹ��־
SERVICE_STATUS servicestatus;
SERVICE_STATUS_HANDLE hstatus;

//opc���ݲɼ���ر���
//const char* m_strRemoteMachine="192.168.214.241";	//opc������ip��ַ
#define LOCALE_ID 0x409 // Code 0x409 = ENGLISH
int C0LLECT_DATA_COUNT;	//�ɼ����ݸ���
IOPCServer *m_IOPCServer;	//opc���������
IOPCItemMgt *m_IOPCItemMgt;	//opc��ӿ�
IOPCSyncIO *m_IOPCSyncIO;	//ͬ���ӿ�
OPCITEMDEF *m_Items;	//�ɼ�������Ϣ
OPCITEMRESULT *m_ItemResult;	//��ص�״̬��Ϣ
OPCHANDLE m_GrpSrvHandle;		//����
HRESULT *m_pErrors;		//���Ƿ����
MULTI_QI m_arrMultiQI [6];	//Զ�̷���

//����
void WINAPI ServiceMain(DWORD argc, LPTSTR * argv);
void WINAPI CtrlHandler(DWORD request);
void StartWork(DWORD argc, LPTSTR * argv);		//��ʼ����
int init_opcclient();	//��ʼ��opc������
void ReportServiceStatus(DWORD dwCurrentState, DWORD dwWin32ExitCode, DWORD dwWaitHint);//�������״̬
int WriteToLog(const char* str);	//д��־
bool isFULL();	//����
void CleanUp();	//�������
void Stop();	//����opc

int _tmain(int argc, _TCHAR* argv[])
{

	SERVICE_TABLE_ENTRY entrytable[2];
	entrytable[0].lpServiceName= "opcService";
	entrytable[0].lpServiceProc=(LPSERVICE_MAIN_FUNCTION)ServiceMain;
	entrytable[1].lpServiceName=NULL;
	entrytable[1].lpServiceProc=NULL;
	StartServiceCtrlDispatcher(entrytable);
	return 0;
}

void WINAPI ServiceMain(DWORD argc, LPTSTR * argv)
{
	servicestatus.dwServiceType = SERVICE_WIN32;
	servicestatus.dwCurrentState = SERVICE_START_PENDING;
	servicestatus.dwControlsAccepted = SERVICE_ACCEPT_SHUTDOWN|SERVICE_ACCEPT_STOP;//�ڱ�����ֻ����ϵͳ�ػ���ֹͣ�������ֿ�������
	servicestatus.dwWin32ExitCode = 0;
	servicestatus.dwServiceSpecificExitCode = 0;
	servicestatus.dwCheckPoint = 0;
	servicestatus.dwWaitHint = 0;

	hstatus = RegisterServiceCtrlHandler("opcService", CtrlHandler);
	if (hstatus==0){
		WriteToLog("RegisterServiceCtrlHandler failed");
		return;
	}
	WriteToLog("RegisterServiceCtrlHandler success");
	ReportServiceStatus(SERVICE_START_PENDING, 0, 3000);
	//��SCM ��������״̬
	SetServiceStatus (hstatus, &servicestatus);

	//��ʼ����
	StartWork(argc, argv);
}

void StartWork(DWORD argc, LPTSTR * argv){

	//��ʼ����ر��� 
	char tmp[64] = "";
	//��ȡҪ�ɼ����ݵ�id
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	reader.parse(argv[1], root);
	WriteToLog(root.toStyledString().c_str());
	req_json = root["setValues"][0]["data"]["samples"];
	C0LLECT_DATA_COUNT = req_json.size();
	dataDefine = new DataDefine[C0LLECT_DATA_COUNT];
	
	//��ȡ�����ļ���Ϣ���������������������Ϣ
	LPTSTR lpPath = "E://config.ini";
	//DATA_COUNT = GetPrivateProfileInt("BUFFER", "DATA_COUNT", 200, lpPath);
	//DATA_LENGTH = GetPrivateProfileInt("BUFFER", "DATA_LENGTH", 5000, lpPath);
	MAP_SIZE = GetPrivateProfileInt("BUFFER", "MAP_SIZE", 65536 * 16, lpPath);

	for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
	{
		//���dataDefine�ṹ��
		strcpy(dataDefine[i].ID, req_json[i]["id"].asString().c_str());
		GetPrivateProfileString(dataDefine[i].ID, "itemID", NULL, dataDefine[i].itemID, 100, lpPath);
		dataDefine[i].type = GetPrivateProfileInt(dataDefine[i].ID, "type", 1, lpPath);

		WriteToLog(dataDefine[i].ID);
		WriteToLog(dataDefine[i].itemID);
		sprintf_s(tmp, 64, "TYPE=%d", dataDefine[i].type);
		WriteToLog(tmp);
	}
	//delete[] lpPath;

	//�����ڴ����ݳ�ʼ��
	opcData.front = 0;
	opcData.rear = 0;
	for(int i=0;i<DATA_COUNT;i++){
		strcpy(opcData.data[i],"");
	}
	WriteToLog("�����ڴ����ݳ�ʼ��");
	//�����ڴ����Ȩ��
	PSECURITY_DESCRIPTOR pSec = (PSECURITY_DESCRIPTOR)LocalAlloc(LMEM_FIXED, SECURITY_DESCRIPTOR_MIN_LENGTH);
	if(!pSec){
		sprintf_s(tmp,64,"!pSec failed w/err 0x%08lx\n", GetLastError());
        WriteToLog(tmp);  
		return;
	}
	if(!InitializeSecurityDescriptor(pSec, SECURITY_DESCRIPTOR_REVISION)){
		LocalFree(pSec);
		sprintf_s(tmp,64,"!InitializeSecurityDescriptor failed w/err 0x%08lx\n", GetLastError());
        WriteToLog(tmp);  
		return;
	}
	if(!SetSecurityDescriptorDacl(pSec, TRUE, NULL, TRUE)){
		LocalFree(pSec);
		sprintf_s(tmp,64,"!SetSecurityDescriptorDacl failed w/err 0x%08lx\n", GetLastError());
        WriteToLog(tmp);  
		return;
	}
	SECURITY_ATTRIBUTES attr;
	attr.bInheritHandle = FALSE;
	attr.lpSecurityDescriptor = pSec;
	attr.nLength = sizeof(SECURITY_ATTRIBUTES);

    //���������ڴ� 
    hMapFile = CreateFileMapping(  
        INVALID_HANDLE_VALUE,   // Use paging file - shared memory  
        &attr,                   // Default security attributes  
        PAGE_READWRITE,         // Allow read and write access  
        0,                      // High-order DWORD of file mapping max size  
        MAP_SIZE,               // Low-order DWORD of file mapping max size  65536byte����Ҫ��64kb��������
        FULL_MAP_NAME	//L"Global\MyFileMappingObject"           // Name of the file mapping object  ��Local\SampleMap��
        );  
	LocalFree(pSec);
    if (hMapFile == NULL){  
		sprintf_s(tmp,64,"CreateFileMapping failed w/err 0x%08lx\n", GetLastError());
        WriteToLog(tmp);  
        CleanUp();  
		return;
    }
	sprintf_s(tmp,64,"The file mapping (%s) is created", FULL_MAP_NAME);
    WriteToLog(tmp);  
  
    //�����ļ�ӳ������
	
    pView = MapViewOfFile(  
        hMapFile,               // Handle of the map object  
        FILE_MAP_ALL_ACCESS,    // Read and write access  
        0,                      // High-order DWORD of the file offset   
        0,            // Low-order DWORD of the file offset   0
        MAP_SIZE               // The number of bytes to map to view  
        );  
		
	
    if (pView == NULL)  {   
		sprintf_s(tmp,64,"MapViewOfFile failed w/err 0x%08lx\n", GetLastError());
        WriteToLog(tmp);  
        CleanUp();  
		return;
    }  
	OPCData* pViewopcData = NULL;	//W***
	pViewopcData = (OPCData*)pView;
	/*
	pViewopcData = (OPCData*)MapViewOfFile(
	hMapFile,               // Handle of the map object
	FILE_MAP_ALL_ACCESS,    // Read and write access
	0,                      // High-order DWORD of the file offset
	0,            // Low-order DWORD of the file offset   0
	MAP_SIZE               // The number of bytes to map to view
	);
	*/

	sprintf_s(tmp,64,"The file view is mapped(%d byte).", MAP_SIZE);
    WriteToLog(tmp);  
	sprintf_s(tmp,64,"�������ݴ�С = %d(byte).",sizeof(opcData));
    WriteToLog(tmp);
	memcpy_s(pView,MAP_SIZE,&opcData,sizeof(opcData));
	
	//��ʼ��opc�ɼ�����
	if(init_opcclient() == 0){	//�����ʼ��ʧ��
		return;	
	}

	//������opc����ֵ��ر���
	HRESULT r1;
	OPCHANDLE *phServer;	//��ķ��������
	OPCITEMSTATE *pItemValue;	//�������
	HRESULT *pErrors;	//������Ϣ
	UINT qnr;
	string data;
	SYSTEMTIME sys;
	clock_t startTime;
	/*
	OPCITEMRESULT �������нṹ��
	typdef struct {
	OPCHANDLE hServer;	//���������������
	VARTYPE vtCanonicalDataType;	//������ʹ�õ����������������
	WORD wReserved;	//
	DWORD dwAccessRights;	//�������дȨ��
	DWORD dwBlobSize;	//�������ϴ洢����С
	BYTE *pBlob;	//�洢��ָ��
	};
	OPCITEMSTATE
	typdef struct {
	OPCHANDLE hClient;	//�ͻ��˾��
	FILETIME ftTimeStamp;	//ʱ���
	WORD wQuality;	//����������
	WORD wReserved;
	VARIANT vDataValue;	//����ֵ
	} ;
	*/
	phServer = new OPCHANDLE[C0LLECT_DATA_COUNT];
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		phServer[i] = m_ItemResult[i].hServer;
	}
	//��ʼ����ϣ���ʼ����
	ReportServiceStatus(SERVICE_RUNNING, 0, 0);

	brun=true;	//��ʼѭ������
	while (brun)
	{
		startTime = clock();//��ʼѭ��ʱ��

		GetLocalTime(&sys);
		sprintf_s(tmp,64,"%d-%d-%dT%d:%d:%d.%d",sys.wYear,sys.wMonth,sys.wDay,sys.wHour,sys.wMinute,sys.wSecond,sys.wMilliseconds);  
		data = tmp;
		data = "{\"time\":\"" + data + "\",\"data\":[";
		
		r1 = m_IOPCSyncIO->Read(OPC_DS_DEVICE,	//����Դ�� ���ʹ�� OPC_DS_CACHE�����ݽ��� OPC�����������н��ж�ȡ��ʹ��OPC_DS_DEVICE����ȡ��ҵ��ͨ���������ִ�С�
			C0LLECT_DATA_COUNT,	//��ȡ��Ŀ
			phServer,	//�������������
			&pItemValue,	//�������� OPCITEMSTATE��Ԫ�ص����飬���ڶ�ȡֵ���йض�ȡ��ҵ��������Ϣ��
			&pErrors);		//���� HRESULT ����Ԫ�ص����顣 ��Щ�����������޷��ɹ�����Read() ʱ�Ĵ�����룬�򷵻��йسɹ��ķ������õ���Ϣ��
		if (r1 == S_OK) {
			
			for(int i=0;i<C0LLECT_DATA_COUNT;i++){
				stringstream ss;
				if (dataDefine[i].type == 0) {
					int val = pItemValue[i].vDataValue.intVal;	//ȡֵ
					ss << val;
				}
				else if (dataDefine[i].type == 1) {
					double val = pItemValue[i].vDataValue.dblVal;	//ȡֵ
					ss << val;
				}
				else if (dataDefine[i].type == 2) {
					BSTR val = pItemValue[i].vDataValue.bstrVal;	//ȡֵ
					_bstr_t bstr_t(val);
					std::string str(bstr_t);
					str = "\"" + str + "\"";
					::SysFreeString(val);
					ss << str;
				}else {
					string str = "unknown data type.";
					ss << str;
				}
				data = data + "{\"id\":\""+ dataDefine[i].ID +"\",\"data\":" + ss.str() + "},";
			}
		}else if (r1 == S_FALSE){
			WriteToLog("Read()����");
			return;
		}else if (FAILED(r1)){
			WriteToLog("ͬ����ʧ��!");
			return;
		}
		data = data.substr(0,data.length()-1);	//ȥ���һ������
		data = data + "]}";
		
		//�����ݷ��빲���ڴ���
		opcData.rear = pViewopcData->rear;	//��ȡ��β���ͻ��˶�ȡ���ݺ���޸Ķ�β

		if(isFULL()){	//���������������β����(��β+1)
			opcData.rear = (opcData.rear+1)%DATA_COUNT;	
		}
		opcData.front = (opcData.front+1)%DATA_COUNT;
		strcpy(opcData.data[opcData.front],data.c_str());
		
		//WriteToLog(opcData.data[opcData.front]);
		memcpy_s(pView,MAP_SIZE,&opcData,sizeof(opcData));	//��仰������

		while ((clock() - startTime) <= 50)	//50ms�ɼ�һ��
		{
			Sleep(1);
		};	
	}
	//�������������
	delete[] phServer;
	CoTaskMemFree(pErrors);
	CoTaskMemFree(pItemValue);
	Stop();
    CleanUp();
	WriteToLog("service stopped");
}

int init_opcclient(){

	HRESULT r1;
	CLSID clsid;
	LONG TimeBias = 0;	//ʱ��ƫ��
	FLOAT PercentDeadband = 0.0;	//����
	DWORD RevisedUpdateRate;	//���������ص����ʵ�ʸ�������
	string szErrorText;
	m_ItemResult = NULL;
	// ��ʼ�� COM ��
	r1 = CoInitializeEx(NULL,COINIT_MULTITHREADED);//COINIT_APARTMENTTHREADED	COINIT_MULTITHREADED
	if (r1 != S_OK){ 
		if (r1 == S_FALSE){
			WriteToLog("COM ���Ѿ���ʼ��"); 
		}else{
			WriteToLog("COM ���ʼ��ʧ��"); 
			return 0;
		}
	}else{
		WriteToLog("COM ���ʼ���ɹ�"); 
	}
	
	//������ڳ����й���ͻ��ͷ������İ�ȫ���ã�ͨ������CoInitializeSecurity API��ɡ������ʹ�����������������߹رհ�ȫ�ԡ�������ڵ���CoInitialize������ϵ������������
	// turn off security - overrides defaults
	HRESULT hr_sec = CoInitializeSecurity(NULL, -1, NULL, NULL,
	RPC_C_AUTHN_LEVEL_NONE, 
	RPC_C_IMP_LEVEL_IMPERSONATE,
	NULL, 
	EOAC_NONE, 
	NULL);
	char temp[64];
	sprintf_s(temp,64,"hr_sec = 0x%08lx",r1);
	WriteToLog(temp);

	// ͨ�� ProgID,����ע����е���� CLSID
	r1 = CLSIDFromProgID(L"OPC.SINUMERIK.Machineswitch", &clsid);
	if (r1 != S_OK){
		WriteToLog("��ȡ CLSID ʧ��"); 
		CoUninitialize();
		return 0;
	}
	//����Զ��opc����������
	memset(m_arrMultiQI,0,sizeof(m_arrMultiQI));	//��s(����1)��ָ���ĳһ���ڴ��еĺ�n������3�����ֽڵ�����ȫ������Ϊch������2��ָ����ASCIIֵ,
	m_arrMultiQI [0].pIID = &IID_IOPCServer;
	m_arrMultiQI [1].pIID = &IID_IConnectionPointContainer;
	m_arrMultiQI [2].pIID = &IID_IOPCItemProperties;
	m_arrMultiQI [3].pIID = &IID_IOPCBrowseServerAddressSpace;
	m_arrMultiQI [4].pIID = &IID_IOPCServerPublicGroups;
	m_arrMultiQI [5].pIID = &IID_IPersistFile;
	/*
	MULTI_QI �ṹ�嶨�����£�
	typedef struct tagMULTI_QI
	{
	// pass this one in
	const IID *pIID;
	// get these out (must set NULL before calling)
	IUnknown *pItf;
	HRESULT hr;
	} MULTI_QI;

	COSERVERINFO   *pServerInfo��ָ����������Ϣ����Ϊһ���ṹ���������£�
	typedef   struct     _COSERVERINFO
	{
		DWORD   dwReserved1; //����
		LPWSTR   pwszName; //ָ��Զ�̼������
		COAUTHINFO     *pAuthInfo; //
		DWORD   dwReserved2; //����
	}   COSERVERINFO;
	*/
	COSERVERINFO tCoServerInfo;
	ZeroMemory (&tCoServerInfo, sizeof (tCoServerInfo));
	
	char* m_strRemoteMachine="192.168.214.241";
	int nSize = strlen(m_strRemoteMachine)+1;		//��������***
	tCoServerInfo.pwszName = new WCHAR [nSize];
	mbstowcs (tCoServerInfo.pwszName, m_strRemoteMachine, nSize);
	
	//������opc������������
	r1 = CoCreateInstanceEx (
		clsid, // CLSID
		NULL, // No aggregation
		CLSCTX_REMOTE_SERVER,// connect to local, inproc and remote servers	CLSCTX_INPROC_HANDLER	CLSCTX_REMOTE_SERVER	CLSCTX_ALL
		&tCoServerInfo, // remote machine name
		sizeof (m_arrMultiQI) / sizeof (MULTI_QI), // number of IIDS to query
		m_arrMultiQI); // array of IID pointers to query

	// COM requires us to free memory allocated for [out] and [in/out] arguments (i.e. name string).
	delete [] tCoServerInfo.pwszName;
	if (r1 == S_FALSE){
		WriteToLog("����Զ�� OPC ����������ʧ�ܣ�r1 == S_FALSE"); 
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}
		char tmp[64];
		sprintf_s(tmp,64,"r1 = 0x%08lx",r1);
		WriteToLog(tmp);

	if (SUCCEEDED(m_arrMultiQI[0].hr)){
		m_IOPCServer = (IOPCServer *)m_arrMultiQI[0].pItf;	//��ȡԶ��opc������
		WriteToLog("����Զ�� OPC ����������ɹ�"); 
	}else{
		char tmp[64];
		sprintf_s(tmp,64,"m_arrMultiQI[0].hr = 0x%08lx",m_arrMultiQI[0].hr);
		WriteToLog(tmp);
		//WriteToLog("����Զ�� OPC ����������ʧ�ܣ�SUCCEEDED(m_arrMultiQI[0].hr)"); 
		return 0;
	}
	//���һ�� group ���󣬲���ѯ IOPCItemMgt �ӿ�
	r1=m_IOPCServer->AddGroup(L"grp1",	//������
		TRUE,	//�Ƿ񼤻�
		100,	//ָ�����������ֵ��״̬�ĸ��ĸ�֪�ͻ��˺�����ʱ������ָ���ʺϵ�ʱ��������ֹ��Ϣ���������ͻ��˵��ٶȳ����ͻ����ܹ�������ٶȡ�	***
		1,		//group������ͻ��˶Ը���ı�ʶ
		&TimeBias,	//��������UTCƫ��
		&PercentDeadband,	//����
		LOCALE_ID,	//����
		&m_GrpSrvHandle,	//�ɷ���������ľ�����������Ը���ı�ʶ
		&RevisedUpdateRate,	//��������ֵ��״̬�ĸ��ĸ�֪�ͻ��˺��ɷ��������ص����ʱ������ʵ�ʷ�������
		IID_IOPCItemMgt,	//�ڴ�����֮��ָ�� OPC�����ӿ�֮һ�ı�ʶ����ָ�뽫����
		(LPUNKNOWN*) &m_IOPCItemMgt);	//ָ������ӿڵ�ָ��

	if (r1 == OPC_S_UNSUPPORTEDRATE){
		char tmp[64];
		sprintf_s(tmp,64,"�����ˢ��������ʵ�ʵ�ˢ������%d��һ��",RevisedUpdateRate);	//%d***
		WriteToLog(tmp); 
	}else if (FAILED(r1)){
		WriteToLog("����Ϊ��������� group ����"); 
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}
	// Ϊ AddItem ���� item ��Ĳ���
	m_Items = new OPCITEMDEF[C0LLECT_DATA_COUNT];
	for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
	{
		//m_Items[i].szItemID = L"/Channel/Spindle/cmdSpeed[u1,1]";	//������id	c2
		
		size_t cSize = strlen(dataDefine[i].itemID) + 1;
		m_Items[i].szItemID = new wchar_t[cSize];
		mbstowcs(m_Items[i].szItemID, dataDefine[i].itemID, cSize);
		
		m_Items[i].szAccessPath = L"";	//������Ŀ�ѡ����·��
		m_Items[i].bActive = TRUE;	//�Ƿ񼤻�
		m_Items[i].hClient = 1;	//����
		m_Items[i].dwBlobSize = 0;	//�������ϴ洢�����˴洢�����ڴ洢�йظ���ط������������ݵ�������Ϣ���Ĵ�С��
		m_Items[i].pBlob = NULL;	//ָ�������洢����ָ��
		m_Items[i].vtRequestedDataType = 0;	//***	�ͻ���������������ͣ�0Ϊ�������淶������
	}
	/*
		VT_EMPTY        = 0,            // Not specified.
		VT_NULL        = 1,            // Null.
		VT_I2        = 2,            // 2-byte signed int.
		VT_I4        = 3,            // 4-byte signed int.
		VT_R4        = 4,            // 4-byte real.
		VT_R8        = 5,            // 8-byte real.
		VT_CY        = 6,            // Currency.
		VT_DATE        = 7,            // Date.
		VT_BSTR        = 8,            // Binary string.
		VT_DISPATCH    = 9,            // IDispatch
		VT_ERROR        = 10,            // Scodes.
		VT_BOOL        = 11,            // Boolean; True=-1, False=0.
		VT_VARIANT    = 12,            // VARIANT FAR*.
		VT_UNKNOWN  = 13,            // IUnknown FAR*.
		VT_UI1        = 17,            // Unsigned char.
	*/
	r1 = m_IOPCItemMgt->AddItems(C0LLECT_DATA_COUNT,	//��������
		m_Items,	//�������ȫ����Ϣ
		&m_ItemResult,	//��ķ���ֵ
		&m_pErrors);	//ʧ�ܵĴ����ɹ�����Ϣ

	if ( (r1 != S_OK) && (r1 != S_FALSE) ){
		WriteToLog("AddItems() ʧ��");
		m_IOPCItemMgt->Release();
		m_IOPCItemMgt = NULL;
		m_GrpSrvHandle = NULL;
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return  0;
	}else if(r1==S_OK){
		WriteToLog("AddItems()�ɹ�");
	}
	// ��� Item �Ŀɶ�д��
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		if (m_ItemResult[i].dwAccessRights != (OPC_READABLE + OPC_WRITEABLE)){
			char tmp[64];
			sprintf_s(tmp,64,"Item%d���ɶ���Ҳ����д,�������������",i+1);
			WriteToLog(tmp);
			return 0;
		}
	}
	
	//��ѯ group �����ͬ���ӿ�
	r1 = m_IOPCItemMgt->QueryInterface(IID_IOPCSyncIO,(void**)&m_IOPCSyncIO);	//����1������ӿڱ�ʶ��������2������ӿ�ָ���ָ�������ַ
	if (r1 < 0){
		WriteToLog("IOPCSyncIO û�з��֣�����Ĳ�ѯ!");	
		CoTaskMemFree(m_ItemResult);
		m_IOPCItemMgt->Release();
		m_IOPCItemMgt = NULL;
		m_GrpSrvHandle = NULL;
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}

	//ȷ�����������
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		if (m_pErrors[i] != S_OK){ 
			char tmp[64];
			sprintf_s(tmp,64,"Item%d���ɶ���Ҳ����д,�������������",i+1);
			WriteToLog(tmp);
			return 0;
		}
	}
	return 1;
}

bool isFULL(){

	if((opcData.front+1)%DATA_COUNT == opcData.rear)
		return true;
	else
		return false;
}

void WINAPI CtrlHandler(DWORD request)
{
	switch (request)
	{
	case SERVICE_CONTROL_STOP:
		brun=false;
		servicestatus.dwCurrentState = SERVICE_STOPPED;
		break;

	case SERVICE_CONTROL_SHUTDOWN:
		brun=false;
		servicestatus.dwCurrentState = SERVICE_STOPPED;
		break;

	default:
		break;
	}

	SetServiceStatus (hstatus, &servicestatus);
}


int WriteToLog(const char* str){

	FILE* pfile;
	fopen_s(&pfile,FILE_PATH,"a+");
	if (pfile==NULL){
		return -1;
	}
	fprintf_s(pfile,"%s\n",str);
	fclose(pfile);
	return 0;
}

// �������ƹ���������״̬��Ϣ
void ReportServiceStatus(DWORD dwCurrentState, DWORD dwWin32ExitCode, DWORD dwWaitHint)
{
    static DWORD dwCheckPoint = 1;

    servicestatus.dwCurrentState = dwCurrentState;
    servicestatus.dwWin32ExitCode = dwWin32ExitCode;
    servicestatus.dwWaitHint = dwWaitHint;

    if (dwCurrentState == SERVICE_START_PENDING)
        servicestatus.dwControlsAccepted = 0;
    else 
        servicestatus.dwControlsAccepted = SERVICE_ACCEPT_STOP | SERVICE_ACCEPT_PAUSE_CONTINUE;

    if ((dwCurrentState == SERVICE_RUNNING) ||
        (dwCurrentState == SERVICE_STOPPED))
        servicestatus.dwCheckPoint = 0;
    else 
        servicestatus.dwCheckPoint = dwCheckPoint++;

    SetServiceStatus(hstatus, &servicestatus);
}

/***********************************************************************   
* ������ THCAR2Char   
* ��������TCHAR* ת��Ϊ char*   
* ���ڣ�  
   
char* THCAR2char(TCHAR* tchStr)    
{    
int iLen = 2*wcslen(tchStr);//CString,TCHAR������һ���ַ�����˲�����ͨ���㳤��    
char* chRtn = new char[iLen+1]; 
wcstombs(chRtn,tchStr,iLen+1);//ת���ɹ�����Ϊ�Ǹ�ֵ    
return chRtn;    
}   
***********************************************************************   
*/ 
void Stop(){

	HRESULT r1;
	HRESULT *pErrors;
	OPCHANDLE *phServer;
	
	// ɾ�� Item
	phServer = new OPCHANDLE[C0LLECT_DATA_COUNT];
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		phServer[i] = m_ItemResult[i].hServer;
	}
	r1 = m_IOPCItemMgt->RemoveItems(C0LLECT_DATA_COUNT, // [in] ɾ��item����
	phServer, // [in] ���������
	&pErrors);// [out] ���������صĴ�����
	if ( (r1 != S_OK) && (r1 != S_FALSE) ){
		WriteToLog("RemoveItems ʧ��!");
	}else if(r1==S_OK){
		WriteToLog("RemoveItems()�ɹ�");
	}
	
	CoTaskMemFree(pErrors);
	CoTaskMemFree(m_ItemResult);
	m_ItemResult=NULL;
	CoTaskMemFree(m_pErrors);
	m_pErrors = NULL;
	// �ͷ�ͬ���ӿ�
	m_IOPCSyncIO->Release();
	m_IOPCSyncIO = NULL;
	// �ͷ� item ����ӿ�
	m_IOPCItemMgt->Release();
	m_IOPCItemMgt = NULL;
	// ɾ�� group ����
	r1=m_IOPCServer->RemoveGroup(m_GrpSrvHandle, TRUE);
	if (r1 != S_OK){
		WriteToLog("RemoveGroup ʧ��!");
	}else{
		WriteToLog("RemoveGroup �ɹ�!");
	}
	m_GrpSrvHandle = NULL;
	// �ͷ� OPC ������
	m_IOPCServer->Release();
	m_IOPCServer = NULL;
	//�ر� COM ��
	CoUninitialize();
}



void CleanUp(){

	if (hMapFile)  {  
		if (pView)  {  
			// Unmap the file view.  
			UnmapViewOfFile(pView);  
			pView = NULL;  
		}  
		// Close the file mapping object.  
		CloseHandle(hMapFile);  
		hMapFile = NULL;  
	} 
}