// opcService.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <stdio.h>
#include <iostream>
#include <string>
#include <comdef.h>	//处理BSTR类型数据
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

//共享内存相关变量
#define FILE_PATH "E:\\OPClog.txt" //信息输出文件
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

//服务相关变量
HANDLE hMapFile = NULL;  
PVOID pView = NULL;  
bool brun=false;	//服务终止标志
SERVICE_STATUS servicestatus;
SERVICE_STATUS_HANDLE hstatus;

//opc数据采集相关变量
//const char* m_strRemoteMachine="192.168.214.241";	//opc服务器ip地址
#define LOCALE_ID 0x409 // Code 0x409 = ENGLISH
int C0LLECT_DATA_COUNT;	//采集数据个数
IOPCServer *m_IOPCServer;	//opc服务器借口
IOPCItemMgt *m_IOPCItemMgt;	//opc项接口
IOPCSyncIO *m_IOPCSyncIO;	//同步接口
OPCITEMDEF *m_Items;	//采集数据信息
OPCITEMRESULT *m_ItemResult;	//项返回的状态信息
OPCHANDLE m_GrpSrvHandle;		//组句柄
HRESULT *m_pErrors;		//项是否可用
MULTI_QI m_arrMultiQI [6];	//远程访问

//函数
void WINAPI ServiceMain(DWORD argc, LPTSTR * argv);
void WINAPI CtrlHandler(DWORD request);
void StartWork(DWORD argc, LPTSTR * argv);		//开始工作
int init_opcclient();	//初始化opc服务器
void ReportServiceStatus(DWORD dwCurrentState, DWORD dwWin32ExitCode, DWORD dwWaitHint);//报告服务状态
int WriteToLog(const char* str);	//写日志
bool isFULL();	//队满
void CleanUp();	//清理服务
void Stop();	//清理opc

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
	servicestatus.dwControlsAccepted = SERVICE_ACCEPT_SHUTDOWN|SERVICE_ACCEPT_STOP;//在本例中只接受系统关机和停止服务两种控制命令
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
	//向SCM 报告运行状态
	SetServiceStatus (hstatus, &servicestatus);

	//开始工作
	StartWork(argc, argv);
}

