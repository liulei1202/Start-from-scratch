#include "stdafx.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <time.h>
#include <windows.h>

#include <string>
//json处理
#include "json//json.h" 
#pragma comment(lib, "json_vc71_libmtd.lib")

#include "gsk25inetfun.h"
#include "handle.h"
#pragma comment(lib, "gsk25inetfun.lib")
using namespace std;

const string equipment = "GS25i_01";		//设备名称
const string fileName = "probe_gs.json";	//probe文件名称
unsigned char ipaddr[4] = { 192,168,188,126 };	//设备ip地址
HINSGSKRM hInst;			//设备句柄
HANDLE hThread = NULL;		//采集线程句柄
DWORD dwThreadId = NULL;	//
string sampleProbe,sampleType;
const string dataType[3] = { "samples","events","exceptions" };	//三种数据类型
int data_count[3] = {0};	//三种数据的数量
const int CollectInterval = 50;	//采样频率，单位ms
const int ReturnCount = 10;	//每次samples指令最多返回ReturnCount条数据
//缓存数据结构
const int BufferDataCount = 200;	//缓存数据数量
const int BufferDataLength = 5000;	//缓存数据长度
struct BufferData {
	int front;
	int rear;
	char data[BufferDataCount][BufferDataLength];
}bufferData;
//BufferData* bufferData;

