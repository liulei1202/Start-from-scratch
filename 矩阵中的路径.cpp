// 12_path.cpp: 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include<iostream>
using namespace std;
bool hasPathCore(const char* data, int rows, int cols, int row, int col, bool* visited, const char* str,int &pathLength)
{
	if (str[pathLength] == '\0')
		return true;
	bool hasPath = false;
	if (row >= 0 && row < rows && col >= 0 && col < cols && data[row*cols + col] == str[pathLength] && !visited[row*cols + col])
	{
		++pathLength;
		visited[row*cols + col] = true;
		hasPath = hasPathCore(data, rows, cols, row+1, col, visited, str, pathLength)
			|| hasPathCore(data, rows, cols, row-1, col, visited, str, pathLength)
			|| hasPathCore(data, rows, cols, row, col+1, visited, str, pathLength)
			|| hasPathCore(data, rows, cols, row, col-1, visited, str, pathLength);
		if (!hasPath)
		{
			--pathLength;
			visited[row*cols + col] = false;
		}
	}
	return hasPath;
}
bool hasPath(const char* data, int rows, int cols, const char* str)
{
	if (data == nullptr || rows <= 0 || cols <= 0 || str == nullptr)
		return false;
	bool* visited = new bool[rows*cols];
	memset(visited, 0, rows*cols);
	int pathLength = 0;
	for (int row = 0; row < rows; ++row)
	{
		for (int col = 0; col < cols; ++col)
		{
			if (hasPathCore(data, rows, cols, row, col, visited, str,pathLength))
				return true;
		}
	}
	delete[] visited;
	return false;
}

int CountCore(int* data, int length, int start, int* end, int rows, int row)
{
	if (row > rows)
		return 0;
	int count = 1;
	for (int i = start + 1; i < length - 1; ++i)
	{
		if (data[i] >= data[start])
		{
			count++;
			*end = i;
		}
	}
}
int Count(int* data, int length, int rows)
{
	//if (data == nullptr || length <= 0 || rows <= 0)
		//return 0;
	for (int i = 0; i < length - 1; ++i)
	{
		int end = i;
		return CountCore(data, length, i, &end, rows, 1);
	}
}


int main()
{
	char data2[3][4] = 
	{
		{'a','b','t','g'},
		{'c','f','c','s'},
		{'j','d','e','h'}
	};
	char data[12] = { 'a','b','t','g','c','f','c','s','j','d','e','h' };
	char path[10] = { "bfce" };
	cout << hasPath((char*)data, 3, 4, path) << endl;
	
    return 0;
}

