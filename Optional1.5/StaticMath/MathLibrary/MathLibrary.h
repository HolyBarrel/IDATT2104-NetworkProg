//MathLibrary.h
#pragma once

#include <vector>
using namespace std;

namespace MathLibrary
{
	class Arithmetic
	{
		public:
			static double Add(double a, double b) noexcept;

			static double Subtract(double a, double b) noexcept;

			static double Multiply(double a, double b) noexcept;

			static double Divide(double a, double b);

			static bool Equals(double a, double b) noexcept;

			static vector<vector<int>> Multiply(const vector<vector<int>>& a, vector<vector<int>>& b);
	};
}


