#ifndef DIVIDE_BY_ZERO_H
#define DIVIDE_BY_ZERO_H

#include <stdexcept>
using namespace std;

class DivideByZeroException : public runtime_error
{
public:
	DivideByZeroException() : runtime_error("A denominator was assigned as zero! Cannot divide by zero, please try again.")
	{
	}

};
#endif