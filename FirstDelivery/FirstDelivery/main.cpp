#include <iostream>
#include <thread>
#include <vector>
using namespace std;

int main()
{
    //thread example inspiration: https://gitlab.com/ntnu-tdat2004/threads/-/blob/master/cpp/main.cpp

    vector<thread> threads;

    int numThreads;
    int lowerBound;
    int upperBound;

    cout << "Welcome! This app takes in a lower bound and an upper bound and uses a user-specified amount of threads to" << endl
        << "find the prime numbers in the interval. Please follow the instructions below." << endl;

    cout << "Please enter the lower bound number:" << endl;
    cin >> lowerBound;

    cout << "Please enter the upper bound number:" << endl;
    cin >> upperBound;

    cout << "Please enter the number of threads you want to compute:" << endl;
    cin >> numThreads;

    int intervalSize = upperBound - lowerBound;
    int gap = intervalSize / numThreads;

    for (int i = 0; i < numThreads; i++)
    {
        threads.emplace_back([i] {                    // i is copied to the thread, do not capture i as reference (&i) as it might be freed before all the threads finishes.
            cout << "Output from thread " << i << endl; // The output might be interleaved since the string, integer and newline are output in succession.
            });
    }

    for (auto& thread : threads)
        thread.join();

    vector<int> startNums;

    for (int i = lowerBound; i < upperBound; i += gap)
    {
        startNums.push_back(i);
    }

    cout << "____" << endl;
    for (auto elm : startNums) {
        cout << elm << endl;
    }
    //TODO: threads should run in paralell
    //TODO: function to find primes
    //TODO; print in sorted order

    return 0;

}