//MathLibrary.h
//compile: cl /c /EHsc MathLibrary.cpp
//post-build: lib MathLibrary.obj

//WEB-SOURCE:
//https://learn.microsoft.com/en-us/cpp/build/walkthrough-creating-and-using-a-static-library-cpp?view=msvc-170

#include "MathLibrary.h"
#include "DivideByZero.h"
#include "IncompatibleMatrices.h"
#include <stdexcept>
#include <vector>
using namespace std;

namespace MathLibrary
{
	double Arithmetic::Add(double a, double b) noexcept
	{
		return a + b;
	}

	double Arithmetic::Subtract(double a, double b) noexcept
	{
		return a - b;
	}

	double Arithmetic::Multiply(double a, double b) noexcept
	{
		return a * b;
	}

	double Arithmetic::Divide(double a, double b)
	{
		if (b == 0) throw DivideByZeroException();
		return a / b;
	}

	bool Arithmetic::Equals(double a, double b) noexcept
	{
		return a == b;
	}

	vector<vector<int>> Arithmetic::Multiply(const vector<vector<int>>& a, vector<vector<int>>& b)
	{
		//Throws if the vectors are incompatible, meaning that the col amount of a does not match the row amount of b
		if (a.at(0).size() != b.size()) throw IncompatibleMatricesException();

		vector<vector<int>> result(a.size(), vector<int>(b.at(0).size()));

		//fill matrix with zeroes first

		for (auto& vect : result) fill(vect.begin(), vect.end(), 0);

		for (int i = 0; i < a.size(); i++)
		{
			for (int j = 0; j < b.at(0).size(); j++)
			{
				for (int k = 0; k < b.size(); k++)
				{
					//Assigning the values of the multiplication matrices to the result 
					result.at(i).at(j) += 
						a.at(i).at(k) * b.at(k).at(j);
				}
			}
		}
		return result;
	}
}