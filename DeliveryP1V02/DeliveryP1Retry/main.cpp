// cpp_compiler_options_openmp.cpp

//be sure to follow the instructions before running: 
// https://learn.microsoft.com/en-us/previous-versions/visualstudio/visual-studio-2010/fw509c3b(v=vs.100)
//otherwise the #pragma omp... will be ignored by the compiler
#include <iostream>
#include <thread>
#include <vector>
#include <algorithm>
#include <omp.h>
using namespace std;

bool isPrimeNum(int num);

vector<int> primes;

int main()
{
    //Variables fetched from user
    int lowerBound; 
    int upperBound;
    int numThreads;

    bool correctInput = false;

    //User instructions
    cout << "Welcome to the PrimeFinder-Application" <<
        endl << "Use this app to find prime numbers in a range with threads by following the instructions:"
        << endl;

    while (!correctInput)
    {
        cout << endl << "Please enter a positive integer amount for the lower bound:" << endl;
        cin >> lowerBound;

        cout << "Please enter a larger positive integer amount for the upper bound:" << endl;
        cin >> upperBound;

        cout << "Please enter a positive integer amount of threads:" << endl;
        cin >> numThreads;

        if (upperBound >= lowerBound && lowerBound > -1 && numThreads > 0)
        {
            correctInput = true;
        }
        else
        {
            cout << "Wrong input parameters, please try again following the instructions carefully!" << endl;
        }
    }

    //CREDZ: https://cplusplus.com/forum/general/253103/ , usr: KarampistisDimitris, May 6, 2019 at 4:59pm

#pragma omp parallel num_threads(numThreads)
    {
#pragma omp for
        for (int i = lowerBound; i <= upperBound; ++i) if (isPrimeNum(i)) primes.push_back(i);
    }

    sort(primes.begin(), primes.end());

    cout << "Result for lower bound '" << lowerBound << "' and upper bound '" << upperBound << "'"
        << endl << " using '" << numThreads << "' threads:" << endl << endl;

    int counter = 0; 
    for (auto& prime : primes)
    {
        if (counter < 16)
        {
            cout << prime << ", ";
        }
        else
        {
            cout << prime << endl;
            counter = 0;
        }
        counter++;
    }

    return 0;

}

bool isPrimeNum(int num)
{
    if (num < 2) return false;
    if (num == 2) return true;
    for (int i = 2; i <= sqrt(num); i++)
    {
        if (num % i == 0) return false;
    }
    return true;
}
