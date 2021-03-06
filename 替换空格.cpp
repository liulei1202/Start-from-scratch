// 5_ReplaceBlank.cpp: 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <iostream>
using namespace std;
/*
*将空格转换为%20，在原字符串上转换
*length:字符串总长度
*return：转换是否成功
*/
bool replaceBlank(char* ch, int length)
{
	if (ch == nullptr || length <= 0)
	{
		return false;
	}
	int strLen = strlen(ch);
	int blockNum = 0;
	for (int i = 0; i < strLen; ++i)
	{
		if (ch[i] == ' ')
			++blockNum;
	}
	cout << "strLen:" << strLen << endl
		<< "blockNum:" << blockNum << endl;
	int ReplaceLen = strLen + blockNum * 2;
	if (ReplaceLen > length)
		return false;
	int p_New = ReplaceLen;
	ch[ReplaceLen] = '\0';
	for (int i = strLen - 1; i >= 0; --i)
	{
		if (ch[i] == ' ')
		{
			ch[--p_New] = '0';
			ch[--p_New] = '2';
			ch[--p_New] = '%';
		}
		else
			ch[--p_New] = ch[i];
	}
	return true;
}
/*
*两个排序的数组A1，A2,将A2插入到A1 中，A1种有足够的位置
*/
void insert(int* a1,int length1, int* a2, int length2)
{
	int des1 = length1-1;
	int des2 = length2-1;  
	for (int i = length1 + length2 - 1; i >= 0; --i)
	{
		if (a1[des1] >= a2[des2])
			a1[i] = a1[des1--];
		else
			a1[i] = a2[des2--];

	}
}
int main()
{
	char* ch = new char[100]{ "we are happy." };
	cout << ch << endl;
	replaceBlank(ch, 100);
	cout << ch << endl;

	cout << endl;
	const int num1 = 20;
	const int actnum1 = 6;
	const int num2 = 5;
	int a1[num1] = { 1,3,5,7,9,15 };
	/*
	*验证：a2存在最大值，存在最小值，都是中间值/最小值/最大值，存在与a1相同值
	*/
	int a2[num2] = { -2,5,6,8,55 };
	insert(a1, actnum1, a2, num2);
	for (int i = 0; i < num1; i++)
	{
		cout << a1[i] << " ";
	}

    return 0;
}