void StartWork(DWORD argc, LPTSTR * argv){

	//初始化相关变量 
	char tmp[64] = "";
	//获取要采集数据的id
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	reader.parse(argv[1], root);
	WriteToLog(root.toStyledString().c_str());
	req_json = root["setValues"][0]["data"]["samples"];
	C0LLECT_DATA_COUNT = req_json.size();
	dataDefine = new DataDefine[C0LLECT_DATA_COUNT];
	
	//获取配置文件信息，尤其是所有数据项的信息
	LPTSTR lpPath = "E://config.ini";
	//DATA_COUNT = GetPrivateProfileInt("BUFFER", "DATA_COUNT", 200, lpPath);
	//DATA_LENGTH = GetPrivateProfileInt("BUFFER", "DATA_LENGTH", 5000, lpPath);
	MAP_SIZE = GetPrivateProfileInt("BUFFER", "MAP_SIZE", 65536 * 16, lpPath);

	for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
	{
		//填充dataDefine结构体
		strcpy(dataDefine[i].ID, req_json[i]["id"].asString().c_str());
		GetPrivateProfileString(dataDefine[i].ID, "itemID", NULL, dataDefine[i].itemID, 100, lpPath);
		dataDefine[i].type = GetPrivateProfileInt(dataDefine[i].ID, "type", 1, lpPath);

		WriteToLog(dataDefine[i].ID);
		WriteToLog(dataDefine[i].itemID);
		sprintf_s(tmp, 64, "TYPE=%d", dataDefine[i].type);
		WriteToLog(tmp);
	}
	//delete[] lpPath;

	//共享内存数据初始化
	opcData.front = 0;
	opcData.rear = 0;
	for(int i=0;i<DATA_COUNT;i++){
		strcpy(opcData.data[i],"");
	}
	WriteToLog("共享内存数据初始化");
	//共享内存访问权限
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

    //创建共享内存 
    hMapFile = CreateFileMapping(  
        INVALID_HANDLE_VALUE,   // Use paging file - shared memory  
        &attr,                   // Default security attributes  
        PAGE_READWRITE,         // Allow read and write access  
        0,                      // High-order DWORD of file mapping max size  
        MAP_SIZE,               // Low-order DWORD of file mapping max size  65536byte，需要是64kb的整数倍
        FULL_MAP_NAME	//L"Global\MyFileMappingObject"           // Name of the file mapping object  “Local\SampleMap”
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
  
    //创建文件映射区域
	
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
	sprintf_s(tmp,64,"共享数据大小 = %d(byte).",sizeof(opcData));
    WriteToLog(tmp);
	memcpy_s(pView,MAP_SIZE,&opcData,sizeof(opcData));
	
	//初始化opc采集程序
	if(init_opcclient() == 0){	//如果初始化失败
		return;	
	}

	//定义与opc返回值相关变量
	HRESULT r1;
	OPCHANDLE *phServer;	//项的服务器句柄
	OPCITEMSTATE *pItemValue;	//项的数据
	HRESULT *pErrors;	//错误信息
	UINT qnr;
	string data;
	SYSTEMTIME sys;
	clock_t startTime;
	/*
	OPCITEMRESULT 具有下列结构：
	typdef struct {
	OPCHANDLE hServer;	//服务器分配的项句柄
	VARTYPE vtCanonicalDataType;	//服务器使用的数据项的数据类型
	WORD wReserved;	//
	DWORD dwAccessRights;	//数据项读写权限
	DWORD dwBlobSize;	//服务器上存储区大小
	BYTE *pBlob;	//存储区指针
	};
	OPCITEMSTATE
	typdef struct {
	OPCHANDLE hClient;	//客户端句柄
	FILETIME ftTimeStamp;	//时间戳
	WORD wQuality;	//数据完整性
	WORD wReserved;
	VARIANT vDataValue;	//数据值
	} ;
	*/
	phServer = new OPCHANDLE[C0LLECT_DATA_COUNT];
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		phServer[i] = m_ItemResult[i].hServer;
	}
	//初始化完毕，开始工作
	ReportServiceStatus(SERVICE_RUNNING, 0, 0);

	brun=true;	//开始循环运行
	while (brun)
	{
		startTime = clock();//开始循环时间

		GetLocalTime(&sys);
		sprintf_s(tmp,64,"%d-%d-%dT%d:%d:%d.%d",sys.wYear,sys.wMonth,sys.wDay,sys.wHour,sys.wMinute,sys.wSecond,sys.wMilliseconds);  
		data = tmp;
		data = "{\"time\":\"" + data + "\",\"data\":[";
		
		r1 = m_IOPCSyncIO->Read(OPC_DS_DEVICE,	//数据源。 如果使用 OPC_DS_CACHE，数据将从 OPC服务器缓存中进行读取；使用OPC_DS_DEVICE，读取作业将通过网络加以执行。
			C0LLECT_DATA_COUNT,	//读取数目
			phServer,	//服务器句柄数组
			&pItemValue,	//具有类型 OPCITEMSTATE的元素的数组，用于读取值和有关读取作业的其它信息。
			&pErrors);		//具有 HRESULT 类型元素的数组。 这些变量将返回无法成功调用Read() 时的错误代码，或返回有关成功的方法调用的信息。
		if (r1 == S_OK) {
			
			for(int i=0;i<C0LLECT_DATA_COUNT;i++){
				stringstream ss;
				if (dataDefine[i].type == 0) {
					int val = pItemValue[i].vDataValue.intVal;	//取值
					ss << val;
				}
				else if (dataDefine[i].type == 1) {
					double val = pItemValue[i].vDataValue.dblVal;	//取值
					ss << val;
				}
				else if (dataDefine[i].type == 2) {
					BSTR val = pItemValue[i].vDataValue.bstrVal;	//取值
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
			WriteToLog("Read()错误");
			return;
		}else if (FAILED(r1)){
			WriteToLog("同步读失败!");
			return;
		}
		data = data.substr(0,data.length()-1);	//去最后一个逗号
		data = data + "]}";
		
		//将数据放入共享内存区
		opcData.rear = pViewopcData->rear;	//获取队尾，客户端读取数据后会修改队尾

		if(isFULL()){	//如果队满，放弃队尾数据(队尾+1)
			opcData.rear = (opcData.rear+1)%DATA_COUNT;	
		}
		opcData.front = (opcData.front+1)%DATA_COUNT;
		strcpy(opcData.data[opcData.front],data.c_str());
		
		//WriteToLog(opcData.data[opcData.front]);
		memcpy_s(pView,MAP_SIZE,&opcData,sizeof(opcData));	//这句话必须有

		while ((clock() - startTime) <= 50)	//50ms采集一次
		{
			Sleep(1);
		};	
	}
	//服务结束，清理
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
	LONG TimeBias = 0;	//时间偏差
	FLOAT PercentDeadband = 0.0;	//死区
	DWORD RevisedUpdateRate;	//服务器返回的组的实际更新速率
	string szErrorText;
	m_ItemResult = NULL;
	// 初始化 COM 库
	r1 = CoInitializeEx(NULL,COINIT_MULTITHREADED);//COINIT_APARTMENTTHREADED	COINIT_MULTITHREADED
	if (r1 != S_OK){ 
		if (r1 == S_FALSE){
			WriteToLog("COM 库已经初始化"); 
		}else{
			WriteToLog("COM 库初始化失败"); 
			return 0;
		}
	}else{
		WriteToLog("COM 库初始化成功"); 
	}
	
	//你可以在程序中管理客户和服务器的安全设置，通过调用CoInitializeSecurity API完成。你可以使用这个调用来加入或者关闭安全性。你可以在调用CoInitialize后就马上调用这个方法。
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

	// 通过 ProgID,查找注册表中的相关 CLSID
	r1 = CLSIDFromProgID(L"OPC.SINUMERIK.Machineswitch", &clsid);
	if (r1 != S_OK){
		WriteToLog("获取 CLSID 失败"); 
		CoUninitialize();
		return 0;
	}
	//连接远程opc服务器参数
	memset(m_arrMultiQI,0,sizeof(m_arrMultiQI));	//将s(参数1)所指向的某一块内存中的后n（参数3）个字节的内容全部设置为ch（参数2）指定的ASCII值,
	m_arrMultiQI [0].pIID = &IID_IOPCServer;
	m_arrMultiQI [1].pIID = &IID_IConnectionPointContainer;
	m_arrMultiQI [2].pIID = &IID_IOPCItemProperties;
	m_arrMultiQI [3].pIID = &IID_IOPCBrowseServerAddressSpace;
	m_arrMultiQI [4].pIID = &IID_IOPCServerPublicGroups;
	m_arrMultiQI [5].pIID = &IID_IPersistFile;
	/*
	MULTI_QI 结构体定义如下：
	typedef struct tagMULTI_QI
	{
	// pass this one in
	const IID *pIID;
	// get these out (must set NULL before calling)
	IUnknown *pItf;
	HRESULT hr;
	} MULTI_QI;

	COSERVERINFO   *pServerInfo：指明服务器信息，它为一个结构，定义如下：
	typedef   struct     _COSERVERINFO
	{
		DWORD   dwReserved1; //保留
		LPWSTR   pwszName; //指定远程计算机名
		COAUTHINFO     *pAuthInfo; //
		DWORD   dwReserved2; //保留
	}   COSERVERINFO;
	*/
	COSERVERINFO tCoServerInfo;
	ZeroMemory (&tCoServerInfo, sizeof (tCoServerInfo));
	
	char* m_strRemoteMachine="192.168.214.241";
	int nSize = strlen(m_strRemoteMachine)+1;		//长度问题***
	tCoServerInfo.pwszName = new WCHAR [nSize];
	mbstowcs (tCoServerInfo.pwszName, m_strRemoteMachine, nSize);
	
	//建立与opc服务器的连接
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
		WriteToLog("创建远程 OPC 服务器对象失败，r1 == S_FALSE"); 
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}
		char tmp[64];
		sprintf_s(tmp,64,"r1 = 0x%08lx",r1);
		WriteToLog(tmp);

	if (SUCCEEDED(m_arrMultiQI[0].hr)){
		m_IOPCServer = (IOPCServer *)m_arrMultiQI[0].pItf;	//获取远程opc服务器
		WriteToLog("创建远程 OPC 服务器对象成功"); 
	}else{
		char tmp[64];
		sprintf_s(tmp,64,"m_arrMultiQI[0].hr = 0x%08lx",m_arrMultiQI[0].hr);
		WriteToLog(tmp);
		//WriteToLog("创建远程 OPC 服务器对象失败，SUCCEEDED(m_arrMultiQI[0].hr)"); 
		return 0;
	}
	//添加一个 group 对象，并查询 IOPCItemMgt 接口
	r1=m_IOPCServer->AddGroup(L"grp1",	//组名称
		TRUE,	//是否激活
		100,	//指定将数据项的值或状态的更改告知客户端后的最短时间间隔。指定适合的时间间隔将防止信息被发送至客户端的速度超过客户端能够处理的速度。	***
		1,		//group句柄，客户端对该组的标识
		&TimeBias,	//服务器与UTC偏差
		&PercentDeadband,	//死区
		LOCALE_ID,	//语言
		&m_GrpSrvHandle,	//由服务器分配的句柄，服务器对该组的标识
		&RevisedUpdateRate,	//将数据项值或状态的更改告知客户端后、由服务器返回的最短时间间隔。实际返回速率
		IID_IOPCItemMgt,	//在创建组之后，指向 OPC组对象接口之一的标识符的指针将可用
		(LPUNKNOWN*) &m_IOPCItemMgt);	//指向所需接口的指针

	if (r1 == OPC_S_UNSUPPORTEDRATE){
		char tmp[64];
		sprintf_s(tmp,64,"请求的刷新速率与实际的刷新速率%d不一致",RevisedUpdateRate);	//%d***
		WriteToLog(tmp); 
	}else if (FAILED(r1)){
		WriteToLog("不能为服务器添加 group 对象"); 
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}
	// 为 AddItem 定义 item 表的参数
	m_Items = new OPCITEMDEF[C0LLECT_DATA_COUNT];
	for (int i = 0; i < C0LLECT_DATA_COUNT; i++)
	{
		//m_Items[i].szItemID = L"/Channel/Spindle/cmdSpeed[u1,1]";	//数据项id	c2
		
		size_t cSize = strlen(dataDefine[i].itemID) + 1;
		m_Items[i].szItemID = new wchar_t[cSize];
		mbstowcs(m_Items[i].szItemID, dataDefine[i].itemID, cSize);
		
		m_Items[i].szAccessPath = L"";	//数据项的可选访问路径
		m_Items[i].bActive = TRUE;	//是否激活
		m_Items[i].hClient = 1;	//组句柄
		m_Items[i].dwBlobSize = 0;	//服务器上存储区（此存储区用于存储有关更快地访问数据项数据的其它信息）的大小。
		m_Items[i].pBlob = NULL;	//指向上述存储区的指针
		m_Items[i].vtRequestedDataType = 0;	//***	客户端请求的数据类型，0为服务器规范的类型
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
	r1 = m_IOPCItemMgt->AddItems(C0LLECT_DATA_COUNT,	//加项数量
		m_Items,	//所加项的全部信息
		&m_ItemResult,	//项的返回值
		&m_pErrors);	//失败的代码或成功的信息

	if ( (r1 != S_OK) && (r1 != S_FALSE) ){
		WriteToLog("AddItems() 失败");
		m_IOPCItemMgt->Release();
		m_IOPCItemMgt = NULL;
		m_GrpSrvHandle = NULL;
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return  0;
	}else if(r1==S_OK){
		WriteToLog("AddItems()成功");
	}
	// 检测 Item 的可读写性
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		if (m_ItemResult[i].dwAccessRights != (OPC_READABLE + OPC_WRITEABLE)){
			char tmp[64];
			sprintf_s(tmp,64,"Item%d不可读，也不可写,请检查服务器配置",i+1);
			WriteToLog(tmp);
			return 0;
		}
	}
	
	//查询 group 对象的同步接口
	r1 = m_IOPCItemMgt->QueryInterface(IID_IOPCSyncIO,(void**)&m_IOPCSyncIO);	//参数1：所需接口标识符；参数2：所需接口指针的指针变量地址
	if (r1 < 0){
		WriteToLog("IOPCSyncIO 没有发现，错误的查询!");	
		CoTaskMemFree(m_ItemResult);
		m_IOPCItemMgt->Release();
		m_IOPCItemMgt = NULL;
		m_GrpSrvHandle = NULL;
		m_IOPCServer->Release();
		m_IOPCServer = NULL;
		CoUninitialize();
		return 0;
	}

	//确定所有项可用
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		if (m_pErrors[i] != S_OK){ 
			char tmp[64];
			sprintf_s(tmp,64,"Item%d不可读，也不可写,请检查服务器配置",i+1);
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

// 向服务控制管理器报告状态信息
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
* 函数： THCAR2Char   
* 描述：将TCHAR* 转换为 char*   
* 日期：  
   
char* THCAR2char(TCHAR* tchStr)    
{    
int iLen = 2*wcslen(tchStr);//CString,TCHAR汉字算一个字符，因此不用普通计算长度    
char* chRtn = new char[iLen+1]; 
wcstombs(chRtn,tchStr,iLen+1);//转换成功返回为非负值    
return chRtn;    
}   
***********************************************************************   
*/ 
void Stop(){

	HRESULT r1;
	HRESULT *pErrors;
	OPCHANDLE *phServer;
	
	// 删除 Item
	phServer = new OPCHANDLE[C0LLECT_DATA_COUNT];
	for(int i=0;i<C0LLECT_DATA_COUNT;i++){
		phServer[i] = m_ItemResult[i].hServer;
	}
	r1 = m_IOPCItemMgt->RemoveItems(C0LLECT_DATA_COUNT, // [in] 删除item个数
	phServer, // [in] 服务器句柄
	&pErrors);// [out] 服务器返回的错误码
	if ( (r1 != S_OK) && (r1 != S_FALSE) ){
		WriteToLog("RemoveItems 失败!");
	}else if(r1==S_OK){
		WriteToLog("RemoveItems()成功");
	}
	
	CoTaskMemFree(pErrors);
	CoTaskMemFree(m_ItemResult);
	m_ItemResult=NULL;
	CoTaskMemFree(m_pErrors);
	m_pErrors = NULL;
	// 释放同步接口
	m_IOPCSyncIO->Release();
	m_IOPCSyncIO = NULL;
	// 释放 item 管理接口
	m_IOPCItemMgt->Release();
	m_IOPCItemMgt = NULL;
	// 删除 group 对象
	r1=m_IOPCServer->RemoveGroup(m_GrpSrvHandle, TRUE);
	if (r1 != S_OK){
		WriteToLog("RemoveGroup 失败!");
	}else{
		WriteToLog("RemoveGroup 成功!");
	}
	m_GrpSrvHandle = NULL;
	// 释放 OPC 服务器
	m_IOPCServer->Release();
	m_IOPCServer = NULL;
	//关闭 COM 库
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