int probe(char *request, char *buff, int *len) 
{
	string req(request), response;
	int result = 0;
	//读取文件内容
	ifstream fin(fileName);
	if (!fin)
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File not exist\"}";
		result = -1;
	}
	else
	{
		stringstream content;
		content << fin.rdbuf();
		//输出全部的数据
		response = R"(
	{
	"type":"PROBE",
	"path":")" + equipment + R"(,
	"code":"SUCCESS",
	"probe":[)"
			+ content.str() +
			R"(]
	})";

		//response = "{\"path\":\"" + equipment + "\",\"code\":\"SUCCESS\",\"probe\":[" + content.str() + "]}";	//writerinfo.write(root)
	}

	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	memcpy(buff, response.c_str(), response.size());
	*len = response.size();
	return result;
};
int query(char *request, char *buff, int *len)
{
	string req(request), response;
	int result = 0;
	//json串变量声明
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	Json::Value jreq;

	reader.parse(request, req_json);
	jreq = req_json["ids"];
	int nArraySize = jreq.size();
	//读取文件内容
	ifstream fin(fileName);
	if (!fin)
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File not exist\"}";
		result = -1;
	}
	else if (!reader.parse(fin, root))
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File can not open\"}";
		result = -1;
	}
	else {
		bool flag = false;
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = inquireID(jreq[i]["id"].asString(), root);
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

		Json::Value json_response;
		json_response["type"] = "QUERY";
		json_response["path"] = equipment;
		if (flag)
			json_response["code"] = "SUCCESS";
		else
			json_response["code"] = "FAILED";
		json_response["data"] = jreq;
		response = json_response.toStyledString();
	}
	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	memcpy(buff, response.c_str(), response.size());
	*len = response.size();
	return result;
};
int set(char *request, char *buff, int *len)
{
	string response, req(request);
	int result = 0;
	//SetSampleProbe指令
	if (req.find("sampleProbe") != string::npos)
	{
		sampleProbe = req;
		bool hasSampleType = false;
		if (req.find("SEQUENCE_SAMPLE") != string::npos) {
			sampleType = "SEQUENCE_SAMPLE";
			hasSampleType = true;
		}
		else if (req.find("BLOCK_SAMPLE") != string::npos) {
			sampleType = "BLOCK_SAMPLE";
			hasSampleType = true;
		}
		
		if (!hasSampleType)
		{
			response = "{\"request\":\"" + req + R"(","code":"FAILED","description":"Has no type of sample"})";
			result = -1;
		}
		else
		{
			hInst = GSKRM_CreateInstance(ipaddr, 1);
			//GSKRM_CloseInstance(hInst)
			//result = GSKRM_SetOvertime(hInst, 1000);
			//cout << "OVERTIME:" << result << endl;
			if (hInst == NULL)
			{
				std::cout << "creator hInst err!" << std::endl;
				response = "{\"request\":\"" + req + R"(","code":"FAILED","description":"creator hInst err!"})";
				result = -1;
			}
			else
			{
				std::cout << "hInst Connnect succeed!" << std::endl;

				int result = GSKRM_GetConnectState(hInst);
				if (1 != result) {
					std::cout << "state:" << result << std::endl;
					response = "{\"request\":\"" + req + R"(","code":"FAILED","description":"ConnectState err!"})";
					result = -1;
				}
				else 
				{
					//gsk25i_bhrunInfo(hInst);	//链接测试
					//gsk25i_axisInfo(hInst);
					//构建返回值
					Json::Reader reader;
					Json::FastWriter writerinfo;
					Json::Value root;
					Json::Value res_json;
					reader.parse(sampleProbe, root);

					res_json["type"] = "SetSampleProbe";
					res_json["path"] = equipment;
					res_json["code"] = "SUCCESS";

					Json::Value temp;
					for (int i = 0; i < 3; i++)
					{
						temp = root["setValues"][(Json::UInt)0]["data"][dataType[i]];
						data_count[i] = temp.size();

						for (int i = 0; i < data_count[i]; i++)
						{
							temp[i]["code"] = "SUCCESS";
							/*
							Json::Value unit;
							unit["id"] = temp[i]["id"].asString();
							unit["code"] = "SUCCESS";
							res_json["result"].append(unit);
							*/
						}
						res_json["result"][(Json::UInt)0][dataType[i]] = temp;
					}
					//response = R"({"code":"SUCCESS","result":[]})";
					response = res_json.toStyledString();
					cout << response << endl;
					//初始化缓存区域
					bufferData.front = 0;
					bufferData.rear = 0;
					for (int i = 0; i < BufferDataCount; i++)
					{
						//memset(bufferData.data[i], 0, BufferDataLength);
						ZeroMemory(bufferData.data[i], BufferDataLength);
					}
					//如果线程已开启，先关闭
					if (hThread != NULL)
					{
						CloseHandle(hThread);
					}
					//启动采集线程
					hThread = CreateThread(
						NULL,			// 默认安全属性
						NULL,			// 默认堆栈大小
						ThreadProFunc,	// 线程入口地址
						NULL,			//传递给线程函数的参数
						0,				// 指定线程立即运行
						&dwThreadId		//线程ID号
					);
					//CloseHandle(hThread);
				}
			}
		}
		if (response.size() >= (*len))
		{
			*len = response.size();
			return -4;
		}
		memcpy(buff, response.c_str(), response.size());
		*len = response.size();
		return result;
	}
	
	//set命令处理
	Json::FastWriter writerinfo;
	Json::Reader reader;
	Json::Value root;
	Json::Value req_json;
	Json::Value jreq;

	reader.parse(request, req_json);
	jreq = req_json["setValues"];
	int nArraySize = jreq.size();

	//读取文件内容
	ifstream fin(fileName);
	if (!fin)
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File not exist\"}";
		result = -1;
	}
	else if (!reader.parse(fin, root))
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File can not open\"}";
		result = -1;
	}
	else {
		bool flag = false;
		for (int i = 0; i < nArraySize; i++)
		{
			Json::Value temp;
			temp = setID(jreq[i]["id"].asString(), root, jreq[i]["data"]);
			if (temp == NULL)
			{
				jreq[i]["code"] = "FAILED";
			}
			else
			{
				flag = true;
				root = temp;
				jreq[i]["code"] = "SUCCESS";
			}
			jreq[i].removeMember("data");
		}
		//写文件
		ofstream fout(fileName);
		if (fout.is_open())
		{
			fout << root;
			fout.close();
		}
		//构建返回值
		Json::Value json_response;
		json_response["type"] = "SET";
		json_response["path"] = "equipment";
		if (flag)
			json_response["code"] = "SUCCESS";
		else
			json_response["code"] = "FAILED";
		json_response["result"] = jreq;
		response = json_response.toStyledString();
	}
	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	memcpy(buff, response.c_str(), response.size());
	*len = response.size();
	return result;
};
int samples(char *request, char *buff, int *len)
{
	string req(request), response;
	int result = 0;
	if (sampleType.empty())
	{
		response = "{\"request\":\"" + req + "\",\"code\":\"failed\",\"description\":\"Json File can not open\"}";
		result =  -1;
	}
	else
	{
		int nowfront = bufferData.front;
		int nowrear = bufferData.rear;
		//char temp[1000];
		//读取过的数据踢出队列
		bufferData.rear = nowfront;
		//获取返回数据的长度
		int length = (nowfront + BufferDataCount - nowrear) % BufferDataCount;//队长
		length = (length >= ReturnCount) ? ReturnCount : length;
		//获取返回数据
		Json::Reader reader;
		Json::Value *data = new Json::Value[length];
		for (int i = 0; i < length; i++)
		{
			reader.parse(bufferData.data[(nowfront - length + i + 1 + BufferDataCount) % BufferDataCount], data[i]);
			//cout << data[i].toStyledString() << endl;
		}
		//从sampleProbe中获取要采集的数据项
		Json::Value json_sampleProbe;
		reader.parse(sampleProbe, json_sampleProbe);
		Json::Value probe_samples = json_sampleProbe["setValues"][(Json::UInt)0]["data"]["samples"];	//sampleprobe中的samples项
		Json::Value probe_events = json_sampleProbe["setValues"][(Json::UInt)0]["data"]["events"];
		Json::Value probe_exceptions = json_sampleProbe["setValues"][(Json::UInt)0]["data"]["exceptions"];

		//cout << "length:" << length << endl;

		//Samples项
		for (int i = 0; i < data_count[0]; i++)
		{
			string id = probe_samples[i]["id"].asString();
			Json::Value unit;
			for (int j = 0; j < length; j++)
			{
				unit.append(data[j][id]);
			}
			probe_samples[i]["data"] = unit;
			probe_samples[i]["sampleInterval"] = CollectInterval;
		}
		Json::Value samples;	//Object型
		samples["beginDate"] = data[0]["time"].asString();
		samples["sampleInterval"] = CollectInterval;
		samples["data"] = probe_samples;
		//cout << "samples:\n" << samples.toStyledString() << endl;

		//Events项
		for (int i = 0; i < data_count[1]; i++)
		{
			string id = probe_events[i]["id"].asString();
			probe_events[i]["data"] = data[length - 1][id];
			probe_events[i]["timestamp"] = data[length - 1]["time"];
		}

		//Exceptions项
		for (int i = 0; i < data_count[2]; i++)
		{
			string id = probe_exceptions[i]["id"].asString();
			probe_exceptions[i]["data"] = data[length - 1][id];
			probe_exceptions[i]["timestamp"] = data[length - 1]["time"];
		}

		//最终结果
		Json::Value json_response;
		json_response["path"] = equipment;
		json_response["code"] = "SUCCESS";
		json_response["samples"][(Json::UInt)0] = samples;
		json_response["events"] = probe_events;
		json_response["exceptions"] = probe_exceptions;
		//cout << json_response.toStyledString() << endl;

		//结果转换为string
		Json::FastWriter fastWriter;
		response = fastWriter.write(json_response);
		//response = "{\"code\":\"failed\",\"description\":\"Json File can not open\"}";
	}
	if (response.size() >= (*len))
	{
		*len = response.size();
		return -4;
	}
	memcpy(buff, response.c_str(), response.size());
	*len = response.size();
	return result;
};

