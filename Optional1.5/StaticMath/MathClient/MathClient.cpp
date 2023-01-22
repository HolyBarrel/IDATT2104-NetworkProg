// MathClient.cpp
// compile with: cl /EHsc MathClient.cpp /link MathLibrary.lib

//COPIED DIRECTLY FROM (minor configs): 
//https://learn.microsoft.com/en-us/cpp/build/walkthrough-creating-and-using-a-static-library-cpp?view=msvc-170
//22.01.23

#include <iostream>
#include "MathLibrary.h"
#include <stdexcept>
#include <vector>
using namespace std;

void printMatrix(vector<vector<int>> vect);

void printEquation(vector<vector<int>> vect1, vector<vector<int>> vect2, vector<vector<int>> result);

int main()
{
    double a = 7.4;
    int b = 99;
    double c = 0;

    vector<vector<int>> vect1 = {
        {20, 2, 8},
        {3, 2, 1},
        {7, 1, 0}
    };

    vector<vector<int>> vect2 = { 
        {2, 2, 13, 20}, 
        {6, 4, 6, 6}, 
        {11, 8, 21, 2} 
    };



    try {
        cout << "a + b = " <<
            MathLibrary::Arithmetic::Add(a, b) << endl;

        cout << "a - b = " <<
            MathLibrary::Arithmetic::Subtract(a, b) << endl;

        cout << "a * b = " <<
            MathLibrary::Arithmetic::Multiply(a, b) << endl;

        cout << "a / b = " <<
            MathLibrary::Arithmetic::Divide(a, b) << endl;

        cout << boolalpha << "a == a = " <<
            MathLibrary::Arithmetic::Equals(a, a) << endl;

        vector<vector<int>> resultVect = MathLibrary::Arithmetic::Multiply(vect1, vect2);

        printEquation(vect1, vect2, resultVect);

        cout << "a / c = " <<
            MathLibrary::Arithmetic::Divide(a, c) << endl;
    }
    catch (const runtime_error& err)
    {
        cerr << err.what() << endl;
    }
 
    return 0;
}

void printMatrix(vector<vector<int>> vect)
{
    for (int r = 0; r < vect.size(); r++)
    {
        cout << "[";
        for (int c = 0; c < vect.at(0).size(); c++)
        {
            cout << vect.at(r).at(c);
            if( c < vect.at(0).size() - 1) cout << ", ";
        }
        cout << "]\n";
    }
}

void printEquation(vector<vector<int>> vect1, vector<vector<int>> vect2, vector<vector<int>> result)
{
    cout << "---Matrix multiplication---" << endl;
    printMatrix(vect1);
    cout << " * " << endl;
    printMatrix(vect2);
    cout << " = " << endl;
    printMatrix(result);
    cout << "---   ---   ---   ---   ---" << endl;
}