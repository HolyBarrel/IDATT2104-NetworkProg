#include <iostream>
#include <thread>
#include <mutex>
#include <vector>
#include <string>
#include <cmath>
#include <algorithm>
using namespace std;

bool isPrimeNum(int num);

void findPrimesInRange(int lower, int gap, int upper);

void findAllPrimes(int lower, int upper, size_t numThreads);

void printFoundPrimes(int lower, int upper, size_t numThreads);

vector<thread> threads;

vector<int> primes;

mutex mut;

int main()
{
    //Variables fetched from user
    int lowerBound; 
    int upperBound;
    size_t numThreads;

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

    findAllPrimes(lowerBound, upperBound, numThreads);

    printFoundPrimes(lowerBound, upperBound, numThreads);
    
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

void findPrimesInRange(int lower, int gap, int upper)
{
    mut.lock();
    for (int i = lower; i < lower + gap; i++)
    {
        if (isPrimeNum(i) && i <= upper) primes.push_back(i);
    }
    mut.unlock();
}

void findAllPrimes(int lower, int upper, size_t numThreads)
{
    int intervalSize = upper - lower;
    double temp = intervalSize / static_cast<double>(numThreads);
    int gap = ceil(temp);

    for (int i = 0; i < numThreads; i++)
    {
        threads.emplace_back(findPrimesInRange, lower + i * gap, gap, upper);
    }

    for (auto& thread : threads) thread.join();
}

void printFoundPrimes(int lower, int upper, size_t numThreads)
{
    sort(primes.begin(), primes.end());

    cout << endl << "Primes within the range [" << lower << "," << upper
        << "]" << endl << "Found using '" << numThreads << "' threads: " << endl;

    int counter = 0;

    for (auto& prime : primes)
    {
        if (counter <= 16)
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
}