Json::Value inquireID(string reqID, Json::Value root) {

	//WriteToLog(root.toStyledString().c_str());
	Json::Value::Members mem = root.getMemberNames();
	for (Json::Value::Members::iterator iter = mem.begin(); iter != mem.end(); iter++)
	{
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
					temp = inquireID(reqID, root[*iter][i]);
					if (temp != NULL)
					{
						return temp;
					}
				}
			}
		}
	}
	//未找到数据
	return NULL;
}

Json::Value setID(string reqID, Json::Value root, Json::Value newValue) {

	//WriteToLog(root.toStyledString().c_str());
	Json::Value::Members mem = root.getMemberNames();
	for (Json::Value::Members::iterator iter = mem.begin(); iter != mem.end(); iter++)
	{
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
					temp = setID(reqID, root[*iter][i], newValue);
					if (temp != NULL)
					{
						root[*iter][i] = temp;
						return root;
					}
				}
			}
		}
	}
	//未找到数据
	return NULL;
}
DWORD WINAPI ThreadProFunc(LPVOID lpParam)
{
	clock_t startTime;	//开始采集时间
	long collectTime;	//采集持续时间
	while (true) 
	{
		startTime = clock();
		
		char time[100];
		SYSTEMTIME current;
		GetLocalTime(&current);
		sprintf_s(time, 100, "%4d-%02d-%02dT%02d:%02d:%02d.%03d\0", current.wYear, current.wMonth, current.wDay, current.wHour, current.wMinute, current.wSecond, current.wMilliseconds);
		
		Json::Value result;
		result["time"] = time;
		/*
		struct BHSAMPLE_STATIC
		{
		float cas;		// 主轴指令进给F
		float ccs;		//主轴指令速度S
		float aload;	//主轴负载电流
		float aspd[NET_AXIS_NUM];// 进给轴实际速度
		float apst[NET_AXIS_NUM];// 进给轴实际位置
		float cpst[NET_AXIS_NUM];// 进给轴指令位置
		float load[NET_AXIS_NUM];// 进给轴负载电流
		char progname[20];		//当前程序名
		char runstatus[60];			// G代码运行状态
		char almhead[16][20];// 报警消除时间
		char almtime[16][38];// 报警发生时间
		char alminfor[16][70];// 报警信息内容
		int runtime;// 累计运行时间
		int ontime;// 累计加工时间
		int gclinenum;	// G代码行号
		short prognum;// G代码程序编号
		short gcmode;	// G代码模态
		short almflag;// 异常信号
		short reserved;
		};
		*/
		struct BHSAMPLE_STATIC* bhSample = GSKRM_GetbhRunInfo(hInst);
		int x = 0;
		if (bhSample != NULL)
		{
			result["c3"] = bhSample->ccs;	//指令转速float
			result["c5"] = bhSample->aload/10;

			//float a = bhSample->apst[0];
			//cout << a << endl;

			result["x2"] = (Json::Value)bhSample->apst[0];
			result["y2"] = bhSample->apst[1];
			result["z2"] = bhSample->apst[2];
				
			result["x3"] = bhSample->cpst[0];
			result["y3"] = bhSample->cpst[1];
			result["z3"] = bhSample->cpst[2];

			result["x4"] = bhSample->aspd[0];
			result["y4"] = bhSample->aspd[1];
			result["z4"] = bhSample->aspd[2];

			result["x5"] = bhSample->load[0]/10000;
			result["y5"] = bhSample->load[1]/10000;
			result["z5"] = bhSample->load[2]/10000;

			result["gf"] = bhSample->progname;
			result["codeline"] = bhSample->gclinenum;
			result["runstatus"] = bhSample->runstatus;

			result["systemExceptin"] = bhSample->almflag;
		}
		else {
			cout << "BHSAMPLE_STATIC IS NULL\n";
			return -1;
		}
		/*
		struct AXIS_INFO
		{
			double Abs_Coord[NET_AXIS_NUM];//绝对坐标
			double Rel_Coord[NET_AXIS_NUM];//相对坐标
			double Mac_Coord[NET_AXIS_NUM];//机床坐标
			double Addi_Coord[NET_AXIS_NUM];//剩余距离
			double Frd;
			int Spd;
			int F;//进给
			int S;//转速比率
			int J;//快速倍率
			double ActiveAxisSpeed[NET_AXIS_NUM];	//各轴实际分速度	
		};
		*/
		struct AXIS_INFO *axisInfo = GSKRM_GetAxisInfo(hInst);
		if (axisInfo != NULL)
		{
			result["c4"] = (double)axisInfo->S/100;
			result["c2"] = result["c4"].asDouble() * result["c3"].asDouble();
		}
		else {
			cout << "AXIS_INFO IS NULL\n";
			return -1;
		}
		/*
		struct ALARM_INFO
		{
		int index;//索引号
		int axisNo;	//报警轴号或从站号
		char ErrorNoStr[8];//报警号
		char ErrorTime[16];//报警时间
		char ErrorMessage[64];//报警信息
		};
		*/
		int cnt = 0;
		struct ALARM_INFO *almInfo = GSKRM_GetAlarmInfo(hInst, &cnt);
		if (almInfo != NULL) {
			char content[200];
			sprintf_s(content, "报警号:%s, 报警轴号或从站号%d, 报警时间：%s, 报警信息：%s", 
				almInfo->ErrorNoStr,almInfo->axisNo,almInfo->ErrorTime,almInfo->ErrorMessage);
			result["exceptionContent"] = content;
		}
		else {
			result["exceptionContent"] = "无";
		}
		Json::FastWriter fastWriter;
		string response = fastWriter.write(result);
		//cout << response << endl;

		if ((bufferData.front + 1) % BufferDataCount == bufferData.rear) //如果队列满
		{
			bufferData.rear = (bufferData.rear + 1) % BufferDataCount;	//舍弃队尾数据
		}
		bufferData.front = (bufferData.front + 1) % BufferDataCount;	//指定队头

		strcpy_s(bufferData.data[bufferData.front], BufferDataLength, response.c_str()); //队头放入数据
		//cout << "新数据:" << bufferData.front << "值：" << bufferData.data[bufferData.front] << endl;
		collectTime = clock() - startTime;
		if (collectTime < CollectInterval)
		{
			Sleep(CollectInterval - collectTime);
		}
	}
}

