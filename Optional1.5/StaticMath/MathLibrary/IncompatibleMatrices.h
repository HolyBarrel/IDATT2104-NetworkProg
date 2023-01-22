#ifndef INCOMPATIBLE_MATRICES_h
#define INCOMPATIBLE_MATRICES_h

#include <stdexcept>
using namespace std;

class IncompatibleMatricesException : public runtime_error
{
public:
	IncompatibleMatricesException() : runtime_error(
		"Matrix multiplication (M1 * M2) on incompatible matrix dimensions. M1 must have an amount of columns matching the number of rows in M2")
	{
	}

};
#endif